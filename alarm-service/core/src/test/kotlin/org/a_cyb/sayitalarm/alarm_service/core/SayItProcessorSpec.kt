/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.alarm_service.core

import kotlin.test.Test
import app.cash.turbine.test
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.a_cyb.sayitalarm.domain.alarm_service.AlarmServiceContract
import org.a_cyb.sayitalarm.domain.alarm_service.AlarmServiceContract.SayItProcessor.ProcessResult
import org.a_cyb.sayitalarm.domain.alarm_service.AlarmServiceContract.SayItProcessor.SayItProcessorState.Error
import org.a_cyb.sayitalarm.domain.alarm_service.AlarmServiceContract.SayItProcessor.SayItProcessorState.Initial
import org.a_cyb.sayitalarm.domain.alarm_service.AlarmServiceContract.SayItProcessor.SayItProcessorState.Processed
import org.a_cyb.sayitalarm.domain.alarm_service.AlarmServiceContract.SayItProcessor.SayItProcessorState.Processing
import org.a_cyb.sayitalarm.domain.alarm_service.AlarmServiceContract.SayItProcessor.Script
import org.a_cyb.sayitalarm.domain.alarm_service.AlarmServiceContract.SayItProcessor.SttResult
import org.a_cyb.sayitalarm.domain.alarm_service.AlarmServiceContract.SttRecognizer
import org.a_cyb.sayitalarm.domain.alarm_service.AlarmServiceContract.SttRecognizer.RecognizerRmsDb
import org.a_cyb.sayitalarm.domain.alarm_service.AlarmServiceContract.SttRecognizer.RecognizerState
import org.a_cyb.sayitalarm.util.fulfils
import org.a_cyb.sayitalarm.util.mustBe
import org.junit.After
import org.junit.Before
import tech.antibytes.kfixture.fixture
import tech.antibytes.kfixture.kotlinFixture

@OptIn(ExperimentalCoroutinesApi::class)
class SayItProcessorSpec {

    private lateinit var scope: CoroutineScope
    private val fixture = kotlinFixture()

    @Before
    fun setup() {
        scope = TestScope(UnconfinedTestDispatcher())
        Dispatchers.setMain(StandardTestDispatcher())
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `When startSayIt is called it invokes SttRecognizer startListening`() {
        // Given
        val recognizer: SttRecognizer = mockk(relaxed = true)
        val processor = SayItProcessor(recognizer, scope)

        // When
        processor.startSayIt("")

        // Then
        verify { recognizer.startListening() }
    }

    @Test
    fun `When stopProcessor is called it invokes SttRecognizer stopSttRecognizer`() {
        // Given
        val recognizer: SttRecognizer = mockk(relaxed = true)
        val processor = SayItProcessor(recognizer, scope)

        // When
        processor.stopProcessor()

        // Then
        verify { recognizer.stopRecognizer() }
    }

    @Test
    fun `When startSayIt is called it sets the state to Processing with a script`() = runTest {
        // Given
        val script: String = fixture.fixture()
        val recognizer = SttRecognizerFake()
        val processor = SayItProcessor(recognizer, scope)

        processor.processorState.test {
            skipItems(1)

            // When
            processor.startSayIt(script)

            // Then
            awaitItem() mustBe Processing(Script(script), SttResult(""))
        }
    }

    @Test
    fun `When the SttRecognizer is processing, it sets the state to Processing with a partial result`() = runTest {
        // Given
        val script: String = fixture.fixture()
        val partialResult: String = fixture.fixture()
        val recognizer = SttRecognizerFake(listOf(RecognizerState.Processing(partialResult)))
        val processor = SayItProcessor(recognizer, scope)

        processor.processorState.test {
            processor.startSayIt(script)
            skipItems(2)

            // When
            recognizer.testHelperUpdateState()

            // Then
            awaitItem() mustBe Processing(Script(script), SttResult(partialResult))
        }
    }

    @Test
    fun `When the SttRecognizer is done, it sets the state to Processed with a success result`() = runTest {
        // Given
        val script: String = fixture.fixture()
        val sttResult: String = script
        val recognizer = SttRecognizerFake(listOf(RecognizerState.Done(sttResult)))
        val processor = SayItProcessor(recognizer, scope)

        processor.processorState.test {
            processor.startSayIt(script)
            skipItems(2)

            // When
            recognizer.testHelperUpdateState()

            // Then
            awaitItem() mustBe Processed(ProcessResult.SUCCESS, SttResult(sttResult))
        }
    }

    @Test
    fun `When the SttRecognizer is done, it sets the state to Done with a failed result`() = runTest {
        // Given
        val script = "The distance result is within the allowed tolerance."
        val sttResult = "The edit distance is validated as being within acceptable limits."
        val recognizer = SttRecognizerFake(listOf(RecognizerState.Done(sttResult)))
        val processor = SayItProcessor(recognizer, scope)

        processor.processorState.test {
            processor.startSayIt(script)
            skipItems(2)

            // When
            recognizer.testHelperUpdateState()

            // Then
            awaitItem() mustBe Processed(ProcessResult.FAILED, SttResult(sttResult))
        }
    }

    @Test
    fun `When SttRecognizer is in Error, it sets the state to Error`() = runTest {
        // Given
        val errorMessage: String = fixture.fixture()
        val recognizer = SttRecognizerFake(listOf(RecognizerState.Error(errorMessage)))
        val processor = SayItProcessor(recognizer, scope)

        processor.processorState.test {
            skipItems(1)

            // When
            recognizer.testHelperUpdateState()

            // Then
            awaitItem() mustBe Error(errorMessage)
        }
    }

    @Test
    fun `It is in Initial state`() {
        // Given
        val processor = SayItProcessor(mockk(relaxed = true), scope)

        // Then
        processor.processorState.value mustBe Initial
    }

    @Test
    fun `It fulfills SayItProcessor`() {
        // Given
        val processor = SayItProcessor(mockk(relaxed = true), scope)

        // Then
        processor fulfils AlarmServiceContract.SayItProcessor::class
    }
}

private class SttRecognizerFake(
    states: List<RecognizerState> = listOf(RecognizerState.Initial),
) : SttRecognizer {
    val states = states.toMutableList()

    private val _recognizerState: MutableStateFlow<RecognizerState> = MutableStateFlow(RecognizerState.Initial)
    override val recognizerState: StateFlow<RecognizerState> = _recognizerState.asStateFlow()

    override val rmsDbState: StateFlow<RecognizerRmsDb>
        get() = TODO("Not yet implemented")

    fun testHelperUpdateState() {
        _recognizerState.update { states.removeFirst() }
    }

    override fun startListening() {
        // _recognizerState.update { states.removeFirst() }
    }

    override fun stopRecognizer() {
        _recognizerState.update { states.removeFirst() }
    }
}
