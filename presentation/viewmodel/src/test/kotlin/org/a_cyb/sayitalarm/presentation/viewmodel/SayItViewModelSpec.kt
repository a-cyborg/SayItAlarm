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
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.a_cyb.sayitalarm.domain.interactor.InteractorContract.SayItInteractor
import org.a_cyb.sayitalarm.domain.interactor.InteractorContract.SayItInteractor.Count
import org.a_cyb.sayitalarm.domain.interactor.InteractorContract.SayItInteractor.ProgressStatus
import org.a_cyb.sayitalarm.entity.SayIt
import org.a_cyb.sayitalarm.presentation.contracts.SayItContract
import org.a_cyb.sayitalarm.presentation.contracts.SayItContract.SayItUIInfo
import org.a_cyb.sayitalarm.presentation.contracts.SayItContract.SayItUiState
import org.a_cyb.sayitalarm.presentation.contracts.SayItContract.SayItUiState.Initial
import org.a_cyb.sayitalarm.util.sound_effect_player.SoundEffectPlayerContract
import org.junit.After
import org.junit.Before
import org.junit.Test
import tech.antibytes.kfixture.fixture
import tech.antibytes.kfixture.kotlinFixture
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class SayItViewModelSpec {
    private val sayItInteractorMock: SayItInteractor = mockk(relaxed = true)
    private val soundEffectPlayerMock: SoundEffectPlayerContract = mockk(relaxed = true)
    private lateinit var interactorStateMock: MutableStateFlow<SayItInteractor.SayItState>
    private lateinit var viewModel: SayItContract.SayItViewModel

    private val fixture = kotlinFixture()

    @Before
    fun setup() {
        Dispatchers.setMain(UnconfinedTestDispatcher())

        interactorStateMock = MutableStateFlow(SayItInteractor.SayItState.Initial)
        every { sayItInteractorMock.sayItState } returns interactorStateMock

        viewModel = SayItViewModel(sayItInteractorMock, soundEffectPlayerMock)
    }

    @After
    fun tearDown() {
        clearAllMocks()
        Dispatchers.resetMain()
    }

    @Test
    fun stateIsInitiallyInitial() = runTest {
        assertEquals(Initial, actual = viewModel.state.value)
    }

    @Test
    fun onInit_beginsCollectingInteractorState() = runTest {
        assertEquals(1, interactorStateMock.subscriptionCount.value)
    }

    @Test
    fun whenProcessScriptIsCalled_triggersInteractorStartListening() {
        // When
        viewModel.processScript()

        // Then
        verify { sayItInteractorMock.startListening() }
    }

    @Test
    fun whenFinishIsCalled_triggersPlayerStopPlay_andInteractorShutDown() {
        // When
        viewModel.finish()

        // Then
        verify { soundEffectPlayerMock.stopPlayer() }
        verify { sayItInteractorMock.shutdown() }
    }

    @Test
    fun whenInteractorStateIsInitial_triggersInteractorStartSayIt() = runTest {
        // Interactor initial state is provided on setUp
        verify { sayItInteractorMock.startSayIt(any()) }
    }

    @Test
    fun whenInteractorStateIsReady_triggersInteractorStartListening() = runTest {
        // When
        interactorStateMock.update { SayItInteractor.SayItState.Ready }

        // Then
        verify { sayItInteractorMock.startListening() }
    }

    @Test
    fun whenInteractorStateIsInProgressAndStatusIsPROGRESS_updateStateToListening() = runTest {
        // Given
        val givenInteractorState = SayItInteractor.SayItState.InProgress(
            status = ProgressStatus.IN_PROGRESS,
            sayIt = SayIt("Script One", ""),
            count = Count(1, 3),
        )

        viewModel.state.test {
            skipItems(1)

            // When
            interactorStateMock.update { givenInteractorState }

            // Then
            val expected = SayItUiState.Listening(
                SayItUIInfo(
                    givenInteractorState.sayIt.script,
                    givenInteractorState.sayIt.transcript,
                    givenInteractorState.count.current,
                    givenInteractorState.count.total,
                ),
            )
            assertEquals(expected, awaitItem())
        }
    }

    @Test
    fun whenInteractorStateIsInProgressAndStatusIsSUCCESS_updateStateToSuccess() = runTest {
        // Given
        val givenInteractorState = SayItInteractor.SayItState.InProgress(
            status = ProgressStatus.SUCCESS,
            sayIt = SayIt(fixture.fixture(), fixture.fixture()),
            count = Count(fixture.fixture(), fixture.fixture()),
        )

        viewModel.state.test {
            skipItems(1)

            // When
            interactorStateMock.update { givenInteractorState }

            // Then
            val expected = SayItUiState.Success(
                SayItUIInfo(
                    givenInteractorState.sayIt.script,
                    givenInteractorState.sayIt.transcript,
                    givenInteractorState.count.current,
                    givenInteractorState.count.total,
                ),
            )
            assertEquals(expected, awaitItem())
            verify { soundEffectPlayerMock.playSuccessSoundEffect() }
            verify { sayItInteractorMock.startListening() }
        }
    }

    @Test
    fun whenInteractorStateIsInProgressAndStatusIsFAILED_updateStateToFailed() = runTest {
        // Given
        val givenInteractorState = SayItInteractor.SayItState.InProgress(
            status = ProgressStatus.FAILED,
            sayIt = SayIt(fixture.fixture(), fixture.fixture()),
            count = Count(fixture.fixture(), fixture.fixture()),
        )

        viewModel.state.test {
            skipItems(1)

            // When
            interactorStateMock.update { givenInteractorState }

            // Then
            val expected = SayItUiState.Failed(
                SayItUIInfo(
                    givenInteractorState.sayIt.script,
                    givenInteractorState.sayIt.transcript,
                    givenInteractorState.count.current,
                    givenInteractorState.count.total,
                ),
            )
            assertEquals(expected, awaitItem())
            verify { soundEffectPlayerMock.playFailureSoundEffect() }
            verify { sayItInteractorMock.startListening() }
        }
    }

    @Test
    fun whenInteractorStateIsCompleted_updateStateToFinished() = runTest {
        viewModel.state.test {
            skipItems(1)

            // When
            interactorStateMock.update { SayItInteractor.SayItState.Completed }

            // Then
            assertEquals(SayItUiState.Finished, awaitItem())
            verify { soundEffectPlayerMock.playSuccessSoundEffect() }
        }
    }

    @Test
    fun whenInteractorStateIsError_updateStateToError() = runTest {
        // Given
        val givenInteractorError = SayItInteractor.SayItError.SPEECH_RECOGNIZER_ERROR

        viewModel.state.test {
            skipItems(1)

            // When
            interactorStateMock.update {
                SayItInteractor.SayItState.Error(givenInteractorError)
            }

            // Then
            assertEquals(SayItUiState.Error(givenInteractorError.name), awaitItem())
        }
    }
}
