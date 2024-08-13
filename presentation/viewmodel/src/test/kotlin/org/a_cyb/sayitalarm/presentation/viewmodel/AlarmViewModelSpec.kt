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
import org.a_cyb.sayitalarm.domain.alarm_service.AlarmServiceContract.AlarmService
import org.a_cyb.sayitalarm.domain.alarm_service.AlarmServiceContract.AlarmServiceController
import org.a_cyb.sayitalarm.domain.alarm_service.AlarmServiceContract.AlarmServiceController.AlarmServiceState
import org.a_cyb.sayitalarm.domain.alarm_service.AlarmServiceContract.SayItRecognizer
import org.a_cyb.sayitalarm.entity.Hour
import org.a_cyb.sayitalarm.entity.Label
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
    private val timeFlowMockk: TimeFlowContract = mockk(relaxed = true)
    private val controllerMockk: AlarmServiceController = mockk(relaxed = true)
    private val sayItRecognizerMockk: SayItRecognizer = mockk(relaxed = true)
    private val timeFormatterFake: TimeFormatterContract = TimeFormatterFake()

    @Before
    fun setup() {
        Dispatchers.setMain(StandardTestDispatcher())
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    private fun getAlarmViewModel(
        timeFlow: TimeFlowContract = timeFlowMockk,
        controller: AlarmServiceController = controllerMockk,
        sayItRecognizer: SayItRecognizer = sayItRecognizerMockk,
        timeFormatter: TimeFormatterContract = timeFormatterFake,
    ): AlarmContract.AlarmViewModel =
        AlarmViewModel(
            timeFlow,
            controller,
            sayItRecognizer,
            timeFormatter,
        )

    @Test
    fun `It start in the Initial state`() {
        val viewModel = getAlarmViewModel()

        viewModel.state.value mustBe AlarmUiState.Initial
    }

    @Test
    fun `When it is in the Initial or Ringing state, it collects the currentTime and maps it to a string`() = runTest {
        // Given
        val label = Label("Good Morning🐿️")
        val currentTimes = listOf(
            Pair(Hour(11), Minute(9)),
            Pair(Hour(11), Minute(10)),
            Pair(Hour(11), Minute(11)),
        )
        val expected = currentTimes.map { (hour, minute) ->
            timeFormatterFake.format(hour, minute)
        }

        every { timeFlowMockk.currentTimeFlow } returns
            flow { currentTimes.forEach { emit(it) } }

        val controller = AlarmServiceControllerFake(listOf(AlarmServiceState.Ringing(label)))
        val viewModel = getAlarmViewModel(controller = controller)

        turbineScope {
            val currentTime = viewModel.currentTime.testIn(backgroundScope)
            val state = viewModel.state.testIn(backgroundScope)

            // When
            state.awaitItem() mustBe AlarmUiState.Initial
            currentTime.skipItems(1) // Initial

            // Then
            currentTime.awaitItem() mustBe expected[0]

            // When
            controller.onServiceBind(mockk(), 0L)
            state.awaitItem() mustBe AlarmUiState.Ringing(label.label)

            // Then
            currentTime.awaitItem() mustBe expected[1]
            currentTime.awaitItem() mustBe expected[2]
        }
    }

    @Test
    fun `When it is not in the Initial or Ringing state, it does not collects the currentTime`() = runTest {
        // Given
        val controller = AlarmServiceControllerFake(listOf(AlarmServiceState.Error))
        val viewModel = getAlarmViewModel(
            timeFlow = TimeFlow,
            controller = controller,
        )

        viewModel.currentTime.test {
            skipItems(2)  // Initial & One currentTime item

            // When
            controller.onServiceBind(mockk(), 0L)  // It goes into the Error state.

            // Then
            expectNoEvents()
        }
    }

    @Test
    fun `When startSayIt is invoked, it triggers the controller's startSayIt`() = runTest {
        // Given
        val viewModel = getAlarmViewModel()

        // When
        viewModel.startSayIt()

        // Then
        verify(exactly = 1) { controllerMockk.startSayIt() }
    }

    @Test
    fun `When snooze is invoked, it triggers the controller's startSnooze`() = runTest {
        // Given
        val viewModel = getAlarmViewModel()

        // When
        viewModel.snooze()

        // Then
        verify(exactly = 1) { controllerMockk.startSnooze() }
    }

    @Test
    fun `When finishAlarm is invoked, it triggers the controller's terminate`() = runTest {
        // Given
        val viewModel = getAlarmViewModel()

        // When
        viewModel.finishAlarm()

        // Then
        verify(exactly = 1) { controllerMockk.terminate() }
    }

    @Test
    fun `It fulfills AlarmViewModel`() {
        val viewModel = getAlarmViewModel()

        viewModel fulfils AlarmContract.AlarmViewModel::class
    }

    // private val alarm = Alarm(
    //     id = 1,
    //     hour = Hour(6),
    //     minute = Minute(0),
    //     weeklyRepeat = WeeklyRepeat(MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY),
    //     label = Label("Wake Up"),
    //     enabled = true,
    //     alertType = AlertType.SOUND_ONLY,
    //     ringtone = Ringtone("file://wake_up_alarm.mp3"),
    //     alarmType = AlarmType.SAY_IT,
    //     sayItScripts = SayItScripts(
    //         "I am peaceful and whole.",
    //         "I do all things in love.",
    //         "I embrace change seamlessly and rise to the new opportunity it presents."
    //     )
    // )
}

private class AlarmServiceControllerFake(
    results: List<AlarmServiceState> = listOf(AlarmServiceState.Initial),
) : AlarmServiceController {

    private val _results = results.toMutableList()

    private val _alarmState: MutableStateFlow<AlarmServiceState> = MutableStateFlow(AlarmServiceState.Initial)
    override val alarmState: StateFlow<AlarmServiceState> = _alarmState.asStateFlow()

    override fun onServiceBind(service: AlarmService, alarmId: Long) {
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

    override fun terminate() {}
}