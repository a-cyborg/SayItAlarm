/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.presentation.add

import kotlinx.coroutines.flow.StateFlow
import org.a_cyb.sayitalarm.presentation.CommandContract
import org.a_cyb.sayitalarm.presentation.alarm_panel.AlarmPanelCommandContractAll

interface AddContract {

    interface AddViewModel : AddCommandContract.Save, AlarmPanelCommandContractAll, CommandContract.CommandExecutor {
        val state: StateFlow<AddState>
    }

    interface AddState
    data object Initial : AddState
    data class AddStateWithContent(val alarmUi: AlarmUi) : AddState

    data class AlarmUi(
        val hour: Int,
        val minute: Int,
        val weeklyRepeat: String,
        val label: String,
        val alertType: String,
        val ringtone: String,
        val sayItScripts: List<String>,
    )
}
