/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.presentation.contracts

import org.a_cyb.sayitalarm.presentation.contracts.command.AlarmPanelCommandContractAll

interface AlarmPanelContract : AlarmPanelCommandContractAll {

    data class AlarmUI(
        val timeUI: TimeUI,
        val weeklyRepeatUI: WeeklyRepeatUI,
        val label: String,
        val alertTypeUI: AlertTypeUI,
        val ringtoneUI: RingtoneUI,
        val sayItScripts: List<String>,
    )

    data class TimeUI(
        val hour: Int,
        val minute: Int,
        val formattedTime: String,
    )

    data class WeeklyRepeatUI(
        val formatted: String,
        val selectableRepeats: List<SelectableRepeat>,
    )

    data class SelectableRepeat(
        val name: String,
        val code: Int,
        val selected: Boolean,
    )

    data class AlertTypeUI(
        val selectableAlertType: List<SelectableAlertType>,
    )

    data class SelectableAlertType(
        val name: String,
        val selected: Boolean,
    )

    data class RingtoneUI(
        val title: String,
        val uri: String,
    )
}
