/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.presentation.viewmodel.fake

import org.a_cyb.sayitalarm.util.formatter.weekday.WeekdayFormatter
import org.a_cyb.sayitalarm.util.formatter.weekday.WeekdayFormatterContract

class WeekdayFormatterFake : WeekdayFormatterContract {

    private val abbrDayNames = listOf("Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat")
    private val fullDayNames = listOf("Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday")

    override fun formatAbbr(days: Set<Int>): String = format(days, true)
    override fun formatFull(days: Set<Int>): String = format(days, false)
    override fun formatFull(vararg day: Int): String = formatFull(day.toSortedSet())

    private fun format(days: Set<Int>, isAbbr: Boolean): String {
        return when (days) {
            WeekdayFormatter.NEVER -> ""
            WeekdayFormatter.EVERYDAY -> "every day"
            WeekdayFormatter.WEEKDAY -> "every weekday"
            WeekdayFormatter.WEEKEND -> "every weekend"
            else -> if (isAbbr) days.toAbbrNames() else days.toFullNames()
        }
    }

    private fun Set<Int>.toAbbrNames(): String =
        concatDayNames(map { abbrDayNames[it - 1] })

    private fun Set<Int>.toFullNames(): String =
        concatDayNames(map { fullDayNames[it - 1] })

    private fun concatDayNames(dayNames: List<String>): String =
        when (dayNames.size) {
            0 -> ""
            1 -> dayNames.first()
            else -> {
                val first = dayNames.dropLast(1).joinToString(", ")
                val last = ", and ${dayNames.last()}"

                "$first$last"
            }
        }
}
