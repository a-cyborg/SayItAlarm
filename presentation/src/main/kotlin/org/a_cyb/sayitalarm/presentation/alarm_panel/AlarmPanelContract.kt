/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.presentation.alarm_panel

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharedFlow
import org.a_cyb.sayitalarm.entity.Alarm
import org.a_cyb.sayitalarm.presentation.CommandContract

interface AlarmPanelContract {

    interface AlarmPanelViewModel : AlarmPanelCommandContractAll, CommandContract.CommandExecutor {

        val scope: CoroutineScope
        val alarmUI: SharedFlow<AlarmUI>

        fun getAlarm(): Alarm
    }

    data class AlarmUI(
        val hour: Int,
        val minute: Int,
        val weeklyRepeat: String,
        val label: String,
        val alertType: String,
        val ringtone: String,
        val sayItScripts: List<String>,
    )
}
