/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.presentation.viewmodel

import kotlin.time.Duration.Companion.minutes
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
import org.a_cyb.sayitalarm.formatter.duration.DurationFormatterContract
import org.a_cyb.sayitalarm.presentation.SettingsContract
import org.a_cyb.sayitalarm.presentation.SettingsContract.SettingsState
import org.a_cyb.sayitalarm.presentation.SettingsContract.SettingsState.Error
import org.a_cyb.sayitalarm.presentation.SettingsContract.SettingsState.Initial
import org.a_cyb.sayitalarm.presentation.SettingsContract.SettingsState.Success
import org.a_cyb.sayitalarm.presentation.SettingsContract.SettingsUI
import org.a_cyb.sayitalarm.presentation.SettingsContract.TimeInput
import org.a_cyb.sayitalarm.presentation.command.CommandContract
import org.a_cyb.sayitalarm.presentation.interactor.InteractorContract

internal class SettingsViewModel(
    private val interactor: InteractorContract.SettingsInteractor,
    private val durationFormatter: DurationFormatterContract,
) : SettingsContract.SettingsViewModel, ViewModel() {

    private var _state: MutableStateFlow<SettingsState> = MutableStateFlow(Initial)
    override val state: StateFlow<SettingsState> = _state.asStateFlow()

    init {
        interactor.settings
            .onEach(::updateState)
            .launchIn(scope)
    }

    private fun updateState(settingsResult: Result<Settings>) {
        settingsResult
            .onSuccess { settings -> _state.update { settings.toStateWithContent() } }
            .onFailure { _: Throwable -> _state.update { Error } }
    }

    private fun Settings.toStateWithContent(): Success =
        Success(
            SettingsUI(
                timeOut = toTimeInput(timeOut.timeOut),
                snooze = toTimeInput(snooze.snooze),
                theme = theme.name.toCamelCase(),
            )
        )

    private fun toTimeInput(time: Int): TimeInput {
        return TimeInput(
            input = time,
            formatted = durationFormatter.format(time.minutes).short
        )
    }

    override fun setTimeOut(timeOut: TimeOut) {
        interactor.setTimeOut(timeOut, scope)
    }

    override fun setSnooze(snooze: Snooze) {
        interactor.setSnooze(snooze, scope)
    }

    override fun setTheme(themeName: String) {
        val theme = Theme.valueOf(themeName.uppercase())

        interactor.setTheme(theme, scope)
    }

    private fun String.toCamelCase(): String = lowercase().replaceFirstChar(Char::titlecase)

    override fun <T : CommandContract.CommandReceiver> runCommand(command: CommandContract.Command<T>) {
        @Suppress("UNCHECKED_CAST")
        command.execute(this as T)
    }

    override val timeOuts: List<TimeInput>
        get() = TIME_OUT_RANGE.step(10).map(::toTimeInput)

    override val snoozes: List<TimeInput>
        get() = SNOOZE_RANGE.step(10).map(::toTimeInput)

    override val themes: List<String>
        get() = Theme.entries.map { it.name.toCamelCase() }

    companion object {
        val TIME_OUT_RANGE = 30..300
        val SNOOZE_RANGE = 5..60
    }
}
