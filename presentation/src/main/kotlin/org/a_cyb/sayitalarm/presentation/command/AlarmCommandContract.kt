/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.presentation.command

interface AlarmCommandContract {
    fun interface StartSayIt : CommandContract.CommandReceiver {
        fun startSayIt()
    }
}

data object StartSayItCommand : CommandContract.Command<AlarmCommandContract.StartSayIt> {
    override fun execute(receiver: AlarmCommandContract.StartSayIt) {
        receiver.startSayIt()
    }
}