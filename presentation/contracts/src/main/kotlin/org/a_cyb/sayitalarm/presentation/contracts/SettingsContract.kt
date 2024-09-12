/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.presentation.contracts

import kotlinx.coroutines.flow.StateFlow
import org.a_cyb.sayitalarm.presentation.contracts.command.CommandContract
import org.a_cyb.sayitalarm.presentation.contracts.command.SettingsCommandContractAll

interface SettingsContract {
    interface SettingsViewModel : SettingsCommandContractAll, CommandContract.CommandExecutor {
        val state: StateFlow<SettingsState>

        val timeOuts: List<TimeInput>
        val snoozes: List<TimeInput>
        val themes: List<String>
        val contact: Contact
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

    data class Contact(
        val email: String,
        val githubUrl: String,
        val googlePlayUrl: String,
    )
}
