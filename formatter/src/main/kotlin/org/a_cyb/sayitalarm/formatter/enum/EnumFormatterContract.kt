/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.formatter.enum

import android.content.res.Resources
import org.a_cyb.sayitalarm.entity.AlarmType
import org.a_cyb.sayitalarm.entity.AlertType

interface EnumFormatterContract {

    fun getStringRes(id: Int) = Resources.getSystem().getString(id)

    fun interface AlertTypeFormatter : EnumFormatterContract {
        fun format(alertType: AlertType): String
    }

    fun interface AlarmTypeFormatter : EnumFormatterContract {
        fun format(alarmType: AlarmType): String
    }
}
