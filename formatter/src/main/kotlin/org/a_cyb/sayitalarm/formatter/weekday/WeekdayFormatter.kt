/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.formatter.weekday

import java.util.Locale
import android.content.res.Resources
import android.icu.text.DateFormatSymbols
import org.a_cyb.sayitalarm.formatter.R

class WeekdayFormatter(
    locale: Locale = Locale.getDefault(),
) : WeekdayFormatterContract {

    private val dateFormatSymbols = DateFormatSymbols.getInstance(locale)
    override fun formatAbbr(days: Set<Int>): String {
        return format(days.toSortedSet(), isAbbr = true)
    }

    override fun formatFull(days: Set<Int>): String {
        return format(days.toSortedSet(), isAbbr = false)
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

    private fun getStringRes(id: Int) = Resources.getSystem().getString(id)

    private fun Set<Int>.toAbbrNames(): String {
        val displayNames = this.map {
            dateFormatSymbols.shortWeekdays[it]
        }

        return concatDayNames(displayNames)
    }

    private fun Set<Int>.toFullNames(): String {
        val displayNames = this.map {
            dateFormatSymbols.weekdays[it]
        }

        return concatDayNames(displayNames)
    }

    private fun concatDayNames(dayNames: List<String>): String {
        when (dayNames.size) {
            0 -> return ""
            1 -> return dayNames.first()
            else -> {
                val first = dayNames.dropLast(1).joinToString(", ")
                val last = "$lastItemSeparator${dayNames.last()}"

                return "$first$last"
            }
        }
    }

    private val lastItemSeparator = when(locale) {
        Locale.ENGLISH -> ", and "
        else -> ", "
    }

    companion object {
        val EVERYDAY = (1..7).toSet()
        val WEEKDAY = (2..6).toSet()
        val WEEKEND = setOf(1, 7)
        val NEVER = emptySet<Int>()
    }
}
