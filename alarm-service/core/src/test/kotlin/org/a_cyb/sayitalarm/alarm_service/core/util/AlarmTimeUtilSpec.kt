/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.alarm_service.core.util

import androidx.test.ext.junit.runners.AndroidJUnit4
import io.mockk.every
import io.mockk.mockkStatic
import io.mockk.unmockkAll
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import kotlin.test.Test
import org.a_cyb.sayitalarm.entity.WeeklyRepeat
import org.a_cyb.sayitalarm.util.mustBe
import org.junit.After
import org.junit.Before
import org.junit.runner.RunWith

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
    fun getNextAlarmTime_RepeatAlarm_AlarmTimeForToday_HasPassed_AvailableRepeatDayInCurrentWeek() {
        // Given
        val alarmTime = LocalTime.of(13, 33)
        val alarmWeeklyRepeat = WeeklyRepeat(DayOfWeek.MONDAY.value, DayOfWeek.WEDNESDAY.value, DayOfWeek.FRIDAY.value)

        // Current clock: 2024-July-18(Thursday) 3:33
        every { LocalDate.now() } returns LocalDate.of(2024, 7, 18)
        every { LocalTime.now() } returns LocalTime.of(3, 33)

        // When
        val actual = getNextAlarmTime(alarmTime, alarmWeeklyRepeat)
        val expected = LocalDateTime.of(2024, 7, 19, 13, 33)

        // Then
        actual mustBe expected
    }

    @Test
    fun getNextAlarmTime_RepeatAlarm_AlarmTimeForToday_HasPassed_NoAvailableRepeatDayInCurrentWeek() {
        // Given
        val alarmTime = LocalTime.of(7, 0)
        val alarmWeeklyRepeat = WeeklyRepeat(DayOfWeek.THURSDAY.value)

        // Current clock: 2024-July-18(Thursday) 9:00
        every { LocalDate.now() } returns LocalDate.of(2024, 7, 18)
        every { LocalTime.now() } returns LocalTime.of(9, 0)

        // When
        val actual = getNextAlarmTime(alarmTime, alarmWeeklyRepeat)
        val expected = LocalDateTime.of(2024, 7, 25, 7, 0)

        // Then
        actual mustBe expected
    }

    @Test
    fun getNextAlarmTime_RepeatAlarm_AlarmTimeForToday_HasNotYetPassed() {
        // Given
        val alarmTime = LocalTime.of(7, 0)
        val alarmWeeklyRepeat = WeeklyRepeat(DayOfWeek.THURSDAY.value)

        // Current clock: 2024-July-28(Thursday) 6:00
        every { LocalDate.now() } returns LocalDate.of(2024, 7, 18)
        every { LocalTime.now() } returns LocalTime.of(6, 0)

        // When
        val actual = getNextAlarmTime(alarmTime, alarmWeeklyRepeat)
        val expected = LocalDateTime.of(2024, 7, 18, 7, 0)

        // Then
        actual mustBe expected
    }

    @Test
    fun getNextAlarmTime_NoRepeatAlarm_AlarmTimeForToday_HasPassed() {
        // Given
        val alarmTime = LocalTime.of(7, 0)
        val alarmWeeklyRepeat = WeeklyRepeat()

        // Current clock: 2024-July-18(Thursday) 9:00
        every { LocalDate.now() } returns LocalDate.of(2024, 7, 18)
        every { LocalTime.now() } returns LocalTime.of(9, 0)

        // When
        val actual = getNextAlarmTime(alarmTime, alarmWeeklyRepeat)
        val expected = LocalDateTime.of(2024, 7, 19, 7, 0)

        // Then
        actual mustBe expected
    }
}
