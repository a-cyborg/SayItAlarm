/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.presentation.viewmodel.mapper

import org.a_cyb.sayitalarm.entity.Alarm
import org.a_cyb.sayitalarm.entity.AlertType
import org.a_cyb.sayitalarm.entity.Hour
import org.a_cyb.sayitalarm.entity.Minute
import org.a_cyb.sayitalarm.entity.Ringtone
import org.a_cyb.sayitalarm.entity.WeeklyRepeat
import org.a_cyb.sayitalarm.presentation.alarm_panel.AlarmPanelContract
import org.a_cyb.sayitalarm.presentation.alarm_panel.AlarmPanelContract.AlarmUI

interface AlarmMapperContract {

    fun mapToAlarm(alarmUI: AlarmUI): Alarm
    fun mapToAlarmUI(alarm: Alarm): AlarmUI

    fun mapToTimeUI(hour: Hour, minute: Minute): AlarmPanelContract.TimeUI
    fun mapToWeeklyRepeatUI(weeklyRepeat: WeeklyRepeat): AlarmPanelContract.WeeklyRepeatUI
    fun mapToAlertTypeUI(alertType: AlertType): AlarmPanelContract.AlertTypeUI
    fun mapToRingtoneUI(ringtone: Ringtone): AlarmPanelContract.RingtoneUI
}
