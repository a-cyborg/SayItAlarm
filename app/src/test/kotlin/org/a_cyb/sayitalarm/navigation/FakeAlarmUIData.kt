/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.navigation

import org.a_cyb.sayitalarm.entity.AlertType
import org.a_cyb.sayitalarm.presentation.contracts.AlarmPanelContract.AlarmUI
import org.a_cyb.sayitalarm.presentation.contracts.AlarmPanelContract.AlertTypeUI
import org.a_cyb.sayitalarm.presentation.contracts.AlarmPanelContract.RingtoneUI
import org.a_cyb.sayitalarm.presentation.contracts.AlarmPanelContract.SelectableAlertType
import org.a_cyb.sayitalarm.presentation.contracts.AlarmPanelContract.SelectableRepeat
import org.a_cyb.sayitalarm.presentation.contracts.AlarmPanelContract.TimeUI
import org.a_cyb.sayitalarm.presentation.contracts.AlarmPanelContract.WeeklyRepeatUI

object FakeAlarmUIData {

    val defaultSelectableRepeats: List<SelectableRepeat>
        get() = mapOf(
            "Sunday" to 1,
            "Monday" to 2,
            "Tuesday" to 3,
            "Wednesday" to 4,
            "Thursday" to 5,
            "Friday" to 6,
            "Saturday" to 7,
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