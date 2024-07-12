/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.presentation.formatter.time

import java.util.Calendar
import java.util.Date
import android.content.Context
import android.text.format.DateFormat
import org.a_cyb.sayitalarm.entity.Hour
import org.a_cyb.sayitalarm.entity.Minute

internal class TimeFormatter internal constructor(
    context: Context,
) : TimeFormatterContract {

    private val timeFormatter = DateFormat.getTimeFormat(context)

    override fun format(hour: Hour, minutes: Minute): String =
        timeFormatter.format(getInstance(hour.hour, minutes.minute))

    private fun getInstance(hour: Int, minute: Int): Date =
        Calendar.getInstance()
            .apply {
                set(Calendar.HOUR_OF_DAY, hour)
                set(Calendar.MINUTE, minute)
            }
            .time
}
