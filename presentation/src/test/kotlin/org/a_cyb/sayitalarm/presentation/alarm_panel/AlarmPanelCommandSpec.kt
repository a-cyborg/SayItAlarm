/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.presentation.alarm_panel

import io.mockk.mockk
import io.mockk.verify
import org.a_cyb.sayitalarm.entity.AlarmType
import org.a_cyb.sayitalarm.entity.AlertType
import org.a_cyb.sayitalarm.entity.Hour
import org.a_cyb.sayitalarm.entity.Label
import org.a_cyb.sayitalarm.entity.Minute
import org.a_cyb.sayitalarm.entity.Ringtone
import org.a_cyb.sayitalarm.entity.SayItScripts
import org.a_cyb.sayitalarm.entity.WeeklyRepeat
import org.a_cyb.sayitalarm.presentation.CommandContract
import org.a_cyb.sayitalarm.util.fulfils
import org.junit.Test

class AlarmPanelCommandSpec {
    @Test
    fun `SetTimeCommand fulfils Command`() {
        SetTimeCommand(Hour(3), Minute(33)) fulfils CommandContract.Command::class
    }

    @Test
    fun `Given SetTimeOutCommand execute is called it runs setTime`() {
        // Given
        val receiver: AlarmPanelCommandContract.SetTime = mockk(relaxed = true)

        // When
        SetTimeCommand(Hour(3), Minute(33)).execute(receiver = receiver)

        // Then
        verify(exactly = 1) { receiver.setTime(any(), any()) }
    }

    @Test
    fun `SetWeeklyRepeatCommand fulfils Command`() {
        SetWeeklyRepeatCommand(WeeklyRepeat()) fulfils CommandContract.Command::class
    }

    @Test
    fun `Given SetWeeklyRepeatCommand execute is called it runs setTime`() {
        // Given
        val receiver: AlarmPanelCommandContract.SetWeeklyRepeat = mockk(relaxed = true)

        // When
        SetWeeklyRepeatCommand(WeeklyRepeat()).execute(receiver = receiver)

        // Then
        verify(exactly = 1) { receiver.setWeeklyRepeat(any()) }
    }

    @Test
    fun `SetLabelCommand fulfils Command`() {
        SetLabelCommand(Label("Say It")) fulfils CommandContract.Command::class
    }

    @Test
    fun `Given SetLabelCommand execute is called it runs setLabel`() {
        // Given
        val receiver: AlarmPanelCommandContract.SetLabel = mockk(relaxed = true)

        // When
        SetLabelCommand(Label("Say It")).execute(receiver = receiver)

        // Then
        verify(exactly = 1) { receiver.setLabel(any()) }
    }

    @Test
    fun `SetAlertTypeCommand fulfils Command`() {
        SetAlertTypeCommand(AlertType.SOUND_ONLY) fulfils CommandContract.Command::class
    }

    @Test
    fun `Given SetAlertTypeCommand execute is called it runs setAlertType`() {
        // Given
        val receiver: AlarmPanelCommandContract.SetAlertType = mockk(relaxed = true)

        // When
        SetAlertTypeCommand(AlertType.SOUND_ONLY).execute(receiver = receiver)

        // Then
        verify(exactly = 1) { receiver.setAlertType(any()) }
    }

    @Test
    fun `SetRingtoneCommand fulfils Command`() {
        SetRingtoneCommand(Ringtone("")) fulfils CommandContract.Command::class
    }

    @Test
    fun `Given SetRingtoneCommand execute is called it runs setRingtone`() {
        // Given
        val receiver: AlarmPanelCommandContract.SetRingtone = mockk(relaxed = true)

        // When
        SetRingtoneCommand(Ringtone("")).execute(receiver = receiver)

        // Then
        verify(exactly = 1) { receiver.setRingtone(any()) }
    }

    @Test
    fun `SetAlarmTypeCommand fulfils Command`() {
        SetAlarmTypeCommand(AlarmType.PUSH_BUTTON) fulfils CommandContract.Command::class
    }

    @Test
    fun `Given SetAlarmTypeCommand execute is called it runs setAlarmType`() {
        // Given
        val receiver: AlarmPanelCommandContract.SetAlarmType = mockk(relaxed = true)

        // When
        SetAlarmTypeCommand(AlarmType.SAY_IT).execute(receiver = receiver)

        // Then
        verify(exactly = 1) { receiver.setAlarmType(any()) }
    }

    @Test
    fun `SetScriptsCommand fulfils Command`() {
        SetScriptsCommand(SayItScripts()) fulfils CommandContract.Command::class
    }

    @Test
    fun `Given SetScriptsCommand execute is called it runs setScripts`() {
        // Given
        val receiver: AlarmPanelCommandContract.SetScripts = mockk(relaxed = true)

        // When
        SetScriptsCommand(SayItScripts()).execute(receiver = receiver)

        // Then
        verify(exactly = 1) { receiver.setScripts(any()) }
    }
}