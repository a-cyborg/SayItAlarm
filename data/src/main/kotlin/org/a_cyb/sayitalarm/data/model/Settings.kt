/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.data.model

import org.a_cyb.sayitalarm.entity.Settings
import org.a_cyb.sayitalarm.entity.Snooze
import org.a_cyb.sayitalarm.entity.Theme
import org.a_cyb.sayitalarm.entity.TimeOut
import org.acyb.sayitalarm.database.Get as SettingsDTO

fun SettingsDTO.toSettings(): Settings =
    Settings(
        timeOut = TimeOut(timeOut.toInt()),
        snooze = Snooze(snooze.toInt()),
        theme = theme.asTheme(),
    )

private fun Long.asTheme(): Theme =
    Theme.entries
        .getOrElse(toInt()) { Theme.LIGHT }

fun Settings.toSettingsDTO(): SettingsDTO =
    SettingsDTO(
        timeOut = this.timeOut.timeOut.toLong(),
        snooze = this.snooze.snooze.toLong(),
        theme = this.theme.ordinal.toLong(),
    )
