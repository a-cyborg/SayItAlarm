/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.presentation.command

import io.mockk.mockk
import io.mockk.verify
import org.a_cyb.sayitalarm.util.fulfils
import org.junit.Test

class SettingsCommandSpec {

    @Test
    fun `SetTimeOutCommand fulfils Command`() {
        SetTimeOutCommand(33) fulfils CommandContract.Command::class
    }

    @Test
    fun `Given SetTimeOutCommand execute is called it runs setTimeOut`() {
        // Given
        val receiver: SettingsCommandContract.SetTimeOut = mockk(relaxed = true)

        // When
        SetTimeOutCommand(1).execute(receiver = receiver)

        // Then
        verify(exactly = 1) { receiver.setTimeOut(any()) }
    }

    @Test
    fun `SetSnoozeCommand fulfils Command`() {
        SetSnoozeCommand(5) fulfils CommandContract.Command::class
    }

    @Test
    fun `Given SetSnoozeCommand execute is called it runs setSnooze`() {
        // Given
        val receiver: SettingsCommandContract.SetSnooze = mockk(relaxed = true)

        // When
        SetSnoozeCommand(1).execute(receiver = receiver)

        // Then
        verify(exactly = 1) { receiver.setSnooze(any()) }
    }

    @Test
    fun `SetThemeCommand fulfils Command`() {
        SetThemeCommand("Light") fulfils CommandContract.Command::class
    }

    @Test
    fun `Given SetThemeCommand execute is called it runs setTheme`() {
        // Given
        val receiver: SettingsCommandContract.SetTheme = mockk(relaxed = true)

        // When
        SetThemeCommand("Light").execute(receiver = receiver)

        // Then
        verify(exactly = 1) { receiver.setTheme(any()) }
    }
}
