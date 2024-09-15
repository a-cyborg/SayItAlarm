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

class AlarmCommandSpec {

    @Test
    fun `StartSayItCommand fulfills Command`() {
        StartSayItCommand fulfils CommandContract.Command::class
    }

    @Test
    fun `When StartSayItCommand is executed, it runs startSayIt`() {
        // Given
        val receiver: AlarmCommandContract.StartSayIt = mockk(relaxed = true)

        // When
        StartSayItCommand.execute(receiver)

        // Then
        verify(exactly = 1) { receiver.startSayIt() }
    }

    @Test
    fun `SnoozeCommand fulfills Command`() {
        SnoozeCommand fulfils CommandContract.Command::class
    }

    @Test
    fun `When SnoozeCommand is executed, it runs snooze`() {
        // Given
        val receiver: AlarmCommandContract.Snooze = mockk(relaxed = true)

        // When
        SnoozeCommand.execute(receiver)

        // Then
        verify(exactly = 1) { receiver.snooze() }
    }
}
