package org.a_cyb.sayitalarm.core.model

import junit.framework.TestCase.assertFalse
import org.junit.Test

class WeeklyRepeatTest {

    @Test
    fun isRepeating() {

        val noRepeat = WeeklyRepeat()
        assertFalse(noRepeat.isRepeating)
    }

    @Test
    fun getWeekdays() {
    }
}