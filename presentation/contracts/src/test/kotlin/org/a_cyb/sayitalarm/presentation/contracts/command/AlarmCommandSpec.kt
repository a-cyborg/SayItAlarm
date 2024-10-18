/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.presentation.contracts.command

import io.mockk.mockk
import io.mockk.verify
import org.a_cyb.sayitalarm.presentation.contracts.command.AlarmCommandContract.Snooze
import org.a_cyb.sayitalarm.presentation.contracts.command.AlarmCommandContract.StartSayIt
import org.a_cyb.sayitalarm.presentation.contracts.command.CommandContract.Command
import org.junit.Test
import kotlin.test.assertIs

class AlarmCommandSpec {

    @Test
    fun `StartSayItCommand fulfills Command`() {
        assertIs<Command<StartSayIt>>(StartSayItCommand)
    }

    @Test
    fun `When StartSayItCommand is executed, it runs startSayIt`() {
        // Given
        val receiver: StartSayIt = mockk(relaxed = true)

        // When
        StartSayItCommand.execute(receiver)

        // Then
        verify(exactly = 1) { receiver.startSayIt() }
    }

    @Test
    fun `SnoozeCommand fulfills Command`() {
        assertIs<Command<Snooze>>(SnoozeCommand)
    }

    @Test
    fun `When SnoozeCommand is executed, it runs snooze`() {
        // Given
        val receiver: Snooze = mockk(relaxed = true)

        // When
        SnoozeCommand.execute(receiver)

        // Then
        verify(exactly = 1) { receiver.snooze() }
    }
}
