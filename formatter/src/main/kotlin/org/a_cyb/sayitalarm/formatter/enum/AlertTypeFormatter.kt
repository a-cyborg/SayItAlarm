/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.formatter.enum

import org.a_cyb.sayitalarm.entity.AlertType
import org.a_cyb.sayitalarm.formatter.R

class AlertTypeFormatter: EnumFormatterContract.AlertTypeFormatter {

    override fun format(alertType: AlertType): String = when (alertType) {
        AlertType.SOUND_ONLY -> getStringRes(R.string.sound_only)
        AlertType.SOUND_AND_VIBRATE -> getStringRes(R.string.sound_and_vibration)
        AlertType.VIBRATE_ONLY -> getStringRes(R.string.vibration_only)
    }
}
