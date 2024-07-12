/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.presentation.formatter.enum

import android.content.Context
import org.a_cyb.sayitalarm.entity.AlertType
import org.a_cyb.sayitalarm.presentation.formatter.R

class AlertTypeFormatter(private val context: Context) : EnumFormatterContract.AlertTypeFormatter {

    private fun getStringRes(id: Int) = context.getString(id)

    override fun format(alertType: AlertType): String = when (alertType) {
        AlertType.SOUND_ONLY -> getStringRes(R.string.sound_only)
        AlertType.SOUND_AND_VIBRATE -> getStringRes(R.string.sound_and_vibration)
        AlertType.VIBRATE_ONLY -> getStringRes(R.string.vibration_only)
    }
}
