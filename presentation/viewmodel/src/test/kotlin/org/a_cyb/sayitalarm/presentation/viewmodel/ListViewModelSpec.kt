/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.presentation.viewmodel

import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import app.cash.turbine.test
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.a_cyb.sayitalarm.entity.Alarm
import org.a_cyb.sayitalarm.presentation.ListContract
import org.a_cyb.sayitalarm.presentation.ListContract.AlarmInfo
import org.a_cyb.sayitalarm.presentation.ListContract.ListState.Initial
import org.a_cyb.sayitalarm.presentation.ListContract.ListState.InitialError
import org.a_cyb.sayitalarm.presentation.ListContract.ListState.Success
import org.a_cyb.sayitalarm.presentation.command.CommandContract
import org.a_cyb.sayitalarm.presentation.formatter.time.TimeFormatterContract
import org.a_cyb.sayitalarm.presentation.formatter.weekday.WeekdayFormatterContract
import org.a_cyb.sayitalarm.presentation.viewmodel.fake.FakeAlarmData
import org.a_cyb.sayitalarm.presentation.viewmodel.fake.ListInteractorFake
import org.a_cyb.sayitalarm.presentation.viewmodel.fake.ListInteractorFake.InvokedType
import org.a_cyb.sayitalarm.presentation.viewmodel.fake.TimeFormatterFake
import org.a_cyb.sayitalarm.presentation.viewmodel.fake.WeekdayFormatterFake
import org.a_cyb.sayitalarm.util.fulfils
import org.a_cyb.sayitalarm.util.mustBe
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ListViewModelSpec {

    private val timeFormatter: TimeFormatterContract = TimeFormatterFake()
    private val weekdayFormatter: WeekdayFormatterContract = WeekdayFormatterFake()

    @BeforeTest
    fun setup() {
        Dispatchers.setMain(StandardTestDispatcher())
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `It is in the Initial State`() {
        val viewModel = ListViewModel(
            ListInteractorFake(),
            timeFormatter,
            weekdayFormatter
        )

        viewModel.state.value mustBe Initial
    }

    @Test
    fun `When interactor initialization fails it is in InitialError state`() = runTest {
        // Given
        val results = listOf(Result.failure<List<Alarm>>(IllegalStateException()))
        val interactor = ListInteractorFake(results)

        val viewModel = ListViewModel(interactor, timeFormatter, weekdayFormatter)

        // When
        viewModel.state.test {
            skipItems(1) // Initial

            // Then
            awaitItem() mustBe InitialError
        }
    }

    @Test
    fun `Given interactor success with alarm it sets Success`() = runTest {
        // Given
        val result = listOf(Result.success(alarms))
        val interactor = ListInteractorFake(result)
        val viewModel = ListViewModel(interactor, timeFormatter, weekdayFormatter)

        // When
        viewModel.state.test {
            skipItems(1)

            // Then
            awaitItem() mustBe Success(alarmInfo)
        }
    }

    @Test
    fun `When setEnabled is called it propagates success state`() = runTest {
        // Given
        val updatedAlarms = alarms.toMutableList().apply {
            set(2, alarms[2].copy(enabled = true))
        }
        val results = listOf(
            Result.success(alarms),
            Result.success(updatedAlarms)
        )
        val interactor = ListInteractorFake(results)
        val viewModel = ListViewModel(interactor, timeFormatter, weekdayFormatter)

        viewModel.state.test {
            skipItems(2)  // Initial

            // When
            viewModel.setEnabled(3, false)

            val expected = alarmInfo.toMutableList()
                .apply { set(2, alarmInfo[2].copy(enabled = true)) }

            // Then
            awaitItem() mustBe Success(expected)

            interactor.invokedType mustBe InvokedType.SET_ENABLED
        }
    }

    @Test
    fun `Given deleteAlarm is called it propagates success`() = runTest {
        // Given
        val updatedAlarms = alarms.toMutableList().apply { removeLast() }
        val results = listOf(
            Result.success(alarms),
            Result.success(updatedAlarms)
        )
        val interactor = ListInteractorFake(results)
        val viewModel = ListViewModel(interactor, timeFormatter, weekdayFormatter)

        viewModel.state.test {
            skipItems(2)

            // When
            viewModel.deleteAlarm(2)

            val expected = alarmInfo.toMutableList()
                .apply { removeLast() }

            // Then
            awaitItem() mustBe Success(expected)

            interactor.invokedType mustBe InvokedType.DELETE_ALARM
        }
    }

    @Test
    fun `Given runCommand is called it executes the given command`() {
        // Given
        val interactor = ListInteractorFake()
        val viewModel = ListViewModel(interactor, timeFormatter, weekdayFormatter)
        val command: CommandContract.Command<ListViewModel> = mockk(relaxed = true)

        // When
        viewModel.runCommand(command)

        // Then
        verify(exactly = 1) { command.execute(any()) }
    }

    @Test
    fun `It fulfills ListViewModel`() {
        val interactor = ListInteractorFake()
        val viewModel = ListViewModel(interactor, timeFormatter, weekdayFormatter)

        viewModel fulfils ListContract.ListViewModel::class
    }

    private val alarms = FakeAlarmData.alarms
    private val alarmInfo = listOf(
        AlarmInfo(
            id = 1,
            time = "6:00 AM",
            labelAndWeeklyRepeat = "Wake Up, every weekday",
            enabled = true
        ),
        AlarmInfo(
            id = 2,
            time = "8:30 PM",
            labelAndWeeklyRepeat = "Workout, Mon, Wed, and Fri",
            enabled = true
        ),
        AlarmInfo(
            id = 3,
            time = "9:00 AM",
            labelAndWeeklyRepeat = "Passion Hour, every weekend",
            enabled = false
        )
    )
}
