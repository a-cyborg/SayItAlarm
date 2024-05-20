/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.screen

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.a_cyb.sayitalarm.entity.Snooze
import org.a_cyb.sayitalarm.entity.Theme
import org.a_cyb.sayitalarm.entity.TimeOut
import org.a_cyb.sayitalarm.presentation.CommandContract
import org.a_cyb.sayitalarm.presentation.SettingsContract

@Suppress("EmptyFunctionBlock")
internal class SettingsViewModelFake(
    private val viewModelScope: CoroutineScope,
    initState: SettingsContract.SettingsState,
) : SettingsContract.SettingsViewModel {

    private val _state: MutableStateFlow<SettingsContract.SettingsState> = MutableStateFlow(initState)
    override val state: StateFlow<SettingsContract.SettingsState> = _state

    override val timeOuts: List<String> = listOf()
    override val snoozes: List<String> = listOf()

    private var _executedCommand = ExecutedCommand.NONE
    val executed: ExecutedCommand
        get() = _executedCommand

    override fun setTimeOut(timeOut: TimeOut) {
        _executedCommand = ExecutedCommand.SET_TIMEOUT

        viewModelScope.launch {
            _state.update {
                (_state.value as SettingsContract.SettingsStateWithContent)
                    .copy(timeOut = SettingsContract.ValidTimeInput(timeOut.timeOut))
            }
        }
    }

    override fun setSnooze(snooze: Snooze) {
        _executedCommand = ExecutedCommand.SET_SNOOZE
    }

    override fun setTheme(theme: Theme) {
        _executedCommand = ExecutedCommand.SET_THEME
    }

    override fun <T : CommandContract.CommandReceiver> runCommand(command: CommandContract.Command<T>) {
        @Suppress("UNCHECKED_CAST")
        command.execute(this as T)
    }

    enum class ExecutedCommand {
        NONE,
        SET_TIMEOUT,
        SET_SNOOZE,
        SET_THEME,
    }
}
