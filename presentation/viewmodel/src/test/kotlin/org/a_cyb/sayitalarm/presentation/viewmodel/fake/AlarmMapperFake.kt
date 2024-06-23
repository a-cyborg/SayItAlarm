/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.presentation.viewmodel.fake

import org.a_cyb.sayitalarm.entity.Alarm
import org.a_cyb.sayitalarm.entity.AlarmType
import org.a_cyb.sayitalarm.entity.AlertType
import org.a_cyb.sayitalarm.entity.Hour
import org.a_cyb.sayitalarm.entity.Label
import org.a_cyb.sayitalarm.entity.Minute
import org.a_cyb.sayitalarm.entity.Ringtone
import org.a_cyb.sayitalarm.entity.SayItScripts
import org.a_cyb.sayitalarm.entity.WeeklyRepeat
import org.a_cyb.sayitalarm.presentation.alarm_panel.AlarmPanelContract
import org.a_cyb.sayitalarm.presentation.viewmodel.mapper.AlarmMapperContract

class AlarmMapperFake : AlarmMapperContract {
    private val timeFormatter = TimeFormatterFake()
    private val weeklyRepeatFormatter = WeekdayFormatterFake()
    private val alertTypeFormatter = AlertTypeFormatterFake()
    private val ringtoneManager = RingtoneManagerFake()

    override fun mapToAlarm(alarmUI: AlarmPanelContract.AlarmUI): Alarm {
        return Alarm(
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
    }

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
            selectableRepeat = FakeAlarmData.selectableRepeat
        )

    override fun mapToAlertTypeUI(alertType: AlertType): AlarmPanelContract.AlertTypeUI =
        AlarmPanelContract.AlertTypeUI(
            selected = alertType,
            formattedAlertType = alertTypeFormatter.format(alertType),
            selectableAlertType = FakeAlarmData.selectableAlertType
        )

    override fun mapToRingtoneUI(ringtone: Ringtone): AlarmPanelContract.RingtoneUI {
        val uri = ringtone.ringtone.ifEmpty { ringtoneManager.getDefaultRingtone() }

        return AlarmPanelContract.RingtoneUI(
            title = ringtoneManager.getRingtoneTitle(uri),
            uri = uri
        )
    }
}
