/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.presentation.command

interface SayItCommandContract {
    fun interface ProcessScript : CommandContract.CommandReceiver {
        fun processScript()
    }

    fun interface ForceQuit : CommandContract.CommandReceiver {
        fun forceQuit()
    }
}

interface SayItCommandContractAll :
    SayItCommandContract.ProcessScript,
    SayItCommandContract.ForceQuit

data object ProcessScriptCommand : CommandContract.Command<SayItCommandContract.ProcessScript> {
    override fun execute(receiver: SayItCommandContract.ProcessScript) {
        receiver.processScript()
    }
}

data object ForceQuitCommand : CommandContract.Command<SayItCommandContract.ForceQuit> {
    override fun execute(receiver: SayItCommandContract.ForceQuit) {
        receiver.forceQuit()
    }
}


