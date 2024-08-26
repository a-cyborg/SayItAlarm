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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.a_cyb.sayitalarm.domain.alarm_service.AlarmServiceContract.AlarmServiceController
import org.a_cyb.sayitalarm.domain.alarm_service.AlarmServiceContract.AlarmServiceController.ControllerState
import org.a_cyb.sayitalarm.domain.alarm_service.AlarmServiceContract.EditDistanceCalculator
import org.a_cyb.sayitalarm.domain.alarm_service.AlarmServiceContract.SttRecognizer
import org.a_cyb.sayitalarm.domain.alarm_service.AlarmServiceContract.SttRecognizer.RecognizerRmsDb
import org.a_cyb.sayitalarm.domain.alarm_service.AlarmServiceContract.SttRecognizer.RecognizerState
import org.a_cyb.sayitalarm.entity.SayItScripts
import org.a_cyb.sayitalarm.presentation.SayItContract
import org.a_cyb.sayitalarm.presentation.SayItContract.Count
import org.a_cyb.sayitalarm.presentation.SayItContract.SayItInfo
import org.a_cyb.sayitalarm.presentation.SayItContract.SayItState.Error
import org.a_cyb.sayitalarm.presentation.SayItContract.SayItState.Finished
import org.a_cyb.sayitalarm.presentation.SayItContract.SayItState.Processing
import org.a_cyb.sayitalarm.presentation.SayItContract.SttStatus
import org.a_cyb.sayitalarm.presentation.command.CommandContract
import org.a_cyb.sayitalarm.presentation.sound_effect_player.SoundEffectPlayerContract
import org.a_cyb.sayitalarm.util.fulfils
import org.a_cyb.sayitalarm.util.mustBe
import org.junit.After
import org.junit.Before
import org.junit.Test
import tech.antibytes.kfixture.fixture
import tech.antibytes.kfixture.kotlinFixture

@OptIn(ExperimentalCoroutinesApi::class)
class SayItViewModelSpec {
    private val serviceController: AlarmServiceController = mockk(relaxed = true)
    private val sttRecognizer: SttRecognizer = mockk(relaxed = true)
    private val editDistanceCalculator: EditDistanceCalculator = mockk(relaxed = true)
    private val soundEffectPlayer: SoundEffectPlayerContract = mockk(relaxed = true)

    private val fixture = kotlinFixture()

    @Before
    fun setup() {
        Dispatchers.setMain(StandardTestDispatcher())

        every { serviceController.controllerState } returns
            MutableStateFlow(ControllerState.RunningSayIt(SayItScripts("")))
    }

    @After
    fun tearDown() {
        clearAllMocks()
        Dispatchers.resetMain()
    }

    @Test
    fun `When processScript is called it invokes SttRecognizer `() {
        // Given
        val viewModel = SayItViewModel(serviceController, sttRecognizer, editDistanceCalculator, soundEffectPlayer)

        // When
        viewModel.processScript()

        // Then
        verify { sttRecognizer.startListening() }
    }

    @Test
    fun `When forceQuite is called it invokes scheduleNextAlarm, terminate, and stopRecognizer`() {
        // Given
        val viewModel = SayItViewModel(serviceController, sttRecognizer, editDistanceCalculator, soundEffectPlayer)

        // When
        viewModel.forceQuit()

        // Then
        verify { serviceController.scheduleNextAlarm(any()) }
        verify { serviceController.terminate() }
        verify { sttRecognizer.stopRecognizer() }
    }

    @Test
    fun `When the ServiceController is in the RunningSayIt state, it sets the state to Processing with SayItInfo`() {
        // Given
        every { serviceController.controllerState } returns
            MutableStateFlow(ControllerState.RunningSayIt(SayItScripts("SayIt")))

        val viewModel = SayItViewModel(serviceController, sttRecognizer, editDistanceCalculator, soundEffectPlayer)

        // Then
        viewModel.state.value mustBe Processing(
            SayItInfo(
                script = "SayIt",
                sttResult = "",
                status = SttStatus.READY,
                count = Count(1, 1),
            )
        )
    }

    @Test
    fun `When the ServiceController is not in the RunningSayIt state, it sets the state to Error`() {
        // Given
        every { serviceController.controllerState } returns MutableStateFlow(ControllerState.Error)

        // When
        val viewModel = SayItViewModel(serviceController, sttRecognizer, editDistanceCalculator, soundEffectPlayer)

        // Then
        viewModel.state.value mustBe Error
    }

    @Test
    fun `when the SttRecognizer is in the Ready, it sets the state to Listening`() = runTest {
        // Given
        val sttRecognizer = SttRecognizerFake(listOf(RecognizerState.Ready))
        val viewModel = SayItViewModel(serviceController, sttRecognizer, editDistanceCalculator, soundEffectPlayer)

        viewModel.state.test {
            skipItems(1)

            // When
            viewModel.processScript()

            // Then
            awaitItem() mustBe Processing(info = sayItInfo.copy(status = SttStatus.LISTENING))
        }
    }

    @Test
    fun `when the SttRecognizer is in the Processing, it updates the Processing state`() = runTest {
        // Given
        val sttResult: String = fixture.fixture()
        val sttRecognizer = SttRecognizerFake(listOf(RecognizerState.Processing(sttResult)))
        val viewModel = SayItViewModel(serviceController, sttRecognizer, editDistanceCalculator, soundEffectPlayer)

        viewModel.state.test {
            skipItems(1)

            // When
            viewModel.processScript()

            // Then
            awaitItem() mustBe Processing(sayItInfo.copy(sttResult = sttResult))
        }
    }

    @Test
    fun `When the SttRecognizer is in Done, it updates the state to Success`() = runTest {
        // Given
        val scripts: List<String> = List(3) { fixture.fixture() }
        // Set the SpeechToText result to match the script.
        val sttRecognizer = SttRecognizerFake(listOf(RecognizerState.Done(scripts.first())))

        // It sets the serviceController.controller state to ControllerState.RunningSayIt(given_scripts)
        setupServiceController(scripts)

        val viewModel = SayItViewModel(serviceController, sttRecognizer, editDistanceCalculator, soundEffectPlayer)

        viewModel.state.test {
            skipItems(1)

            // When
            viewModel.processScript()

            // Then
            awaitItem() mustBe Processing(
                SayItInfo(
                    script = scripts[1], // On success, it sets the script to the next script.
                    count = Count(2, 3), // On success, it increases the counter.
                    sttResult = "", // On success, it resets the SttResult.
                    status = SttStatus.SUCCESS
                )
            )
            verify { soundEffectPlayer.playSuccessSoundEffect() }
        }
    }

    @Test
    fun `When the SttRecognizer is in Done, it updates the state to Failure`() = runTest {
        // Given
        val scripts: List<String> = List(3) { fixture.fixture() }
        val wrongResult: String = fixture.fixture()
        val recognizerState = listOf(RecognizerState.Done(wrongResult))
        val sttRecognizer = SttRecognizerFake(recognizerState)

        setupServiceController(scripts)
        every { editDistanceCalculator.calculateEditDistance(any(), any()) } returns Int.MAX_VALUE

        val viewModel = SayItViewModel(serviceController, sttRecognizer, editDistanceCalculator, soundEffectPlayer)

        viewModel.state.test {
            skipItems(1)

            // When
            viewModel.processScript()

            // Then
            awaitItem() mustBe Processing(
                SayItInfo(
                    script = scripts[0],
                    sttResult = wrongResult,
                    status = SttStatus.FAILED,
                    count = Count(current = 1, total = 3)
                )
            )
            verify { soundEffectPlayer.playFailureSoundEffect() }
        }
    }

    @Test
    fun `When the SttRecognizer is in Done and It's the last script, It sets the state to Finish`() = runTest {
        // Given
        val scripts: List<String> = List(3) { fixture.fixture() }
        val recognizerState = listOf(
            RecognizerState.Done(scripts[0]),
            RecognizerState.Done(scripts[1]),
            RecognizerState.Done(scripts[2]),
        )
        val sttRecognizer = SttRecognizerFake(recognizerState)
        setupServiceController(scripts)

        val viewModel = SayItViewModel(serviceController, sttRecognizer, editDistanceCalculator, soundEffectPlayer)

        viewModel.state.test {
            skipItems(1)

            // When
            viewModel.processScript()
            awaitItem() mustBe Processing(
                SayItInfo(
                    script = scripts[1],
                    sttResult = "",
                    status = SttStatus.SUCCESS,
                    count = Count(current = 2, total = 3)
                )
            )

            viewModel.processScript()
            awaitItem() mustBe Processing(
                SayItInfo(
                    script = scripts[2],
                    sttResult = "",
                    status = SttStatus.SUCCESS,
                    count = Count(current = 3, total = 3)
                )
            )

            viewModel.processScript()

            // Then
            awaitItem() mustBe Finished
            verify { soundEffectPlayer.stopPlayer() }
        }
    }

    @Test
    fun `When Recognizer is in Done, It determines success or failure based on ERROR_ACCEPTANCE_RATE`() = runTest {
        // Given
        val lengthOfScript: Int = fixture.fixture(range = 9..90)
        val script = "A".repeat(lengthOfScript)
        val sttRecognizer = SttRecognizerFake(listOf(RecognizerState.Done("")))

        setupServiceController(listOf(script))
        every { editDistanceCalculator.calculateEditDistance(any(), any()) } returns
            (lengthOfScript * 0.2).toInt() + 1

        val viewModel = SayItViewModel(serviceController, sttRecognizer, editDistanceCalculator, soundEffectPlayer)

        viewModel.state.test {
            skipItems(1)

            // When
            viewModel.processScript()

            // Then
            awaitItem() mustBe Processing(
                SayItInfo(
                    script = script,
                    sttResult = "",
                    status = SttStatus.FAILED,
                    count = Count(current = 1, total = 1)
                )
            )
        }
    }

    @Test
    fun `When Recognizer is in Done, It determines success or failure based on ERROR_ACCEPTANCE_RATE(success case)`() =
        runTest {
            // Given
            val lengthOfScript: Int = fixture.fixture(range = 9..90)
            val script = "A".repeat(lengthOfScript)
            val sttRecognizer = SttRecognizerFake(listOf(RecognizerState.Done("")))

            setupServiceController(listOf(script))
            every { editDistanceCalculator.calculateEditDistance(any(), any()) } returns
                (lengthOfScript * 0.2).toInt() - 1

            val viewModel = SayItViewModel(serviceController, sttRecognizer, editDistanceCalculator, soundEffectPlayer)

            viewModel.state.test {
                skipItems(1)

                // When
                viewModel.processScript()

                // Then
                awaitItem() mustBe Finished
            }
        }

    @Test
    fun `Given runCommand is called it executes the given command`() {
        // Given
        val viewModel = SayItViewModel(serviceController, sttRecognizer, editDistanceCalculator, soundEffectPlayer)
        val command: CommandContract.Command<SayItViewModel> = mockk(relaxed = true)

        // When
        viewModel.runCommand(command)

        // Then
        verify(exactly = 1) { command.execute(any()) }
    }

    @Test
    fun `It fulfills SayItViewModel`() {
        // Given
        val viewModel = SayItViewModel(serviceController, sttRecognizer, editDistanceCalculator, soundEffectPlayer)

        viewModel fulfils SayItContract.SayItViewModel::class
    }

    private fun setupServiceController(scripts: List<String>) {
        every { serviceController.controllerState } returns
            MutableStateFlow(ControllerState.RunningSayIt(SayItScripts(scripts)))
    }

    private val sayItInfo =
        SayItInfo(
            script = "",
            sttResult = "",
            status = SttStatus.READY,
            count = Count(1, 1),
        )
}

private class SttRecognizerFake(state: List<RecognizerState>) : SttRecognizer {
    private val states = state.toMutableList()

    private val _recognizerState: MutableStateFlow<RecognizerState> = MutableStateFlow(RecognizerState.Initial)
    override val recognizerState: StateFlow<RecognizerState> = _recognizerState.asStateFlow()

    override val rmsDbState: StateFlow<RecognizerRmsDb>
        get() = TODO()

    override fun startListening() {
        _recognizerState.update { states.removeFirst() }
    }

    override fun stopRecognizer() {
        _recognizerState.update { states.removeFirst() }
    }
}