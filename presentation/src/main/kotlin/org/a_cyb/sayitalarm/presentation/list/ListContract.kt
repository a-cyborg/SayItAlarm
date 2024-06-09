/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.presentation.list

import kotlinx.coroutines.flow.StateFlow
import org.a_cyb.sayitalarm.presentation.CommandContract
import org.a_cyb.sayitalarm.presentation.list.ListCommandContract.DeleteAlarm
import org.a_cyb.sayitalarm.presentation.list.ListCommandContract.SetEnabled

interface ListContract {
    interface ListViewModel : SetEnabled, DeleteAlarm, CommandContract.CommandExecutor {
        val state: StateFlow<ListState>
    }

    interface ListState
    data object Initial : ListState
    data object InitialError : ListState

    interface ListStateWithContent : ListState {
        val alarmData: List<AlarmInfo>
    }

    data class Error(
        override val alarmData: List<AlarmInfo>
    ) : ListStateWithContent

    data class Success(
        override val alarmData: List<AlarmInfo>
    ) : ListStateWithContent

    data class AlarmInfo(
        val id: Long,
        val time: String,
        val labelAndWeeklyRepeat: String,
        val enabled: Boolean,
    )
}
