/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.presentation.contracts

import kotlinx.coroutines.flow.StateFlow
import org.a_cyb.sayitalarm.presentation.contracts.command.AlarmCommandContractAll
import org.a_cyb.sayitalarm.presentation.contracts.command.CommandContract

interface AlarmContract {

    interface AlarmViewModel : AlarmCommandContractAll, CommandContract.CommandExecutor {
        val state: StateFlow<AlarmUiState>
        val currentTime: StateFlow<String>
    }

    sealed interface AlarmUiState {
        data object Initial : AlarmUiState
        data class Ringing(val label: String) : AlarmUiState
        data object Stopped : AlarmUiState
        data object Error : AlarmUiState
    }
}
