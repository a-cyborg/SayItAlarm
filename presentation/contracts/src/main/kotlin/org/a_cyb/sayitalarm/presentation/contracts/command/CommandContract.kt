/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.presentation.contracts.command

interface CommandContract {

    interface CommandReceiver

    interface Command<T : CommandReceiver> {
        fun execute(receiver: T)
    }

    interface CommandExecutor {
        fun <T : CommandReceiver> runCommand(command: Command<T>)
    }
}
