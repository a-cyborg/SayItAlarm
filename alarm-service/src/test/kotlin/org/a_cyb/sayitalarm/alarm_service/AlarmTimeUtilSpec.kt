/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.alarm_service

import kotlin.test.Test
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import androidx.test.ext.junit.runners.AndroidJUnit4
import io.mockk.every
import io.mockk.mockkStatic
import io.mockk.unmockkAll
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
    fun `When getNextAlarmTime is called it returns next alarm time`() {
        // Given
        every { LocalDate.now() } returns LocalDate.of(2024, 7, 18)
        every { LocalTime.now() } returns LocalTime.of(3, 33)

        // When
        val actual = getNextAlarmTime(
            LocalTime.of(13, 33),
            WeeklyRepeat(
                DayOfWeek.MONDAY.value,
                DayOfWeek.WEDNESDAY.value,
                DayOfWeek.FRIDAY.value,
            )
        )

        val expected = LocalDateTime.of(2024, 7, 19, 13, 33)

        // Then
        actual mustBe expected
    }

    @Test
    fun `When getNextAlarmTime is called and the alarm time has not yet passed it returns next alarm time`() {
        // Given
        every { LocalDate.now() } returns LocalDate.of(2024, 7, 18)
        every { LocalTime.now() } returns LocalTime.of(6, 0)

        // When
        val actual = getNextAlarmTime(
            LocalTime.of(7, 0),
            WeeklyRepeat(DayOfWeek.THURSDAY.value)
        )

        val expected = LocalDateTime.of(2024, 7, 18, 7, 0)

        // Then
        actual mustBe expected
    }

    @Test
    fun `When getNextAlarmTime is called and the alarm time for today has passed it returns next alarm time`() {
        // Given
        every { LocalDate.now() } returns LocalDate.of(2024, 7, 18)
        every { LocalTime.now() } returns LocalTime.of(9, 0)

        // When
        val actual = getNextAlarmTime(
            LocalTime.of(7, 0),
            WeeklyRepeat(DayOfWeek.THURSDAY.value)
        )

        val expected = LocalDateTime.of(2024, 7, 25, 7, 0)

        // Then
        actual mustBe expected
    }

    @Test
    fun `When getNextAlarmTime is called and the alarm is one-time alarm it returns next alarm time`() {
        // Given
        every { LocalDate.now() } returns LocalDate.of(2024, 7, 18)
        every { LocalTime.now() } returns LocalTime.of(9, 0)

        // When
        val actual = getNextAlarmTime(
            LocalTime.of(7, 0),
            WeeklyRepeat()
        )

        val expected = LocalDateTime.of(2024, 7, 19, 7, 0)

        // Then
        actual mustBe expected
    }
}
