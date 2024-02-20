package org.a_cyb.sayitalarm.core.alarm.util

import org.a_cyb.sayitalarm.core.model.Alarm
import org.a_cyb.sayitalarm.core.model.WeeklyRepeat
import java.util.Calendar

object AlarmTimeCalculator {
    fun getNextAlarmTime(alarm: Alarm, currentTime: Calendar): Calendar {
        val nextAlarmTime = Calendar.getInstance(currentTime.timeZone).apply {
            set(Calendar.YEAR, currentTime[Calendar.YEAR])
            set(Calendar.MONTH, currentTime[Calendar.MONTH])
            set(Calendar.DAY_OF_MONTH, currentTime[Calendar.DAY_OF_MONTH])
            set(Calendar.HOUR_OF_DAY, alarm.combinedMinutes.hour)
            set(Calendar.MINUTE, alarm.combinedMinutes.minute)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }

        // If next alarm time has already passed in the current day then add 1 day.
        if (nextAlarmTime.timeInMillis <= currentTime.timeInMillis) {
            nextAlarmTime.add(Calendar.DAY_OF_YEAR, 1)
        }

        if (alarm.weeklyRepeat.isRepeating) {
            nextAlarmTime.set(
                Calendar.DAY_OF_WEEK,
                getNextDayOfWeek(nextAlarmTime[Calendar.DAY_OF_WEEK], alarm.weeklyRepeat)
            )

            // If the next day of the week has already passed in the current day, then add 1 week.
            if (nextAlarmTime.timeInMillis <= currentTime.timeInMillis) {
                nextAlarmTime.add(Calendar.WEEK_OF_YEAR, 1)
            }
        }

        return nextAlarmTime
    }

    /**
     * It returns the earliest day of week from today based on weekly repeat.
     * Note: Returns today if today is a valid day in weekly repeat.
     *
     * @param today a day of week value represented by the constants in [Calendar], for example [Calendar.MONDAY].
     * @param weeklyRepeat [WeeklyRepeat]
     *
     * @return int value for [Calendar.DAY_OF_WEEK] field.
     */
    fun getNextDayOfWeek(today: Int, weeklyRepeat: WeeklyRepeat): Int {
        val nextDay = weeklyRepeat.weekdays.firstOrNull { it >= today }

        return nextDay ?: weeklyRepeat.weekdays.first()
    }
}