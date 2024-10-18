/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.alarm_service.core

import app.cash.turbine.test
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.a_cyb.sayitalarm.domain.alarm_service.AlarmServiceContract
import org.a_cyb.sayitalarm.domain.alarm_service.AlarmServiceContract.AlarmServiceController.ControllerState
import org.a_cyb.sayitalarm.domain.alarm_service.AlarmServiceContract.AlarmServiceController.ControllerState.Initial
import org.a_cyb.sayitalarm.domain.alarm_service.AlarmServiceContract.AlarmServiceController.ControllerState.Ringing
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
import org.a_cyb.sayitalarm.presentation.viewmodel.SettingsViewModel
import org.a_cyb.sayitalarm.util.test_utils.fulfils
import org.a_cyb.sayitalarm.util.test_utils.mustBe
import org.junit.After
import org.junit.Before
import org.junit.Test
import tech.antibytes.kfixture.fixture
import tech.antibytes.kfixture.kotlinFixture
import java.util.Calendar.FRIDAY
import java.util.Calendar.MONDAY
import java.util.Calendar.THURSDAY
import java.util.Calendar.TUESDAY
import java.util.Calendar.WEDNESDAY

@OptIn(ExperimentalCoroutinesApi::class)
class AlarmServiceControllerSpec {

    private lateinit var controller: AlarmServiceController

    private val alarmRepository: RepositoryContract.AlarmRepository = mockk(relaxed = true)
    private val settingsRepository: RepositoryContract.SettingsRepository = mockk(relaxed = true)
    private val alarmService: AlarmServiceContract.AlarmService = mockk(relaxed = true)
    private val alarmScheduler: AlarmServiceContract.AlarmScheduler = mockk(relaxed = true)

    private val fixture = kotlinFixture()

    @Before
    fun setup() {
        Dispatchers.setMain(StandardTestDispatcher())

        every { alarmRepository.getAlarm(any(), any()) } returns
            CompletableDeferred(Result.success(alarm))

        every { settingsRepository.getSettings() } returns
            flow { emit(Result.success(SettingsViewModel.getDefaultSettings())) }

        controller = AlarmServiceController(
            alarmRepository,
            settingsRepository,
            alarmScheduler,
            CoroutineScope(StandardTestDispatcher()),
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `It is in the Initial state`() {
        controller.controllerState.value mustBe Initial
    }

    @Test
    fun `When onServiceBind is invoked and the alarm is fetched, it rings the alarm and update the state`() = runTest {
        // Given
        controller.controllerState.test {
            skipItems(1)

            // When
            controller.onServiceBind(alarmService, alarm.id)

            // Then
            awaitItem() mustBe Ringing(alarm.label)
        }

        verify(exactly = 1) { alarmService.ringAlarm(any(), any()) }
    }

    @Test
    fun `When onServiceBind is invoked and the alarm fetch is failed, it terminate the service`() = runTest {
        // Given
        every { alarmRepository.getAlarm(any(), any()) } returns
            CompletableDeferred(Result.failure(IllegalStateException()))

        controller.controllerState.test {
            skipItems(1)

            // When
            controller.onServiceBind(alarmService, alarm.id)
            advanceUntilIdle()

            // Then
            verify(exactly = 1) { alarmService.stopService() }
        }
    }

    @Test
    fun `When onServiceBind is invoked and the settings is fetched, it sets the settings`() = runTest {
        // Given
        val captured = slot<Snooze>()
        val settings = Settings(
            TimeOut(fixture.fixture()),
            Snooze(fixture.fixture()),
            Theme.DARK,
        )
        every { settingsRepository.getSettings() } returns flow { emit(Result.success(settings)) }

        controller.controllerState.test {
            // Given
            controller.onServiceBind(alarmService, alarm.id)
            skipItems(2)

            // When
            controller.startSnooze()

            // Then
            verify(exactly = 1) { alarmScheduler.scheduleSnooze(alarm.id, capture(captured)) }
            captured.captured mustBe settings.snooze
        }
    }

    @Test
    fun `When onServiceBind is invoked and the settings fetch is failed, it sets default settings`() = runTest {
        // Given
        val captured = slot<Snooze>()
        every { settingsRepository.getSettings() } returns
            flow { emit(Result.failure(IllegalStateException())) }

        controller.controllerState.test {
            // Given
            controller.onServiceBind(alarmService, alarm.id)
            skipItems(2)

            // When
            controller.startSnooze()

            // Then
            verify(exactly = 1) { alarmScheduler.scheduleSnooze(any(), capture(captured)) }
            captured.captured mustBe SettingsViewModel.getDefaultSettings().snooze
        }
    }

    @Test
    fun `When the service is disconnected, it sets the state to Error`() = runTest {
        // Given
        val alarmService: AlarmServiceContract.AlarmService = mockk(relaxed = true)

        controller.controllerState.test {
            controller.onServiceBind(alarmService, alarm.id)
            skipItems(2)

            // When
            controller.onServiceDisconnected()

            // Then
            awaitItem() mustBe ControllerState.Error
        }
    }

    @Test
    fun `When the service is bound and startSayIt is called, it sets the state to RunningSayIt and invokes the service function`() =
        runTest {
            // Given
            val alarmService: AlarmServiceContract.AlarmService = mockk(relaxed = true)

            controller.controllerState.test {
                controller.onServiceBind(alarmService, alarm.id)
                skipItems(2)

                // When
                controller.startSayIt()

                // Then
                awaitItem() mustBe ControllerState.RunningSayIt(alarm.sayItScripts)
            }

            verify(exactly = 1) { alarmService.startSayIt() }
        }

    @Test
    fun `When the service is unbound and startSayIt is called, it sets the state to error`() = runTest {
        controller.controllerState.test {
            // Given
            skipItems(1)

            // When
            controller.startSayIt()

            // Then
            awaitItem() mustBe ControllerState.Error
        }
    }

    @Test
    fun `When the service is bound and startSnooze is called, it invokes the service snooze`() = runTest {
        controller.controllerState.test {
            // Given
            controller.onServiceBind(alarmService, alarm.id)
            skipItems(2)

            // When
            controller.startSnooze()

            // Then
            verify(exactly = 1) { alarmService.startSnooze() }
            verify(exactly = 1) { alarmScheduler.scheduleSnooze(any(), any()) }
        }
    }

    @Test
    fun `When the service is unbound and snooze is called, it sets the state to error`() = runTest {
        controller.controllerState.test {
            // Given
            skipItems(1)

            // When
            controller.startSnooze()

            // Then
            awaitItem() mustBe ControllerState.Error
        }
    }

    @Test
    fun `When terminate is called, it invokes service stopService`() = runTest {
        // Given
        val alarmService: AlarmServiceContract.AlarmService = mockk(relaxed = true)

        controller.onServiceBind(alarmService, alarm.id)

        // When
        controller.terminate()

        // Then
        verify(exactly = 1) { alarmService.stopService() }
    }

    @Test
    fun `When scheduleNextAlarm is called, it invokes Scheduler scheduleAlarms`() = runTest {
        // Given
        val scope = CoroutineScope(UnconfinedTestDispatcher())

        controller.onServiceBind(alarmService, alarm.id)
        advanceUntilIdle()

        // When
        controller.scheduleNextAlarm(scope)

        // Then
        coVerify { alarmScheduler.scheduleAlarms() }
    }

    @Test
    fun `When scheduleNextAlarm is called and the alarm is not repeatable, it invokes alarmRepository update`() =
        runTest {
            // Given
            val scope = CoroutineScope(UnconfinedTestDispatcher())
            val captured = slot<Alarm>()

            every { alarmRepository.getAlarm(any(), any()) } returns
                CompletableDeferred(Result.success(alarm.copy(weeklyRepeat = WeeklyRepeat())))

            controller.onServiceBind(alarmService, alarm.id)
            advanceUntilIdle()

            // When
            controller.scheduleNextAlarm(scope)

            // Then
            coVerify { alarmRepository.update(capture(captured), any()) }
            captured.captured.enabled mustBe false
        }

    @Test
    fun `It fulfills AlarmServiceControllerContract`() {
        controller fulfils AlarmServiceContract.AlarmServiceController::class
    }

    private val alarm = Alarm(
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
            "I embrace change seamlessly and rise to the new opportunity it presents.",
        ),
    )
}
