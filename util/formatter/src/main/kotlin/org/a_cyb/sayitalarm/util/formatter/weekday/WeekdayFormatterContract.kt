/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.util.formatter.weekday

interface WeekdayFormatterContract {
    fun formatAbbr(days: Set<Int>): String
    fun formatFull(days: Set<Int>): String
    fun formatFull(vararg day: Int): String
}
