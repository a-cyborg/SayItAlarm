/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.presentation.command

import io.mockk.mockk
import io.mockk.verify
import org.a_cyb.sayitalarm.entity.AlarmType
import org.a_cyb.sayitalarm.entity.Hour
import org.a_cyb.sayitalarm.entity.Minute
import org.a_cyb.sayitalarm.entity.SayItScripts
import org.a_cyb.sayitalarm.presentation.AlarmPanelContract.RingtoneUI
import org.a_cyb.sayitalarm.util.fulfils
import org.junit.Test

class AlarmPanelCommandSpec {

    @Test
    fun `SetTimeCommand fulfills Command`() {
        SetTimeCommand(Hour(0), Minute(0)) fulfils CommandContract.Command::class
    }

    @Test
    fun `Given SetTimeCommand execute it runs setTime`() {
        // Given
        val receiver: AlarmPanelCommandContract.SetTime = mockk(relaxed = true)

        // When
        SetTimeCommand(Hour(0), Minute(0)).execute(receiver)

        // Then
        verify(exactly = 1) { receiver.setTime(any(), any()) }
    }

    @Test
    fun `SetWeeklyRepeatCommand fulfills Command`() {
        SetWeeklyRepeatCommand(emptyList()) fulfils CommandContract.Command::class
    }

    @Test
    fun `Given SetWeeklyRepeatCommand execute it runs setTime`() {
        // Given
        val receiver: AlarmPanelCommandContract.SetWeeklyRepeat = mockk(relaxed = true)

        // When
        SetWeeklyRepeatCommand(emptyList()).execute(receiver)

        // Then
        verify(exactly = 1) { receiver.setWeeklyRepeat(any()) }
    }

    @Test
    fun `SetLabelCommand fulfills Command`() {
        SetLabelCommand("Label") fulfils CommandContract.Command::class
    }

    @Test
    fun `Given SetLabelCommand execute it runs setTime`() {
        // Given
        val receiver: AlarmPanelCommandContract.SetLabel = mockk(relaxed = true)

        // When
        SetLabelCommand("Label").execute(receiver)

        // Then
        verify(exactly = 1) { receiver.setLabel(any()) }
    }

    @Test
    fun `SetAlertTypeCommand fulfills Command`() {
        SetAlertTypeCommand("") fulfils CommandContract.Command::class
    }

    @Test
    fun `Given SetAlertTypeCommand execute it runs setTime`() {
        // Given
        val receiver: AlarmPanelCommandContract.SetAlertType = mockk(relaxed = true)

        // When
        SetAlertTypeCommand("").execute(receiver)

        // Then
        verify(exactly = 1) { receiver.setAlertType(any()) }
    }

    @Test
    fun `SetRingtoneCommand fulfills Command`() {
        SetRingtoneCommand(RingtoneUI("", "")) fulfils CommandContract.Command::class
    }

    @Test
    fun `Given SetRingtoneCommand execute it runs setTime`() {
        // Given
        val receiver: AlarmPanelCommandContract.SetRingtone = mockk(relaxed = true)

        // When
        SetRingtoneCommand(RingtoneUI("", "")).execute(receiver)

        // Then
        verify(exactly = 1) { receiver.setRingtone(any()) }
    }

    @Test
    fun `SetAlarmTypeCommand fulfills Command`() {
        SetAlarmTypeCommand(AlarmType.SAY_IT) fulfils CommandContract.Command::class
    }

    @Test
    fun `Given SetAlarmTypeCommand execute it runs setTime`() {
        // Given
        val receiver: AlarmPanelCommandContract.SetAlarmType = mockk(relaxed = true)

        // When
        SetAlarmTypeCommand(AlarmType.SAY_IT).execute(receiver)

        // Then
        verify(exactly = 1) { receiver.setAlarmType(any()) }
    }

    @Test
    fun `SetScriptsCommand fulfills Command`() {
        SetScriptsCommand(SayItScripts()) fulfils CommandContract.Command::class
    }

    @Test
    fun `Given SetScriptsCommand execute it runs setTime`() {
        // Given
        val receiver: AlarmPanelCommandContract.SetScripts = mockk(relaxed = true)

        // When
        SetScriptsCommand(SayItScripts()).execute(receiver)

        // Then
        verify(exactly = 1) { receiver.setScripts(SayItScripts()) }
    }

    @Test
    fun `SaveCommand fulfils Command`() {
        SaveCommand fulfils CommandContract.Command::class
    }

    @Test
    fun `Given SaveCommand executes it runs save`() {
        // Given
        val receiver: AlarmPanelCommandContract.Save = mockk(relaxed = true)

        // When
        SaveCommand.execute(receiver)

        // Then
        verify(exactly = 1) { receiver.save() }
    }
}
