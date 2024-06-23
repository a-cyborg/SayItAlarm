/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.presentation.alarm_panel

import org.a_cyb.sayitalarm.entity.AlertType

interface AlarmPanelContract : AlarmPanelCommandContractAll {

    data class AlarmUI(
        val time: TimeUI,
        val weeklyRepeat: WeeklyRepeatUI,
        val label: String,
        val alertType: AlertTypeUI,
        val ringtone: RingtoneUI,
        val sayItScripts: List<String>,
    )

    data class TimeUI(
        val hour: Int,
        val minute: Int,
        val formattedTime: String
    )

    data class WeeklyRepeatUI(
        val selected: Set<Int>,
        val formattedSelectedRepeat: String,
        val selectableRepeat: Map<String, Int>
    )

    data class AlertTypeUI(
        val selected: AlertType,
        val formattedAlertType: String,
        val selectableAlertType: Map<String, AlertType>
    )

    data class RingtoneUI(
        val title: String,
        val uri: String,
    )
}
