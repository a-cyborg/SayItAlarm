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
import org.a_cyb.sayitalarm.presentation.formatter.time.TimeFormatterContract
import org.a_cyb.sayitalarm.presentation.formatter.weekday.WeekdayFormatterContract

class AlarmUiConverter(
    private val timeFormatter: TimeFormatterContract,
    private val weeklyRepeatFormatter: WeekdayFormatterContract,
) : AlarmUIConverterContract {

    override fun convertAsTimeUi(hour: Hour, minute: Minute): TimeUI =
        TimeUI(
            hour.hour,
            minute.minute,
            timeFormatter.format(hour, minute)
        )

    override fun convertAsWeeklyRepeatUi(selectableRepeats: List<SelectableRepeat>): WeeklyRepeatUI {
        val codes = selectableRepeats
            .filter { it.selected }
            .map { it.code }
            .toSet()

        return WeeklyRepeatUI(
            weeklyRepeatFormatter.formatAbbr(codes),
            selectableRepeats
        )
    }

    override fun convertToAlertTypeUi(
        selectableTypes: List<SelectableAlertType>,
        chosenType: String,
    ): AlertTypeUI =
        AlertTypeUI(
            selectableTypes.map {
                SelectableAlertType(
                    it.name,
                    it.name == chosenType
                )
            }
        )
}
