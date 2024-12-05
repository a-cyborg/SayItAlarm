/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.domain.interactor

import app.cash.turbine.test
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.mockk.verifySequence
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.a_cyb.sayitalarm.domain.alarm_service.AlarmControllerContract
import org.a_cyb.sayitalarm.domain.alarm_service.AlarmControllerContract.AlarmControllerState
import org.a_cyb.sayitalarm.domain.alarm_service.EditDistanceCalculatorContract
import org.a_cyb.sayitalarm.domain.alarm_service.SttRecognizerContract
import org.a_cyb.sayitalarm.domain.alarm_service.SttRecognizerContract.RecognizerState
import org.a_cyb.sayitalarm.domain.interactor.InteractorContract.SayItInteractor.Count
import org.a_cyb.sayitalarm.domain.interactor.InteractorContract.SayItInteractor.ProgressStatus
import org.a_cyb.sayitalarm.domain.interactor.InteractorContract.SayItInteractor.SayItError
import org.a_cyb.sayitalarm.domain.interactor.InteractorContract.SayItInteractor.SayItState
import org.a_cyb.sayitalarm.domain.interactor.InteractorContract.SayItInteractor.SayItState.Error
import org.a_cyb.sayitalarm.domain.repository.RepositoryContract
import org.a_cyb.sayitalarm.entity.Alarm
import org.a_cyb.sayitalarm.entity.AlarmType
import org.a_cyb.sayitalarm.entity.AlertType
import org.a_cyb.sayitalarm.entity.Hour
import org.a_cyb.sayitalarm.entity.Label
import org.a_cyb.sayitalarm.entity.Minute
import org.a_cyb.sayitalarm.entity.Ringtone
import org.a_cyb.sayitalarm.entity.SayIt
import org.a_cyb.sayitalarm.entity.SayItScripts
import org.a_cyb.sayitalarm.entity.WeeklyRepeat
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class SayItInteractorSpec {
    private val alarmControllerMock: AlarmControllerContract = mockk(relaxed = true)
    private val sttRecognizerMock: SttRecognizerContract = mockk(relaxed = true)
    private val editDistanceCalculatorMock: EditDistanceCalculatorContract = mockk(relaxed = true)
    private val alarmRepositoryMock: RepositoryContract.AlarmRepository = mockk(relaxed = true)
    private lateinit var testDispatcher: CoroutineDispatcher

    private lateinit var sayItInteractor: SayItInteractor

    @Before
    fun setup() {
        testDispatcher = UnconfinedTestDispatcher()

        Dispatchers.setMain(testDispatcher)

        sayItInteractor = SayItInteractor(
            alarmControllerMock,
            sttRecognizerMock,
            editDistanceCalculatorMock,
            alarmRepositoryMock,
            testDispatcher,
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        clearAllMocks()
    }

    @Test
    fun stateIsInitiallyInitial() = runTest {
        assertEquals(SayItState.Initial, sayItInteractor.sayItState.value)
    }

    @Test
    fun `When startSayIt is called and the alarmController state is ServiceConnected, it loads the alarm`() = runTest {
        // Given
        val givenAlarmId = 12L
        val alarmControllerStateMock = MutableStateFlow(AlarmControllerState.ServiceConnected(givenAlarmId))

        every {
            alarmControllerMock.alarmControllerState
        } returns
            alarmControllerStateMock

        every {
            alarmRepositoryMock.getAlarm(any(), any())
        } returns
            async { Result.failure(IllegalStateException()) }

        // When
        sayItInteractor.startSayIt(this)

        // Then
        verify {
            @Suppress("DeferredResultUnused")
            alarmRepositoryMock.getAlarm(givenAlarmId, any())
        }
    }

    @Test
    fun `When startSayIt is called and the alarmController state is ServiceDisconnected, it updates the state to Error`() =
        runTest {
            // Given
            val alarmControllerStateMock = MutableStateFlow(AlarmControllerState.ServiceDisconnected)

            every {
                alarmControllerMock.alarmControllerState
            } returns
                alarmControllerStateMock

            sayItInteractor.sayItState.test {
                skipItems(1)

                // When
                sayItInteractor.startSayIt(this@runTest)

                // Then
                assertEquals(
                    Error(SayItError.SERVICE_DISCONNECTED_WHILE_RESOLVING_ALARM_ID),
                    awaitItem(),
                )
            }
        }

    @Test
    fun `When the Alarm is successfully loaded and the sayItScripts is not empty, it updates the state to Ready`() =
        runTest {
            // Given
            val alarmControllerStateMock = MutableStateFlow(AlarmControllerState.ServiceConnected(fakeAlarm.id))

            every {
                alarmControllerMock.alarmControllerState
            } returns
                alarmControllerStateMock

            every {
                alarmRepositoryMock.getAlarm(any(), any())
            } returns
                async { Result.success(fakeAlarm) }

            sayItInteractor.sayItState.test {
                skipItems(1)

                // When
                sayItInteractor.startSayIt(this@runTest)

                // Then
                assertEquals(SayItState.Ready, awaitItem())
            }
        }

    @Test
    fun `When the Alarm is successfully loaded and the sayItScripts is empty, it updates the state to Completed`() =
        runTest {
            // Given
            val alarmControllerStateMock = MutableStateFlow(AlarmControllerState.ServiceConnected(fakeAlarm.id))

            every {
                alarmControllerMock.alarmControllerState
            } returns
                alarmControllerStateMock

            every {
                alarmRepositoryMock.getAlarm(any(), any())
            } returns
                async { Result.success(fakeAlarm.copy(sayItScripts = SayItScripts())) }

            sayItInteractor.sayItState.test {
                skipItems(1)

                // When
                sayItInteractor.startSayIt(this@runTest)

                // Then
                assertEquals(SayItState.Completed, awaitItem())
            }
        }

    @Test
    fun `When the Alarm fails to load, it updates the state to Error`() = runTest {
        // Given
        val alarmControllerStateMock = MutableStateFlow(AlarmControllerState.ServiceConnected(fakeAlarm.id))

        every {
            alarmControllerMock.alarmControllerState
        } returns
            alarmControllerStateMock

        every {
            alarmRepositoryMock.getAlarm(any(), any())
        } returns
            async { Result.failure(IllegalStateException()) }

        sayItInteractor.sayItState.test {
            skipItems(1)

            // When
            sayItInteractor.startSayIt(this@runTest)

            // Then
            assertEquals(Error(SayItError.ALARM_LOAD_FAILED), awaitItem())
        }
    }

    @Test
    fun `When startListening is called, it calls SttRecognizer startListening`() {
        // When
        sayItInteractor.startListening()

        // Then
        verify { sttRecognizerMock.startListening() }
    }

    @Test
    fun `When stopSayIt is called, it calls SttRecognizer stopRecognizer`() {
        // When
        sayItInteractor.stopSayIt()

        // Then
        verify { sttRecognizerMock.stopRecognizer() }
    }

    @Test
    fun `When shutDown is called, it calls SttRecognizer stopRecognizer and alarmController stopService`() {
        // When
        sayItInteractor.shutdown()

        // Then
        verifySequence {
            sttRecognizerMock.stopRecognizer()
            alarmControllerMock.stopService()
        }
    }

    @Test
    fun `SttRecognizerState is updated to Ready and the current state is Ready, the state is updated to InProgress`() =
        runTest {
            // Given
            val recognizerStateMock = MutableStateFlow<RecognizerState>(RecognizerState.Initial)

            every { sttRecognizerMock.recognizerState } returns recognizerStateMock

            setupControllerAndRepositoryMock()

            // When
            sayItInteractor.sayItState.test {
                sayItInteractor.startSayIt(backgroundScope)
                // Skip Initial & Ready
                skipItems(2)

                // When
                recognizerStateMock.update { RecognizerState.Ready }

                // Then
                val expected =
                    SayItState.InProgress(
                        ProgressStatus.IN_PROGRESS,
                        SayIt(fakeAlarm.sayItScripts.scripts.first(), ""),
                        Count(1, fakeAlarm.sayItScripts.scripts.size),
                    )

                assertEquals(expected, awaitItem())
            }
        }

    @Test
    fun `SttRecognizerState is updated to Processing and the current state is InProgress, it updates transcript`() =
        runTest {
            // Given
            val givenTranscript = "Stars are born from"
            val recognizerStateMock = MutableStateFlow<RecognizerState>(RecognizerState.Initial)
            every { sttRecognizerMock.recognizerState } returns recognizerStateMock
            setupControllerAndRepositoryMock()

            sayItInteractor.sayItState.test {
                // It updates the state to Ready
                sayItInteractor.startSayIt(backgroundScope)
                // It updates the state to InProgress
                recognizerStateMock.update { RecognizerState.Ready }
                // Initial & Ready & InProgress
                skipItems(3)

                // When
                recognizerStateMock.update { RecognizerState.Processing(givenTranscript) }

                // Then
                val expected =
                    SayItState.InProgress(
                        ProgressStatus.IN_PROGRESS,
                        SayIt(fakeAlarm.sayItScripts.scripts.first(), givenTranscript),
                        Count(1, fakeAlarm.sayItScripts.scripts.size),
                    )
                assertEquals(expected, awaitItem())
            }
        }

    @Test
    fun `SttRecognizerState is updated to Done, the state is updated to InProgress(Success)`() = runTest {
        // Given
        val givenResult = fakeAlarm.sayItScripts.scripts.first()
        val recognizerStateMock = MutableStateFlow<RecognizerState>(RecognizerState.Initial)
        every { sttRecognizerMock.recognizerState } returns recognizerStateMock
        setupControllerAndRepositoryMock()

        sayItInteractor.sayItState.test {
            // It updates the state to Ready
            sayItInteractor.startSayIt(backgroundScope)
            // It updates the state to InProgress
            recognizerStateMock.update { RecognizerState.Ready }
            // Initial & Ready & InProgress
            skipItems(3)

            // When
            recognizerStateMock.update { RecognizerState.Done(givenResult) }

            // Then
            val expected =
                SayItState.InProgress(
                    ProgressStatus.SUCCESS,
                    SayIt(givenResult, ""),
                    Count(1, fakeAlarm.sayItScripts.scripts.size),
                )
            assertEquals(expected, awaitItem())
        }
    }

    @Test
    fun `SttRecognizerState is updated to Done, the state is updated to InProgress(Failed)`() = runTest {
        // Given
        val givenResult = "I said different things"
        val recognizerStateMock = MutableStateFlow<RecognizerState>(RecognizerState.Initial)
        every { sttRecognizerMock.recognizerState } returns recognizerStateMock
        // It returns an error number of 6, which is above the allowed error number for the given test string.
        every { editDistanceCalculatorMock.calculateEditDistance(any(), any()) } returns 6
        setupControllerAndRepositoryMock()

        sayItInteractor.sayItState.test {
            // It updates the state to Ready
            sayItInteractor.startSayIt(backgroundScope)
            // It updates the state to InProgress
            recognizerStateMock.update { RecognizerState.Ready }
            // Initial & Ready & InProgress
            skipItems(3)

            // When
            recognizerStateMock.update { RecognizerState.Done(givenResult) }

            // Then
            val expected =
                SayItState.InProgress(
                    ProgressStatus.FAILED,
                    SayIt(fakeAlarm.sayItScripts.scripts.first(), ""),
                    Count(1, fakeAlarm.sayItScripts.scripts.size),
                )
            assertEquals(expected, awaitItem())
        }
    }

    @Test
    fun `SttRecognizerState is updated to Done, the state is updated to Completed`() = runTest {
        // Given
        val givenFakeAlarm = fakeAlarm.copy(sayItScripts = SayItScripts("Only one"))
        val recognizerStateMock = MutableStateFlow<RecognizerState>(RecognizerState.Initial)
        every { sttRecognizerMock.recognizerState } returns recognizerStateMock

        every {
            alarmControllerMock.alarmControllerState
        } returns
            MutableStateFlow(AlarmControllerState.ServiceConnected(fakeAlarm.id))

        every {
            alarmRepositoryMock.getAlarm(any(), any())
        } returns
            async { Result.success(givenFakeAlarm) }

        sayItInteractor.sayItState.test {
            // It updates the state to Ready
            sayItInteractor.startSayIt(backgroundScope)
            // It updates the state to InProgress
            recognizerStateMock.update { RecognizerState.Ready }
            // Initial & Ready & InProgress
            skipItems(3)

            // When
            recognizerStateMock.update { RecognizerState.Done("Only one") }

            // Then
            assertEquals(SayItState.Completed, awaitItem())
        }
    }

    @Test
    fun `SttRecognizerState is updated to Ready and the current state is InProgress(Success), the state is updated to InProgress`() =
        runTest {
            // Given
            val recognizerStateMock = MutableStateFlow<RecognizerState>(RecognizerState.Initial)
            every { sttRecognizerMock.recognizerState } returns recognizerStateMock
            setupControllerAndRepositoryMock()

            sayItInteractor.sayItState.test {
                // It updates the state to Ready
                sayItInteractor.startSayIt(backgroundScope)
                // It updates the state to InProgress
                recognizerStateMock.update { RecognizerState.Ready }
                // Initial & Ready & InProgress
                skipItems(3)
                recognizerStateMock.update { RecognizerState.Done(fakeAlarm.sayItScripts.scripts.first()) }
                // Success
                skipItems(1)

                // When
                recognizerStateMock.update { RecognizerState.Ready }

                // Then
                val expected =
                    SayItState.InProgress(
                        ProgressStatus.IN_PROGRESS,
                        SayIt(fakeAlarm.sayItScripts.scripts[1], ""),
                        Count(2, fakeAlarm.sayItScripts.scripts.size),
                    )
                assertEquals(expected, awaitItem())
            }
        }

    private fun setupControllerAndRepositoryMock() {
        every {
            alarmRepositoryMock.getAlarm(any(), any())
        } returns
            CompletableDeferred(Result.success(fakeAlarm))

        every {
            alarmControllerMock.alarmControllerState
        } returns
            MutableStateFlow(AlarmControllerState.ServiceConnected(fakeAlarm.id))
    }

    private val fakeAlarm =
        Alarm(
            id = 3L,
            Hour(3),
            Minute(30),
            WeeklyRepeat(),
            Label("3AM"),
            true,
            AlertType.SOUND_AND_VIBRATE,
            Ringtone("default_ringtone.mp3"),
            AlarmType.SAY_IT,
            SayItScripts("fake script one", "fake script two", "fake script three"),
        )
}
