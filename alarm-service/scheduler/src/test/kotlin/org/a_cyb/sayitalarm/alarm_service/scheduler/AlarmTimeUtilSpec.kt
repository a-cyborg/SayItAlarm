/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.alarm_service.scheduler

import androidx.test.ext.junit.runners.AndroidJUnit4
import io.mockk.every
import io.mockk.mockkStatic
import io.mockk.unmockkAll
import org.a_cyb.sayitalarm.entity.Hour
import org.a_cyb.sayitalarm.entity.Minute
import org.a_cyb.sayitalarm.entity.WeeklyRepeat
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.runner.RunWith
import java.time.DayOfWeek
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import kotlin.test.Test

@RunWith(AndroidJUnit4::class)
class AlarmTimeUtilSpec {
    @Before
    fun setup() {
        mockkStatic(LocalDate::class, LocalTime::class)
    }

    @After
    fun teatDown() {
        unmockkAll()
    }

    @Test
    fun getNextAlarmTimeInMills_RepeatAlarm_AlarmTimeForTodayHasPassed_AvailableRepeatDayInCurrentWeek() {
        // Given
        val alarmWeeklyRepeat = WeeklyRepeat(DayOfWeek.MONDAY.value, DayOfWeek.WEDNESDAY.value, DayOfWeek.FRIDAY.value)
        // Current clock: 2024-July-18(Thursday) 3:33
        every { LocalDate.now() } returns LocalDate.of(2024, 7, 18)
        every { LocalTime.now() } returns LocalTime.of(3, 33)

        // When
        val actual = getNextAlarmTimeInMills(Hour(13), Minute(33), alarmWeeklyRepeat)

        // Then
        val actualInstant = Instant.ofEpochMilli(actual).atZone(ZoneId.systemDefault())
        // Expected: 2024-July-19(Friday) 3:33
        assertEquals(2024, actualInstant.year)
        assertEquals(7, actualInstant.month.value)
        assertEquals(19, actualInstant.dayOfMonth)
        assertEquals(DayOfWeek.FRIDAY, actualInstant.dayOfWeek)
        assertEquals(13, actualInstant.hour)
        assertEquals(33, actualInstant.minute)
        assertEquals(0, actualInstant.second)
        assertEquals(0, actualInstant.nano)
    }

    @Test
    fun getNextAlarmTime_RepeatAlarm_AlarmTimeForTodayHasPassed_NoAvailableRepeatDayInCurrentWeek() {
        // Given
        val alarmWeeklyRepeat = WeeklyRepeat(DayOfWeek.THURSDAY.value)
        // Current clock: 2024-July-18(Thursday) 9:00
        every { LocalDate.now() } returns LocalDate.of(2024, 7, 18)
        every { LocalTime.now() } returns LocalTime.of(9, 0)

        // When
        val actual = getNextAlarmTimeInMills(Hour(7), Minute(0), alarmWeeklyRepeat)

        // Then
        val actualInstant = Instant.ofEpochMilli(actual).atZone(ZoneId.systemDefault())
        // Expected clock: 2024-July-25(Thursday) 7:00
        assertEquals(2024, actualInstant.year)
        assertEquals(7, actualInstant.month.value)
        assertEquals(25, actualInstant.dayOfMonth)
        assertEquals(DayOfWeek.THURSDAY, actualInstant.dayOfWeek)
        assertEquals(7, actualInstant.hour)
        assertEquals(0, actualInstant.minute)
        assertEquals(0, actualInstant.second)
        assertEquals(0, actualInstant.nano)
    }

    @Test
    fun getNextAlarmTime_RepeatAlarm_AlarmTimeForToday_HasNotYetPassed() {
        // Given
        val weeklyRepeat = WeeklyRepeat(DayOfWeek.WEDNESDAY.value)

        // Current clock: 2024-July-17(Wednesday) 5:00
        every { LocalDate.now() } returns LocalDate.of(2024, 7, 17)
        every { LocalTime.now() } returns LocalTime.of(5, 0)

        // When
        val actual = getNextAlarmTimeInMills(Hour(5), Minute(33), weeklyRepeat)

        // Then
        val actualInstant = Instant.ofEpochMilli(actual).atZone(ZoneId.systemDefault())
        // Expected: 2024-July-17(Wednesday) 5:33
        assertEquals(2024, actualInstant.year)
        assertEquals(7, actualInstant.month.value)
        assertEquals(17, actualInstant.dayOfMonth)
        assertEquals(DayOfWeek.WEDNESDAY, actualInstant.dayOfWeek)
        assertEquals(5, actualInstant.hour)
        assertEquals(33, actualInstant.minute)
    }

    @Test
    fun getNextAlarmTime_NoRepeatAlarm_AlarmTimeForToday_HasPassed() {
        // Given
        // Current clock: 2024-November-17(Sunday) 9:00
        every { LocalDate.now() } returns LocalDate.of(2024, 11, 17)
        every { LocalTime.now() } returns LocalTime.of(9, 0)

        // When
        val actual = getNextAlarmTimeInMills(Hour(8), Minute(3), WeeklyRepeat())

        // Then
        val actualInstant = Instant.ofEpochMilli(actual).atZone(ZoneId.systemDefault())
        // Expected: 2024-November-18(Monday) 8:03
        assertEquals(2024, actualInstant.year)
        assertEquals(11, actualInstant.month.value)
        assertEquals(18, actualInstant.dayOfMonth)
        assertEquals(DayOfWeek.MONDAY, actualInstant.dayOfWeek)
        assertEquals(8, actualInstant.hour)
        assertEquals(3, actualInstant.minute)
    }

    @Test
    fun getNextAlarmTime_NoRepeatAlarm_AlarmTimeForToday_HasNotYetPassed() {
        // Given
        // Current clock: 2024-November-17(Sunday) 6:00
        every { LocalDate.now() } returns LocalDate.of(2024, 11, 17)
        every { LocalTime.now() } returns LocalTime.of(6, 0)

        // When
        val actual = getNextAlarmTimeInMills(Hour(8), Minute(3), WeeklyRepeat())

        // Then
        val actualInstant = Instant.ofEpochMilli(actual).atZone(ZoneId.systemDefault())
        // Expected: 2024-November-17(Monday) 8:03
        assertEquals(2024, actualInstant.year)
        assertEquals(11, actualInstant.month.value)
        assertEquals(17, actualInstant.dayOfMonth)
        assertEquals(DayOfWeek.SUNDAY, actualInstant.dayOfWeek)
        assertEquals(8, actualInstant.hour)
        assertEquals(3, actualInstant.minute)
    }

    @Test
    fun getSnoozeTimeInMills_5min() {
        // Given
        mockkStatic(LocalDateTime::class)

        // Current clock: 2024-December-3(Tuesday) 14:20
        every { LocalDateTime.now() } returns LocalDateTime.of(2024, 12, 3, 14, 20)

        // When
        val actual = getSnoozeTimeInMills(5)

        // Then
        val actualInstant = Instant.ofEpochMilli(actual).atZone(ZoneId.systemDefault())
        // Expected: 2024-December-3(Tuesday) 14:25
        assertEquals(2024, actualInstant.year)
        assertEquals(12, actualInstant.month.value)
        assertEquals(3, actualInstant.dayOfMonth)
        assertEquals(14, actualInstant.hour)
        assertEquals(25, actualInstant.minute)
        assertEquals(0, actualInstant.second)
        assertEquals(0, actualInstant.nano)
    }

    @Test
    fun getSnoozeTimeInMills_15min() {
        // Given
        mockkStatic(LocalDateTime::class)

        // Current clock: 2024-December-3(Tuesday) 7:50
        every { LocalDateTime.now() } returns LocalDateTime.of(2024, 12, 3, 7, 50)

        // When
        val actual = getSnoozeTimeInMills(15)

        // Then
        val actualInstant = Instant.ofEpochMilli(actual).atZone(ZoneId.systemDefault())
        // Expected: 2024-December-3(Tuesday) 8:05
        assertEquals(2024, actualInstant.year)
        assertEquals(12, actualInstant.month.value)
        assertEquals(3, actualInstant.dayOfMonth)
        assertEquals(8, actualInstant.hour)
        assertEquals(5, actualInstant.minute)
    }
}
