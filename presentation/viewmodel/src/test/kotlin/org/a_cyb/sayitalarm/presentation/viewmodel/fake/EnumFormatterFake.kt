/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.presentation.viewmodel.fake

import org.a_cyb.sayitalarm.entity.AlarmType
import org.a_cyb.sayitalarm.entity.AlertType
import org.a_cyb.sayitalarm.formatter.enum.EnumFormatterContract

class EnumFormatterFake : EnumFormatterContract {

    override fun format(alertType: AlertType): String {
        return when (alertType) {
            AlertType.SOUND_ONLY -> "Sound only"
            AlertType.SOUND_AND_VIBRATE -> "Sound and vibration"
            AlertType.VIBRATE_ONLY -> "Vibration only"
        }
    }

    override fun format(alarmType: AlarmType): String = ""
}