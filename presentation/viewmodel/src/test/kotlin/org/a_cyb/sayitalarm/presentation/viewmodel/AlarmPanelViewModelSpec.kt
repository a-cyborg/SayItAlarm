/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.presentation.viewmodel

import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import android.icu.util.Calendar
import app.cash.turbine.test
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.a_cyb.sayitalarm.entity.Alarm
import org.a_cyb.sayitalarm.entity.AlertType
import org.a_cyb.sayitalarm.entity.Hour
import org.a_cyb.sayitalarm.entity.Label
import org.a_cyb.sayitalarm.entity.Minute
import org.a_cyb.sayitalarm.entity.Ringtone
import org.a_cyb.sayitalarm.entity.SayItScripts
import org.a_cyb.sayitalarm.entity.WeeklyRepeat
import org.a_cyb.sayitalarm.presentation.CommandContract
import org.a_cyb.sayitalarm.presentation.alarm_panel.AlarmPanelContract
import org.a_cyb.sayitalarm.presentation.interactor.InteractorContract
import org.a_cyb.sayitalarm.presentation.viewmodel.fake.EnumFormatterFake
import org.a_cyb.sayitalarm.presentation.viewmodel.fake.RingtoneManagerFake
import org.a_cyb.sayitalarm.presentation.viewmodel.fake.TestAlarms
import org.a_cyb.sayitalarm.presentation.viewmodel.fake.WeekdayFormatterFake
import org.a_cyb.sayitalarm.util.fulfils
import org.a_cyb.sayitalarm.util.mustBe
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class AlarmPanelViewModelSpec {

    private val testAlarms = TestAlarms().getAlarms()
    private val defaultAlarmUI = AlarmPanelContract.AlarmUI(
        hour = 8,
        minute = 0,
        weeklyRepeat = "",
        label = "",
        alertType = "Sound and vibration",
        ringtone = "Radial",
        sayItScripts = emptyList()
    )

    private fun getAlarmPanelViewModel(id: Long = 0L, scope: CoroutineScope = TestScope()) =
        AlarmPanelViewModel(
            id,
            scope,
            AlarmPanelInteractorFake(),
            RingtoneManagerFake(),
            WeekdayFormatterFake(),
            EnumFormatterFake(),
        )

    @BeforeTest
    fun setup() {
        Dispatchers.setMain(StandardTestDispatcher())
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `It fulfills AlarmPanelContract`() {
        // Given
        val panelViewModel = getAlarmPanelViewModel()

        // Then
        panelViewModel fulfils AlarmPanelContract.AlarmPanelViewModel::class
    }

    @Test
    fun `It emits default AlarmUI`() = runTest {
        // Given
        val panelViewModel = getAlarmPanelViewModel()

        // Then
        panelViewModel.alarmUI.test {
            awaitItem() mustBe defaultAlarmUI
        }
    }

    @Test
    fun `When alarm id is given it emits alarmUI of it`() = runTest {
        // Given
        val panelViewModel = getAlarmPanelViewModel(2)

        // Then
        panelViewModel.alarmUI.test {
            skipItems(1)
            awaitItem() mustBe AlarmPanelContract.AlarmUI(
                20, 30, "Mon, Wed, and Fri", "Workout", "Sound only",
                "file://workout_time_alarm.mp3", testAlarms[1].sayItScripts.scripts,
            )
        }
    }

    @Test
    fun `When setTime is called it emits alarmUI`() = runTest {
        // Given
        val panelViewModel = getAlarmPanelViewModel()

        // When
        panelViewModel.setTime(Hour(11), Minute(33))

        panelViewModel.alarmUI.test {
            skipItems(1)

            // Then
            awaitItem() mustBe defaultAlarmUI.copy(hour = 11, minute = 33)
        }
    }

    @Test
    fun `When setWeeklyRepeat is called it emits alarmUI`() = runTest {
        // Given
        val panelViewModel = getAlarmPanelViewModel()

        // When
        panelViewModel.setWeeklyRepeat(WeeklyRepeat(Calendar.WEDNESDAY))

        panelViewModel.alarmUI.test {
            skipItems(1)

            // Then
            awaitItem() mustBe defaultAlarmUI.copy(weeklyRepeat = "Wed")
        }
    }

    @Test
    fun `When setLabel is called it emits alarmUI`() = runTest {
        // Given
        val panelViewModel = getAlarmPanelViewModel()
        val label = Label("TestLabel")

        // When
        panelViewModel.setLabel(label)

        panelViewModel.alarmUI.test {
            skipItems(1)

            // Then
            awaitItem() mustBe defaultAlarmUI.copy(label = label.label)
        }
    }

    @Test
    fun `When setAlertType is called it emits alarmUI`() = runTest {
        // Given
        val panelViewModel = getAlarmPanelViewModel()
        val alertType = AlertType.SOUND_ONLY

        // When
        panelViewModel.setAlertType(alertType)

        panelViewModel.alarmUI.test {
            skipItems(1)

            // Then
            awaitItem() mustBe defaultAlarmUI.copy(alertType = "Sound only")
        }
    }

    @Test
    fun `When setRingtone is called it emits alarmUI`() = runTest {
        // Given
        val panelViewModel = getAlarmPanelViewModel()
        val ringtone = Ringtone("paradise.mp3")

        // When
        panelViewModel.setRingtone(ringtone)

        panelViewModel.alarmUI.test {
            skipItems(1)

            // Then
            awaitItem() mustBe defaultAlarmUI.copy(ringtone = ringtone.ringtone)
        }
    }

    @Test
    fun `When setScripts is called it emits alarmUI`() = runTest {
        // Given
        val panelViewModel = getAlarmPanelViewModel()
        val sayItScripts = SayItScripts(listOf("All is well"))

        // When
        panelViewModel.setScripts(sayItScripts)

        panelViewModel.alarmUI.test {
            skipItems(1)

            // Then
            awaitItem() mustBe defaultAlarmUI.copy(sayItScripts = sayItScripts.scripts)
        }
    }

    @Test
    fun `Given runCommand is called it executes the given command`() {
        // Given
        val command: CommandContract.Command<AlarmPanelViewModel> = mockk(relaxed = true)

        // When
        getAlarmPanelViewModel().runCommand(command)

        // Then
        verify(exactly = 1) { command.execute(any()) }
    }
}

private class AlarmPanelInteractorFake : InteractorContract.AlarmPanelInteractor {
    private val alarms = TestAlarms().getAlarms()

    override fun fetchAlarm(id: Long, scope: CoroutineScope): Alarm {
        return alarms.first { it.id == id }
    }
}

