package org.a_cyb.sayitalarm.core.alarm.util

import org.a_cyb.sayitalarm.core.model.WeeklyRepeat

object AlarmTimeCalculator {
    fun getNextDayOfWeek(today: Int, weeklyRepeat: WeeklyRepeat): Int {
        val nextDay = weeklyRepeat.weekdays.firstOrNull { it > today }

        return nextDay ?: weeklyRepeat.weekdays.first()
    }

//    fun getNextAlarmTime(alarm: Alarm): Calendar {
//    }
}