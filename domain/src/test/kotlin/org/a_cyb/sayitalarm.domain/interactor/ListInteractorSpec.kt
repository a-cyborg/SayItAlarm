/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.domain.interactor

import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import java.util.Calendar.FRIDAY
import java.util.Calendar.MONDAY
import java.util.Calendar.SATURDAY
import java.util.Calendar.SUNDAY
import java.util.Calendar.THURSDAY
import java.util.Calendar.TUESDAY
import java.util.Calendar.WEDNESDAY
import app.cash.turbine.test
import io.mockk.clearAllMocks
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.a_cyb.sayitalarm.domain.alarm_service.AlarmServiceContract
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
import org.a_cyb.sayitalarm.util.fulfils
import org.a_cyb.sayitalarm.util.mustBe
import org.junit.Test

@ExperimentalCoroutinesApi
class ListInteractorSpec {

    private val alarmRepository: RepositoryContract.AlarmRepository = mockk(relaxed = true)
    private val alarmScheduler: AlarmServiceContract.AlarmScheduler = mockk(relaxed = true)

    private lateinit var interactor: ListInteractor

    @BeforeTest
    fun setup() {
        interactor = ListInteractor(alarmRepository, alarmScheduler)
        Dispatchers.setMain(StandardTestDispatcher())
    }

    @AfterTest
    fun clear() {
        clearAllMocks()
        Dispatchers.resetMain()
    }

    @Test
    fun `When getAllAlarms is called it starts to collect flow`() = runTest {
        // Given
        val exception = IllegalStateException()

        every { alarmRepository.getAllAlarms() } returns flow {
            emit(Result.failure(exception))
        }

        // When
        interactor.getAllAlarms().test {
            // Then
            awaitItem() mustBe Result.failure(exception)

            awaitComplete()
        }

        coVerify(exactly = 1) { alarmRepository.getAllAlarms() }
    }

    @Test
    fun `When getAllAlarms is called it propagates success with alarms`() = runTest {
        // Given
        every { alarmRepository.getAllAlarms() } returns flow {
            emit(Result.success(alarms))
        }

        // When
        interactor.getAllAlarms().test {
            // Then
            awaitItem() mustBe Result.success(alarms)

            awaitComplete()
        }
    }

    @Test
    fun `When setEnable is called it triggers AlarmRepository updatedEnabled`() = runTest {
        // When
        interactor.setEnabled(3L, true, this)
        runCurrent()

        // Then
        verify(exactly = 1) {
            alarmRepository.updateEnabled(any(), any(), any())
        }
    }

    @Test
    fun `When setEnable is called with enabled true it triggers AlarmScheduler setAlarm`() = runTest {
        // When
        interactor.setEnabled(3L, true, this)
        runCurrent()

        // Then
        coVerify(exactly = 1) {
            alarmScheduler.setAlarm(any())
        }
    }

    @Test
    fun `When setEnable is called with enabled false it triggers AlarmScheduler cancelAlarm`() = runTest {
        // When
        interactor.setEnabled(3L, false, this)
        runCurrent()

        // Then
        coVerify(exactly = 1) {
            alarmScheduler.cancelAlarm(any(), any())
        }
    }

    @Test
    fun `When deleteAlarm is called it triggers AlarmRepository delete`() = runTest {
        // When
        interactor.deleteAlarm(3L, this)
        this.testScheduler.runCurrent()

        // Then
        verify(exactly = 1) {
            alarmRepository.delete(any(), any())
        }
    }

    @Test
    fun `When deleteAlarm is called it triggers AlarmScheduler cancelAlarm`() = runTest {
        // When
        interactor.deleteAlarm(3L, this)
        this.testScheduler.runCurrent()

        // Then
        coVerify(exactly = 1) {
            alarmScheduler.cancelAlarm(any(), any())
        }
    }

    @Test
    fun `It fulfills ListInteractor`() {
        interactor fulfils InteractorContract.ListInteractor::class
    }

    private val alarms = listOf(
        Alarm(
            id = 1,
            hour = Hour(6),
            minute = Minute(0),
            weeklyRepeat = WeeklyRepeat(MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY),
            label = Label("Wake Up"),
            enabled = true,
            alertType = AlertType.SOUND_ONLY,
            ringtone = Ringtone("file://wake_up_alarm.mp3"),
            alarmType = AlarmType.SAY_IT,
            sayItScripts = SayItScripts(
                "I am peaceful and whole.",
                "I do all things in love.",
                "I embrace change seamlessly and rise to the new opportunity it presents."
            )
        ),
        Alarm(
            id = 2,
            hour = Hour(20),
            minute = Minute(30),
            weeklyRepeat = WeeklyRepeat(MONDAY, WEDNESDAY, FRIDAY),
            label = Label("Workout"),
            enabled = true,
            alertType = AlertType.VIBRATE_ONLY,
            ringtone = Ringtone("file://workout_time_alarm.mp3"),
            alarmType = AlarmType.SAY_IT,
            sayItScripts = SayItScripts(
                "My body is strong.",
                "I am thankful for my body.",
                "My body helps me play.",
                "I am worthy of investing time and effort into my physical health."
            )
        ),
        Alarm(
            id = 3,
            hour = Hour(9),
            minute = Minute(0),
            weeklyRepeat = WeeklyRepeat(SUNDAY, SATURDAY),
            label = Label("Passion Hour"),
            enabled = false,
            alertType = AlertType.SOUND_AND_VIBRATE,
            ringtone = Ringtone("file://passion_hour_ringtone.mp3"),
            alarmType = AlarmType.SAY_IT,
            sayItScripts = SayItScripts(
                "I'm ready to dive into my passion",
                "Ready to explore, ready to learn.",
                "I embrace this hour with enthusiasm."
            )
        )
    )
}
