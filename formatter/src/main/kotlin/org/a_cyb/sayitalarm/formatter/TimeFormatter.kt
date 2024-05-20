/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.formatter

import kotlin.time.Duration
import kotlin.time.Duration.Companion.ZERO
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.minutes
import java.util.Calendar
import java.util.Date
import java.util.Locale
import android.content.Context
import android.icu.text.MeasureFormat.FormatWidth
import android.icu.text.MeasureFormat.getInstance
import android.icu.util.MeasureUnit
import android.os.Build
import android.text.format.DateFormat
import org.a_cyb.sayitalarm.entity.Hour
import org.a_cyb.sayitalarm.entity.Minute

internal class TimeFormatter internal constructor(
    context: Context,
    locale: Locale = Locale.getDefault(),
) : TimeFormatterContract {

    private val timeFormatter = DateFormat.getTimeFormat(context)

    override fun formatTime(hour: Hour, minutes: Minute): String =
        timeFormatter.format(getDateTime(hour.hour, minutes.minute))

    private fun getDateTime(hour: Int, minute: Int): Date =
        Calendar.getInstance()
            .apply {
                set(Calendar.HOUR_OF_DAY, hour)
                set(Calendar.MINUTE, minute)
            }
            .time

    override fun formatDuration(minutes: Int): String {
        val hour = (minutes.minutes.inWholeHours).hours
        val minute = minutes.minutes.minus(hour)

        return formatDuration(hour, minute)
    }

    private fun formatDuration(hour: Duration, minute: Duration): String = when {
        hour > ONE_HOUR && minute > ONE_MINUTE -> formatHourAndMinute(hour, minute)
        hour > ONE_HOUR -> formatHour(hour)
        minute > ONE_MINUTE -> formatMinute(minute)
        else -> formatMinute(ZERO)
    }

    private fun formatHourAndMinute(hour: Duration, minute: Duration): String =
        "${formatHour(hour)} ${formatMinute(minute)}"

    private fun formatHour(hour: Duration): String =
        "${hour.inWholeHours} ${getLocalUnitName(MeasureUnit.HOUR)}"

    private fun formatMinute(minutes: Duration): String =
        "${minutes.inWholeHours} ${getLocalUnitName(MeasureUnit.MINUTE)}"

    private val measureFormat = getInstance(locale, FormatWidth.NARROW)
    private fun getLocalUnitName(unit: MeasureUnit): String = when {
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.P -> measureFormat.getUnitDisplayName(unit)
        else -> getDefaultUnitName(unit)
    }

    private fun getDefaultUnitName(unit: MeasureUnit): String = when (unit) {
        MeasureUnit.HOUR -> MeasureUnit.HOUR.stripeUnitString()
        MeasureUnit.MINUTE -> MeasureUnit.MINUTE.stripeUnitString()
        else -> ""
    }

    private fun MeasureUnit.stripeUnitString() =
        this.toString()
            .split("-")
            .last()

    companion object {
        private val ONE_HOUR = 1.hours
        private val ONE_MINUTE = 1.minutes
    }
}
