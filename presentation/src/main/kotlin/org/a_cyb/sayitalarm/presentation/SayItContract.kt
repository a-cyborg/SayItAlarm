/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.presentation

import kotlinx.coroutines.flow.StateFlow
import org.a_cyb.sayitalarm.presentation.command.CommandContract
import org.a_cyb.sayitalarm.presentation.command.SayItCommandContractAll

interface SayItContract {
    interface SayItViewModel : SayItCommandContractAll, CommandContract.CommandExecutor {
        val state: StateFlow<SayItState>
        val isOffline: StateFlow<IsOffline>
    }

    sealed interface SayItState {
        data object Initial : SayItState
        data object Error : SayItState
        data class Processing(val info: SayItInfo) : SayItState
        data object Finished : SayItState
    }

    data class SayItInfo(
        val script: String,
        val sttResult: String,
        val status: SttStatus,
        val count: Count,
    )

    data class Count(val current: Int, val total: Int)
    enum class SttStatus { READY, LISTENING, SUCCESS, FAILED, }
    enum class IsOffline { True, False }
}
