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
import org.a_cyb.sayitalarm.domain.alarm_service.AlarmServiceContract.AlarmServiceController.ControllerState
import org.a_cyb.sayitalarm.presentation.AlarmContract
import org.a_cyb.sayitalarm.presentation.AlarmContract.AlarmUiState
import org.a_cyb.sayitalarm.presentation.AlarmContract.AlarmUiState.Error
import org.a_cyb.sayitalarm.presentation.AlarmContract.AlarmUiState.Initial
import org.a_cyb.sayitalarm.presentation.AlarmContract.AlarmUiState.Ringing
import org.a_cyb.sayitalarm.presentation.command.CommandContract.Command
import org.a_cyb.sayitalarm.presentation.command.CommandContract.CommandReceiver
import org.a_cyb.sayitalarm.presentation.formatter.time.TimeFormatterContract
import org.a_cyb.sayitalarm.presentation.viewmodel.time_flow.TimeFlowContract

class AlarmViewModel(
    timeFlow: TimeFlowContract,
    private val serviceController: AlarmServiceController,
    private val timeFormatter: TimeFormatterContract,
) : AlarmContract.AlarmViewModel, ViewModel() {

    override val currentTime: StateFlow<String> = timeFlow.currentTimeFlow
        .takeWhile { state.value is Initial || state.value is Ringing }
        .map { (hour, minute) -> timeFormatter.format(hour, minute) }
        .stateIn(
            scope = scope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = ""
        )

    override val state: StateFlow<AlarmUiState> = serviceController.controllerState
        .map(::mapToState)
        .stateIn(
            scope = scope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = Initial
        )

    private fun mapToState(controllerState: ControllerState): AlarmUiState =
        when (controllerState) {
            is ControllerState.Ringing -> Ringing(controllerState.label.label)
            is ControllerState.Error -> Error
            is ControllerState.Initial, is ControllerState.RunningSayIt -> Initial
        }

    override fun startSayIt() {
        serviceController.startSayIt()
    }

    override fun snooze() {
        serviceController.startSnooze()
    }

    override fun <T : CommandReceiver> runCommand(command: Command<T>) {
        @Suppress("UNCHECKED_CAST")
        command.execute(this as T)
    }
}
