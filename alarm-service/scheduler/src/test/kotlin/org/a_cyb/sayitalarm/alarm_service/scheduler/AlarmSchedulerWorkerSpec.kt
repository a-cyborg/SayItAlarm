/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.alarm_service.scheduler

import android.app.AlarmManager
import android.app.AlarmManager.AlarmClockInfo
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
import io.mockk.unmockkAll
import io.mockk.verify
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.a_cyb.sayitalarm.alarm_service.core.AlarmReceiver
import org.a_cyb.sayitalarm.alarm_service.scheduler.AlarmScheduler.Companion.SCHEDULER_WORKER_INPUT_DATA_ALARM_ID
import org.a_cyb.sayitalarm.alarm_service.scheduler.AlarmScheduler.Companion.SCHEDULER_WORKER_INPUT_DATA_SNOOZE_MIN
import org.a_cyb.sayitalarm.alarm_service.scheduler.AlarmScheduler.Companion.SCHEDULER_WORKER_WORK_CANCEL_ALARM
import org.a_cyb.sayitalarm.alarm_service.scheduler.AlarmScheduler.Companion.SCHEDULER_WORKER_WORK_SET_ALARM
import org.a_cyb.sayitalarm.alarm_service.scheduler.AlarmScheduler.Companion.SCHEDULER_WORKER_WORK_SET_SNOOZE
import org.a_cyb.sayitalarm.alarm_service.scheduler.AlarmScheduler.Companion.SCHEDULER_WORKER_WORK_TYPE
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
import java.time.Instant
import java.time.ZoneId
import kotlin.random.Random
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(AndroidJUnit4::class)
@Config(sdk = [33])
class AlarmSchedulerWorkerSpec {
    private val contextStub: Context = mockk(relaxed = true)
    private val alarmRepositoryStub: RepositoryContract.AlarmRepository = mockk(relaxed = true)
    private val alarmManagerMock: AlarmManager = mockk(relaxed = true)
    private val alarmSchedulerWorker = TestListenableWorkerBuilder<AlarmSchedulerWorker>(contextStub)
    private val fixture = kotlinFixture()

    @Before
    fun setUp() {
        every { contextStub.getSystemService(AlarmManager::class.java) } returns alarmManagerMock

        startKoin {
            modules(
                module {
                    single<RepositoryContract.AlarmRepository> { alarmRepositoryStub }
                    single<CoroutineDispatcher>(named("io")) { Dispatchers.IO }
                },
            )
        }

        Dispatchers.setMain(StandardTestDispatcher())
    }

    @After
    fun tearDown() {
        unmockkAll()
        stopKoin()
        Dispatchers.resetMain()
    }

    @Test
    fun `When a worker is called with SET_ALARM data, it schedules alarms`() = runTest {
        // Given
        val numOfAlarms: Int = fixture.fixture(range = 0..10)
        val inputData = Data.Builder().putInt(SCHEDULER_WORKER_WORK_TYPE, SCHEDULER_WORKER_WORK_SET_ALARM).build()
        val worker = alarmSchedulerWorker.setInputData(inputData).build()

        every { alarmRepositoryStub.getAllEnabledAlarm(any()) } returns async { getFakeAlarms(numOfAlarms) }

        // When
        val result = worker.doWork()

        // Then
        @Suppress("DeferredResultUnused")
        verify(exactly = 1) { alarmRepositoryStub.getAllEnabledAlarm(any()) }
        verify(exactly = numOfAlarms) { alarmManagerMock.setAlarmClock(any(), any()) }
        assertEquals(ListenableWorker.Result.success(), result)
    }

    @Test
    fun `When a worker schedules an alarm, it calls setAlarmClock with PendingIntent`() = runTest {
        // Given
        val alarm = getFakeAlarm(enabled = true)
        val capturedPendingIntent = slot<PendingIntent>()
        val capturedAlarmClock = slot<AlarmClockInfo>()
        val inputData = Data.Builder().putInt(SCHEDULER_WORKER_WORK_TYPE, SCHEDULER_WORKER_WORK_SET_ALARM).build()
        val worker = alarmSchedulerWorker.setInputData(inputData).build()

        every { alarmRepositoryStub.getAllEnabledAlarm(any()) } returns async { listOf(alarm) }
        every {
            alarmManagerMock.setAlarmClock(capture(capturedAlarmClock), capture(capturedPendingIntent))
        } answers { }

        // When
        worker.doWork()

        // Then
        assertTrue(capturedPendingIntent.isCaptured)
        assertTrue(capturedPendingIntent.captured.isBroadcast)
        assertTrue(capturedPendingIntent.captured.isImmutable)

        val pendingIntent = (Shadow.extract(capturedPendingIntent.captured) as ShadowPendingIntent).savedIntent
        assertEquals(AlarmReceiver::class.qualifiedName, pendingIntent.component!!.shortClassName)
        assertEquals(AlarmReceiver.INTENT_ACTION_DELIVER_ALARM, pendingIntent.action)
        assertEquals(Intent.FLAG_RECEIVER_FOREGROUND, pendingIntent.flags)
        assertEquals(alarm.id, pendingIntent.getLongExtra(AlarmReceiver.INTENT_EXTRA_ALARM_ID, 0L))

        val actualAlarmClock =
            Instant.ofEpochMilli(capturedAlarmClock.captured.triggerTime).atZone(ZoneId.systemDefault())
        assertEquals(alarm.hour.hour, actualAlarmClock.hour)
        assertEquals(alarm.minute.minute, actualAlarmClock.minute)
        assertEquals(0, actualAlarmClock.second)
    }

    @Test
    fun `When a worker schedules alarms, it calls setAlarmClocks only for unscheduled alarms`() = runTest {
        // Given
        val initialAlarms = getFakeAlarms(size = 2)
        val additionalAlarms = getFakeAlarms(size = 3, idOffset = 2)
        val inputData = Data.Builder().putInt(SCHEDULER_WORKER_WORK_TYPE, SCHEDULER_WORKER_WORK_SET_ALARM).build()
        val worker = alarmSchedulerWorker.setInputData(inputData).build()

        every { alarmRepositoryStub.getAllEnabledAlarm(any()) } returns async { initialAlarms }

        worker.doWork() // Schedule alarms only for initial alarms.

        clearMocks(alarmManagerMock)

        every { alarmRepositoryStub.getAllEnabledAlarm(any()) } returns async { initialAlarms.plus(additionalAlarms) }

        // When
        worker.doWork()

        // Then
        verify(exactly = 3) { alarmManagerMock.setAlarmClock(any(), any()) }
    }

    @Test
    fun `When a worker is called with SET_SNOOZE data, it schedules snooze alarm`() = runTest {
        // Given
        mockkStatic(::getSnoozeTimeInMills)

        val alarmId: Long = fixture.fixture()
        val snoozeMin: Int = fixture.fixture()
        val snoozeMinSlot = slot<Int>()
        val capturedIntent = slot<PendingIntent>()
        val inputData = Data.Builder()
            .putInt(SCHEDULER_WORKER_WORK_TYPE, SCHEDULER_WORKER_WORK_SET_SNOOZE)
            .putLong(SCHEDULER_WORKER_INPUT_DATA_ALARM_ID, alarmId)
            .putInt(SCHEDULER_WORKER_INPUT_DATA_SNOOZE_MIN, snoozeMin)
            .build()
        val worker = alarmSchedulerWorker.setInputData(inputData).build()

        // When
        worker.doWork()

        // Then
        verify(exactly = 1) { alarmManagerMock.setAlarmClock(any(), capture((capturedIntent))) }
        verify(exactly = 1) { getSnoozeTimeInMills(capture(snoozeMinSlot)) }

        assertTrue(capturedIntent.isCaptured)
        assertTrue(capturedIntent.captured.isBroadcast)
        assertTrue(capturedIntent.captured.isImmutable)

        val pendingIntent = (Shadow.extract(capturedIntent.captured) as ShadowPendingIntent).savedIntent
        assertEquals(AlarmReceiver::class.qualifiedName, pendingIntent.component!!.shortClassName)
        assertEquals(AlarmReceiver.INTENT_ACTION_DELIVER_ALARM, pendingIntent.action)
        assertEquals(alarmId, pendingIntent.getLongExtra(AlarmReceiver.INTENT_EXTRA_ALARM_ID, 0L))
        assertEquals(Intent.FLAG_RECEIVER_FOREGROUND, pendingIntent.flags)

        assertEquals(snoozeMin, snoozeMinSlot.captured)
    }

    @Test
    fun `When a worker is called with CANCEL_ALARM data, it cancels the alarm`() = runTest {
        // Given
        val alarmId: Int = fixture.fixture()
        val capturedIntent = slot<PendingIntent>()
        val cancelAlarmData = Data.Builder()
            .putInt(SCHEDULER_WORKER_WORK_TYPE, SCHEDULER_WORKER_WORK_CANCEL_ALARM)
            .putLong(SCHEDULER_WORKER_INPUT_DATA_ALARM_ID, alarmId.toLong())
            .build()
        val worker = TestListenableWorkerBuilder<AlarmSchedulerWorker>(contextStub)
            .setInputData(cancelAlarmData)
            .build()

        // When
        worker.doWork()

        // Then
        verify(exactly = 1) { alarmManagerMock.cancel(capture(capturedIntent)) }

        assertTrue(capturedIntent.isCaptured)
        assertTrue(capturedIntent.captured.isImmutable)
        assertTrue(capturedIntent.captured.isBroadcast)

        val pendingIntent = (Shadow.extract(capturedIntent.captured) as ShadowPendingIntent).savedIntent
        assertEquals(AlarmReceiver.INTENT_ACTION_DELIVER_ALARM, pendingIntent.action)
        assertEquals(Intent.FLAG_RECEIVER_FOREGROUND, pendingIntent.flags)
        assertEquals(AlarmReceiver::class.qualifiedName, pendingIntent.component!!.shortClassName)
        assertEquals(
            alarmId.toLong(),
            pendingIntent.getLongExtra(AlarmReceiver.INTENT_EXTRA_ALARM_ID, 0L),
        )
    }

    private fun getFakeAlarms(size: Int, idOffset: Int = 0) =
        List(size) { getFakeAlarm(id = (it + idOffset), enabled = true) }

    private fun getFakeAlarm(
        id: Int = fixture.fixture(),
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
            id = id.toLong(),
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
