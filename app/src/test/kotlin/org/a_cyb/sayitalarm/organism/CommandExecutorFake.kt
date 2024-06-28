/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.organism

import org.a_cyb.sayitalarm.entity.Hour
import org.a_cyb.sayitalarm.entity.Minute
import org.a_cyb.sayitalarm.entity.SayItScripts
import org.a_cyb.sayitalarm.presentation.AlarmPanelContract
import org.a_cyb.sayitalarm.presentation.command.AlarmPanelCommandContractAll
import org.a_cyb.sayitalarm.presentation.command.CommandContract

class CommandExecutorFake :
    AlarmPanelCommandContractAll,
    CommandContract.CommandExecutor
{
    private var _invokedType: InvokedType = InvokedType.NONE
    val invokedType: InvokedType
        get() = _invokedType

    override fun setTime(hour: Hour, minute: Minute) {
        _invokedType = InvokedType.SET_TIME
    }

    override fun setWeeklyRepeat(selectableRepeats: List<AlarmPanelContract.SelectableRepeat>) {
        _invokedType = InvokedType.SET_WEEKLY_REPEAT
    }

    override fun setLabel(label: String) {
        _invokedType = InvokedType.SET_LABEL
    }

    override fun setAlertType(alertTypeName: String) {
        _invokedType = InvokedType.SET_ALERT_TYPE
    }

    override fun setRingtone(ringtoneUI: AlarmPanelContract.RingtoneUI) {
        _invokedType = InvokedType.SET_RINGTONE
    }

    override fun setScripts(scripts: SayItScripts) {
        _invokedType = InvokedType.SET_SCRIPTS
    }

    override fun save() {
        _invokedType = InvokedType.SAVE
    }

    override fun <T : CommandContract.CommandReceiver> runCommand(command: CommandContract.Command<T>) {
        @Suppress("UNCHECKED_CAST")
        command.execute(this as T)
    }

    enum class InvokedType {
        SET_TIME,
        SET_WEEKLY_REPEAT,
        SET_LABEL,
        SET_ALERT_TYPE,
        SET_RINGTONE,
        SET_SCRIPTS,
        SAVE,
        NONE,
    }
}
