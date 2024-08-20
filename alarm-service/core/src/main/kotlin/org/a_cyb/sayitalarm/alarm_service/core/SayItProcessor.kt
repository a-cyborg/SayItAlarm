/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.alarm_service.core

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import org.a_cyb.sayitalarm.alarm_service.core.util.calculateEditDistance
import org.a_cyb.sayitalarm.domain.alarm_service.AlarmServiceContract.SayItProcessor
import org.a_cyb.sayitalarm.domain.alarm_service.AlarmServiceContract.SayItProcessor.ProcessResult
import org.a_cyb.sayitalarm.domain.alarm_service.AlarmServiceContract.SayItProcessor.SayItProcessorState
import org.a_cyb.sayitalarm.domain.alarm_service.AlarmServiceContract.SayItProcessor.SayItProcessorState.Error
import org.a_cyb.sayitalarm.domain.alarm_service.AlarmServiceContract.SayItProcessor.SayItProcessorState.Initial
import org.a_cyb.sayitalarm.domain.alarm_service.AlarmServiceContract.SayItProcessor.SayItProcessorState.Processed
import org.a_cyb.sayitalarm.domain.alarm_service.AlarmServiceContract.SayItProcessor.SayItProcessorState.Processing
import org.a_cyb.sayitalarm.domain.alarm_service.AlarmServiceContract.SayItProcessor.Script
import org.a_cyb.sayitalarm.domain.alarm_service.AlarmServiceContract.SayItProcessor.SttResult
import org.a_cyb.sayitalarm.domain.alarm_service.AlarmServiceContract.SttRecognizer
import org.a_cyb.sayitalarm.domain.alarm_service.AlarmServiceContract.SttRecognizer.RecognizerState

private const val TAG = "[***] SayItProcessor"

class SayItProcessor(
    private val recognizer: SttRecognizer,
    scope: CoroutineScope,
) : SayItProcessor {

    private val _processorState: MutableStateFlow<SayItProcessorState> = MutableStateFlow(Initial)
    override val processorState: StateFlow<SayItProcessorState> = _processorState.asStateFlow()

    init {
        recognizer.recognizerState
            .onEach(::handleRecognizerState)
            .launchIn(scope)
    }

    override fun startSayIt(script: String) {
        Processing(Script(script), SttResult("")).update()
        recognizer.startListening()
    }

    override fun stopProcessor() {
        recognizer.stopRecognizer()
    }

    private fun handleRecognizerState(recognizerState: RecognizerState) {
        when (recognizerState) {
            is RecognizerState.Processing -> onProcessing(recognizerState.partialResults) // Listening.
            is RecognizerState.Done -> onDone(recognizerState.result)
            is RecognizerState.Error -> Error(recognizerState.cause).update()
            is RecognizerState.Initial, RecognizerState.Ready -> {}
        }
    }

    private fun onProcessing(sttResult: String) {
        val state = getProcessingOrNull()
            ?.copy(sttResult = SttResult(sttResult))
            ?: Error("Unable to resolve state as Processing")

        state.update()
    }

    private fun onDone(resultText: String) {
        val script = getProcessingOrNull()?.script ?: Script("")
        val result = resolveResult(script, resultText)
        Processed(result, SttResult(resultText)).update()
    }

    private fun resolveResult(script: Script, sttResult: String): ProcessResult =
        when (isEditDistanceValid(script.script, sttResult)) {
            true -> ProcessResult.SUCCESS
            false -> ProcessResult.FAILED
        }

    private fun isEditDistanceValid(script: String, sttResult: String): Boolean {
        val maxAllowedErrors = script.length * ERROR_ACCEPTANCE_RATE
        val errors = calculateEditDistance(script, sttResult)

        return if (errors < maxAllowedErrors) true else false
    }

    private fun getProcessingOrNull(): Processing? = (_processorState.value as? Processing)

    private fun SayItProcessorState.update() {
        _processorState.update { this }
    }

    companion object {
        private const val ERROR_ACCEPTANCE_RATE = 0.2
    }
}