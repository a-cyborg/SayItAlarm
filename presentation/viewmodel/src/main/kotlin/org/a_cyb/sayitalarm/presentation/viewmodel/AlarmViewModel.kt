/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.presentation.viewmodel

import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.takeWhile
import org.a_cyb.sayitalarm.domain.alarm_service.AlarmServiceContract.AlarmServiceController
import org.a_cyb.sayitalarm.domain.alarm_service.AlarmServiceContract.AlarmServiceController.AlarmServiceState
import org.a_cyb.sayitalarm.domain.alarm_service.AlarmServiceContract.SayItRecognizer
import org.a_cyb.sayitalarm.domain.interactor.InteractorContract
import org.a_cyb.sayitalarm.entity.Alarm
import org.a_cyb.sayitalarm.presentation.AlarmContract
import org.a_cyb.sayitalarm.presentation.AlarmContract.AlarmUiState
import org.a_cyb.sayitalarm.presentation.AlarmContract.AlarmUiState.Completed
import org.a_cyb.sayitalarm.presentation.AlarmContract.AlarmUiState.Error
import org.a_cyb.sayitalarm.presentation.AlarmContract.AlarmUiState.Initial
import org.a_cyb.sayitalarm.presentation.AlarmContract.AlarmUiState.Ringing
import org.a_cyb.sayitalarm.presentation.AlarmContract.AlarmUiState.VoiceInputProcessing
import org.a_cyb.sayitalarm.presentation.command.CommandContract.Command
import org.a_cyb.sayitalarm.presentation.command.CommandContract.CommandReceiver
import org.a_cyb.sayitalarm.presentation.formatter.time.TimeFormatterContract
import org.a_cyb.sayitalarm.presentation.viewmodel.time_flow.TimeFlowContract

class AlarmViewModel(
    alarmId: Long,
    alarmInteractor: InteractorContract.AlarmInteractor,
    timeFlow: TimeFlowContract,
    private val serviceController: AlarmServiceController,
    private val sayItRecognizer: SayItRecognizer,
    private val timeFormatter: TimeFormatterContract,
) : AlarmContract.AlarmViewModel, ViewModel() {

    private lateinit var alarm: Alarm

    init {
        alarmInteractor
            .getAlarm(alarmId, scope)
            .onSuccess { alarm = it }
            .onFailure { serviceController.terminate() } // If the alarm is no longer in the database, terminate the application.
    }

    override val currentTime: StateFlow<String> = timeFlow.currentTimeFlow
        .takeWhile { state.value is Initial || state.value is Ringing }
        .map { (hour, minute) -> timeFormatter.format(hour, minute) }
        .stateIn(
            scope = scope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = ""
        )

    override val state: StateFlow<AlarmUiState> = serviceController.alarmState
        .map(::mapToState)
        .stateIn(
            scope = scope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = Initial
        )

    private fun mapToState(alarmState: AlarmServiceState): AlarmUiState =
        when (alarmState) {
            AlarmServiceState.Initial -> Initial
            AlarmServiceState.Ringing -> Ringing(alarm.label.label)
            AlarmServiceState.RunningSayIt -> VoiceInputProcessing(alarm.sayItScripts.scripts)
            AlarmServiceState.Completed -> Completed
            AlarmServiceState.Error -> Error
        }

    override fun startSayIt() {
        serviceController.startSayIt()
        sayItRecognizer.startSayItRecognizer()
    }

    override fun finishAlarm() {
        serviceController.terminate()
    }

    override fun <T : CommandReceiver> runCommand(command: Command<T>) {
        @Suppress("UNCHECKED_CAST")
        command.execute(this as T)
    }
}
