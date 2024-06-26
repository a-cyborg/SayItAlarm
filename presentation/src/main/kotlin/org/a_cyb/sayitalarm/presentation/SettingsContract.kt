/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.presentation

import kotlinx.coroutines.flow.StateFlow
import org.a_cyb.sayitalarm.presentation.command.CommandContract
import org.a_cyb.sayitalarm.presentation.command.SettingsCommandContract.SetSnooze
import org.a_cyb.sayitalarm.presentation.command.SettingsCommandContract.SetTheme
import org.a_cyb.sayitalarm.presentation.command.SettingsCommandContract.SetTimeOut

interface SettingsContract {
    interface SettingsViewModel : SetTimeOut, SetSnooze, SetTheme, CommandContract.CommandExecutor {
        val state: StateFlow<SettingsState>

        val timeOuts: List<TimeInput>
        val snoozes: List<TimeInput>
        val themes: List<String>
    }

    interface SettingsState
    data object Initial : SettingsState
    data object InitialError : SettingsState

    data class SettingsStateWithContent(
        val timeOut: TimeInput,
        val snooze: TimeInput,
        val theme: String,
    ) : SettingsState

    data class TimeInput(
        val input: Int,
        val formatted: String,
    )
}
