/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.presentation.viewmodel

import kotlin.time.Duration.Companion.minutes
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import org.a_cyb.sayitalarm.domain.interactor.InteractorContract
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

class SettingsViewModel(
    private val interactor: InteractorContract.SettingsInteractor,
    private val durationFormatter: DurationFormatterContract,
) : SettingsContract.SettingsViewModel, ViewModel() {

    override val state: StateFlow<SettingsState> = interactor.settings
        .map(::mapToState)
        .stateIn(
            scope = scope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = Initial
        )

    private fun mapToState(result: Result<Settings>): SettingsState {
        return result.getOrNull()
            ?.toStateWithContent()
            ?: Error
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
