/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.design_system.screen

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import org.a_cyb.sayitalarm.entity.Snooze
import org.a_cyb.sayitalarm.entity.Theme
import org.a_cyb.sayitalarm.entity.TimeOut
import org.a_cyb.sayitalarm.presentation.SettingsContract
import org.a_cyb.sayitalarm.presentation.SettingsContract.SettingsState
import org.a_cyb.sayitalarm.presentation.SettingsContract.SettingsState.Error
import org.a_cyb.sayitalarm.presentation.SettingsContract.SettingsState.Success
import org.a_cyb.sayitalarm.presentation.SettingsContract.SettingsViewModel
import org.a_cyb.sayitalarm.presentation.SettingsContract.TimeInput
import org.a_cyb.sayitalarm.presentation.command.CommandContract

internal class SettingsViewModelFake(initState: SettingsState = Error) : SettingsViewModel {

    private val _state: MutableStateFlow<SettingsState> = MutableStateFlow(initState)
    override val state: StateFlow<SettingsState> = _state

    private var _executedCommand = ExecutedCommand.NONE
    val executed: ExecutedCommand
        get() = _executedCommand

    override fun setTimeOut(timeOut: TimeOut) {
        val settingsUI = (_state.value as Success).settingsUI.copy(
            timeOut = TimeInput(
                timeOut.timeOut,
                timeOut.timeOut.formatAsDuration()
            )
        )
        _state.update { Success(settingsUI) }

        _executedCommand = ExecutedCommand.SET_TIMEOUT
    }

    override fun setSnooze(snooze: Snooze) {
        _executedCommand = ExecutedCommand.SET_SNOOZE
    }

    override fun setTheme(themeName: String) {
        _executedCommand = ExecutedCommand.SET_THEME
    }

    override fun sendEmail() {
        _executedCommand = ExecutedCommand.SEND_EMAIL
    }

    override fun openGitHub() {
        _executedCommand = ExecutedCommand.OPEN_GITHUB
    }

    override fun openGooglePlay() {
        _executedCommand = ExecutedCommand.OPEN_GOOGLE_PLAY
    }

    override fun <T : CommandContract.CommandReceiver> runCommand(command: CommandContract.Command<T>) {
        @Suppress("UNCHECKED_CAST")
        command.execute(this as T)
    }

    override val timeOuts: List<TimeInput> = (30..300).map { TimeInput(it, it.formatAsDuration()) }
    override val snoozes: List<TimeInput> = (5..60).map { TimeInput(it, it.formatAsDuration()) }
    override val themes: List<String> = Theme.entries.map { formatToCamelCase(it.name) }
    override val contact: SettingsContract.Contact = SettingsContract.Contact(
        email = "hello@email.com",
        githubUrl = "www.github.com/sayItAlarm",
        googlePlayUrl = "www.google-play.com/sayItAlarm"
    )

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
        SEND_EMAIL,
        OPEN_GOOGLE_PLAY,
        OPEN_GITHUB,
    }
}
