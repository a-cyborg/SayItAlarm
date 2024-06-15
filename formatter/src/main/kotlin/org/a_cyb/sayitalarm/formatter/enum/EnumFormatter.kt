/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.formatter.enum

import android.content.res.Resources
import org.a_cyb.sayitalarm.entity.AlarmType
import org.a_cyb.sayitalarm.entity.AlertType
import org.a_cyb.sayitalarm.formatter.R

class EnumFormatter : EnumFormatterContract {

    private fun getStringRes(id: Int) = Resources.getSystem().getString(id)

    override fun format(alertType: AlertType): String =
        when (alertType) {
            AlertType.SOUND_ONLY -> getStringRes(R.string.sound_only)
            AlertType.SOUND_AND_VIBRATE -> getStringRes(R.string.sound_and_vibration)
            AlertType.VIBRATE_ONLY -> getStringRes(R.string.vibration_only)
        }

    override fun format(alarmType: AlarmType): String =
        when (alarmType) {
            AlarmType.SAY_IT -> getStringRes(R.string.alarm_type_say_it)
            AlarmType.TYPING -> getStringRes(R.string.alarm_type_typing)
            AlarmType.PUSH_BUTTON -> getStringRes(R.string.alarm_type_push_button)
        }
}
