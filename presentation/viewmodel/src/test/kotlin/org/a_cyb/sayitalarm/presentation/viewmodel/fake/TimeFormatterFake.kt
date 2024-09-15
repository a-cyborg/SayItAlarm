/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.presentation.viewmodel.fake

import org.a_cyb.sayitalarm.entity.Hour
import org.a_cyb.sayitalarm.entity.Minute
import org.a_cyb.sayitalarm.util.formatter.time.TimeFormatterContract
import java.text.SimpleDateFormat
import java.util.Calendar

class TimeFormatterFake : TimeFormatterContract {

    private val timeFormat12H = SimpleDateFormat("h:mm a")

    override fun format(hour: Hour, minutes: Minute): String {
        val instance = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, hour.hour)
            set(Calendar.MINUTE, minutes.minute)
        }

        return timeFormat12H.format(instance.time)
    }
}
