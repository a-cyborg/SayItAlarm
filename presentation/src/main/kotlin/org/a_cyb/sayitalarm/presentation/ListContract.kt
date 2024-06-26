/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.presentation

import kotlinx.coroutines.flow.StateFlow
import org.a_cyb.sayitalarm.presentation.command.CommandContract
import org.a_cyb.sayitalarm.presentation.command.ListCommandContractAll

interface ListContract {

    interface ListViewModel : ListCommandContractAll, CommandContract.CommandExecutor {
        val state: StateFlow<ListState>
    }

    sealed interface ListState {
        data object Initial : ListState
        data object InitialError : ListState

        data class Success(override val alarmData: List<AlarmInfo>) : ListStateWithContent
        data class Error(override val alarmData: List<AlarmInfo>) : ListStateWithContent
    }

    interface ListStateWithContent : ListState {
        val alarmData: List<AlarmInfo>
    }

    data class AlarmInfo(
        val id: Long,
        val time: String,
        val labelAndWeeklyRepeat: String,
        val enabled: Boolean,
    )
}
