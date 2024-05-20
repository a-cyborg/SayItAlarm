/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.presentation

import kotlinx.coroutines.flow.StateFlow
import org.a_cyb.sayitalarm.entity.Theme
import org.a_cyb.sayitalarm.presentation.SettingsCommandContract.SetSnooze
import org.a_cyb.sayitalarm.presentation.SettingsCommandContract.SetTheme
import org.a_cyb.sayitalarm.presentation.SettingsCommandContract.SetTimeOut

interface SettingsContract {
    interface SettingsViewModel : SetTimeOut, SetSnooze, SetTheme, CommandContract.CommandExecutor {
        val state: StateFlow<SettingsState>
        val timeOuts: List<String>
        val snoozes: List<String>
    }

    enum class SettingsError {
        INITIAL_SETTINGS_UNRESOLVED,
        SETTINGS_STATE_WITH_CONTENT_UNRESOLVED,
    }

    interface SettingsState
    object Initial : SettingsState
    data class Error(val error: SettingsError) : SettingsState

    interface TimeInput {
        val input: Int
    }

    data class ValidTimeInput(override val input: Int) : TimeInput
    data class InvalidTimeInput(override val input: Int) : TimeInput

    data class SettingsStateWithContent(
        val timeOut: TimeInput,
        val snooze: TimeInput,
        val theme: Theme,
    ) : SettingsState
}
