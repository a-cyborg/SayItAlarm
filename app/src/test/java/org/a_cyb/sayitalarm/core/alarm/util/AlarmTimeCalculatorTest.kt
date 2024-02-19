package org.a_cyb.sayitalarm.core.alarm.util

import org.a_cyb.sayitalarm.core.model.WeeklyRepeat
import org.junit.Test
import java.util.Calendar
import kotlin.test.assertEquals

class NextAlarmTimeCalculatorTest {
    @Test
    fun getNextDayOfWeek_returnsEarliestNextDayOfWeek() {
        // If today is Monday and WeeklyRepeat is everyday then should return Tuesday.
        val weeklyRepeat = WeeklyRepeat.EVERYDAY

        val nextDayOfWeek = NextAlarmTimeCalculator.getNextDayOfWeek(
            today = Calendar.MONDAY,
            weeklyRepeat = weeklyRepeat
        )
        assertEquals(
            expected = Calendar.TUESDAY,
            actual = nextDayOfWeek,
            message = "If today is Monday and WeeklyRepeat is Everyday then returns Calendar.Tuesday"
        )
    }
}