/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.presentation.viewmodel.fake

import kotlin.time.Duration
import kotlin.time.Duration.Companion.hours
import org.a_cyb.sayitalarm.formatter.duration.DurationFormatterContract
import org.a_cyb.sayitalarm.formatter.duration.DurationFormatterContract.FormattedDuration

class DurationFormatterFake : DurationFormatterContract {
    override fun format(duration: Duration): FormattedDuration {
        val hour = duration.inWholeHours
        val min = duration.minus(hour.hours).inWholeMinutes

        return when {
            (hour >= 1 && min >= 1) -> concatFormattedHourAndMin(formatHour(hour), formatMin(min))
            hour >= 1 -> formatHour(hour)
            min >= 1 -> formatMin(min)
            else -> formatMin(0)
        }
    }

    private fun concatFormattedHourAndMin(formattedHour: FormattedDuration, formattedMin: FormattedDuration) =
        FormattedDuration(
            short = "${formattedHour.short} ${formattedMin.short}",
            long = "${formattedHour.long} ${formattedMin.long}"
        )

    private fun formatHour(hour: Long): FormattedDuration = FormattedDuration(
        short = "$hour hr",
        long = "$hour hours"
    )

    private fun formatMin(min: Long): FormattedDuration = FormattedDuration(
        short = "$min min",
        long = "$min minutes"
    )
}
