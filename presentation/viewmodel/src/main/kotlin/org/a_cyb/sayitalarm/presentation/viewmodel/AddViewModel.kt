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
import org.a_cyb.sayitalarm.presentation.CommandContract.*
import org.a_cyb.sayitalarm.presentation.add.AddContract
import org.a_cyb.sayitalarm.presentation.add.AddContract.AddState
import org.a_cyb.sayitalarm.presentation.add.AddContract.AddStateWithContent
import org.a_cyb.sayitalarm.presentation.add.AddContract.Initial
import org.a_cyb.sayitalarm.presentation.alarm_panel.AlarmPanelContract
import org.a_cyb.sayitalarm.presentation.interactor.InteractorContract

internal class AddViewModel(
    private val interactor: InteractorContract.AddInteractor,
    private val alarmPanelViewModel: AlarmPanelViewModel,
) : AddContract.AddViewModel, ViewModel() {

    private val _state: MutableStateFlow<AddState> = MutableStateFlow(Initial)
    override val state: StateFlow<AddState> = _state.asStateFlow()

    override val alarmPanelExecutor: (Command<out CommandReceiver>) -> Unit = {
        alarmPanelViewModel.runCommand(it)
    }

    init {
        alarmPanelViewModel.alarmUI
            .onEach(::updateState)
            .launchIn(scope)
    }

    private fun updateState(alarmUI: AlarmPanelContract.AlarmUI) {
        _state.update {
            AddStateWithContent(alarmUI)
        }
    }

    override fun save() {
        scope.launch {
            interactor.save(alarmPanelViewModel.getAlarm(), scope)
        }
    }

    override fun <T : CommandReceiver> runCommand(command: Command<T>) {
        @Suppress("UNCHECKED_CAST")
        command.execute(this as T)
    }
}
