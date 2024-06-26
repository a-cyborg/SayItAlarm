/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.presentation.command

import org.a_cyb.sayitalarm.entity.Snooze
import org.a_cyb.sayitalarm.entity.TimeOut

interface SettingsCommandContract {
    fun interface SetTimeOut : CommandContract.CommandReceiver {
        fun setTimeOut(timeOut: TimeOut)
    }

    fun interface SetSnooze : CommandContract.CommandReceiver {
        fun setSnooze(snooze: Snooze)
    }

    fun interface SetTheme : CommandContract.CommandReceiver {
        fun setTheme(themeName: String)
    }
}

interface SettingsCommandContractAll :
    SettingsCommandContract.SetTimeOut,
    SettingsCommandContract.SetSnooze,
    SettingsCommandContract.SetTheme

data class SetTimeOutCommand(val timeOut: Int) : CommandContract.Command<SettingsCommandContract.SetTimeOut> {
    override fun execute(receiver: SettingsCommandContract.SetTimeOut) {
        receiver.setTimeOut(TimeOut(timeOut))
    }
}

data class SetSnoozeCommand(val snooze: Int) : CommandContract.Command<SettingsCommandContract.SetSnooze> {
    override fun execute(receiver: SettingsCommandContract.SetSnooze) {
        receiver.setSnooze(Snooze(snooze))
    }
}

data class SetThemeCommand(val themeName: String) : CommandContract.Command<SettingsCommandContract.SetTheme> {
    override fun execute(receiver: SettingsCommandContract.SetTheme) {
        receiver.setTheme(themeName)
    }
}
