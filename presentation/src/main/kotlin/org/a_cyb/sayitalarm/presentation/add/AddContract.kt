/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.presentation.add

import kotlinx.coroutines.flow.StateFlow
import org.a_cyb.sayitalarm.presentation.CommandContract
import org.a_cyb.sayitalarm.presentation.alarm_panel.AlarmPanelContract

interface AddContract {

    interface AddViewModel : AlarmPanelContract, AddCommandContract.Save, CommandContract.CommandExecutor {
        val state: StateFlow<AddState>
    }

    sealed interface AddState {
        data class Success(val alarmUI: AlarmPanelContract.AlarmUI) : AddState
        data object Error : AddState
        data object Initial : AddState
    }
}
