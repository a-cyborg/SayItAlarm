/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.presentation

import kotlin.test.Test
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
import org.a_cyb.sayitalarm.presentation.alarm_panel.AlarmPanelCommandContract
import org.a_cyb.sayitalarm.presentation.alarm_panel.SetAlarmTypeCommand
import org.a_cyb.sayitalarm.presentation.alarm_panel.SetAlertTypeCommand
import org.a_cyb.sayitalarm.presentation.alarm_panel.SetLabelCommand
import org.a_cyb.sayitalarm.presentation.alarm_panel.SetRingtoneCommand
import org.a_cyb.sayitalarm.presentation.alarm_panel.SetScriptsCommand
import org.a_cyb.sayitalarm.presentation.alarm_panel.SetTimeCommand
import org.a_cyb.sayitalarm.presentation.alarm_panel.SetWeeklyRepeatCommand
import org.a_cyb.sayitalarm.util.fulfils

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
        SetWeeklyRepeatCommand(WeeklyRepeat()) fulfils CommandContract.Command::class
    }

    @Test
    fun `Given SetWeeklyRepeatCommand execute it runs setTime`() {
        // Given
        val receiver: AlarmPanelCommandContract.SetWeeklyRepeat = mockk(relaxed = true)

        // When
        SetWeeklyRepeatCommand(WeeklyRepeat()).execute(receiver)

        // Then
        verify(exactly = 1) { receiver.setWeeklyRepeat(any()) }
    }

    @Test
    fun `SetLabelCommand fulfills Command`() {
        SetLabelCommand(Label("")) fulfils CommandContract.Command::class
    }

    @Test
    fun `Given SetLabelCommand execute it runs setTime`() {
        // Given
        val receiver: AlarmPanelCommandContract.SetLabel = mockk(relaxed = true)

        // When
        SetLabelCommand(Label("")).execute(receiver)

        // Then
        verify(exactly = 1) { receiver.setLabel(any()) }
    }

    @Test
    fun `SetAlertTypeCommand fulfills Command`() {
        SetAlertTypeCommand(AlertType.SOUND_AND_VIBRATE) fulfils CommandContract.Command::class
    }

    @Test
    fun `Given SetAlertTypeCommand execute it runs setTime`() {
        // Given
        val receiver: AlarmPanelCommandContract.SetAlertType = mockk(relaxed = true)

        // When
        SetAlertTypeCommand(AlertType.SOUND_AND_VIBRATE).execute(receiver)

        // Then
        verify(exactly = 1) { receiver.setAlertType(any()) }
    }

    @Test
    fun `SetRingtoneCommand fulfills Command`() {
        SetRingtoneCommand(Ringtone("")) fulfils CommandContract.Command::class
    }

    @Test
    fun `Given SetRingtoneCommand execute it runs setTime`() {
        // Given
        val receiver: AlarmPanelCommandContract.SetRingtone = mockk(relaxed = true)

        // When
        SetRingtoneCommand(Ringtone("")).execute(receiver)

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
}
