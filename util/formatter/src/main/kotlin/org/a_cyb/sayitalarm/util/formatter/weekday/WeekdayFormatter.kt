/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.util.formatter.weekday

import java.util.Locale
import android.content.Context
import android.icu.text.DateFormatSymbols
import android.icu.text.ListFormatter
import org.a_cyb.sayitalarm.util.formatter.R

class WeekdayFormatter(
    private val context: Context,
    private val locale: Locale = Locale.getDefault(),
) : WeekdayFormatterContract {

    private val dateFormatSymbols = DateFormatSymbols.getInstance(locale)

    private fun getStringRes(id: Int) = context.getString(id)

    override fun formatAbbr(days: Set<Int>): String = format(days.convertToIcu(), isAbbr = true)
    override fun formatFull(days: Set<Int>): String = format(days.convertToIcu(), isAbbr = false)
    override fun formatFull(vararg day: Int): String = formatFull(day.toSet())

    private fun Set<Int>.convertToIcu(): Set<Int> =
        this.map { it.convertToIcu() }
            .toSortedSet()

    private fun Int.convertToIcu(): Int =
        when (this) {
            7 -> 1
            else -> this + 1
        }

    private fun format(days: Set<Int>, isAbbr: Boolean): String {
        return when (days) {
            NEVER -> ""
            EVERYDAY -> getStringRes(R.string.every_day)
            WEEKDAY -> getStringRes(R.string.every_weekday)
            WEEKEND -> getStringRes(R.string.every_weekend)
            else -> if (isAbbr) days.toAbbrNames() else days.toFullNames()
        }
    }

    private fun Set<Int>.toAbbrNames(): String =
        map { dateFormatSymbols.shortWeekdays[it] }
            .concatDayNames()

    private fun Set<Int>.toFullNames(): String =
        map { dateFormatSymbols.weekdays[it] }
            .concatDayNames()

    private fun List<String>.concatDayNames(): String =
        when (size) {
            0 -> ""
            1 -> first()
            else -> joinDayNames(this)
        }

    private fun joinDayNames(dayNames: List<String>): String = ListFormatter.getInstance(locale).format(dayNames)

    companion object {
        val EVERYDAY = (1..7).toSet()
        val WEEKDAY = (2..6).toSet()
        val WEEKEND = setOf(1, 7)
        val NEVER = emptySet<Int>()
    }
}
