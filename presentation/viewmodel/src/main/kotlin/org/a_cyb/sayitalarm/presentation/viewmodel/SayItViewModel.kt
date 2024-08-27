/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.presentation.viewmodel

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.a_cyb.sayitalarm.domain.alarm_service.AlarmServiceContract.AlarmServiceController
import org.a_cyb.sayitalarm.domain.alarm_service.AlarmServiceContract.AlarmServiceController.ControllerState
import org.a_cyb.sayitalarm.domain.alarm_service.AlarmServiceContract.EditDistanceCalculator
import org.a_cyb.sayitalarm.domain.alarm_service.AlarmServiceContract.SttRecognizer
import org.a_cyb.sayitalarm.presentation.SayItContract
import org.a_cyb.sayitalarm.presentation.SayItContract.Count
import org.a_cyb.sayitalarm.presentation.SayItContract.SayItInfo
import org.a_cyb.sayitalarm.presentation.SayItContract.SayItState
import org.a_cyb.sayitalarm.presentation.SayItContract.SayItState.Error
import org.a_cyb.sayitalarm.presentation.SayItContract.SayItState.Initial
import org.a_cyb.sayitalarm.presentation.SayItContract.SayItState.Processing
import org.a_cyb.sayitalarm.presentation.SayItContract.SttStatus
import org.a_cyb.sayitalarm.presentation.command.CommandContract.Command
import org.a_cyb.sayitalarm.presentation.command.CommandContract.CommandReceiver
import org.a_cyb.sayitalarm.presentation.sound_effect_player.SoundEffectPlayerContract

class SayItViewModel(
    private val serviceController: AlarmServiceController,
    private val sttRecognizer: SttRecognizer,
    private val editDistanceCalculator: EditDistanceCalculator,
    private val soundEffectPlayer: SoundEffectPlayerContract,
) : SayItContract.SayItViewModel, ViewModel() {

    private val _state: MutableStateFlow<SayItState> = MutableStateFlow(Initial)
    override val state: StateFlow<SayItState> = _state.asStateFlow()
    private val sayItScripts: List<String> by lazy { resolveSayItScripts() }

    init {
        setupSayIt()

        sttRecognizer.recognizerState
            .onEach(::handleRecognizerState)
            .launchIn(scope)
    }

    private fun resolveSayItScripts(): List<String> =
        (serviceController.controllerState.value as? ControllerState.RunningSayIt)
            ?.scripts?.scripts
            ?: emptyList()

    private fun setupSayIt() {
        when (sayItScripts.isNotEmpty()) {
            true -> Processing(getInitialInfo())
            false -> Error
        }.update()
    }

    private fun getInitialInfo(): SayItInfo =
        SayItInfo(
            script = sayItScripts.first(),
            sttResult = "",
            status = SttStatus.READY,
            count = Count(1, sayItScripts.size),
        )

    private fun SayItState.update() {
        _state.update { this }
    }

    override fun processScript() {
        sttRecognizer.startListening()
    }

    override fun finish() {
        serviceController.scheduleNextAlarm(scope)
        sttRecognizer.stopRecognizer()
        serviceController.terminate()
    }

    private fun handleRecognizerState(recognizerState: SttRecognizer.RecognizerState) {
        when (recognizerState) {
            is SttRecognizer.RecognizerState.Ready -> onReady()
            is SttRecognizer.RecognizerState.Processing -> onProcessing(recognizerState.partialResults)
            is SttRecognizer.RecognizerState.Done -> onDone(recognizerState.result)
            is SttRecognizer.RecognizerState.Error -> Error.update()
            is SttRecognizer.RecognizerState.Initial -> {}
        }
    }

    private fun onReady() {
        updateProcessing {
            copy(status = SttStatus.LISTENING)
        }
    }

    private fun onProcessing(sttResult: String) {
        updateProcessing {
            copy(sttResult = sttResult)
        }
    }

    private fun onDone(sttResult: String) {
        val script = (_state.value as Processing).info.script
        val isSuccess = isSuccess(script, sttResult)

        if (isSuccess) {
            scope.launch { soundEffectPlayer.playSuccessSoundEffect() }
            updateToSuccessOrFinished()
        } else {
            scope.launch { soundEffectPlayer.playFailureSoundEffect() }
            updateToFailure(sttResult)
        }
    }

    private fun isSuccess(script: String, sttResult: String): Boolean {
        val maxAllowedErrors = script.length * ERROR_ACCEPTANCE_RATE
        val errorCounts = editDistanceCalculator.calculateEditDistance(script, sttResult)

        return errorCounts <= maxAllowedErrors
    }

    private fun updateToSuccessOrFinished() {
        if (isTheLastScript()) {
            scope.launch { soundEffectPlayer.stopPlayer() }
            SayItState.Finished.update()
        } else {
            updateProcessing {
                copy(
                    script = sayItScripts[this.count.current],
                    sttResult = "",
                    status = SttStatus.SUCCESS,
                    count = count.copy(current = this.count.current + 1),
                )
            }
        }
    }

    private fun isTheLastScript(): Boolean {
        val count = (_state.value as Processing).info.count
        return count.current == count.total
    }

    private fun updateToFailure(sttResult: String) {
        updateProcessing {
            copy(
                sttResult = sttResult,
                status = SttStatus.FAILED
            )
        }
    }

    private fun updateProcessing(updateAction: SayItInfo.() -> SayItInfo) {
        val state = (_state.value as? Processing)
            ?.let { Processing(it.info.updateAction()) }
            ?: Error

        state.update()
    }

    override fun <T : CommandReceiver> runCommand(command: Command<T>) {
        @Suppress("UNCHECKED_CAST")
        command.execute(this as T)
    }

    companion object {
        private const val ERROR_ACCEPTANCE_RATE = 0.2
    }
}
