/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.presentation.viewmodel.mapper

import android.icu.util.Calendar
import org.a_cyb.sayitalarm.entity.Alarm
import org.a_cyb.sayitalarm.entity.AlarmType
import org.a_cyb.sayitalarm.entity.AlertType
import org.a_cyb.sayitalarm.entity.Hour
import org.a_cyb.sayitalarm.entity.Label
import org.a_cyb.sayitalarm.entity.Minute
import org.a_cyb.sayitalarm.entity.Ringtone
import org.a_cyb.sayitalarm.entity.SayItScripts
import org.a_cyb.sayitalarm.entity.WeeklyRepeat
import org.a_cyb.sayitalarm.formatter.enum.EnumFormatterContract
import org.a_cyb.sayitalarm.formatter.time.TimeFormatterContract
import org.a_cyb.sayitalarm.formatter.weekday.WeekdayFormatterContract
import org.a_cyb.sayitalarm.presentation.alarm_panel.AlarmPanelContract
import org.a_cyb.sayitalarm.ringtone_manager.RingtoneManagerContract

class AlarmMapper(
    private val timeFormatter: TimeFormatterContract,
    private val weeklyRepeatFormatter: WeekdayFormatterContract,
    private val alertTypeFormatter: EnumFormatterContract.AlertTypeFormatter,
    private val ringtoneManager: RingtoneManagerContract,
) : AlarmMapperContract {

    override fun mapToAlarm(alarmUI: AlarmPanelContract.AlarmUI): Alarm =
        Alarm(
            hour = Hour(alarmUI.time.hour),
            minute = Minute(alarmUI.time.minute),
            weeklyRepeat = WeeklyRepeat(alarmUI.weeklyRepeat.selected),
            label = Label(alarmUI.label),
            enabled = true,
            alertType = alarmUI.alertType.selected,
            ringtone = Ringtone(alarmUI.ringtone.uri),
            alarmType = AlarmType.SAY_IT,
            sayItScripts = SayItScripts(alarmUI.sayItScripts)
        )

    override fun mapToAlarmUI(alarm: Alarm): AlarmPanelContract.AlarmUI =
        AlarmPanelContract.AlarmUI(
            mapToTimeUI(alarm.hour, alarm.minute),
            mapToWeeklyRepeatUI(alarm.weeklyRepeat),
            alarm.label.label,
            mapToAlertTypeUI(alarm.alertType),
            mapToRingtoneUI(alarm.ringtone),
            alarm.sayItScripts.scripts
        )

    override fun mapToTimeUI(hour: Hour, minute: Minute): AlarmPanelContract.TimeUI =
        AlarmPanelContract.TimeUI(
            hour.hour,
            minute.minute,
            timeFormatter.format(hour, minute)
        )

    override fun mapToWeeklyRepeatUI(weeklyRepeat: WeeklyRepeat): AlarmPanelContract.WeeklyRepeatUI =
        AlarmPanelContract.WeeklyRepeatUI(
            selected = weeklyRepeat.weekdays,
            formattedSelectedRepeat = weeklyRepeatFormatter.formatAbbr(weeklyRepeat.weekdays),
            selectableRepeat = selectableRepeat
        )

    private val selectableRepeat: Map<String, Int>
        get() = (Calendar.SUNDAY..Calendar.SATURDAY)
            .associateBy { weeklyRepeatFormatter.formatFull(it) }

    override fun mapToAlertTypeUI(alertType: AlertType): AlarmPanelContract.AlertTypeUI =
        AlarmPanelContract.AlertTypeUI(
            selected = alertType,
            formattedAlertType = alertTypeFormatter.format(alertType),
            selectableAlertType = selectableAlertType
        )

    private val selectableAlertType: Map<String, AlertType>
        get() = AlertType.entries
            .associateBy { alertTypeFormatter.format(it) }

    override fun mapToRingtoneUI(ringtone: Ringtone): AlarmPanelContract.RingtoneUI {
        val uri = ringtone.ringtone.ifEmpty { ringtoneManager.getDefaultRingtone() }

        return AlarmPanelContract.RingtoneUI(
            title = ringtoneManager.getRingtoneTitle(uri),
            uri = uri
        )
    }
}
