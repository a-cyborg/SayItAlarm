package org.a_cyb.sayitalarm.core.model

class CombinedMinutesTest {

//    @Test
//    fun getHour() {
//        // One minute before midnight.
//        val testOne = CombinedMinutes(1439)
//        assertEquals(testOne.hour, 23)
//
//        val testTwo = CombinedMinutes(1361)
//        assertEquals(testTwo.hour, 22)
//
//        val testThree = CombinedMinutes(333)
//        assertEquals(testThree.hour, 5)
//
//        val testFour = CombinedMinutes(0)
//        assertEquals(testFour.hour, 0)
//    }
//
//    @Test
//    fun getMinute() {
//        val testOne = CombinedMinutes(1439)
//        assertEquals(testOne.minute, 59)
//
//        val testTwo = CombinedMinutes(1361)
//        assertEquals(testTwo.minute, 41)
//
//        val testThree = CombinedMinutes(333)
//        assertEquals(testThree.minute, 33)
//
//        val testFour = CombinedMinutes(0)
//        assertEquals(testFour.minute, 0)
//    }
//
//    @Test
//    fun getValue() {
//        val testOne = CombinedMinutes(23, 59)
//        assertEquals(testOne.value, 1439)
//
//        val testTwo = CombinedMinutes(22, 41)
//        assertEquals(testTwo.value, 1361)
//
//        val testThree = CombinedMinutes(5, 33)
//        assertEquals(testThree.value, 333)
//
//        val testFour = CombinedMinutes(0, 0)
//        assertEquals(testFour.value, 0)
//    }
//
//    @Test
//    fun getMeridian() {
//        val testOne = CombinedMinutes(23, 59)
//        assertEquals(testOne.meridian, Meridian.PM)
//
//        val testTwo = CombinedMinutes(22, 41)
//        assertEquals(testTwo.meridian, Meridian.PM)
//
//        val testThree= CombinedMinutes(12, 0)
//        assertEquals(testThree.meridian, Meridian.PM)
//
//        val testFour = CombinedMinutes(5, 33)
//        assertEquals(testFour.meridian, Meridian.AM)
//
//        val testFive = CombinedMinutes(0, 0)
//        assertEquals(testFive.value, 0)
//    }
//
//    @Test
//    fun getHourIn12hourFormat() {
//        val testOne = CombinedMinutes(23, 59)
//        assertEquals(testOne.hourIn12hourFormat, 11)
//
//        val testTwo = CombinedMinutes(22, 41)
//        assertEquals(testTwo.hourIn12hourFormat, 10)
//
//        val testThree= CombinedMinutes(12, 0)
//        assertEquals(testThree.hourIn12hourFormat, 12)
//
//        val testFour = CombinedMinutes(5, 33)
//        assertEquals(testFour.hourIn12hourFormat, 5)
//
//        val testFive = CombinedMinutes(0, 0)
//        assertEquals(testFive.hourIn12hourFormat, 12)
//    }
//
//    @Test
//    fun init_with_invalid_value_should_throw_exception() {
//        // TODO: Check if right message is printed.
//        // Cases of invalid combined minutes.
//        assertFailsWith<IllegalArgumentException>(
//            message = "Invalid combined minutes value. Combined minutes must be within the range of 0 to 1439.",
//            block = { CombinedMinutes(-4) }
//        )
//        assertFailsWith<IllegalArgumentException> { CombinedMinutes(1568) }
//        assertFailsWith<IllegalArgumentException> { CombinedMinutes(1440) }
//
//        // Cases of invalid hour.
//        assertFailsWith<IllegalArgumentException>(
//            message = "Invalid hour value. Hour must be within the range of 0 to 23.",
//            block = { CombinedMinutes(28, 21)}
//        )
//        assertFailsWith<IllegalArgumentException> { CombinedMinutes(-8, 33) }
//        assertFailsWith<IllegalArgumentException> { CombinedMinutes(24, 21) }
//
//
//        // Cases of invalid minute.
//        assertFailsWith<IllegalArgumentException>(
//            message = "Invalid minute value. Minute must be within the range of 0 to 59.",
//            block = { CombinedMinutes(4, 80) }
//        )
//        assertFailsWith<IllegalArgumentException> { CombinedMinutes(23, -21) }
//        assertFailsWith<IllegalArgumentException> { CombinedMinutes(23, 60) }
//    }
}

