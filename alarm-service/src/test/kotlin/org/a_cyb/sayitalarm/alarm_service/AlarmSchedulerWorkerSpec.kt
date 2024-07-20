/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.alarm_service

import kotlin.random.Random
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.work.ListenableWorker
import androidx.work.testing.TestListenableWorkerBuilder
import io.mockk.clearMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.a_cyb.sayitalarm.alarm_service.AlarmScheduler.Companion.BUNDLE_KEY_ALARM_ID
import org.a_cyb.sayitalarm.alarm_service.AlarmScheduler.Companion.BUNDLE_KEY_ALERT_TYPE
import org.a_cyb.sayitalarm.alarm_service.AlarmScheduler.Companion.BUNDLE_KEY_IS_REPEAT
import org.a_cyb.sayitalarm.alarm_service.AlarmScheduler.Companion.BUNDLE_KEY_LABEL
import org.a_cyb.sayitalarm.alarm_service.AlarmScheduler.Companion.BUNDLE_KEY_RINGTONE_URI
import org.a_cyb.sayitalarm.alarm_service.AlarmScheduler.Companion.BUNDLE_KEY_SAYIT_SCRIPTS
import org.a_cyb.sayitalarm.alarm_service.AlarmScheduler.Companion.BUNDLE_KEY_SNOOZE
import org.a_cyb.sayitalarm.alarm_service.AlarmScheduler.Companion.BUNDLE_KEY_THEME
import org.a_cyb.sayitalarm.alarm_service.AlarmScheduler.Companion.BUNDLE_KEY_TIME_OUT
import org.a_cyb.sayitalarm.domain.repository.RepositoryContract
import org.a_cyb.sayitalarm.entity.Alarm
import org.a_cyb.sayitalarm.entity.AlarmType
import org.a_cyb.sayitalarm.entity.AlertType
import org.a_cyb.sayitalarm.entity.Hour
import org.a_cyb.sayitalarm.entity.Label
import org.a_cyb.sayitalarm.entity.Minute
import org.a_cyb.sayitalarm.entity.Ringtone
import org.a_cyb.sayitalarm.entity.SayItScripts
import org.a_cyb.sayitalarm.entity.Settings
import org.a_cyb.sayitalarm.entity.Snooze
import org.a_cyb.sayitalarm.entity.Theme
import org.a_cyb.sayitalarm.entity.TimeOut
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
import org.robolectric.shadow.api.Shadow
import org.robolectric.shadows.ShadowPendingIntent
import tech.antibytes.kfixture.fixture
import tech.antibytes.kfixture.kotlinFixture

@RunWith(AndroidJUnit4::class)
class AlarmSchedulerWorkerSpec {
    private lateinit var context: Context
    private lateinit var alarmSchedulerWorker: AlarmSchedulerWorker

    private val alarmRepository: RepositoryContract.AlarmRepository = mockk(relaxed = true)
    private val settingsRepository: RepositoryContract.SettingsRepository = mockk(relaxed = true)
    private val alarmManager: AlarmManager = mockk(relaxed = true)

    private val fixture = kotlinFixture()

    @Before
    fun setUp() {
        context = mockk(relaxed = true)
        alarmSchedulerWorker = TestListenableWorkerBuilder<AlarmSchedulerWorker>(context).build()

        every { context.getSystemService(AlarmManager::class.java) } returns
            alarmManager

        every { settingsRepository.getSettings() } returns
            flow { emit(Result.success(settings)) }

        val module = module {
            single<RepositoryContract.AlarmRepository> { alarmRepository }
            single<RepositoryContract.SettingsRepository> { settingsRepository }
            single<CoroutineDispatcher>(named("io")) { Dispatchers.IO }
        }

        startKoin {
            modules(module)
        }
    }

    @After
    fun tearDown() {
        stopKoin()
    }

    @Test
    fun `When doWork is invoked it schedules enabled alarms in the database`() = runTest {
        // Given
        val alarms = getRandomEnabledAlarms(size = 2)

        every { alarmRepository.getAllEnabledAlarm(any()) } returns
            async { alarms }

        // When
        val result = alarmSchedulerWorker.doWork()

        // Then
        verify(exactly = 2) { alarmManager.setAlarmClock(any(), any()) }

        result mustBe ListenableWorker.Result.success()
    }

    @Test
    fun `When doWork is invoked it only schedules alarms that have not been scheduled yet`() = runTest {
        // Given

        // Call for the first time with 2 alarms.
        val alarms = getRandomEnabledAlarms(size = 2)

        every { alarmRepository.getAllEnabledAlarm(any()) } returns
            async { alarms }

        alarmSchedulerWorker.doWork()

        verify(exactly = 2) { alarmManager.setAlarmClock(any(), any()) }
        clearMocks(alarmManager)

        // Call for the second time with 2 alarms already scheduled, plus 3 more alarms.
        val newAlarms = getRandomEnabledAlarms(size = 3, idOffset = 2)

        every { alarmRepository.getAllEnabledAlarm(any()) } returns
            async { alarms.plus(newAlarms) }

        // When
        alarmSchedulerWorker.doWork()

        // Then
        verify(exactly = 3) { alarmManager.setAlarmClock(any(), any()) }
    }

    @Test
    fun `When doWork is invoked it schedules broadcast pendingIntent`() = runTest {
        // Given
        val alarm = getRandomAlarm(id = 1, enabled = true)
        every { alarmRepository.getAllEnabledAlarm(any()) } returns
            async { listOf(alarm) }

        val capturedIntent = slot<PendingIntent>()
        every { alarmManager.setAlarmClock(any(), capture(capturedIntent)) } answers { mockk() }

        // When
        alarmSchedulerWorker.doWork()

        // Then
        capturedIntent.isCaptured mustBe true
        capturedIntent.captured.isBroadcast mustBe true
        capturedIntent.captured.isImmutable mustBe true
        capturedIntent.captured.creatorPackage?.contains("org.a_cyb.sayitalarm.alarm_service") mustBe true
    }

    @Test
    fun `When doWork is invoked it constructs intent with alarm data`() = runTest {
        // Given
        val alarm = getRandomAlarm(id = 1, enabled = true)
        every { alarmRepository.getAllEnabledAlarm(any()) } returns async { listOf(alarm) }

        val capturedIntent = slot<PendingIntent>()
        every { alarmManager.setAlarmClock(any(), capture(capturedIntent)) } answers { mockk() }

        alarmSchedulerWorker.doWork()

        // When
        val actual = extractIntent(capturedIntent.captured)

        // Then
        actual.action mustBe "org.a_cyb.sayitalarm.DELIVER_ALARM"
        actual.component!!.shortClassName mustBe AlarmBroadcastReceiver::class.qualifiedName
        actual.flags mustBe Intent.FLAG_RECEIVER_FOREGROUND

        val extras = actual.extras!!
        extras.getLong(BUNDLE_KEY_ALARM_ID) mustBe alarm.id
        extras.getBoolean(BUNDLE_KEY_IS_REPEAT) mustBe alarm.weeklyRepeat.weekdays.isNotEmpty()
        extras.getString(BUNDLE_KEY_LABEL) mustBe alarm.label.label
        extras.getInt(BUNDLE_KEY_ALERT_TYPE) mustBe alarm.alertType.ordinal
        extras.getString(BUNDLE_KEY_RINGTONE_URI) mustBe alarm.ringtone.ringtone
        extras.getStringArray(BUNDLE_KEY_SAYIT_SCRIPTS)?.get(0) mustBe alarm.sayItScripts.scripts[0]
        extras.getInt(BUNDLE_KEY_TIME_OUT) mustBe settings.timeOut.timeOut
        extras.getInt(BUNDLE_KEY_SNOOZE) mustBe settings.snooze.snooze
        extras.getInt(BUNDLE_KEY_THEME) mustBe settings.theme.ordinal
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

    private val settings = Settings(
        timeOut = TimeOut(180),
        snooze = Snooze(15),
        theme = Theme.LIGHT,
    )
}
