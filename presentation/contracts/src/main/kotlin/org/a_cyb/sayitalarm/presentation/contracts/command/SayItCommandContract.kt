/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.presentation.contracts.command

interface SayItCommandContract {
    fun interface ProcessScript : CommandContract.CommandReceiver {
        fun processScript()
    }

    fun interface Finish : CommandContract.CommandReceiver {
        fun finish()
    }
}

interface SayItCommandContractAll :
    SayItCommandContract.ProcessScript,
    SayItCommandContract.Finish

data object ProcessScriptCommand : CommandContract.Command<SayItCommandContract.ProcessScript> {
    override fun execute(receiver: SayItCommandContract.ProcessScript) {
        receiver.processScript()
    }
}

data object FinishCommand : CommandContract.Command<SayItCommandContract.Finish> {
    override fun execute(receiver: SayItCommandContract.Finish) {
        receiver.finish()
    }
}


