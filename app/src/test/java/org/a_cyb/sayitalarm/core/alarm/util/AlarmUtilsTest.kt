package org.a_cyb.sayitalarm.core.alarm.util

import org.a_cyb.sayitalarm.core.model.CombinedMinutes
import org.a_cyb.sayitalarm.core.model.WeeklyRepeat
import org.a_cyb.sayitalarm.util.getRandomAlarm
import org.junit.Test
import java.util.Calendar
import kotlin.test.assertEquals

class AlarmUtilsTest {
    @Test
    fun getNextAlarmTime_testCase1() {
        /* When the current time is 2024-FEB-20(TUE) 16:14, the alarm time is set for 8am, and it repeats weekly on [MON, WED, FRI]. getNextAlarmTime() returns a Calendar instance for 2024-FEB-21(WED) 8:00. */
        val testCurrentTime = Calendar.getInstance()
            .setCurrentTime(2024, Calendar.FEBRUARY, 20, 16, 14)

        val testAlarm = getRandomAlarm(
            combinedMinutes = CombinedMinutes(hour = 8, minute = 0),
            weeklyRepeat = WeeklyRepeat(Calendar.MONDAY, Calendar.WEDNESDAY, Calendar.FRIDAY),
        )

        val nextAlarmTime = testAlarm.getNextAlarmTime(testCurrentTime)

        assertEquals(2024, nextAlarmTime[Calendar.YEAR])
        assertEquals(Calendar.FEBRUARY, nextAlarmTime[Calendar.MONTH])
        assertEquals(21, nextAlarmTime[Calendar.DAY_OF_MONTH])
        assertEquals(Calendar.WEDNESDAY, nextAlarmTime[Calendar.DAY_OF_WEEK])
        assertEquals(8, nextAlarmTime[Calendar.HOUR_OF_DAY])
        assertEquals(0, nextAlarmTime[Calendar.MINUTE])
        assertEquals(0, nextAlarmTime[Calendar.SECOND])
        assertEquals(0, nextAlarmTime[Calendar.MILLISECOND])
    }

    @Test
    fun getNextAlarmTime_testCase2() {
        /* When the current time is 2024-JAN-31(WED) 23:30, the alarm time is set for 15:30, and it repeats weekly on everyday. getNextAlarmTime() returns a Calendar instance for 2024-FEB-1(THU) 15:30. */

        val testCurrentTime = Calendar.getInstance()
            .setCurrentTime(2024, Calendar.JANUARY, 31, 22, 30)

        val testAlarm = getRandomAlarm(
            combinedMinutes = CombinedMinutes(hour = 15, minute = 30),
            weeklyRepeat = WeeklyRepeat.EVERYDAY,
        )

        val nextAlarmTime = testAlarm.getNextAlarmTime(testCurrentTime)

        assertEquals(2024, nextAlarmTime[Calendar.YEAR])
        assertEquals(Calendar.FEBRUARY, nextAlarmTime[Calendar.MONTH])
        assertEquals(1, nextAlarmTime[Calendar.DAY_OF_MONTH])
        assertEquals(Calendar.THURSDAY, nextAlarmTime[Calendar.DAY_OF_WEEK])
        assertEquals(15, nextAlarmTime[Calendar.HOUR_OF_DAY])
        assertEquals(30, nextAlarmTime[Calendar.MINUTE])
        assertEquals(0, nextAlarmTime[Calendar.SECOND])
        assertEquals(0, nextAlarmTime[Calendar.MILLISECOND])
    }

    @Test
    fun getNextAlarmTime_testCase3() {
        /* When the current time is 2024-FEB-15(THU) 03:22, the alarm time is set for 07:00, and it repeats weekly on Sun. getNextAlarmTime() returns a Calendar instance for 2024-FEB-18(SUN) 07:00. */

        val testCurrentTime = Calendar.getInstance()
            .setCurrentTime(2024, Calendar.FEBRUARY, 15, 3, 22)

        val testAlarm = getRandomAlarm(
            combinedMinutes = CombinedMinutes(hour = 7, minute = 0),
            weeklyRepeat = WeeklyRepeat(Calendar.SUNDAY),
        )

        val nextAlarmTime = testAlarm.getNextAlarmTime(testCurrentTime)

        assertEquals(2024, nextAlarmTime[Calendar.YEAR])
        assertEquals(Calendar.FEBRUARY, nextAlarmTime[Calendar.MONTH])
        assertEquals(18, nextAlarmTime[Calendar.DAY_OF_MONTH])
        assertEquals(Calendar.SUNDAY, nextAlarmTime[Calendar.DAY_OF_WEEK])
        assertEquals(7, nextAlarmTime[Calendar.HOUR_OF_DAY])
        assertEquals(0, nextAlarmTime[Calendar.MINUTE])
        assertEquals(0, nextAlarmTime[Calendar.SECOND])
        assertEquals(0, nextAlarmTime[Calendar.MILLISECOND])
    }

    @Test
    fun getNextAlarmTime_testCase4() {
        /* When the current time is 2024-MAR-9(SAT) 11:11, the alarm time is set for 06:00, and it's not repeat. getNextAlarmTime() returns a Calendar instance for 2024-MAR-10(SUN) 06:00. */

        val testCurrentTime = Calendar.getInstance()
            .setCurrentTime(2024, Calendar.MARCH, 9, 11, 11)

        val testAlarm = getRandomAlarm(
            combinedMinutes = CombinedMinutes(hour = 6, minute = 0),
            weeklyRepeat = WeeklyRepeat.NEVER,
        )

        val nextAlarmTime = testAlarm.getNextAlarmTime(testCurrentTime)

        assertEquals(2024, nextAlarmTime[Calendar.YEAR])
        assertEquals(Calendar.MARCH, nextAlarmTime[Calendar.MONTH])
        assertEquals(10, nextAlarmTime[Calendar.DAY_OF_MONTH])
        assertEquals(Calendar.SUNDAY, nextAlarmTime[Calendar.DAY_OF_WEEK])
        assertEquals(6, nextAlarmTime[Calendar.HOUR_OF_DAY])
        assertEquals(0, nextAlarmTime[Calendar.MINUTE])
        assertEquals(0, nextAlarmTime[Calendar.SECOND])
        assertEquals(0, nextAlarmTime[Calendar.MILLISECOND])
    }

    @Test
    fun getNextDayOfWeek_returnsEarliestNextDayOfWeek() {
        // Today is Monday and WeeklyRepeat.Everyday then should [Calendar.MONDAY].
        assertEquals(
            expected = Calendar.MONDAY,
            actual = (WeeklyRepeat.EVERYDAY)
                .getNextDayOfWeek(Calendar.MONDAY)
        )

        // Today is Wednesday and weeklyRepeat[Sat, Sun] should return [Calendar.Saturday]
        assertEquals(
            expected = Calendar.SATURDAY,
            actual =  WeeklyRepeat(Calendar.SATURDAY, Calendar.SUNDAY)
                .getNextDayOfWeek(today = Calendar.WEDNESDAY)
        )

        // Today is Sunday and weeklyRepeat[Sun] should return [Calendar.Sunday]
        assertEquals(
            expected = Calendar.SUNDAY,
            actual = WeeklyRepeat(Calendar.SUNDAY)
                .getNextDayOfWeek(today = Calendar.SUNDAY)
        )

        // Today is Tuesday and weeklyRepeat[Mon, Wed, Fri] should return [Calendar.Wednesday]
        assertEquals(
            expected = Calendar.WEDNESDAY,
            actual =  WeeklyRepeat(Calendar.MONDAY, Calendar.WEDNESDAY, Calendar.FRIDAY)
                .getNextDayOfWeek(today = Calendar.TUESDAY)
        )
    }

    private fun Calendar.setCurrentTime(
        year: Int,
        month: Int,
        dayOfMonth: Int,
        hour: Int,
        minute: Int,
    ) = this.apply {
        set(Calendar.YEAR, year)
        set(Calendar.MONTH, month)
        set(Calendar.DAY_OF_MONTH, dayOfMonth)
        set(Calendar.HOUR_OF_DAY, hour)
        set(Calendar.MINUTE, minute)
    }
}