/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.presentation.contracts.command

import io.mockk.mockk
import io.mockk.verify
import org.a_cyb.sayitalarm.entity.AlarmType
import org.a_cyb.sayitalarm.entity.Hour
import org.a_cyb.sayitalarm.entity.Minute
import org.a_cyb.sayitalarm.entity.SayItScripts
import org.a_cyb.sayitalarm.presentation.contracts.AlarmPanelContract.RingtoneUI
import org.a_cyb.sayitalarm.presentation.contracts.command.AlarmPanelCommandContract.Save
import org.a_cyb.sayitalarm.presentation.contracts.command.AlarmPanelCommandContract.SetAlarmType
import org.a_cyb.sayitalarm.presentation.contracts.command.AlarmPanelCommandContract.SetAlertType
import org.a_cyb.sayitalarm.presentation.contracts.command.AlarmPanelCommandContract.SetLabel
import org.a_cyb.sayitalarm.presentation.contracts.command.AlarmPanelCommandContract.SetRingtone
import org.a_cyb.sayitalarm.presentation.contracts.command.AlarmPanelCommandContract.SetScripts
import org.a_cyb.sayitalarm.presentation.contracts.command.AlarmPanelCommandContract.SetTime
import org.a_cyb.sayitalarm.presentation.contracts.command.AlarmPanelCommandContract.SetWeeklyRepeat
import org.a_cyb.sayitalarm.presentation.contracts.command.CommandContract.Command
import org.junit.Test
import kotlin.test.assertIs

class AlarmPanelCommandSpec {

    @Test
    fun `SetTimeCommand fulfills Command`() {
        assertIs<Command<SetTime>>(SetTimeCommand(Hour(0), Minute(0)))
    }

    @Test
    fun `Given SetTimeCommand execute it runs setTime`() {
        // Given
        val receiver: SetTime = mockk(relaxed = true)

        // When
        SetTimeCommand(Hour(0), Minute(0)).execute(receiver)

        // Then
        verify(exactly = 1) { receiver.setTime(any(), any()) }
    }

    @Test
    fun `SetWeeklyRepeatCommand fulfills Command`() {
        assertIs<Command<SetWeeklyRepeat>>(SetWeeklyRepeatCommand(emptyList()))
    }

    @Test
    fun `Given SetWeeklyRepeatCommand execute it runs setTime`() {
        // Given
        val receiver: SetWeeklyRepeat = mockk(relaxed = true)

        // When
        SetWeeklyRepeatCommand(emptyList()).execute(receiver)

        // Then
        verify(exactly = 1) { receiver.setWeeklyRepeat(any()) }
    }

    @Test
    fun `SetLabelCommand fulfills Command`() {
        assertIs<Command<SetLabel>>(SetLabelCommand(""))
    }

    @Test
    fun `Given SetLabelCommand execute it runs setTime`() {
        // Given
        val receiver: SetLabel = mockk(relaxed = true)

        // When
        SetLabelCommand("Label").execute(receiver)

        // Then
        verify(exactly = 1) { receiver.setLabel(any()) }
    }

    @Test
    fun `SetAlertTypeCommand fulfills Command`() {
        assertIs<Command<SetAlertType>>(SetAlertTypeCommand(""))
    }

    @Test
    fun `Given SetAlertTypeCommand execute it runs setTime`() {
        // Given
        val receiver: SetAlertType = mockk(relaxed = true)

        // When
        SetAlertTypeCommand("").execute(receiver)

        // Then
        verify(exactly = 1) { receiver.setAlertType(any()) }
    }

    @Test
    fun `SetRingtoneCommand fulfills Command`() {
        assertIs<Command<SetRingtone>>(SetRingtoneCommand(RingtoneUI("", "")))
    }

    @Test
    fun `Given SetRingtoneCommand execute it runs setTime`() {
        // Given
        val receiver: SetRingtone = mockk(relaxed = true)

        // When
        SetRingtoneCommand(RingtoneUI("", "")).execute(receiver)

        // Then
        verify(exactly = 1) { receiver.setRingtone(any()) }
    }

    @Test
    fun `SetAlarmTypeCommand fulfills Command`() {
        assertIs<Command<SetAlarmType>>(SetAlarmTypeCommand(AlarmType.SAY_IT))
    }

    @Test
    fun `Given SetAlarmTypeCommand execute it runs setTime`() {
        // Given
        val receiver: SetAlarmType = mockk(relaxed = true)

        // When
        SetAlarmTypeCommand(AlarmType.SAY_IT).execute(receiver)

        // Then
        verify(exactly = 1) { receiver.setAlarmType(any()) }
    }

    @Test
    fun `SetScriptsCommand fulfills Command`() {
        assertIs<Command<SetScripts>>(SetScriptsCommand(SayItScripts()))
    }

    @Test
    fun `Given SetScriptsCommand execute it runs setTime`() {
        // Given
        val receiver: SetScripts = mockk(relaxed = true)

        // When
        SetScriptsCommand(SayItScripts()).execute(receiver)

        // Then
        verify(exactly = 1) { receiver.setScripts(SayItScripts()) }
    }

    @Test
    fun `SaveCommand fulfils Command`() {
        assertIs<Command<Save>>(SaveCommand)
    }

    @Test
    fun `Given SaveCommand executes it runs save`() {
        // Given
        val receiver: Save = mockk(relaxed = true)

        // When
        SaveCommand.execute(receiver)

        // Then
        verify(exactly = 1) { receiver.save() }
    }
}
