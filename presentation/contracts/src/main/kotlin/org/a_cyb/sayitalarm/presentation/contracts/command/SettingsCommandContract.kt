/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.presentation.contracts.command

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

    fun interface SendEmail : CommandContract.CommandReceiver {
        fun sendEmail()
    }

    fun interface OpenGooglePlay : CommandContract.CommandReceiver {
        fun openGooglePlay()
    }

    fun interface OpenGitHub : CommandContract.CommandReceiver {
        fun openGitHub()
    }
}

interface SettingsCommandContractAll :
    SettingsCommandContract.SetTimeOut,
    SettingsCommandContract.SetSnooze,
    SettingsCommandContract.SetTheme,
    SettingsCommandContract.SendEmail,
    SettingsCommandContract.OpenGitHub,
    SettingsCommandContract.OpenGooglePlay

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

data object SendEmailCommand : CommandContract.Command<SettingsCommandContract.SendEmail> {
    override fun execute(receiver: SettingsCommandContract.SendEmail) {
        receiver.sendEmail()
    }
}

data object OpenGooglePlayCommand : CommandContract.Command<SettingsCommandContract.OpenGooglePlay> {
    override fun execute(receiver: SettingsCommandContract.OpenGooglePlay) {
        receiver.openGooglePlay()
    }
}

data object OpenGitHubCommand : CommandContract.Command<SettingsCommandContract.OpenGitHub> {
    override fun execute(receiver: SettingsCommandContract.OpenGitHub) {
        receiver.openGitHub()
    }
}

