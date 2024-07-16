/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.presentation.viewmodel.converter

import org.a_cyb.sayitalarm.entity.Hour
import org.a_cyb.sayitalarm.entity.Minute
import org.a_cyb.sayitalarm.presentation.AlarmPanelContract.AlertTypeUI
import org.a_cyb.sayitalarm.presentation.AlarmPanelContract.SelectableAlertType
import org.a_cyb.sayitalarm.presentation.AlarmPanelContract.SelectableRepeat
import org.a_cyb.sayitalarm.presentation.AlarmPanelContract.TimeUI
import org.a_cyb.sayitalarm.presentation.AlarmPanelContract.WeeklyRepeatUI

interface AlarmUIConverterContract {
    fun convertAsTimeUi(hour: Hour, minute: Minute): TimeUI
    fun convertAsWeeklyRepeatUi(selectableRepeats: List<SelectableRepeat>): WeeklyRepeatUI
    fun convertToAlertTypeUi(
        selectableTypes: List<SelectableAlertType>,
        chosenType: String
    ): AlertTypeUI
}
