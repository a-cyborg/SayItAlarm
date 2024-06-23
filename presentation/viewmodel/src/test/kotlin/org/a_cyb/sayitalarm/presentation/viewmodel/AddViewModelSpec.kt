/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.presentation.viewmodel

import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import android.icu.util.Calendar
import app.cash.turbine.test
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.a_cyb.sayitalarm.entity.AlertType
import org.a_cyb.sayitalarm.entity.Hour
import org.a_cyb.sayitalarm.entity.Label
import org.a_cyb.sayitalarm.entity.Minute
import org.a_cyb.sayitalarm.entity.WeeklyRepeat
import org.a_cyb.sayitalarm.presentation.add.AddContract
import org.a_cyb.sayitalarm.presentation.add.AddContract.AddState.Initial
import org.a_cyb.sayitalarm.presentation.add.AddContract.AddState.Success
import org.a_cyb.sayitalarm.presentation.add.SaveCommand
import org.a_cyb.sayitalarm.presentation.alarm_panel.AlarmPanelContract
import org.a_cyb.sayitalarm.presentation.alarm_panel.AlarmPanelContract.AlarmUI
import org.a_cyb.sayitalarm.presentation.alarm_panel.AlarmPanelContract.AlertTypeUI
import org.a_cyb.sayitalarm.presentation.alarm_panel.AlarmPanelContract.TimeUI
import org.a_cyb.sayitalarm.presentation.alarm_panel.AlarmPanelContract.WeeklyRepeatUI
import org.a_cyb.sayitalarm.presentation.interactor.InteractorContract
import org.a_cyb.sayitalarm.presentation.viewmodel.fake.AlarmMapperFake
import org.a_cyb.sayitalarm.presentation.viewmodel.fake.FakeAlarmData
import org.a_cyb.sayitalarm.presentation.viewmodel.fake.FakeAlarmData.selectableAlertType
import org.a_cyb.sayitalarm.presentation.viewmodel.fake.FakeAlarmData.selectableRepeat
import org.a_cyb.sayitalarm.presentation.viewmodel.mapper.AlarmMapperContract
import org.a_cyb.sayitalarm.util.fulfils
import org.a_cyb.sayitalarm.util.mustBe

@OptIn(ExperimentalCoroutinesApi::class)
class AddViewModelSpec {

    private val interactor: InteractorContract.AddInteractor = mockk(relaxed = true)
    private val mapper: AlarmMapperContract = AlarmMapperFake()

    private lateinit var viewModel: AddContract.AddViewModel

    @BeforeTest
    fun setup() {
        Dispatchers.setMain(StandardTestDispatcher())

        viewModel = AddViewModel(interactor, mapper)
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `It fulfills AddViewModel`() {
        viewModel fulfils AddContract.AddViewModel::class
    }

    @Test
    fun `It is in Initial state`() = runTest {
        viewModel.state.value mustBe Initial
    }

    @Test
    fun `Given initialization succeeds then it is in Success state and contains default AlarmUI data`() = runTest {
        // Given
        viewModel.state.test {
            // When
            skipItems(1)

            // Then
            awaitItem() mustBe Success(defaultAlarmUI)
        }
    }

    @Test
    fun `Given setTime is called when it succeeds it is in Success state with updated AlarmUI data`() = runTest {
        // Given
        val hour = Hour(3)
        val minute = Minute(33)

        viewModel.state.test {
            skipItems(2)

            // When
            viewModel.setTime(hour, minute)

            // Then
            awaitItem() mustBe Success(
                defaultAlarmUI.copy(
                    time = mapper.mapToTimeUI(hour, minute)
                )
            )
        }
    }

    @Test
    fun `Given setWeeklyRepeat is called when it succeeds it is in Success state with updated AlarmUI data`() =
        runTest {
            // Given
            val weeklyRepeat = WeeklyRepeat(Calendar.SUNDAY, Calendar.WEDNESDAY, Calendar.FRIDAY)

            viewModel.state.test {
                skipItems(2)

                // When
                viewModel.setWeeklyRepeat(weeklyRepeat)

                // Then
                awaitItem() mustBe Success(
                    defaultAlarmUI.copy(
                        weeklyRepeat = mapper.mapToWeeklyRepeatUI(weeklyRepeat)
                        // weeklyRepeat = AlarmPanelContract.WeeklyRepeatUI(
                        //     selected = weeklyRepeat.weekdays,
                        //     formattedSelectedRepeat = "Sun, Wed, and Fri",
                        //     selectableRepeat = FakeAlarmData.selectableRepeat
                        // )
                    )
                )
            }
        }

    @Test
    fun `Given setLabel is called when it succeeds it is in Success state with updated AlarmUI data`() =
        runTest {
            // Given
            val label = Label("Say It")

            viewModel.state.test {
                skipItems(2)

                // When
                viewModel.setLabel(label)

                // Then
                awaitItem() mustBe Success(
                    defaultAlarmUI.copy(
                        label = label.label
                    )
                )
            }
        }

    @Test
    fun `Given setAlertType is called when it succeeds it is in Success state with updated AlarmUI data`() =
        runTest {
            // Given
            val alertType = AlertType.VIBRATE_ONLY

            viewModel.state.test {
                skipItems(2)

                // When
                viewModel.setAlertType(alertType)

                // Then
                awaitItem() mustBe Success(
                    defaultAlarmUI.copy(
                        alertType = mapper.mapToAlertTypeUI(alertType)
                    )
                )
            }
        }

    @Test
    fun `Given setRingtone is called when it succeeds it is in Success state with updated AlarmUI data`() =
        runTest {
            // Given
            val ringtone = FakeAlarmData.alarms[1].ringtone

            viewModel.state.test {
                skipItems(2)

                // When
                viewModel.setRingtone(ringtone)

                // Then
                awaitItem() mustBe Success(
                    defaultAlarmUI.copy(
                        ringtone = mapper.mapToRingtoneUI(ringtone)
                    )
                )
            }
        }

    @Test
    fun `Given setScripts is called when it succeeds it is in Success state with updated AlarmUI data`() =
        runTest {
            // Given
            val scripts = FakeAlarmData.alarms[1].sayItScripts

            viewModel.state.test {
                skipItems(2)

                // When
                viewModel.setScripts(scripts)

                // Then
                awaitItem() mustBe Success(
                    defaultAlarmUI.copy(
                        sayItScripts = scripts.scripts
                    )
                )
            }
        }

    @Test
    fun `Given save is called it maps AlarmUI to Alarm and trigger interactor save`() = runTest {
        // Given
        val alarm = FakeAlarmData.alarms[1].copy(id = 0)

        viewModel.state.test {
            skipItems(2)

            viewModel.setTime(alarm.hour, alarm.minute)
            viewModel.setWeeklyRepeat(alarm.weeklyRepeat)
            viewModel.setLabel(alarm.label)
            viewModel.setAlertType(alarm.alertType)
            viewModel.setRingtone(alarm.ringtone)
            viewModel.setScripts(alarm.sayItScripts)

            // When
            viewModel.save()

            // Then
            verify(exactly = 1) {
                interactor.save(alarm = withArg { it mustBe alarm }, scope = any())
            }
        }
    }

    @Test
    fun `Given runCommand is called it executes given command`() = runTest {
        // Given
        val command = SaveCommand

        // When
        viewModel.runCommand(command)

        // Then
        verify(exactly = 1) { interactor.save(any(), any()) }
    }
}


private val defaultAlarmUI: AlarmUI =
    AlarmUI(
        time = TimeUI(8, 0, "8:00 AM"),
        weeklyRepeat = WeeklyRepeatUI(emptySet(), "", selectableRepeat),
        label = "",
        alertType = AlertTypeUI(AlertType.SOUND_AND_VIBRATE, "Sound and vibration", selectableAlertType),
        ringtone = AlarmPanelContract.RingtoneUI("Radial", "file://Radial.mp3"),
        sayItScripts = emptyList()
    )
