/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

@file:Suppress("MagicNumber")

package org.a_cyb.sayitalarm.entity

data class WeeklyRepeat(val weekdays: Set<Day>) {
    constructor(vararg days: Day) :
        this(days.toSet())

    companion object {
        val EVERYDAY = WeeklyRepeat(Mon, Tue, Wed, Thu, Fri, Sat, Sun)
    }
}

interface Day {
    val code: Int
}

object Sun : Day {
    override val code = 1
}

object Mon : Day {
    override val code = 2
}

object Tue : Day {
    override val code = 3
}

object Wed : Day {
    override val code = 4
}

object Thu : Day {
    override val code = 5
}

object Fri : Day {
    override val code = 6
}

object Sat : Day {
    override val code = 7
}
