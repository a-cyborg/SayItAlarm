/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.design_system

import org.a_cyb.sayitalarm.entity.AlertType
import org.a_cyb.sayitalarm.presentation.AlarmPanelContract.AlarmUI
import org.a_cyb.sayitalarm.presentation.AlarmPanelContract.AlertTypeUI
import org.a_cyb.sayitalarm.presentation.AlarmPanelContract.RingtoneUI
import org.a_cyb.sayitalarm.presentation.AlarmPanelContract.SelectableAlertType
import org.a_cyb.sayitalarm.presentation.AlarmPanelContract.SelectableRepeat
import org.a_cyb.sayitalarm.presentation.AlarmPanelContract.TimeUI
import org.a_cyb.sayitalarm.presentation.AlarmPanelContract.WeeklyRepeatUI

object FakeAlarmUIData {

    val defaultSelectableRepeats: List<SelectableRepeat>
        get() = mapOf(
            "Monday" to 1,
            "Tuesday" to 2,
            "Wednesday" to 3,
            "Thursday" to 4,
            "Friday" to 5,
            "Saturday" to 6,
            "Sunday" to 7,
        ).map { (key, value) ->
            SelectableRepeat(key, value, false)
        }

    val defaultSelectableAlertTypes: List<SelectableAlertType>
        get() = AlertType.entries
            .map {
                SelectableAlertType(
                    name = it.format(),
                    selected = it == AlertType.SOUND_AND_VIBRATE
                )
            }

    private fun AlertType.format(): String = when (this) {
        AlertType.SOUND_ONLY -> "Sound only"
        AlertType.SOUND_AND_VIBRATE -> "Sound and vibration"
        AlertType.VIBRATE_ONLY -> "Vibration only"
    }

    val defaultAlarmUI: AlarmUI
        get() = AlarmUI(
            timeUI = TimeUI(8, 0, "8:00 AM"),
            weeklyRepeatUI = WeeklyRepeatUI("", defaultSelectableRepeats),
            label = "",
            alertTypeUI = AlertTypeUI(defaultSelectableAlertTypes),
            ringtoneUI = RingtoneUI(
                "Drip",
                "content://media/internal/audio/media/190?title=Drip&canonical=1"
            ),
            sayItScripts = emptyList()
        )
}