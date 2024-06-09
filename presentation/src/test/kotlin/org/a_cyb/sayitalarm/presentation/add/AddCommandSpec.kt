/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.presentation.add

import kotlin.test.Test
import io.mockk.mockk
import io.mockk.verify
import org.a_cyb.sayitalarm.entity.Alarm
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

class AddCommandSpec {

    private val alarm = Alarm(
        id = 0,
        hour = Hour(0),
        minute = Minute(0),
        weeklyRepeat = WeeklyRepeat(),
        label = Label(""),
        enabled = true,
        alertType = AlertType.SOUND_AND_VIBRATE,
        ringtone = Ringtone(""),
        alarmType = AlarmType.SAY_IT,
        sayItScripts = SayItScripts()
    )

    @Test
    fun `SaveCommand fulfils Command`() {
        SaveCommand(alarm = alarm) fulfils CommandContract.Command::class
    }

    @Test
    fun `Given SaveCommand executes it runs save`() {
        // Given
        val receiver: AddCommandContract.Save = mockk(relaxed = true)

        // When
        SaveCommand(alarm = alarm).execute(receiver)

        // Then
        verify(exactly = 1) { receiver.save(any()) }
    }
}
