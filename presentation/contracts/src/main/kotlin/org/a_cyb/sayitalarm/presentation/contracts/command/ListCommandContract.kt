/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.presentation.contracts.command

interface ListCommandContract {

    fun interface SetEnabled : CommandContract.CommandReceiver {
        fun setEnabled(id: Long, enabled: Boolean)
    }

    fun interface DeleteAlarm : CommandContract.CommandReceiver {
        fun deleteAlarm(id: Long)
    }
}

interface ListCommandContractAll :
    ListCommandContract.SetEnabled,
    ListCommandContract.DeleteAlarm

data class SetEnabledCommand(val id: Long, val enabled: Boolean) :
    CommandContract.Command<ListCommandContract.SetEnabled> {
    override fun execute(receiver: ListCommandContract.SetEnabled) {
        receiver.setEnabled(id, enabled)
    }
}

data class DeleteAlarmCommand(val id: Long) :
    CommandContract.Command<ListCommandContract.DeleteAlarm> {
    override fun execute(receiver: ListCommandContract.DeleteAlarm) {
        receiver.deleteAlarm(id)
    }
}
