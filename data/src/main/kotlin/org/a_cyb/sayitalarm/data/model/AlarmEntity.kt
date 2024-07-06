/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.data.model

import org.a_cyb.sayitalarm.entity.Alarm
import org.a_cyb.sayitalarm.entity.AlarmType
import org.a_cyb.sayitalarm.entity.AlertType
import org.a_cyb.sayitalarm.entity.Hour
import org.a_cyb.sayitalarm.entity.Label
import org.a_cyb.sayitalarm.entity.Minute
import org.a_cyb.sayitalarm.entity.Ringtone
import org.a_cyb.sayitalarm.entity.SayItScripts
import org.a_cyb.sayitalarm.entity.WeeklyRepeat

data class AlarmEntity(
    val id: Long,
    val hour: Long,
    val minute: Long,
    val weeklyRepeat: Long,
    val label: String,
    val enabled: Boolean,
    val alertType: Long,
    val ringtone: String,
    val alarmType: Long,
    val sayItScripts: String,
)

fun AlarmEntity.toAlarm(): Alarm =
    Alarm(
        id = id,
        hour = Hour(hour.toInt()),
        minute = Minute(minute.toInt()),
        weeklyRepeat = WeeklyRepeat(weeklyRepeat.asSetOfDayCode()),
        label = Label(label),
        enabled = enabled,
        alertType = alertType.asAlertType(),
        ringtone = Ringtone(ringtone),
        alarmType = alarmType.asAlarmType(),
        sayItScripts = SayItScripts(sayItScripts.toScripts()),
    )

private fun Long.asSetOfDayCode(): Set<Int> {
    return (0..6).fold(mutableSetOf()) { acc, i ->
        val isBitOn = (this shr i) and 1 == 1L

        if (isBitOn) {
            acc.add(i + 1)
        }

        acc
    }
}

private fun Long.asAlertType(): AlertType {
    return AlertType.entries
        .getOrElse(toInt()) { AlertType.SOUND_AND_VIBRATE }
}

private fun Long.asAlarmType(): AlarmType {
    return AlarmType.entries
        .getOrElse(toInt()) { AlarmType.SAY_IT }
}

private fun String.toScripts(): List<String> {
    return split(",")
}

fun Alarm.toAlarmEntity() =
    AlarmEntity(
        id = id,
        hour = hour.hour.toLong(),
        minute = minute.minute.toLong(),
        weeklyRepeat = weeklyRepeat.toLong(),
        label = label.label,
        enabled = enabled,
        alertType = alertType.ordinal.toLong(),
        ringtone = ringtone.ringtone,
        alarmType = alarmType.ordinal.toLong(),
        sayItScripts = sayItScripts.scripts.joinToString(","),
    )

private fun WeeklyRepeat.toLong(): Long {
    return weekdays.fold(0b0000000) { acc, i ->
        acc xor (1 shl i - 1)
    }.toLong()
}
