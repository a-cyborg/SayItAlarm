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

    interface AddViewModel : AddCommandContract.Save, CommandContract.CommandExecutor {
        val state: StateFlow<AddState>
        val alarmPanelExecutor: (CommandContract.Command<out CommandContract.CommandReceiver>) -> Unit
    }

    interface AddState

    data object Initial : AddState

    data class AddStateWithContent(val alarmUI: AlarmPanelContract.AlarmUI) : AddState
}
