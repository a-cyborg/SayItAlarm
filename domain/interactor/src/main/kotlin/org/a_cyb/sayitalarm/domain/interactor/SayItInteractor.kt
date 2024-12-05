/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.domain.interactor

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.a_cyb.sayitalarm.domain.alarm_service.AlarmControllerContract
import org.a_cyb.sayitalarm.domain.alarm_service.AlarmControllerContract.AlarmControllerState.ServiceConnected
import org.a_cyb.sayitalarm.domain.alarm_service.EditDistanceCalculatorContract
import org.a_cyb.sayitalarm.domain.alarm_service.SttRecognizerContract
import org.a_cyb.sayitalarm.domain.interactor.InteractorContract.SayItInteractor
import org.a_cyb.sayitalarm.domain.interactor.InteractorContract.SayItInteractor.Count
import org.a_cyb.sayitalarm.domain.interactor.InteractorContract.SayItInteractor.ProgressStatus
import org.a_cyb.sayitalarm.domain.interactor.InteractorContract.SayItInteractor.SayItError.ALARM_LOAD_FAILED
import org.a_cyb.sayitalarm.domain.interactor.InteractorContract.SayItInteractor.SayItError.SERVICE_DISCONNECTED_WHILE_RESOLVING_ALARM_ID
import org.a_cyb.sayitalarm.domain.interactor.InteractorContract.SayItInteractor.SayItError.SPEECH_RECOGNIZER_ERROR
import org.a_cyb.sayitalarm.domain.interactor.InteractorContract.SayItInteractor.SayItState
import org.a_cyb.sayitalarm.domain.interactor.InteractorContract.SayItInteractor.SayItState.Completed
import org.a_cyb.sayitalarm.domain.interactor.InteractorContract.SayItInteractor.SayItState.Error
import org.a_cyb.sayitalarm.domain.interactor.InteractorContract.SayItInteractor.SayItState.InProgress
import org.a_cyb.sayitalarm.domain.interactor.InteractorContract.SayItInteractor.SayItState.Initial
import org.a_cyb.sayitalarm.domain.interactor.InteractorContract.SayItInteractor.SayItState.Ready
import org.a_cyb.sayitalarm.domain.repository.RepositoryContract
import org.a_cyb.sayitalarm.entity.Alarm
import org.a_cyb.sayitalarm.entity.SayIt

class SayItInteractor(
    private val alarmController: AlarmControllerContract,
    private val sttRecognizer: SttRecognizerContract,
    private val editDistanceCalculator: EditDistanceCalculatorContract,
    private val alarmRepository: RepositoryContract.AlarmRepository,
    private val ioDispatcher: CoroutineDispatcher,
) : SayItInteractor {

    private val _sayItState = MutableStateFlow<SayItState>(Initial)
    override val sayItState: StateFlow<SayItState> = _sayItState.asStateFlow()

    private lateinit var alarm: Alarm
    private lateinit var sayItScripts: List<String>

    override fun startSayIt(scope: CoroutineScope) {
        setupAlarm(scope)

        sttRecognizer.recognizerState
            .onEach(::handleSttRecognizerState)
            .launchIn(scope)
    }

    private fun setupAlarm(scope: CoroutineScope) {
        scope.launch(ioDispatcher) {
            val alarmId: Long? =
                (alarmController.alarmControllerState.first() as? ServiceConnected)?.alarmId

            if (alarmId != null) {
                alarmRepository
                    .getAlarm(alarmId, scope)
                    .await()
                    .onSuccess {
                        alarm = it
                        sayItScripts = it.sayItScripts.scripts

                        (if (sayItScripts.isNotEmpty()) Ready else Completed).update()
                    }
                    .onFailure { Error(ALARM_LOAD_FAILED).update() }
            } else {
                Error(SERVICE_DISCONNECTED_WHILE_RESOLVING_ALARM_ID).update()
            }
        }
    }

    override fun startListening() {
        sttRecognizer.startListening()
    }

    override fun stopSayIt() {
        sttRecognizer.stopRecognizer()
    }

    override fun shutdown() {
        sttRecognizer.stopRecognizer()
        alarmController.stopService()
    }

    private fun handleSttRecognizerState(state: SttRecognizerContract.RecognizerState) {
        when (state) {
            is SttRecognizerContract.RecognizerState.Initial -> {}
            is SttRecognizerContract.RecognizerState.Ready -> handleReadyState()
            is SttRecognizerContract.RecognizerState.Processing -> handleProcessingState(state.partialResults)
            is SttRecognizerContract.RecognizerState.Done -> handleDoneState(state.result)
            is SttRecognizerContract.RecognizerState.Error -> Error(SPEECH_RECOGNIZER_ERROR).update()
        }
    }

    private fun handleReadyState() {
        if (sayItState.value is InProgress) {
            (sayItState.value as? InProgress)?.let(::setNextScriptOrRetry)
        } else {
            // Begins the progress from the first script.
            InProgress(
                ProgressStatus.IN_PROGRESS,
                SayIt(sayItScripts.first(), ""),
                Count(1, sayItScripts.size),
            ).update()
        }
    }

    private fun setNextScriptOrRetry(inProgress: InProgress) {
        if (inProgress.status == ProgressStatus.SUCCESS) {
            InProgress(
                ProgressStatus.IN_PROGRESS,
                SayIt(sayItScripts[inProgress.count.current], ""),
                Count(inProgress.count.current + 1, inProgress.count.total),
            )
        } else {
            inProgress.copy(
                status = ProgressStatus.IN_PROGRESS,
                sayIt = inProgress.sayIt.copy(transcript = ""),
            )
        }.update()
    }

    private fun handleProcessingState(partialResult: String) {
        (sayItState.value as? InProgress)?.let { state ->
            state
                .copy(sayIt = state.sayIt.copy(transcript = partialResult))
                .update()
        }
    }

    private fun handleDoneState(result: String) {
        (sayItState.value as? InProgress)?.let { state ->
            if (isSuccess(state.sayIt.script, result)) {
                if (state.count.current < state.count.total) {
                    state.copy(status = ProgressStatus.SUCCESS)
                } else {
                    Completed
                }
            } else {
                state.copy(status = ProgressStatus.FAILED)
            }.update()
        }
    }

    private fun isSuccess(script: String, sttResult: String): Boolean {
        val maxAllowedErrors = script.length * ERROR_ACCEPTANCE_RATE
        val errorCounts = editDistanceCalculator.calculateEditDistance(script, sttResult)

        return errorCounts <= maxAllowedErrors
    }

    private fun SayItState.update() {
        _sayItState.update { this }
    }

    companion object {
        private const val ERROR_ACCEPTANCE_RATE = 0.2
    }
}
