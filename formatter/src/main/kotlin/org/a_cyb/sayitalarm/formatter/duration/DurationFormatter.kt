/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.formatter.duration

import kotlin.time.Duration
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.minutes
import java.util.Locale
import android.icu.text.DisplayContext
import android.icu.text.NumberFormat
import android.icu.text.RelativeDateTimeFormatter
import android.icu.text.RelativeDateTimeFormatter.Direction
import android.icu.text.RelativeDateTimeFormatter.RelativeUnit
import android.icu.text.RelativeDateTimeFormatter.Style
import android.icu.util.ULocale
import org.a_cyb.sayitalarm.formatter.duration.DurationFormatterContract.FormattedDuration

class DurationFormatter(
    locale: Locale = Locale.getDefault(),
) : DurationFormatterContract {

    private val formatterShort = RelativeDateTimeFormatter.getInstance(
        ULocale.forLocale(locale),
        NumberFormat.getInstance(locale),
        Style.SHORT,
        DisplayContext.CAPITALIZATION_NONE,
    )

    private val formatterLong = RelativeDateTimeFormatter.getInstance(
        ULocale.forLocale(locale),
        NumberFormat.getInstance(locale),
        Style.LONG,
        DisplayContext.CAPITALIZATION_NONE,
    )

    override fun format(duration: Duration): FormattedDuration {
        val hour = duration.inWholeHours.hours
        val minute = duration.minus(hour)

        return formatDuration(hour, minute)
    }

    private fun formatDuration(hour: Duration, minute: Duration): FormattedDuration = when {
        hour >= ONE_HOUR && minute >= ONE_MINUTE -> concatHourAndMinute(formatHour(hour), formatMinutes(minute))
        hour >= ONE_HOUR -> formatHour(hour)
        minute >= ONE_MINUTE -> formatMinutes(minute)
        else -> formatMinutes(0.minutes)
    }

    private fun concatHourAndMinute(hour: FormattedDuration, minute: FormattedDuration): FormattedDuration =
        FormattedDuration(
            short = "${hour.short} ${minute.short}",
            long = "${hour.long} ${minute.long}"
        )

    private fun formatHour(hour: Duration): FormattedDuration {
        val short = formatterShort.format(
            hour.inWholeHours.toDouble(), Direction.NEXT, RelativeUnit.HOURS
        )
        val long = formatterLong.format(
            hour.inWholeHours.toDouble(), Direction.NEXT, RelativeUnit.HOURS
        )

        return FormattedDuration(short.stripAffixation(), long.stripAffixation())
    }

    private fun formatMinutes(minutes: Duration): FormattedDuration {
        val short = formatterShort.format(
            minutes.inWholeMinutes.toDouble(), Direction.NEXT, RelativeUnit.MINUTES
        )
        val long = formatterLong.format(
            minutes.inWholeMinutes.toDouble(), Direction.NEXT, RelativeUnit.MINUTES
        )

        return FormattedDuration(short.stripAffixation(), long.stripAffixation())
    }

    private fun String.stripAffixation(): String =
        if (this.first().isDigit()) {
            this.stripSuffix().removeSuffix(".")
        } else {
            this.stripPrefix().removeSuffix(".")
        }

    private fun String.stripSuffix(): String = this.split(" ").dropLast(1).joinToString(" ")
    private fun String.stripPrefix(): String = this.split(" ").drop(1).joinToString(" ")

    companion object {
        private val ONE_HOUR = 1.hours
        private val ONE_MINUTE = 1.minutes
    }
}
