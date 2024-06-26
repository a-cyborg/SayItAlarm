/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.presentation

import kotlinx.coroutines.flow.StateFlow
import org.a_cyb.sayitalarm.presentation.command.AlarmPanelCommandContractAll
import org.a_cyb.sayitalarm.presentation.command.CommandContract

interface EditContract {

    interface EditViewModel : AlarmPanelCommandContractAll, CommandContract.CommandExecutor {
        val state: StateFlow<EditState>

        sealed interface EditState {
            data object Initial : EditState
            data class Success(val alarmUI: AlarmPanelContract.AlarmUI) : EditState
            data object Error : EditState
        }
    }
}
