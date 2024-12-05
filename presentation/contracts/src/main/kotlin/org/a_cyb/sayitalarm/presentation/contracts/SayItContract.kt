/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.presentation.contracts

import kotlinx.coroutines.flow.StateFlow
import org.a_cyb.sayitalarm.presentation.contracts.command.CommandContract
import org.a_cyb.sayitalarm.presentation.contracts.command.SayItCommandContractAll

interface SayItContract {
    interface SayItViewModel : SayItCommandContractAll, CommandContract.CommandExecutor {
        val state: StateFlow<SayItUiState>
    }

    sealed interface SayItUiState {
        data object Initial : SayItUiState
        data class Listening(val sayItUi: SayItUIInfo) : SayItUiState
        data class Success(val sayItUi: SayItUIInfo) : SayItUiState
        data class Failed(val sayItUi: SayItUIInfo) : SayItUiState
        data class Error(val message: String) : SayItUiState
        data object Finished : SayItUiState
    }

    data class SayItUIInfo(
        val script: String,
        val transcript: String,
        val currentCount: Int,
        val totalCount: Int,
    )
}
