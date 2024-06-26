/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.organism

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import org.a_cyb.sayitalarm.presentation.AlarmPanelContract.*

@Preview
@Composable
fun AlarmPanelPreview() {
    AlarmPanel(
        alarmUI = defaultAlarmUI,
        executor = { _ -> }
    )
}

private val defaultAlarmUI: AlarmUI =
    AlarmUI(
        timeUI = TimeUI(8, 0, "8:00 AM"),
        weeklyRepeatUI = WeeklyRepeatUI("", emptyList()),
        label = "",
        alertTypeUI = AlertTypeUI(listOf(SelectableAlertType("Sound only", true))),
        ringtoneUI = RingtoneUI("Radial", "file://Radial.mp3"),
        sayItScripts = emptyList()
    )
