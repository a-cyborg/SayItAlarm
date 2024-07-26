/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.design_system.screen

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.a_cyb.sayitalarm.entity.Snooze
import org.a_cyb.sayitalarm.entity.Theme
import org.a_cyb.sayitalarm.entity.TimeOut
import org.a_cyb.sayitalarm.presentation.SettingsContract.SettingsState
import org.a_cyb.sayitalarm.presentation.SettingsContract.SettingsState.Error
import org.a_cyb.sayitalarm.presentation.SettingsContract.SettingsState.Success
import org.a_cyb.sayitalarm.presentation.SettingsContract.SettingsViewModel
import org.a_cyb.sayitalarm.presentation.SettingsContract.TimeInput
import org.a_cyb.sayitalarm.presentation.command.CommandContract

@Suppress("EmptyFunctionBlock")
internal class SettingsViewModelFake(
    private val viewModelScope: CoroutineScope,
    initState: SettingsState = Error,
) : SettingsViewModel {

    private val _state: MutableStateFlow<SettingsState> = MutableStateFlow(initState)
    override val state: StateFlow<SettingsState> = _state

    private var _executedCommand = ExecutedCommand.NONE
    val executed: ExecutedCommand
        get() = _executedCommand

    override fun setTimeOut(timeOut: TimeOut) {
        _executedCommand = ExecutedCommand.SET_TIMEOUT

        val settingsUI = (_state.value as Success).settingsUI.copy(
            timeOut = TimeInput(
                timeOut.timeOut,
                timeOut.timeOut.formatAsDuration()
            )
        )

        viewModelScope.launch {
            _state.update {
                Success(settingsUI)
            }
        }
    }

    override fun setSnooze(snooze: Snooze) {
        _executedCommand = ExecutedCommand.SET_SNOOZE
    }

    override fun setTheme(themeName: String) {
        _executedCommand = ExecutedCommand.SET_THEME
    }

    override fun <T : CommandContract.CommandReceiver> runCommand(command: CommandContract.Command<T>) {
        @Suppress("UNCHECKED_CAST")
        command.execute(this as T)
    }

    override val timeOuts: List<TimeInput> = (30..300).map {
        TimeInput(it, it.formatAsDuration())
    }

    override val snoozes: List<TimeInput> = (5..60).map {
        TimeInput(it, it.formatAsDuration())
    }

    override val themes: List<String> = Theme.entries.map { formatToCamelCase(it.name) }

    private fun formatToCamelCase(text: String): String = text.lowercase().replaceFirstChar(Char::titlecase)
    private fun Int.formatAsDuration(): String {
        val hour = this / 60
        val min = this % 60

        return when {
            hour >= 1 && min >= 1 -> "$hour hr $min min"
            hour >= 1 -> "$hour hr"
            min >= 1 -> "$min min"
            else -> "0 min"
        }
    }

    enum class ExecutedCommand {
        NONE,
        SET_TIMEOUT,
        SET_SNOOZE,
        SET_THEME,
    }
}
