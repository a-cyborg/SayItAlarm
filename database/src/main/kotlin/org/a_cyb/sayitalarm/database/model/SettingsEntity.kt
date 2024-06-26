/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.database.model

data class SettingsEntity(
    val timeOut: Long,
    val snooze: Long,
    val theme: Long,
)