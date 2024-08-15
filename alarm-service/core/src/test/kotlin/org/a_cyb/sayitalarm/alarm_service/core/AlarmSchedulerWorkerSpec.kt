/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.alarm_service.core

import kotlin.random.Random
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.work.Data
import androidx.work.ListenableWorker
import androidx.work.testing.TestListenableWorkerBuilder
import io.mockk.clearMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.slot
import io.mockk.verify
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.a_cyb.sayitalarm.alarm_service.core.AlarmScheduler.Companion.SCHEDULER_WORKER_INPUT_DATA_ALARM_ID
import org.a_cyb.sayitalarm.alarm_service.core.AlarmScheduler.Companion.SCHEDULER_WORKER_INPUT_DATA_SNOOZE_MIN
import org.a_cyb.sayitalarm.alarm_service.core.AlarmScheduler.Companion.SCHEDULER_WORKER_WORK_SET_ALARM
import org.a_cyb.sayitalarm.alarm_service.core.AlarmScheduler.Companion.SCHEDULER_WORKER_WORK_SET_SNOOZE
import org.a_cyb.sayitalarm.alarm_service.core.AlarmScheduler.Companion.SCHEDULER_WORKER_WORK_TYPE
import org.a_cyb.sayitalarm.alarm_service.core.util.getSnoozeTimeInMills
import org.a_cyb.sayitalarm.domain.repository.RepositoryContract
import org.a_cyb.sayitalarm.entity.Alarm
import org.a_cyb.sayitalarm.entity.AlarmType
import org.a_cyb.sayitalarm.entity.AlertType
import org.a_cyb.sayitalarm.entity.Hour
import org.a_cyb.sayitalarm.entity.Label
import org.a_cyb.sayitalarm.entity.Minute
import org.a_cyb.sayitalarm.entity.Ringtone
import org.a_cyb.sayitalarm.entity.SayItScripts
import org.a_cyb.sayitalarm.entity.WeeklyRepeat
import org.a_cyb.sayitalarm.util.mustBe
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.core.qualifier.named
import org.koin.dsl.module
import org.robolectric.annotation.Config
import org.robolectric.shadow.api.Shadow
import org.robolectric.shadows.ShadowPendingIntent
import tech.antibytes.kfixture.fixture
import tech.antibytes.kfixture.kotlinFixture

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(AndroidJUnit4::class)
@Config(sdk = [33])
class AlarmSchedulerWorkerSpec {
    private lateinit var context: Context
    private lateinit var alarmRepository: RepositoryContract.AlarmRepository
    private lateinit var alarmManager: AlarmManager

    private val fixture = kotlinFixture()

    @Before
    fun setUp() {
        context = mockk(relaxed = true)
        alarmRepository = mockk(relaxed = true)
        alarmManager = mockk(relaxed = true)

        Dispatchers.setMain(StandardTestDispatcher())
    }

    @After
    fun tearDown() {
        stopKoin()
        Dispatchers.resetMain()
    }

    private fun setupMockk(alarms: List<Alarm>) {
        val module = module {
            single<RepositoryContract.AlarmRepository> { alarmRepository }
            single<CoroutineDispatcher>(named("io")) { Dispatchers.IO }
        }
        startKoin { modules(module) }

        every { context.getSystemService(AlarmManager::class.java) } returns alarmManager
        every { alarmRepository.getAllEnabledAlarm(any()) } returns CompletableDeferred(alarms)
    }

    /**
     * Test: SCHEDULER_WORKER_WORK_SET_ALARM
     */
    private fun getSetAlarmWorker(context: Context): AlarmSchedulerWorker {
        val setAlarmData = Data.Builder()
            .putInt(SCHEDULER_WORKER_WORK_TYPE, SCHEDULER_WORKER_WORK_SET_ALARM)
            .build()

        return TestListenableWorkerBuilder<AlarmSchedulerWorker>(context)
            .setInputData(setAlarmData)
            .build()
    }

    @Test
    fun `When doWork invoked with WORKER_WORK_SET_ALARM it schedules enabled alarms in the database`() = runTest {
        // Given
        val alarms = getRandomEnabledAlarms(size = 2)
        setupMockk(alarms)

        // When
        val result = getSetAlarmWorker(context).doWork()

        // Then
        verify(exactly = 2) { alarmManager.setAlarmClock(any(), any()) }
        result mustBe ListenableWorker.Result.success()
    }

    @Test
    fun `When worker schedule enabled alarms it only schedules alarms that have not been scheduled yet`() = runTest {
        // Given
        val worker = getSetAlarmWorker(context)

        // Call for the first time with 2 alarms.
        val alarms = getRandomEnabledAlarms(size = 2)
        setupMockk(alarms)

        worker.doWork()

        verify(exactly = 2) { alarmManager.setAlarmClock(any(), any()) }
        clearMocks(alarmManager)

        // Call for the second time with 2 alarms already scheduled, plus 3 more alarms.
        val newAlarms = getRandomEnabledAlarms(size = 3, idOffset = 2)

        every { alarmRepository.getAllEnabledAlarm(any()) } returns
            async { alarms.plus(newAlarms) }

        // When
        worker.doWork()

        // Then
        verify(exactly = 3) { alarmManager.setAlarmClock(any(), any()) }
    }

    @Test
    fun `When worker schedules an alarm it calls setAlarmClock with AlarmService pendingIntent`() = runTest {
        // Given
        val alarm = getRandomAlarm(id = 1, enabled = true)
        setupMockk(listOf(alarm))

        val capturedIntent = slot<PendingIntent>()
        every { alarmManager.setAlarmClock(any(), capture(capturedIntent)) } answers { mockk() }

        // When
        getSetAlarmWorker(context).doWork()

        // Then
        capturedIntent.isCaptured mustBe true
        capturedIntent.captured.isBroadcast mustBe true
        capturedIntent.captured.isImmutable mustBe true
        capturedIntent.captured.creatorPackage?.contains("org.a_cyb.sayitalarm.alarm_service") mustBe true
    }

    @Test
    fun `When worker schedules an alarm, it passes intent with alarm data`() = runTest {
        // Given
        val alarm = getRandomAlarm(id = 3, enabled = true)
        setupMockk(listOf(alarm))

        val capturedIntent = slot<PendingIntent>()
        every { alarmManager.setAlarmClock(any(), capture(capturedIntent)) } answers { mockk() }

        // When
        getSetAlarmWorker(context).doWork()

        // Then
        val actual = extractIntent(capturedIntent.captured)
        actual.action mustBe AlarmScheduler.ACTION_DELIVER_ALARM
        actual.flags mustBe Intent.FLAG_RECEIVER_FOREGROUND
        actual.component!!.shortClassName mustBe AlarmBroadcastReceiver::class.qualifiedName
        actual.getLongExtra(AlarmScheduler.EXTRA_ALARM_ID, 9L) mustBe alarm.id
    }

    /**
     * Test: SCHEDULER_WORKER_WORK_SET_SNOOZE
     */
    private fun getSetSnoozeWorker(context: Context, alarmId: Long, snoozeMin: Int): AlarmSchedulerWorker {
        val setAlarmData = Data.Builder()
            .putInt(SCHEDULER_WORKER_WORK_TYPE, SCHEDULER_WORKER_WORK_SET_SNOOZE)
            .putLong(SCHEDULER_WORKER_INPUT_DATA_ALARM_ID, alarmId)
            .putInt(SCHEDULER_WORKER_INPUT_DATA_SNOOZE_MIN, snoozeMin)
            .build()

        return TestListenableWorkerBuilder<AlarmSchedulerWorker>(context)
            .setInputData(setAlarmData)
            .build()
    }

    @Test
    fun `When worker schedules a snooze, it schedules snooze alarm with AlarmManager`() = runTest {
        // Given
        val alarmId: Long = fixture.fixture()
        val snoozeMin: Int = fixture.fixture()
        val snoozeMinSlot = slot<Int>()
        val capturedIntent = slot<PendingIntent>()

        setupMockk(emptyList())
        mockkStatic(::getSnoozeTimeInMills)

        // When
        getSetSnoozeWorker(context, alarmId, snoozeMin).doWork()

        // Then
        verify(exactly = 1) { alarmManager.setAlarmClock(any(), capture((capturedIntent))) }
        verify(exactly = 1) { getSnoozeTimeInMills(capture(snoozeMinSlot)) }

        snoozeMinSlot.captured mustBe snoozeMin
        extractIntent(capturedIntent.captured)
            .getLongExtra(AlarmScheduler.EXTRA_ALARM_ID, 0L) mustBe alarmId
    }

    private fun extractIntent(pendingIntent: PendingIntent?): Intent =
        (Shadow.extract(pendingIntent) as ShadowPendingIntent)
            .savedIntent

    private fun getRandomEnabledAlarms(size: Int, idOffset: Int = 0) =
        List(size) { idx ->
            getRandomAlarm(
                id = (idx + idOffset).toLong(),
                enabled = true
            )
        }

    private fun getRandomAlarm(
        id: Long,
        hour: Int = fixture.fixture(range = 0..23),
        minute: Int = fixture.fixture(range = 0..59),
        weeklyRepeat: List<Int> = List(Random.nextInt(0, 8)) { Random.nextInt(1, 8) },
        label: String = fixture.fixture(),
        enabled: Boolean = fixture.fixture(),
        alertType: Int = Random.nextInt(AlertType.entries.size),
        ringtone: String = fixture.fixture(),
        sayItScripts: String = fixture.fixture(),
    ): Alarm =
        Alarm(
            id = id,
            Hour(hour),
            Minute(minute),
            WeeklyRepeat(weeklyRepeat.toSortedSet()),
            Label(label),
            enabled,
            AlertType.entries[alertType],
            Ringtone(ringtone),
            AlarmType.SAY_IT,
            SayItScripts(sayItScripts),
        )
}
