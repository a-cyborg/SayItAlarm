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
import org.a_cyb.sayitalarm.entity.Settings
import org.a_cyb.sayitalarm.entity.Snooze
import org.a_cyb.sayitalarm.entity.Theme
import org.a_cyb.sayitalarm.entity.TimeOut
import org.a_cyb.sayitalarm.presentation.CommandContract
import org.a_cyb.sayitalarm.presentation.SettingsContract
import org.a_cyb.sayitalarm.presentation.SettingsContract.Error
import org.a_cyb.sayitalarm.presentation.SettingsContract.Initial
import org.a_cyb.sayitalarm.presentation.SettingsContract.InvalidTimeInput
import org.a_cyb.sayitalarm.presentation.SettingsContract.SettingsError
import org.a_cyb.sayitalarm.presentation.SettingsContract.SettingsState
import org.a_cyb.sayitalarm.presentation.SettingsContract.SettingsStateWithContent
import org.a_cyb.sayitalarm.presentation.SettingsContract.TimeInput
import org.a_cyb.sayitalarm.presentation.SettingsContract.ValidTimeInput
import org.a_cyb.sayitalarm.presentation.tasker.SettingsTaskerContract

internal class SettingsViewModel(
    private val tasker: SettingsTaskerContract,
) : SettingsContract.SettingsViewModel, ViewModel() {

    private var _state: MutableStateFlow<SettingsState> = MutableStateFlow(Initial)
    override val state: StateFlow<SettingsState> = _state.asStateFlow()

    override val timeOuts: List<String> by lazy { getFormattedTimeOuts() }
    override val snoozes: List<String> by lazy { getFormattedSnoozes() }
    private fun getFormattedTimeOuts(): List<String> = listOf()

    private fun getFormattedSnoozes(): List<String> = listOf()

    init {
        tasker.settings
            .onEach(::updateState)
            .launchIn(scope)
    }

    private fun updateState(settingsResult: Result<Settings>) {
        settingsResult
            .onSuccess { settings -> _state.update { settings.toStateWithContent() } }
            .onFailure { throwable -> _state.update { throwable.toErrorState(SettingsError.INITIAL_SETTINGS_UNRESOLVED) } }
    }

    private fun Settings.toStateWithContent(): SettingsStateWithContent =
        SettingsStateWithContent(
            timeOut = timeOut.toValidatedTimeInput(),
            snooze = snooze.toValidatedTimeInput(),
            theme = theme,
        )

    private fun Throwable.toErrorState(settingsError: SettingsError): SettingsState =
        Error(error = settingsError)

    @Suppress("MagicNumber")
    private fun TimeOut.toValidatedTimeInput(): TimeInput =
        if (timeOut !in (30..300)) {
            InvalidTimeInput(timeOut)
        } else {
            ValidTimeInput(timeOut)
        }

    @Suppress("MagicNumber")
    private fun Snooze.toValidatedTimeInput(): TimeInput =
        if (snooze !in (5..60)) {
            InvalidTimeInput(snooze)
        } else {
            ValidTimeInput(snooze)
        }

    override fun setTimeOut(timeOut: TimeOut) {
        updateContentOrError { tasker.setTimeOut(timeOut, scope) }
    }

    override fun setSnooze(snooze: Snooze) {
        updateContentOrError { tasker.setSnooze(snooze, scope) }
    }

    override fun setTheme(theme: Theme) {
        updateContentOrError { tasker.setTheme(theme, scope) }
    }

    private fun updateContentOrError(updateContent: () -> Unit) {
        if (state.value !is SettingsStateWithContent) {
            _state.update {
                IllegalStateException().toErrorState(SettingsError.SETTINGS_STATE_WITH_CONTENT_UNRESOLVED)
            }
        } else {
            updateContent()
        }
    }

    override fun <T : CommandContract.CommandReceiver> runCommand(command: CommandContract.Command<T>) {
        @Suppress("UNCHECKED_CAST")
        command.execute(this as T)
    }
}
