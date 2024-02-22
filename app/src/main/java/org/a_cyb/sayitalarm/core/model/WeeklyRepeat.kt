package org.a_cyb.sayitalarm.core.model

import java.util.Calendar

data class WeeklyRepeat(val weekdays: Set<Int>) {

    val isRepeating: Boolean
        get() = weekdays.isNotEmpty()

    init {
        require(weekdays.all { it in 1..7 } && weekdays.size <= 7) {
            "Invalid day value. Day must be one of the java.util.Calendar.DAY_OF_WEEK values."
        }
    }

    constructor(vararg days: Int) : this(days.sorted().toSet()) {
        require(days.all { it in 1..7 }) {
            "Invalid day value. Day must be one of the java.util.Calendar.DAY_OF_WEEK values."
        }
    }

    companion object {
        val EVERYDAY = WeeklyRepeat(setOf(Calendar.SUNDAY, Calendar.MONDAY, Calendar.TUESDAY,
            Calendar.WEDNESDAY, Calendar.THURSDAY, Calendar.FRIDAY, Calendar.SATURDAY)
        )
        val NEVER = WeeklyRepeat(emptySet())
    }
}