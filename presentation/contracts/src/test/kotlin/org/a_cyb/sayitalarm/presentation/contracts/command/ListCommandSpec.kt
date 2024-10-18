/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.presentation.contracts.command

import io.mockk.mockk
import io.mockk.verify
import org.a_cyb.sayitalarm.presentation.contracts.command.CommandContract.Command
import org.a_cyb.sayitalarm.presentation.contracts.command.ListCommandContract.DeleteAlarm
import org.a_cyb.sayitalarm.presentation.contracts.command.ListCommandContract.SetEnabled
import org.junit.Test
import kotlin.test.assertIs

class ListCommandSpec {

    @Test
    fun `SetEnabledCommand fulfils Command`() {
        assertIs<Command<SetEnabled>>(SetEnabledCommand(1, true))
    }

    @Test
    fun `Given SetEnabledCommand execute is called it runs setEnabled`() {
        // Given
        val receiver: SetEnabled = mockk(relaxed = true)

        // When
        SetEnabledCommand(1, true).execute(receiver = receiver)

        // Then
        verify(exactly = 1) { receiver.setEnabled(any(), any()) }
    }

    @Test
    fun `DeleteAlarmCommand fulfils Command`() {
        assertIs<Command<DeleteAlarm>>(DeleteAlarmCommand(1))
    }

    @Test
    fun `Given DeleteAlarmCommand execute is called it runs deleteAlarm`() {
        // Given
        val receiver: DeleteAlarm = mockk(relaxed = true)

        // When
        DeleteAlarmCommand(1).execute(receiver = receiver)

        // Then
        verify(exactly = 1) { receiver.deleteAlarm(any()) }
    }
}
