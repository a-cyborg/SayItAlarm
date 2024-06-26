/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.presentation.command

interface AddCommandContract {
    fun interface Save : CommandContract.CommandReceiver {
        fun save()
    }
}

data object SaveCommand : CommandContract.Command<AddCommandContract.Save> {
    override fun execute(receiver: AddCommandContract.Save) {
        receiver.save()
    }
}