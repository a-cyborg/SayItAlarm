/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.presentation.command

import org.a_cyb.sayitalarm.entity.AlarmType
import org.a_cyb.sayitalarm.entity.Hour
import org.a_cyb.sayitalarm.entity.Minute
import org.a_cyb.sayitalarm.entity.SayItScripts
import org.a_cyb.sayitalarm.presentation.AlarmPanelContract.*

interface AlarmPanelCommandContract {
    fun interface SetTime : CommandContract.CommandReceiver {
        fun setTime(hour: Hour, minute: Minute)
    }

    fun interface SetWeeklyRepeat : CommandContract.CommandReceiver {
        fun setWeeklyRepeat(selectableRepeats: List<SelectableRepeat>)
    }

    fun interface SetLabel : CommandContract.CommandReceiver {
        fun setLabel(label: String)
    }

    fun interface SetAlertType : CommandContract.CommandReceiver {
        fun setAlertType(alertTypeUI: AlertTypeUI)
    }

    fun interface SetRingtone : CommandContract.CommandReceiver {
        fun setRingtone(ringtoneUI: RingtoneUI)
    }

    fun interface SetAlarmType : CommandContract.CommandReceiver {
        fun setAlarmType(alarmType: AlarmType)
    }

    fun interface SetScripts : CommandContract.CommandReceiver {
        fun setScripts(scripts: SayItScripts)
    }

    fun interface Save : CommandContract.CommandReceiver {
        fun save()
    }
}

interface AlarmPanelCommandContractAll :
    AlarmPanelCommandContract.SetTime,
    AlarmPanelCommandContract.SetWeeklyRepeat,
    AlarmPanelCommandContract.SetLabel,
    AlarmPanelCommandContract.SetAlertType,
    AlarmPanelCommandContract.SetRingtone,
    // AlarmPanelCommandContract.SetAlarmType,
    AlarmPanelCommandContract.SetScripts,
    AlarmPanelCommandContract.Save

data class SetTimeCommand(val hour: Hour, val minute: Minute) :
    CommandContract.Command<AlarmPanelCommandContract.SetTime> {
    override fun execute(receiver: AlarmPanelCommandContract.SetTime) {
        receiver.setTime(hour, minute)
    }
}

data class SetWeeklyRepeatCommand(val selectableRepeats: List<SelectableRepeat>) :
    CommandContract.Command<AlarmPanelCommandContract.SetWeeklyRepeat> {
    override fun execute(receiver: AlarmPanelCommandContract.SetWeeklyRepeat) {
        receiver.setWeeklyRepeat(selectableRepeats)
    }
}

data class SetLabelCommand(val label: String) :
    CommandContract.Command<AlarmPanelCommandContract.SetLabel> {
    override fun execute(receiver: AlarmPanelCommandContract.SetLabel) {
        receiver.setLabel(label)
    }
}

data class SetAlertTypeCommand(val alertTypeUI: AlertTypeUI) :
    CommandContract.Command<AlarmPanelCommandContract.SetAlertType> {
    override fun execute(receiver: AlarmPanelCommandContract.SetAlertType) {
        receiver.setAlertType(alertTypeUI)
    }
}

data class SetRingtoneCommand(val ringtoneUI: RingtoneUI) :
    CommandContract.Command<AlarmPanelCommandContract.SetRingtone> {
    override fun execute(receiver: AlarmPanelCommandContract.SetRingtone) {
        receiver.setRingtone(ringtoneUI)
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

data object SaveCommand : CommandContract.Command<AlarmPanelCommandContract.Save> {
    override fun execute(receiver: AlarmPanelCommandContract.Save) {
        receiver.save()
    }
}
