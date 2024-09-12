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

class SettingsCommandSpec {

    @Test
    fun `SetTimeOutCommand fulfils Command`() {
        SetTimeOutCommand(33) fulfils CommandContract.Command::class
    }

    @Test
    fun `Given SetTimeOutCommand execute is called it runs setTimeOut`() {
        // Given
        val receiver: SettingsCommandContractAll = mockk(relaxed = true)

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
        val receiver: SettingsCommandContractAll = mockk(relaxed = true)

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
        val receiver: SettingsCommandContractAll = mockk(relaxed = true)

        // When
        SetThemeCommand("Light").execute(receiver = receiver)

        // Then
        verify(exactly = 1) { receiver.setTheme(any()) }
    }

    @Test
    fun `SendEmailCommand fulfils Command`() {
        SendEmailCommand fulfils CommandContract.Command::class
    }

    @Test
    fun `Given SendEmailCommand execute is called it runs sendEmail`() {
        // Given
        val receiver: SettingsCommandContractAll = mockk(relaxed = true)

        // When
        SendEmailCommand.execute(receiver = receiver)

        // Then
        verify(exactly = 1) { receiver.sendEmail() }
    }

    @Test
    fun `OpenGooglePlayCommand fulfils Command`() {
        OpenGooglePlayCommand fulfils CommandContract.Command::class
    }

    @Test
    fun `Given OpenGooglePlayCommand execute is called it runs openGooglePlay`() {
        // Given
        val receiver: SettingsCommandContractAll = mockk(relaxed = true)

        // When
        OpenGooglePlayCommand.execute(receiver = receiver)

        // Then
        verify(exactly = 1) { receiver.openGooglePlay() }
    }

    @Test
    fun `OpenGitHubCommand fulfils Command`() {
        OpenGitHubCommand fulfils CommandContract.Command::class
    }

    @Test
    fun `Given OpenGitHubCommand execute is called it runs openGooglePlay`() {
        // Given
        val receiver: SettingsCommandContractAll = mockk(relaxed = true)

        // When
        OpenGitHubCommand.execute(receiver = receiver)

        // Then
        verify(exactly = 1) { receiver.openGitHub() }
    }
}
