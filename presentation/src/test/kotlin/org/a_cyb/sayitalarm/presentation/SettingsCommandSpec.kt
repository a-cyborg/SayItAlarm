/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.presentation

import kotlin.test.Test
import org.a_cyb.sayitalarm.entity.Snooze
import org.a_cyb.sayitalarm.entity.Theme
import org.a_cyb.sayitalarm.entity.TimeOut
import org.a_cyb.sayitalarm.util.fulfils
import org.a_cyb.sayitalarm.util.mustBe

class SettingsCommandSpec {
    @Test
    fun `SetTimeOutCommand fulfils Command`() {
        SetTimeOutCommand(33) fulfils CommandContract.Command::class
    }

    @Test
    fun `Given SetTimeOutCommand execute is called it runs setTimeOut`() {
        // Given
        val command = SetTimeOutCommand(33)
        val receiver = object : SettingsCommandContract.SetTimeOut {
            var hasBeenCalled: Boolean = false

            override fun setTimeOut(timeOut: TimeOut) {
                hasBeenCalled = true
            }
        }

        // When
        command.execute(receiver)

        // Then
        receiver.hasBeenCalled mustBe true
    }

    @Test
    fun `SetSnoozeCommand fulfils Command`() {
        SetSnoozeCommand(5) fulfils CommandContract.Command::class
    }

    @Test
    fun `Given SetSnoozeCommand execute is called it runs setSnooze`() {
        // Given
        val command = SetSnoozeCommand(5)
        val receiver = object : SettingsCommandContract.SetSnooze {
            var hasBeenCalled: Boolean = false

            override fun setSnooze(snooze: Snooze) {
                hasBeenCalled = true
            }
        }

        // When
        command.execute(receiver)

        // Then
        receiver.hasBeenCalled mustBe true
    }

    @Test
    fun `SetThemeCommand fulfils Command`() {
        SetThemeCommand(Theme.DARK) fulfils CommandContract.Command::class
    }

    @Test
    fun `Given SetThemeCommand execute is called it runs setSnooze`() {
        // Given
        val command = SetThemeCommand(Theme.DARK)
        val receiver = object : SettingsCommandContract.SetTheme {
            var hasBeenCalled: Boolean = false

            override fun setTheme(theme: Theme) {
                hasBeenCalled = true
            }
        }

        // When
        command.execute(receiver)

        // Then
        receiver.hasBeenCalled mustBe true
    }
}
