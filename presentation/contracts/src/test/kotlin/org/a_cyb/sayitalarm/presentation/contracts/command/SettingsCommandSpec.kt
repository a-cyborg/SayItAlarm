/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.presentation.contracts.command

import io.mockk.mockk
import io.mockk.verify
import org.a_cyb.sayitalarm.presentation.contracts.command.CommandContract.Command
import org.a_cyb.sayitalarm.presentation.contracts.command.SettingsCommandContract.OpenGitHub
import org.a_cyb.sayitalarm.presentation.contracts.command.SettingsCommandContract.OpenGooglePlay
import org.a_cyb.sayitalarm.presentation.contracts.command.SettingsCommandContract.SendEmail
import org.a_cyb.sayitalarm.presentation.contracts.command.SettingsCommandContract.SetSnooze
import org.a_cyb.sayitalarm.presentation.contracts.command.SettingsCommandContract.SetTheme
import org.a_cyb.sayitalarm.presentation.contracts.command.SettingsCommandContract.SetTimeOut
import org.junit.Test
import kotlin.test.assertIs

class SettingsCommandSpec {

    @Test
    fun `SetTimeOutCommand fulfils Command`() {
        assertIs<Command<SetTimeOut>>(SetTimeOutCommand(0))
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
        assertIs<Command<SetSnooze>>(SetSnoozeCommand(0))
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
        assertIs<Command<SetTheme>>(SetThemeCommand(""))
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
        assertIs<Command<SendEmail>>(SendEmailCommand)
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
        assertIs<Command<OpenGooglePlay>>(OpenGooglePlayCommand)
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
        assertIs<Command<OpenGitHub>>(OpenGitHubCommand)
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
