/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.presentation.viewmodel

import app.cash.turbine.test
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.a_cyb.sayitalarm.domain.interactor.InteractorContract
import org.a_cyb.sayitalarm.entity.Hour
import org.a_cyb.sayitalarm.entity.Label
import org.a_cyb.sayitalarm.entity.Minute
import org.a_cyb.sayitalarm.presentation.contracts.AlarmContract
import org.a_cyb.sayitalarm.presentation.contracts.AlarmContract.AlarmUiState
import org.a_cyb.sayitalarm.presentation.contracts.command.SnoozeCommand
import org.a_cyb.sayitalarm.presentation.viewmodel.fake.TimeFormatterFake
import org.a_cyb.sayitalarm.util.formatter.time.TimeFormatterContract
import org.a_cyb.sayitalarm.util.time_flow.TimeFlowContract
import org.junit.After
import org.junit.Before
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

@OptIn(ExperimentalCoroutinesApi::class)
class AlarmViewModelSpec {
    private val timeFlowMockk: TimeFlowContract = mockk(relaxed = true)
    private val timeFormatterFake: TimeFormatterContract = TimeFormatterFake()
    private val interactorMock: InteractorContract.AlarmInteractor = mockk(relaxed = true)

    @Before
    fun setup() {
        Dispatchers.setMain(StandardTestDispatcher())
    }

    @After
    fun tearDown() {
        clearAllMocks()
        Dispatchers.resetMain()
    }

    @Test
    fun `When it is initialized, it calls interactor startAlarm`() {
        // When
        AlarmViewModel(timeFlowMockk, timeFormatterFake, interactorMock)

        // Then
        verify { interactorMock.startAlarm(any()) }
    }

    @Test
    fun `When it is initialized, it begins collecting the AlarmInteractor label`() = runTest {
        val interactorLabelMock = MutableSharedFlow<Result<Label>>()

        every { interactorMock.label } returns interactorLabelMock
        assertEquals(0, interactorLabelMock.subscriptionCount.value)

        // When
        AlarmViewModel(timeFlowMockk, timeFormatterFake, interactorMock)
        advanceUntilIdle()

        // Then
        assertEquals(1, interactorLabelMock.subscriptionCount.value)
    }

    @Test
    fun `When the interactor emits a success label, it updates the state to Ringing`() = runTest {
        // Given
        val givenLabel = Label("Power Morning")
        val interactorLabelMock = MutableSharedFlow<Result<Label>>()
        every { interactorMock.label } returns interactorLabelMock

        val viewModel = AlarmViewModel(timeFlowMockk, timeFormatterFake, interactorMock)

        viewModel.state.test {
            skipItems(1) // Initial.
            advanceUntilIdle()

            // When
            interactorLabelMock.emit(Result.success(givenLabel))

            // Then
            assertEquals(AlarmUiState.Ringing(givenLabel.label), awaitItem())
        }
    }

    @Test
    fun `When the interactor emits a failure label, it updates the state to Error`() = runTest {
        // Given
        val interactorLabelMock = MutableSharedFlow<Result<Label>>()
        every { interactorMock.label } returns interactorLabelMock

        val viewModel = AlarmViewModel(timeFlowMockk, timeFormatterFake, interactorMock)

        viewModel.state.test {
            skipItems(1)
            advanceUntilIdle()

            // When
            interactorLabelMock.emit(Result.failure(IllegalStateException()))

            // Then
            assertEquals(AlarmUiState.Error, awaitItem())
        }
    }

    @Test
    fun `When in the Initial or Ringing state, it collects the TimeFlow`() = runTest {
        // Given
        val interactorLabelMock = MutableStateFlow(Result.success(Label("label")))
        val currentTimeFlowMock = flowOf(
            Pair(Hour(11), Minute(9)),
            Pair(Hour(11), Minute(10)),
            Pair(Hour(11), Minute(11)),
        )
        every { interactorMock.label } returns interactorLabelMock
        every { timeFlowMockk.currentTimeFlow } returns currentTimeFlowMock

        val viewModel = AlarmViewModel(timeFlowMockk, timeFormatterFake, interactorMock)

        viewModel.currentTime.test {
            skipItems(1)
            assertEquals("11:09 AM", awaitItem())
            assertEquals("11:10 AM", awaitItem())
            assertEquals("11:11 AM", awaitItem())
        }
    }

    @Test
    fun `When not in the Ringing state, it does not collect the TimeFlow`() = runTest {
        // Given
        val interactorLabelMock = MutableStateFlow<Result<Label>>(Result.failure(IllegalStateException()))
        val currentTimeFlowMock = flowOf(
            Pair(Hour(11), Minute(9)),
            Pair(Hour(11), Minute(10)),
            Pair(Hour(11), Minute(11)),
        )
        every { interactorMock.label } returns interactorLabelMock
        every { timeFlowMockk.currentTimeFlow } returns currentTimeFlowMock

        val viewModel = AlarmViewModel(timeFlowMockk, timeFormatterFake, interactorMock)

        viewModel.currentTime.test {
            skipItems(1)
            // When
            advanceUntilIdle()

            // Then
            expectNoEvents()
        }
    }

    @Test
    fun `When startSayIt is called, it calls the AlarmInteractor stopAlarm`() {
        // Given
        val viewModel = AlarmViewModel(timeFlowMockk, timeFormatterFake, interactorMock)

        // When
        viewModel.startSayIt()

        // Then
        verify { interactorMock.stopAlarm() }
    }

    @Test
    fun `When snooze is called, it calls the AlarmInteractor snooze`() {
        // Given
        val viewModel = AlarmViewModel(timeFlowMockk, timeFormatterFake, interactorMock)

        // When
        viewModel.snooze()

        // Then
        verify { interactorMock.snooze() }
    }

    @Test
    fun `When runCommand is called with the Snooze command, it executes snooze`() {
        // Given
        val viewModel: AlarmContract.AlarmViewModel = AlarmViewModel(timeFlowMockk, timeFormatterFake, interactorMock)
        val command = SnoozeCommand

        // When
        viewModel.runCommand(command)

        // Then
        verify { viewModel.snooze() }
    }

    @Test
    fun `It fulfills AlarmViewModel`() {
        val viewModel = AlarmViewModel(timeFlowMockk, timeFormatterFake, interactorMock)

        assertIs<AlarmContract.AlarmViewModel>(viewModel)
    }

    @Test
    fun `It is in the Initial state`() {
        val viewModel = AlarmViewModel(timeFlowMockk, timeFormatterFake, interactorMock)

        assertEquals(AlarmUiState.Initial, viewModel.state.value)
    }
}
