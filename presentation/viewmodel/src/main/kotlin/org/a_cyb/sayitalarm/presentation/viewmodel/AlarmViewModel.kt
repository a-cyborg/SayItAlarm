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
import org.a_cyb.sayitalarm.presentation.AlarmContract
import org.a_cyb.sayitalarm.presentation.AlarmContract.AlarmState
import org.a_cyb.sayitalarm.presentation.AlarmContract.AlarmState.Initial
import org.a_cyb.sayitalarm.presentation.command.CommandContract.Command
import org.a_cyb.sayitalarm.presentation.command.CommandContract.CommandReceiver
import org.a_cyb.sayitalarm.presentation.formatter.time.TimeFormatterContract
import org.a_cyb.sayitalarm.presentation.viewmodel.time_flow.TimeFlowContract

class AlarmViewModel(
    private val timeFormatter: TimeFormatterContract,
    timeFlow: TimeFlowContract,
) : AlarmContract.AlarmViewModel, ViewModel() {

    private val _state: MutableStateFlow<AlarmState> = MutableStateFlow(Initial)
    override val state: StateFlow<AlarmState> = _state.asStateFlow()

    override val currentTime: StateFlow<String> = timeFlow.currentTimeFlow
        .takeWhile { _state.value == Initial }
        .map { (hour, minute) -> timeFormatter.format(hour, minute) }
        .stateIn(
            scope = scope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = ""
        )

    override fun startSayIt() {
        _state.value = AlarmState.VoiceInputProcessing
    }

    override fun <T : CommandReceiver> runCommand(command: Command<T>) {
        @Suppress("UNCHECKED_CAST")
        command.execute(this as T)
    }
}
