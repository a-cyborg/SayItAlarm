/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.entity

@JvmInline
value class Hour(val hour: Int)

@JvmInline
value class Minute(val minute: Int)

@JvmInline
value class Label(val label: String)

@JvmInline
value class Ringtone(val ringtone: String)

data class WeeklyRepeat(val weekdays: Set<Int>) {
    constructor(vararg weekdays: Int) : this(weekdays.toSet())
}

data class SayItScripts(val scripts: List<String>) {
    constructor(vararg scripts: String) : this(scripts.toList())
}

enum class AlertType {
    SOUND_ONLY,
    VIBRATE_ONLY,
    SOUND_AND_VIBRATE,
}

enum class AlarmType {
    SAY_IT,
    TYPING,
    PUSH_BUTTON,
}

data class Alarm(
    val id: Long = 0L,
    val hour: Hour,
    val minute: Minute,
    val weeklyRepeat: WeeklyRepeat,
    val label: Label,
    val enabled: Boolean,
    val alertType: AlertType,
    val ringtone: Ringtone,
    val alarmType: AlarmType,
    val sayItScripts: SayItScripts,
)
