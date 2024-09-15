/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.presentation.viewmodel

import app.cash.turbine.test
import io.mockk.clearMocks
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.a_cyb.sayitalarm.domain.interactor.InteractorContract
import org.a_cyb.sayitalarm.entity.AlertType
import org.a_cyb.sayitalarm.entity.Hour
import org.a_cyb.sayitalarm.entity.Minute
import org.a_cyb.sayitalarm.presentation.contracts.AddContract
import org.a_cyb.sayitalarm.presentation.contracts.AddContract.AddState.Initial
import org.a_cyb.sayitalarm.presentation.contracts.AddContract.AddState.Success
import org.a_cyb.sayitalarm.presentation.contracts.AlarmPanelContract.AlarmUI
import org.a_cyb.sayitalarm.presentation.contracts.AlarmPanelContract.AlertTypeUI
import org.a_cyb.sayitalarm.presentation.contracts.AlarmPanelContract.RingtoneUI
import org.a_cyb.sayitalarm.presentation.contracts.AlarmPanelContract.SelectableAlertType
import org.a_cyb.sayitalarm.presentation.contracts.AlarmPanelContract.TimeUI
import org.a_cyb.sayitalarm.presentation.contracts.AlarmPanelContract.WeeklyRepeatUI
import org.a_cyb.sayitalarm.presentation.contracts.command.SaveCommand
import org.a_cyb.sayitalarm.presentation.viewmodel.converter.AlarmUiConverter
import org.a_cyb.sayitalarm.presentation.viewmodel.fake.AlertTypeFormatterFake
import org.a_cyb.sayitalarm.presentation.viewmodel.fake.FakeAlarmData
import org.a_cyb.sayitalarm.presentation.viewmodel.fake.RingtoneResolverFake
import org.a_cyb.sayitalarm.presentation.viewmodel.fake.TimeFormatterFake
import org.a_cyb.sayitalarm.presentation.viewmodel.fake.WeekdayFormatterFake
import org.a_cyb.sayitalarm.presentation.viewmodel.mapper.AlarmMapper
import org.a_cyb.sayitalarm.presentation.viewmodel.mapper.AlarmMapperContract
import org.a_cyb.sayitalarm.util.fulfils
import org.a_cyb.sayitalarm.util.mustBe
import org.a_cyb.sayitalarm.util.ringtone_resolver.RingtoneResolverContract
import tech.antibytes.kfixture.fixture
import tech.antibytes.kfixture.kotlinFixture
import java.util.Calendar
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test

@OptIn(ExperimentalCoroutinesApi::class)
class AddViewModelSpec {

    private val fixture = kotlinFixture()

    private val timeFormatter: org.a_cyb.sayitalarm.util.formatter.time.TimeFormatterContract = TimeFormatterFake()
    private val weeklyRepeatFormatter: org.a_cyb.sayitalarm.util.formatter.weekday.WeekdayFormatterContract = WeekdayFormatterFake()
    private val alertTypeFormatter: org.a_cyb.sayitalarm.util.formatter.enum.EnumFormatterContract.AlertTypeFormatter = AlertTypeFormatterFake()
    private val ringtoneResolver: RingtoneResolverContract = RingtoneResolverFake()
    private val mapper: AlarmMapperContract =
        AlarmMapper(timeFormatter, weeklyRepeatFormatter, alertTypeFormatter, ringtoneResolver)
    private val converter: AlarmUiConverter = AlarmUiConverter(timeFormatter, weeklyRepeatFormatter)

    private val interactor: InteractorContract.AddInteractor = mockk(relaxed = true)

    private lateinit var viewModel: AddContract.AddViewModel

    @BeforeTest
    fun setup() {
        Dispatchers.setMain(StandardTestDispatcher())

        viewModel = AddViewModel(interactor, mapper, converter)
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
        clearMocks(interactor)
    }

    @Test
    fun `It fulfills AddViewModel`() {
        viewModel fulfils AddContract.AddViewModel::class
    }

    @Test
    fun `It is in Initial state`() = runTest {
        viewModel.state.value mustBe Initial(defaultAlarmUI)
    }

    @Test
    fun `Given setTime is called when it succeeds it is in Success state with updated AlarmUI data`() = runTest {
        // Given
        val hour = Hour(3)
        val minute = Minute(33)

        viewModel.state.test {
            skipItems(1)

            // When
            viewModel.setTime(hour, minute)

            // Then
            awaitItem() mustBe Success(
                defaultAlarmUI.copy(
                    TimeUI(hour.hour, minute.minute, "3:33 AM"),
                ),
            )
        }
    }

    @Test
    fun `Given setWeeklyRepeat is called when it succeeds it is in Success state with updated AlarmUI data`() =
        runTest {
            // Given
            val selectedDays = listOf(Calendar.MONDAY, Calendar.WEDNESDAY, Calendar.FRIDAY)
            val selectableRepeats = defaultAlarmUI.weeklyRepeatUI.selectableRepeats.map {
                it.copy(selected = selectedDays.contains(it.code))
            }

            viewModel.state.test {
                skipItems(1)

                // When
                viewModel.setWeeklyRepeat(selectableRepeats)

                // Then
                awaitItem() mustBe Success(
                    defaultAlarmUI.copy(
                        weeklyRepeatUI = WeeklyRepeatUI(
                            formatted = "Mon, Wed, and Fri",
                            selectableRepeats = selectableRepeats,
                        ),
                    ),
                )
            }
        }

    @Test
    fun `Given setLabel is called when it succeeds it is in Success state with updated AlarmUI data`() =
        runTest {
            // Given
            val label: String = fixture.fixture()

            viewModel.state.test {
                skipItems(1)

                // When
                viewModel.setLabel(label)

                // Then
                awaitItem() mustBe Success(
                    defaultAlarmUI.copy(label = label),
                )
            }
        }

    @Test
    fun `Given setAlertType is called when it succeeds it is in Success state with updated AlarmUI data`() =
        runTest {
            // Given
            val selected = "Sound only"
            val alertTypes = defaultAlarmUI.alertTypeUI.selectableAlertType.map {
                SelectableAlertType(it.name, it.name == selected)
            }

            viewModel.state.test {
                skipItems(1)

                // When
                viewModel.setAlertType(selected)

                // Then
                awaitItem() mustBe Success(
                    defaultAlarmUI.copy(
                        alertTypeUI = AlertTypeUI(alertTypes),
                    ),
                )
            }
        }

    @Test
    fun `Given setRingtone is called when it succeeds it is in Success state with updated AlarmUI data`() =
        runTest {
            // Given
            val selected = FakeAlarmData.alarms[1].ringtone
            val ringtoneUI = RingtoneUI(
                ringtoneResolver.getRingtoneTitle(selected.ringtone).getOrNull()!!,
                selected.ringtone,
            )

            viewModel.state.test {
                skipItems(1)

                // When
                viewModel.setRingtone(ringtoneUI)

                // Then
                awaitItem() mustBe Success(
                    defaultAlarmUI.copy(ringtoneUI = ringtoneUI),
                )
            }
        }

    @Test
    fun `Given setScripts is called when it succeeds it is in Success state with updated AlarmUI data`() =
        runTest {
            // Given
            val scripts = FakeAlarmData.alarms[1].sayItScripts

            viewModel.state.test {
                skipItems(1)

                // When
                viewModel.setScripts(scripts)

                // Then
                awaitItem() mustBe Success(
                    defaultAlarmUI.copy(
                        sayItScripts = scripts.scripts,
                    ),
                )
            }
        }

    @Test
    fun `When save is called it maps AlarmUI to Alarm and trigger interactor save`() = runTest {
        // Given
        val alarm = FakeAlarmData.alarms[1].copy(id = 0)
        val weeklyRepeatUI = defaultAlarmUI.weeklyRepeatUI.selectableRepeats.map {
            it.copy(selected = alarm.weeklyRepeat.weekdays.contains(it.code))
        }
        val alertTypeName = alertTypeFormatter.format(AlertType.VIBRATE_ONLY)
        val ringtoneUI = RingtoneUI(
            ringtoneResolver.getRingtoneTitle(alarm.ringtone.ringtone).getOrNull()!!,
            alarm.ringtone.ringtone,
        )

        viewModel.state.test {
            skipItems(1)

            viewModel.setTime(alarm.hour, alarm.minute)
            viewModel.setWeeklyRepeat(weeklyRepeatUI)
            viewModel.setLabel(alarm.label.label)
            viewModel.setAlertType(alertTypeName)
            viewModel.setRingtone(ringtoneUI)
            viewModel.setScripts(alarm.sayItScripts)
            skipItems(6)

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

        viewModel.setLabel("Change state to success")

        // When
        viewModel.runCommand(command)

        // Then
        verify(exactly = 1) { interactor.save(any(), any()) }
    }
}

private val defaultAlarmUI: AlarmUI =
    AlarmUI(
        timeUI = TimeUI(8, 0, "8:00 AM"),
        weeklyRepeatUI = WeeklyRepeatUI("", FakeAlarmData.selectableRepeats),
        label = "",
        alertTypeUI = AlertTypeUI(FakeAlarmData.selectableAlertTypes),
        ringtoneUI = RingtoneUI("Radial", "file://Radial.mp3"),
        sayItScripts = emptyList(),
    )
