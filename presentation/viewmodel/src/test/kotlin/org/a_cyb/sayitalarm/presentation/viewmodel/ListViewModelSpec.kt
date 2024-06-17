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
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.a_cyb.sayitalarm.alarm_service.AlarmSchedulerContract
import org.a_cyb.sayitalarm.presentation.CommandContract
import org.a_cyb.sayitalarm.presentation.interactor.InteractorContract.ListInteractor
import org.a_cyb.sayitalarm.presentation.list.ListContract
import org.a_cyb.sayitalarm.presentation.list.ListContract.AlarmInfo
import org.a_cyb.sayitalarm.presentation.list.ListContract.Initial
import org.a_cyb.sayitalarm.presentation.list.ListContract.InitialError
import org.a_cyb.sayitalarm.presentation.list.ListContract.Success
import org.a_cyb.sayitalarm.presentation.viewmodel.fake.AlarmSchedulerFake
import org.a_cyb.sayitalarm.presentation.viewmodel.fake.ListInteractorFake
import org.a_cyb.sayitalarm.presentation.viewmodel.fake.TestAlarms
import org.a_cyb.sayitalarm.presentation.viewmodel.fake.TimeFormatterFake
import org.a_cyb.sayitalarm.presentation.viewmodel.fake.WeekdayFormatterFake
import org.a_cyb.sayitalarm.util.fulfils
import org.a_cyb.sayitalarm.util.mustBe
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ListViewModelSpec {

    private val alarms = TestAlarms().getAlarms()
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

    private fun getListViewModel(
        interactor: ListInteractor = ListInteractorFake(TestScope()),
        schedulerFake: AlarmSchedulerContract = AlarmSchedulerFake()
    ) = ListViewModel(interactor, schedulerFake, TimeFormatterFake(), WeekdayFormatterFake())

    @BeforeTest
    fun setup() {
        Dispatchers.setMain(StandardTestDispatcher())
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `It fulfills ListViewModel`() {
        val interactor = ListInteractorFake(TestScope())

        getListViewModel(interactor) fulfils ListContract.ListViewModel::class
    }

    @Test
    fun `It is in the Initial State`() {
        val interactor = ListInteractorFake(TestScope())

        getListViewModel(interactor).state.value mustBe Initial
    }

    @Test
    fun `Given interactor initialization fails it sets InitialError`() = runTest {
        // Given
        val interactor = ListInteractorFake(this, listOf(Result.failure(IllegalStateException())))

        // When
        val viewModel = getListViewModel(interactor)

        viewModel.state.test {
            skipItems(1) // Initial

            // Then
            awaitItem() mustBe InitialError
        }
    }

    @Test
    fun `Given interactor success with alarm it sets Success`() = runTest {
        // Given
        val interactor = ListInteractorFake(this, listOf(Result.success(alarms)))

        // When
        val viewModel = getListViewModel(interactor)

        viewModel.state.test {
            skipItems(1)

            // Then
            awaitItem() mustBe Success(alarmInfo)
        }
    }

    @Test
    fun `Given setEnabled is called it propagates success`() = runTest {
        // Given
        val updatedAlarms = alarms.toMutableList().apply {
            set(2, alarms[2].copy(enabled = true))
        }
        val results = listOf(Result.success(alarms), Result.success(updatedAlarms))
        val interactor = ListInteractorFake(this, results)
        val scheduler = AlarmSchedulerFake()
        val viewModel = getListViewModel(interactor, scheduler)

        viewModel.state.test {
            skipItems(2)
            advanceUntilIdle()

            // When
            viewModel.setEnabled(3, false)

            // Then
            awaitItem() mustBe Success(
                alarmInfo
                    .toMutableList()
                    .apply { set(2, alarmInfo[2].copy(enabled = true)) }
            )

            interactor.invokedType mustBe ListInteractorFake.InvokedType.SET_ENABLED
            scheduler.invokedType mustBe AlarmSchedulerFake.InvokedType.SET_ALARM
        }
    }

    @Test
    fun `Given deleteAlarm is called it propagates success`() = runTest {
        // Given
        val updatedAlarms = alarms.toMutableList().apply { removeLast() }
        val results = listOf(Result.success(alarms), Result.success(updatedAlarms))
        val interactor = ListInteractorFake(this, results)
        val scheduler = AlarmSchedulerFake()
        val viewModel = getListViewModel(interactor, scheduler)

        viewModel.state.test {
            skipItems(2)
            advanceUntilIdle()

            // When
            viewModel.deleteAlarm(2)

            // Then
            awaitItem() mustBe Success(
                alarmInfo
                    .toMutableList()
                    .apply { removeLast() }
            )

            interactor.invokedType mustBe ListInteractorFake.InvokedType.DELETE_ALARM
            scheduler.invokedType mustBe AlarmSchedulerFake.InvokedType.CANCEL_ALARM
        }
    }

    @Test
    fun `Given runCommand is called it executes the given command`() {
        // Given
        val interactor = ListInteractorFake(TestScope())
        val command: CommandContract.Command<ListViewModel> = mockk(relaxed = true)

        // When
        getListViewModel(interactor).runCommand(command)

        // Then
        verify(exactly = 1) { command.execute(any()) }
    }
}
