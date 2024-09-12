/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.presentation.contracts.command

import io.mockk.mockk
import io.mockk.verify
import org.a_cyb.sayitalarm.util.fulfils
import org.junit.Test

class ListCommandSpec {

    @Test
    fun `SetEnabledCommand fulfils Command`() {
        SetEnabledCommand(1, true) fulfils CommandContract.Command::class
    }

    @Test
    fun `Given SetEnabledCommand execute is called it runs setEnabled`() {
        // Given
        val receiver: ListCommandContract.SetEnabled = mockk(relaxed = true)

        // When
        SetEnabledCommand(1, true).execute(receiver = receiver)

        // Then
        verify(exactly = 1) { receiver.setEnabled(any(), any()) }
    }

    @Test
    fun `DeleteAlarmCommand fulfils Command`() {
        DeleteAlarmCommand(1) fulfils CommandContract.Command::class
    }

    @Test
    fun `Given DeleteAlarmCommand execute is called it runs deleteAlarm`() {
        // Given
        val receiver: ListCommandContract.DeleteAlarm = mockk(relaxed = true)

        // When
        DeleteAlarmCommand(1).execute(receiver = receiver)

        // Then
        verify(exactly = 1) { receiver.deleteAlarm(any()) }
    }
}
