/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.presentation

import kotlinx.coroutines.flow.StateFlow
import org.a_cyb.sayitalarm.presentation.command.CommandContract
import org.a_cyb.sayitalarm.presentation.command.SettingsCommandContractAll

interface SettingsContract {
    interface SettingsViewModel : SettingsCommandContractAll, CommandContract.CommandExecutor {
        val state: StateFlow<SettingsState>

        val timeOuts: List<TimeInput>
        val snoozes: List<TimeInput>
        val themes: List<String>
    }

    sealed interface SettingsState {
        data object Initial : SettingsState
        data class Success(val settingsUI: SettingsUI) : SettingsState
        data object Error : SettingsState
    }

    data class SettingsUI(
        val timeOut: TimeInput,
        val snooze: TimeInput,
        val theme: String,
    )

    data class TimeInput(
        val input: Int,
        val formatted: String,
    )
}
