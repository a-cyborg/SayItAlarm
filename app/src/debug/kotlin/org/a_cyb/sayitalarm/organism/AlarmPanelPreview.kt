/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.organism

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import org.a_cyb.sayitalarm.entity.AlertType
import org.a_cyb.sayitalarm.presentation.alarm_panel.AlarmPanelContract.*

@Preview
@Composable
fun AlarmPanelPreview() {
    AlarmPanel(
        alarmUI = defaultAlarmUI,
        executor = { _ -> }
    )
}

private val defaultAlarmUI = AlarmUI(
    time = TimeUI(8, 0, "8:00 AM"),
    weeklyRepeat = WeeklyRepeatUI(emptySet(), "Never", mapOf()),
    label = "",
    alertType = AlertTypeUI(
        selected = AlertType.SOUND_AND_VIBRATE,
        formattedAlertType = "Sound and vibration",
        selectableAlertType = mapOf()
    ),
    ringtone = RingtoneUI("Radial", "file:://Radial.mp3"),
    sayItScripts = emptyList()
)
