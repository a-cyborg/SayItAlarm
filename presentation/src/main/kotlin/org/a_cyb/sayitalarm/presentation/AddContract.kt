/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.presentation

import kotlinx.coroutines.flow.StateFlow
import org.a_cyb.sayitalarm.presentation.command.CommandContract
import org.a_cyb.sayitalarm.presentation.command.AddCommandContract

interface AddContract {

    interface AddViewModel : AlarmPanelContract, AddCommandContract.Save, CommandContract.CommandExecutor {
        val state: StateFlow<AddState>
    }

    sealed interface AddState {
        val alarmUI: AlarmPanelContract.AlarmUI

        data class Initial(override val alarmUI: AlarmPanelContract.AlarmUI) : AddState
        data class Success(override val alarmUI: AlarmPanelContract.AlarmUI) : AddState
        data class Error(override val alarmUI: AlarmPanelContract.AlarmUI) : AddState
    }
}
