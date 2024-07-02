/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.database.model

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
