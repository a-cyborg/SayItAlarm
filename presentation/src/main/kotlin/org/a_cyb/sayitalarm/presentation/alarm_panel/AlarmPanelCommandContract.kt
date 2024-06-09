/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.presentation.alarm_panel

import org.a_cyb.sayitalarm.entity.AlarmType
import org.a_cyb.sayitalarm.entity.AlertType
import org.a_cyb.sayitalarm.entity.Hour
import org.a_cyb.sayitalarm.entity.Label
import org.a_cyb.sayitalarm.entity.Minute
import org.a_cyb.sayitalarm.entity.Ringtone
import org.a_cyb.sayitalarm.entity.SayItScripts
import org.a_cyb.sayitalarm.entity.WeeklyRepeat
import org.a_cyb.sayitalarm.presentation.CommandContract

interface AlarmPanelCommandContract {
    fun interface SetTime : CommandContract.CommandReceiver {
        fun setTime(hour: Hour, minute: Minute)
    }

    fun interface SetWeeklyRepeat : CommandContract.CommandReceiver {
        fun setWeeklyRepeat(weeklyRepeat: WeeklyRepeat)
    }

    fun interface SetLabel : CommandContract.CommandReceiver {
        fun setLabel(label: Label)
    }

    fun interface SetAlertType : CommandContract.CommandReceiver {
        fun setAlertType(alertType: AlertType)
    }

    fun interface SetRingtone : CommandContract.CommandReceiver {
        fun setRingtone(ringtone: Ringtone)
    }

    fun interface SetAlarmType : CommandContract.CommandReceiver {
        fun setAlarmType(alarmType: AlarmType)
    }

    fun interface SetScripts : CommandContract.CommandReceiver {
        fun setScripts(scripts: SayItScripts)
    }
}

interface AlarmPanelCommandContractAll :
    AlarmPanelCommandContract.SetTime,
    AlarmPanelCommandContract.SetWeeklyRepeat,
    AlarmPanelCommandContract.SetLabel,
    AlarmPanelCommandContract.SetAlertType,
    AlarmPanelCommandContract.SetRingtone,
    AlarmPanelCommandContract.SetAlarmType,
    AlarmPanelCommandContract.SetScripts

data class SetTimeCommand(val hour: Hour, val minute: Minute) :
    CommandContract.Command<AlarmPanelCommandContract.SetTime> {
    override fun execute(receiver: AlarmPanelCommandContract.SetTime) {
        receiver.setTime(hour, minute)
    }
}

data class SetWeeklyRepeatCommand(val weeklyRepeat: WeeklyRepeat) :
    CommandContract.Command<AlarmPanelCommandContract.SetWeeklyRepeat> {
    override fun execute(receiver: AlarmPanelCommandContract.SetWeeklyRepeat) {
        receiver.setWeeklyRepeat(weeklyRepeat)
    }
}

data class SetLabelCommand(val label: Label) :
    CommandContract.Command<AlarmPanelCommandContract.SetLabel> {
    override fun execute(receiver: AlarmPanelCommandContract.SetLabel) {
        receiver.setLabel(label)
    }
}

data class SetAlertTypeCommand(val alertType: AlertType) :
    CommandContract.Command<AlarmPanelCommandContract.SetAlertType> {
    override fun execute(receiver: AlarmPanelCommandContract.SetAlertType) {
        receiver.setAlertType(alertType)
    }
}

data class SetRingtoneCommand(val ringtone: Ringtone) :
    CommandContract.Command<AlarmPanelCommandContract.SetRingtone> {
    override fun execute(receiver: AlarmPanelCommandContract.SetRingtone) {
        receiver.setRingtone(ringtone)
    }
}

data class SetAlarmTypeCommand(val alarmType: AlarmType) :
    CommandContract.Command<AlarmPanelCommandContract.SetAlarmType> {
    override fun execute(receiver: AlarmPanelCommandContract.SetAlarmType) {
        receiver.setAlarmType(alarmType)
    }
}

data class SetScriptsCommand(val sayItScripts: SayItScripts) :
    CommandContract.Command<AlarmPanelCommandContract.SetScripts> {
    override fun execute(receiver: AlarmPanelCommandContract.SetScripts) {
        receiver.setScripts(sayItScripts)
    }
}
