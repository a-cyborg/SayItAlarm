/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.presentation.viewmodel

import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import java.util.Calendar
import app.cash.turbine.test
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.a_cyb.sayitalarm.domain.interactor.InteractorContract
import org.a_cyb.sayitalarm.entity.Hour
import org.a_cyb.sayitalarm.entity.Minute
import org.a_cyb.sayitalarm.presentation.AlarmPanelContract
import org.a_cyb.sayitalarm.presentation.AlarmPanelContract.AlarmUI
import org.a_cyb.sayitalarm.presentation.AlarmPanelContract.AlertTypeUI
import org.a_cyb.sayitalarm.presentation.AlarmPanelContract.RingtoneUI
import org.a_cyb.sayitalarm.presentation.AlarmPanelContract.SelectableAlertType
import org.a_cyb.sayitalarm.presentation.AlarmPanelContract.TimeUI
import org.a_cyb.sayitalarm.presentation.AlarmPanelContract.WeeklyRepeatUI
import org.a_cyb.sayitalarm.presentation.EditContract
import org.a_cyb.sayitalarm.presentation.EditContract.EditViewModel.EditState.Error
import org.a_cyb.sayitalarm.presentation.EditContract.EditViewModel.EditState.Initial
import org.a_cyb.sayitalarm.presentation.EditContract.EditViewModel.EditState.Success
import org.a_cyb.sayitalarm.presentation.command.SaveCommand
import org.a_cyb.sayitalarm.presentation.formatter.enum.EnumFormatterContract
import org.a_cyb.sayitalarm.presentation.formatter.time.TimeFormatterContract
import org.a_cyb.sayitalarm.presentation.formatter.weekday.WeekdayFormatterContract
import org.a_cyb.sayitalarm.presentation.viewmodel.converter.AlarmUIConverterContract
import org.a_cyb.sayitalarm.presentation.viewmodel.converter.AlarmUiConverter
import org.a_cyb.sayitalarm.presentation.viewmodel.fake.AlertTypeFormatterFake
import org.a_cyb.sayitalarm.presentation.viewmodel.fake.EditInteractorFake
import org.a_cyb.sayitalarm.presentation.viewmodel.fake.EditInteractorFake.InvokedType
import org.a_cyb.sayitalarm.presentation.viewmodel.fake.FakeAlarmData
import org.a_cyb.sayitalarm.presentation.viewmodel.fake.RingtoneResolverFake
import org.a_cyb.sayitalarm.presentation.viewmodel.fake.TimeFormatterFake
import org.a_cyb.sayitalarm.presentation.viewmodel.fake.WeekdayFormatterFake
import org.a_cyb.sayitalarm.presentation.viewmodel.mapper.AlarmMapper
import org.a_cyb.sayitalarm.presentation.viewmodel.mapper.AlarmMapperContract
import org.a_cyb.sayitalarm.system_service.ringtone_resolver.RingtoneResolverContract
import org.a_cyb.sayitalarm.util.fulfils
import org.a_cyb.sayitalarm.util.mustBe
import org.junit.Test
import tech.antibytes.kfixture.fixture
import tech.antibytes.kfixture.kotlinFixture

@OptIn(ExperimentalCoroutinesApi::class)
class EditViewModelSpec {

    private val fixture = kotlinFixture()

    private val timeFormatter: TimeFormatterContract = TimeFormatterFake()
    private val weeklyRepeatFormatter: WeekdayFormatterContract = WeekdayFormatterFake()
    private val alertTypeFormatter: EnumFormatterContract.AlertTypeFormatter = AlertTypeFormatterFake()
    private val ringtoneManager: RingtoneResolverContract = RingtoneResolverFake()
    private val alarmUiConverter: AlarmUIConverterContract = AlarmUiConverter(timeFormatter, weeklyRepeatFormatter)
    private val mapper: AlarmMapperContract =
        AlarmMapper(timeFormatter, weeklyRepeatFormatter, alertTypeFormatter, ringtoneManager)

    private val viewModel: (Long, InteractorContract.EditInteractor) -> EditViewModel = { alarmId, interactor ->
        EditViewModel(alarmId, interactor, mapper, alarmUiConverter)
    }

    @BeforeTest
    fun setup() {
        Dispatchers.setMain(StandardTestDispatcher())
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `It is in Initial state`() {
        // Given
        val interactor = EditInteractorFake(
            Result.failure(IllegalStateException())
        )
        val viewModel = viewModel(3L, interactor)

        viewModel.state.value mustBe Initial
    }

    @Test
    fun `When interactor getAlarm succeeds it is in Success state with AlarmUI`() = runTest {
        // Given
        val interactor = EditInteractorFake(
            Result.success(alarm)
        )
        val viewModel = viewModel(3L, interactor)

        viewModel.state.test {
            // When
            skipItems(1)

            // Then
            awaitItem() mustBe Success(alarmUI)
        }
    }

    @Test
    fun `When interactor getAlarm failed it is in Error state`() = runTest {
        // Given
        val interactor = EditInteractorFake(
            Result.failure(IllegalStateException())
        )
        val viewModel = viewModel(3L, interactor)

        viewModel.state.test {
            // When
            skipItems(1)

            // Then
            awaitItem() mustBe Error
        }
    }

    @Test
    fun `When initialized it triggers interactor getAlarm`() {
        // Given
        val interactor = EditInteractorFake(
            Result.failure(IllegalStateException())
        )

        // When
        viewModel(3L, interactor)

        // Then
        interactor.invoked mustBe InvokedType.GET_ALARM
    }

    @Test
    fun `When setTime is called and succeeds it is in Success state with updated AlarmUI data`() = runTest {
        // Given
        val interactor = EditInteractorFake(
            Result.success(alarm)
        )
        val viewModel = viewModel(3L, interactor)

        val hour = Hour(3)
        val minute = Minute(33)

        viewModel.state.test {
            skipItems(2)

            // When
            viewModel.setTime(hour, minute)

            // Then
            awaitItem() mustBe Success(
                alarmUI.copy(
                    TimeUI(hour.hour, minute.minute, "3:33 AM")
                )
            )
        }
    }

    @Test
    fun `When setWeeklyRepeat is called and it succeeds it is in Success state with updated AlarmUI data`() =
        runTest {
            // Given
            val interactor = EditInteractorFake(
                Result.success(alarm)
            )
            val viewModel = viewModel(3L, interactor)

            val selectedDays = listOf(Calendar.MONDAY, Calendar.WEDNESDAY, Calendar.FRIDAY)
            val selectableRepeats = alarmUI.weeklyRepeatUI.selectableRepeats.map {
                it.copy(selected = selectedDays.contains(it.code))
            }

            viewModel.state.test {
                skipItems(2)

                // When
                viewModel.setWeeklyRepeat(selectableRepeats)

                // Then
                awaitItem() mustBe Success(
                    alarmUI.copy(
                        weeklyRepeatUI = WeeklyRepeatUI(
                            formatted = "Mon, Wed, and Fri",
                            selectableRepeats = selectableRepeats
                        )
                    )
                )
            }
        }

    @Test
    fun `Given setLabel is called and succeeds it is in Success state with updated AlarmUI data`() = runTest {
        // Given
        val interactor = EditInteractorFake(
            Result.success(alarm)
        )
        val viewModel = viewModel(3L, interactor)

        val label: String = fixture.fixture()

        viewModel.state.test {
            skipItems(2)

            // When
            viewModel.setLabel(label)

            // Then
            awaitItem() mustBe Success(
                alarmUI.copy(label = label)
            )
        }
    }

    @Test
    fun `When setAlertType is called and succeeds it is in Success state with updated AlarmUI data`() =
        runTest {
            // Given
            val interactor = EditInteractorFake(
                Result.success(alarm)
            )
            val viewModel = viewModel(3L, interactor)

            val selectable = alarmUI.alertTypeUI.selectableAlertType.map {
                SelectableAlertType(it.name, it.name == "Sound only")
            }

            viewModel.state.test {
                skipItems(2)

                // When
                viewModel.setAlertType("Sound only")

                // Then
                awaitItem() mustBe Success(
                    alarmUI.copy(
                        alertTypeUI = AlertTypeUI(selectable)
                    )
                )
            }
        }

    @Test
    fun `Given setRingtone is called and succeeds it is in Success state with updated AlarmUI data`() =
        runTest {
            // Given
            val interactor = EditInteractorFake(
                Result.success(alarm)
            )
            val viewModel = viewModel(3L, interactor)

            val selected = FakeAlarmData.alarms[1].ringtone
            val ringtoneUI = RingtoneUI(
                ringtoneManager.getRingtoneTitle(selected.ringtone).getOrNull()!!,
                selected.ringtone
            )

            viewModel.state.test {
                skipItems(2)

                // When
                viewModel.setRingtone(ringtoneUI)

                // Then
                awaitItem() mustBe Success(
                    alarmUI.copy(ringtoneUI = ringtoneUI)
                )
            }
        }

    @Test
    fun `When setScripts is called and it succeeds it is in Success state with updated AlarmUI data`() =
        runTest {
            // Given
            val interactor = EditInteractorFake(
                Result.success(alarm)
            )
            val viewModel = viewModel(3L, interactor)

            val scripts = FakeAlarmData.alarms[1].sayItScripts

            viewModel.state.test {
                skipItems(2)

                // When
                viewModel.setScripts(scripts)

                // Then
                awaitItem() mustBe Success(
                    alarmUI.copy(
                        sayItScripts = scripts.scripts
                    )
                )
            }
        }

    @Test
    fun `When save is called it triggers interactor update`() = runTest {
        // Given
        val interactor = EditInteractorFake(
            Result.success(alarm)
        )
        val viewModel = viewModel(3L, interactor)

        viewModel.state.test {
            skipItems(2)
        }

        // When
        viewModel.save()

        // Then
        interactor.invoked mustBe InvokedType.UPDATE
    }

    @Test
    fun `When save is called it maps AlarmUI to Alarm and triggers interactor save`() = runTest {
        // Given
        val interactor = EditInteractorFake(
            Result.success(alarm)
        )
        val viewModel = viewModel(3L, interactor)

        val hour = Hour(3)
        val minute = Minute(33)

        viewModel.state.test {
            skipItems(2)
            viewModel.setTime(hour, minute)
            skipItems(1)

            // When
            viewModel.save()
        }

        // Then
        interactor.updatedAlarm mustBe alarm.copy(
            hour = hour,
            minute = minute,
            enabled = true
        )
    }

    @Test
    fun `It fulfills EditViewModel`() {
        val interactor = EditInteractorFake(
            Result.failure(IllegalStateException())
        )

        viewModel(3L, interactor) fulfils EditContract.EditViewModel::class
    }

    @Test
    fun `Given runCommand is called it executes given command`() = runTest {
        // Given
        // Given
        val interactor = EditInteractorFake(
            Result.success(alarm)
        )
        val viewModel = viewModel(3L, interactor)

        viewModel.state.test {
            skipItems(2)
        }

        // When
        viewModel.runCommand(SaveCommand)

        // Then
        interactor.invoked mustBe InvokedType.UPDATE
    }

    private val alarm = FakeAlarmData.alarms[2]
    private val alarmUI: AlarmUI =
        AlarmUI(
            timeUI = TimeUI(9, 0, "9:00 AM"),
            weeklyRepeatUI = WeeklyRepeatUI(
                "every weekend",
                setWeeklyRepeatUI(listOf(Calendar.SUNDAY, Calendar.SATURDAY))
            ),
            label = alarm.label.label,
            alertTypeUI = AlertTypeUI(FakeAlarmData.selectableAlertTypes),
            ringtoneUI = RingtoneUI(
                ringtoneManager.getRingtoneTitle(alarm.ringtone.ringtone).getOrNull()!!,
                alarm.ringtone.ringtone
            ),
            sayItScripts = alarm.sayItScripts.scripts
        )

    private fun setWeeklyRepeatUI(codes: List<Int>): List<AlarmPanelContract.SelectableRepeat> {
        return FakeAlarmData.selectableRepeats.map {
            if (codes.contains(it.code)) {
                it.copy(selected = true)
            } else {
                it.copy()
            }
        }
    }
}
