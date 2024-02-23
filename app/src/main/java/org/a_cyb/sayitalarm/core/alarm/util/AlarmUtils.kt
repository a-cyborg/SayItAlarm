package org.a_cyb.sayitalarm.core.alarm.util

import org.a_cyb.sayitalarm.core.model.Alarm
import org.a_cyb.sayitalarm.core.model.WeeklyRepeat
import java.util.Calendar

fun Alarm.getNextAlarmTime(currentTime: Calendar): Calendar {
    val alarm = this
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
            alarm.weeklyRepeat.getNextDayOfWeek(nextAlarmTime[Calendar.DAY_OF_WEEK])
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
 * @return int value for [Calendar.DAY_OF_WEEK] field.
 */
fun WeeklyRepeat.getNextDayOfWeek(today: Int): Int {
    val nextDay = this.weekdays.firstOrNull { it >= today }

    return nextDay ?: this.weekdays.first()
}
