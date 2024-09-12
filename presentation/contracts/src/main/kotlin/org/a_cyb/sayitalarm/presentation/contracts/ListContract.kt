/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.presentation.contracts

import kotlinx.coroutines.flow.StateFlow
import org.a_cyb.sayitalarm.presentation.contracts.command.CommandContract
import org.a_cyb.sayitalarm.presentation.contracts.command.DownloadRecognizerModel
import org.a_cyb.sayitalarm.presentation.contracts.command.ListCommandContractAll

interface ListContract {

    interface ListViewModel : ListCommandContractAll, DownloadRecognizerModel, CommandContract.CommandExecutor {
        val state: StateFlow<ListState>
        val isOfflineAvailable: StateFlow<Boolean>
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
