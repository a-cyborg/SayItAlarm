/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.presentation.viewmodel

import app.cash.turbine.test
import app.cash.turbine.turbineScope
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.a_cyb.sayitalarm.domain.alarm_service.AlarmServiceContract
import org.a_cyb.sayitalarm.domain.alarm_service.AlarmServiceContract.AlarmServiceController.AlarmServiceState
import org.a_cyb.sayitalarm.entity.Hour
import org.a_cyb.sayitalarm.entity.Minute
import org.a_cyb.sayitalarm.presentation.AlarmContract
import org.a_cyb.sayitalarm.presentation.AlarmContract.AlarmUiState
import org.a_cyb.sayitalarm.presentation.formatter.time.TimeFormatterContract
import org.a_cyb.sayitalarm.presentation.viewmodel.fake.TimeFormatterFake
import org.a_cyb.sayitalarm.presentation.viewmodel.time_flow.TimeFlow
import org.a_cyb.sayitalarm.presentation.viewmodel.time_flow.TimeFlowContract
import org.a_cyb.sayitalarm.util.fulfils
import org.a_cyb.sayitalarm.util.mustBe
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class AlarmViewModelSpec {

    private val controllerMockk: AlarmServiceContract.AlarmServiceController = mockk(relaxed = true)
    private val timeFormatter: TimeFormatterContract = TimeFormatterFake()
    private val timeFlowMockk: TimeFlowContract = mockk(relaxed = true)

    @Before
    fun setup() {
        Dispatchers.setMain(StandardTestDispatcher())
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `It start in the Initial state`() {
        val viewModel = AlarmViewModel(controllerMockk, timeFormatter, timeFlowMockk)

        viewModel.state.value mustBe AlarmUiState.Initial
    }

    @Test
    fun `When it is in the Initial or Ringing state, it collects the currentTime and maps it to a string`() = runTest {
        // Given
        val currentTimes = listOf(
            Pair(Hour(11), Minute(9)),
            Pair(Hour(11), Minute(10)),
            Pair(Hour(11), Minute(11)),
        )
        val expected = currentTimes.map { (hour, minute) ->
            timeFormatter.format(hour, minute)
        }

        every { timeFlowMockk.currentTimeFlow } returns flow { currentTimes.forEach { emit(it) } }

        val controller = AlarmServiceControllerFake(listOf(AlarmServiceState.Ringing))
        val viewModel = AlarmViewModel(controller, timeFormatter, timeFlowMockk)

        turbineScope {
            val currentTime = viewModel.currentTime.testIn(backgroundScope)
            val state = viewModel.state.testIn(backgroundScope)

            // When
            state.awaitItem() mustBe AlarmUiState.Initial
            currentTime.skipItems(1)

            // Then
            currentTime.awaitItem() mustBe expected[0]

            // When
            controller.onServiceBind(mockk())
            state.awaitItem() mustBe AlarmUiState.Ringing

            // Then
            currentTime.awaitItem() mustBe expected[1]
            currentTime.awaitItem() mustBe expected[2]
        }
    }

    @Test
    fun `When it is not in the Initial or Ringing state, it does not collects the currentTime`() = runTest {
        // Given
        val controller = AlarmServiceControllerFake(listOf(AlarmServiceState.Error))
        val viewModel = AlarmViewModel(controller, timeFormatter, TimeFlow)

        viewModel.currentTime.test {
            skipItems(2) // Initial & One currentTime item

            // When
            viewModel.startSayIt()  // It goes into the Error state.

            // Then
            expectNoEvents()
        }
    }

    @Test
    fun `When startSayIt is called, it triggers the controller's startSayIt`() = runTest {
        // Given
        val viewModel = AlarmViewModel(controllerMockk, timeFormatter, timeFlowMockk)

        // When
        viewModel.startSayIt()

        // Then
        verify(exactly = 1) { controllerMockk.startSayIt() }
    }

    @Test
    fun `It fulfills AlarmViewModel`() {
        val viewModel = AlarmViewModel(controllerMockk, timeFormatter, timeFlowMockk)

        viewModel fulfils AlarmContract.AlarmViewModel::class
    }
}

private class AlarmServiceControllerFake(
    results: List<AlarmServiceState> = listOf(AlarmServiceState.Initial),
): AlarmServiceContract.AlarmServiceController {

    private val _results = results.toMutableList()

    private val _alarmState: MutableStateFlow<AlarmServiceState> = MutableStateFlow(AlarmServiceState.Initial)
    override val alarmState: StateFlow<AlarmServiceState> = _alarmState.asStateFlow()

    override fun onServiceBind(serviceContract: AlarmServiceContract.AlarmService) {
        _alarmState.value = _results.removeFirst()
    }

    override fun onServiceDisconnected() {
        _alarmState.value = _results.removeFirst()
    }

    override fun startSayIt() {
        _alarmState.value = _results.removeFirst()
    }

    override fun startSnooze() {
        _alarmState.value = _results.removeFirst()
    }
}