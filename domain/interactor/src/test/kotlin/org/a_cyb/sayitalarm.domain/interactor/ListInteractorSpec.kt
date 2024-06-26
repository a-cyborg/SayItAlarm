/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.domain.interactor

import kotlin.test.BeforeTest
import java.util.Calendar.FRIDAY
import java.util.Calendar.MONDAY
import java.util.Calendar.SATURDAY
import java.util.Calendar.SUNDAY
import java.util.Calendar.THURSDAY
import java.util.Calendar.TUESDAY
import java.util.Calendar.WEDNESDAY
import app.cash.turbine.test
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.async
import kotlinx.coroutines.test.runTest
import org.a_cyb.sayitalarm.alarm_service.AlarmSchedulerContract
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
import org.a_cyb.sayitalarm.presentation.interactor.InteractorContract
import org.a_cyb.sayitalarm.util.fulfils
import org.a_cyb.sayitalarm.util.mustBe
import org.junit.Test

class ListInteractorSpec {

    private val alarmRepository: RepositoryContract.AlarmRepository = mockk(relaxed = true)
    private val alarmSchedular: AlarmSchedulerContract = mockk(relaxed = true)

    private lateinit var interactor: ListInteractor

    @BeforeTest
    fun setup() {
        interactor = ListInteractor(alarmRepository, alarmSchedular)
    }

    @Test
    fun `When load is called it propagates failure with exception`() = runTest {
        // Given
        val exception = IllegalStateException()

        every { alarmRepository.load(any()) } returns async { Result.failure(exception) }

        interactor.alarms.test {
            // When
            interactor.load(this)

            // Then
            awaitItem() mustBe Result.failure(exception)
        }
    }

    @Test
    fun `When load is called it propagates success with alarms`() = runTest {
        // Given
        every { alarmRepository.load(any()) } returns async { Result.success(alarms) }

        interactor.alarms.test {
            // When
            interactor.load(this)

            // Then
            awaitItem() mustBe Result.success(alarms)
        }
    }

    @Test
    fun `When load is called it triggers AlarmRepository load`() = runTest {
        // Given
        every { alarmRepository.load(any()) } returns async { Result.success(emptyList()) }

        interactor.alarms.test {
            // When
            interactor.load(this)

            skipItems(1)
        }

        // Then
        verify(exactly = 1) {
            @Suppress("DeferredResultUnused")
            alarmRepository.load(any())
        }
    }

    @Test
    fun `When setEnable is called it propagates success with updated alarms`() = runTest {
        // Given
        val alarms = alarms.map { it.copy(enabled = !it.enabled) }

        every { alarmRepository.load(any()) } returns async { Result.success(alarms) }

        interactor.alarms.test {
            // When
            interactor.setEnabled(2, false, this)

            // Then
            awaitItem() mustBe Result.success(alarms)
        }
    }

    @Test
    fun `When setEnable is called it triggers AlarmRepository updatedEnabled and load`() = runTest {
        // Given
        every { alarmRepository.load(any()) } returns async { Result.success(emptyList()) }

        interactor.alarms.test {
            // When
            interactor.setEnabled(3L, true, this)

            skipItems(1)
        }

        // Then
        verify(exactly = 1) { alarmRepository.updateEnabled(any(), any(), any()) }
        verify(exactly = 1) {
            @Suppress("DeferredResultUnused")
            alarmRepository.load(any())
        }
    }

    @Test
    fun `When setEnable is called with enabled true it triggers AlarmScheduler setAlarm`() = runTest {
        // Given
        every { alarmRepository.load(any()) } returns async { Result.success(emptyList()) }

        interactor.alarms.test {
            // When
            interactor.setEnabled(3L, true, this)

            skipItems(1)
        }

        coVerify(exactly = 1) { alarmSchedular.setAlarm(any()) }

    }

    @Test
    fun `When setEnable is called with enabled false it triggers AlarmScheduler cancelAlarm`() = runTest {
        // Given
        every { alarmRepository.load(any()) } returns async { Result.success(emptyList()) }

        interactor.alarms.test {
            // When
            interactor.setEnabled(3L, false, this)

            skipItems(1)
        }

        coVerify(exactly = 1) { alarmSchedular.cancelAlarm(any(), any()) }
    }

    @Test
    fun `When deleteAlarm is called it propagates success with updated alarms`() = runTest {
        // Given
        val alarms = alarms.drop(1)

        every { alarmRepository.load(any()) } returns async { Result.success(alarms) }

        interactor.alarms.test {
            // When
            interactor.deleteAlarm(1, this)

            // Then
            awaitItem() mustBe Result.success(alarms)
        }
    }

    @Test
    fun `When deleteAlarm is called it triggers AlarmRepository delete and load`() = runTest {
        // Given
        every { alarmRepository.load(any()) } returns async { Result.success(emptyList()) }

        interactor.alarms.test {
            // When
            interactor.deleteAlarm(3L, this)

            skipItems(1)
        }

        // Then
        verify(exactly = 1) { alarmRepository.delete(any(), any()) }
        verify(exactly = 1) {
            @Suppress("DeferredResultUnused")
            alarmRepository.load(any())
        }
    }

    @Test
    fun `When deleteAlarm is called it triggers AlarmScheduler cancelAlarm`() = runTest {
        // Given
        every { alarmRepository.load(any()) } returns async { Result.success(emptyList()) }

        interactor.alarms.test {
            // When
            interactor.deleteAlarm(3L, this)

            skipItems(1)
        }

        // Then
        coVerify(exactly = 1) { alarmSchedular.cancelAlarm(any(), any()) }
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
