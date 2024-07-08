/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.formatter.enum

import android.content.Context
import org.a_cyb.sayitalarm.entity.AlarmType
import org.a_cyb.sayitalarm.formatter.R

class AlarmTypeFormatter(private val context: Context) : EnumFormatterContract.AlarmTypeFormatter {

    private fun getStringRes(id: Int) = context.getString(id)

    override fun format(alarmType: AlarmType): String = when (alarmType) {
        AlarmType.SAY_IT -> getStringRes(R.string.alarm_type_say_it)
        AlarmType.TYPING -> getStringRes(R.string.alarm_type_typing)
        AlarmType.PUSH_BUTTON -> getStringRes(R.string.alarm_type_push_button)
    }
}
