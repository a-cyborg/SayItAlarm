/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.presentation.viewmodel

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.takeWhile
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.a_cyb.sayitalarm.domain.interactor.InteractorContract
import org.a_cyb.sayitalarm.entity.Label
import org.a_cyb.sayitalarm.presentation.contracts.AlarmContract
import org.a_cyb.sayitalarm.presentation.contracts.AlarmContract.AlarmUiState
import org.a_cyb.sayitalarm.presentation.contracts.command.CommandContract
import org.a_cyb.sayitalarm.util.formatter.time.TimeFormatterContract
import org.a_cyb.sayitalarm.util.time_flow.TimeFlowContract

class AlarmViewModel(
    timeFlow: TimeFlowContract,
    timeFormatter: TimeFormatterContract,
    private val interactor: InteractorContract.AlarmInteractor,
) : AlarmContract.AlarmViewModel, ViewModel() {

    private val _state = MutableStateFlow<AlarmUiState>(AlarmUiState.Initial)
    override val state: StateFlow<AlarmUiState> = _state.asStateFlow()

    override val currentTime: StateFlow<String> = timeFlow.currentTimeFlow
        .takeWhile { state.value is AlarmUiState.Initial || state.value is AlarmUiState.Ringing }
        .map { (hour, minute) -> timeFormatter.format(hour, minute) }
        .stateIn(
            scope = scope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = "",
        )

    init {
        scope.launch {
            interactor.label.collect(::handleLabel)
        }

        interactor.startAlarm(scope)
    }

    private fun handleLabel(result: Result<Label>) {
        result
            .onSuccess { label -> _state.update { AlarmUiState.Ringing(label.label) } }
            .onFailure { _state.update { AlarmUiState.Error } }
    }

    override fun startSayIt() {
        interactor.stopAlarm()
    }

    override fun snooze() {
        interactor.snooze()
    }

    override fun <T : CommandContract.CommandReceiver> runCommand(command: CommandContract.Command<T>) {
        @Suppress("UNCHECKED_CAST")
        command.execute(this as T)
    }
}
