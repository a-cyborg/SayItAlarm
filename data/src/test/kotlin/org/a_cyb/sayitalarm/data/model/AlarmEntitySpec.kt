/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.data.model

import kotlin.test.Test
import java.util.Calendar
import org.a_cyb.sayitalarm.entity.Alarm
import org.a_cyb.sayitalarm.entity.AlarmType
import org.a_cyb.sayitalarm.entity.AlertType
import org.a_cyb.sayitalarm.entity.Hour
import org.a_cyb.sayitalarm.entity.Label
import org.a_cyb.sayitalarm.entity.Minute
import org.a_cyb.sayitalarm.entity.Ringtone
import org.a_cyb.sayitalarm.entity.SayItScripts
import org.a_cyb.sayitalarm.entity.WeeklyRepeat
import org.a_cyb.sayitalarm.util.mustBe

class AlarmEntitySpec {

    @Test
    fun `When toAlarm is called it maps AlarmEntity to Alarm`() {
        // Given
        val hour = 3
        val minute = 33
        val weeklyRepeat = 85  // 0b1010101 : [Sun, Tue, Tur, Sat].
        val label = "Label"
        val enabled = true
        val alertType: Long = AlertType.SOUND_ONLY.ordinal.toLong()
        val ringtone = "content://media/internal/audio/media/190?title=Drip&canonical=1"
        val alarmType: Long = AlarmType.SAY_IT.ordinal.toLong()
        val sayItScripts = "script A,script B,script C"

        val alarmEntity =
            AlarmEntity(
                id = 0L,
                hour.toLong(),
                minute.toLong(),
                weeklyRepeat.toLong(),
                label,
                enabled,
                alertType,
                ringtone,
                alarmType,
                sayItScripts
            )

        // When
        val actual = alarmEntity.toAlarm()

        // Then
        actual mustBe
            Alarm(
                id = 0L,
                hour = Hour(hour),
                minute = Minute(minute),
                weeklyRepeat = WeeklyRepeat(
                    setOf(
                        Calendar.SUNDAY,
                        Calendar.TUESDAY,
                        Calendar.THURSDAY,
                        Calendar.SATURDAY
                    )
                ),
                label = Label(label),
                enabled = enabled,
                alertType = AlertType.SOUND_ONLY,
                ringtone = Ringtone(ringtone),
                alarmType = AlarmType.SAY_IT,
                sayItScripts = SayItScripts(listOf("script A", "script B", "script C"))
            )
    }

    @Test
    fun `When toAlarmEntity is called it maps Alarm to AlarmEntity`() {
        // Given
        val alarm =
            Alarm(
                id = 0L,
                hour = Hour(3),
                minute = Minute(3),
                weeklyRepeat = WeeklyRepeat(
                    setOf(
                        Calendar.SUNDAY,
                        Calendar.TUESDAY,
                        Calendar.THURSDAY,
                        Calendar.SATURDAY
                    )
                ),
                label = Label("Label"),
                enabled = true,
                alertType = AlertType.SOUND_AND_VIBRATE,
                ringtone = Ringtone("content://media/internal/audio/media/190?title=Drip&canonical=1"),
                alarmType = AlarmType.SAY_IT,
                sayItScripts = SayItScripts(listOf("script A", "script B", "script C"))
            )

        // When
        val actual = alarm.toAlarmEntity()

        // Then
        actual mustBe
            AlarmEntity(
                id = 0L,
                hour = 3,
                minute = 3,
                weeklyRepeat = 85,
                label = "Label",
                enabled = true,
                alertType = 2,
                ringtone = "content://media/internal/audio/media/190?title=Drip&canonical=1",
                alarmType = 0,
                sayItScripts = "script A,script B,script C"
            )
    }
}
