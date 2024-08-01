/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.presentation.formatter

import java.time.DayOfWeek.FRIDAY
import java.time.DayOfWeek.MONDAY
import java.time.DayOfWeek.SATURDAY
import java.time.DayOfWeek.SUNDAY
import java.time.DayOfWeek.THURSDAY
import java.time.DayOfWeek.TUESDAY
import java.time.DayOfWeek.WEDNESDAY
import java.util.Locale
import android.content.Context
import android.content.res.Resources
import android.icu.text.DateFormatSymbols
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.unmockkStatic
import org.a_cyb.sayitalarm.presentation.formatter.weekday.WeekdayFormatter
import org.a_cyb.sayitalarm.presentation.formatter.weekday.WeekdayFormatterContract
import org.a_cyb.sayitalarm.util.fulfils
import org.a_cyb.sayitalarm.util.mustBe
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config

@RunWith(AndroidJUnit4::class)
@Config(sdk = [33])
class WeekdayFormatterSpec {

    private val context: Context = ApplicationProvider.getApplicationContext()
    private fun getStringRes(id: Int) = context.getString(id)

    @Before
    fun setup() {
        mockkStatic(DateFormatSymbols::class, Resources::class)
    }

    @After
    fun teatDown() {
        unmockkStatic(DateFormatSymbols::class, Resources::class)
    }

    private fun setupMock(locale: Locale) {
        var dateFormatSymbolsFake: com.ibm.icu.text.DateFormatSymbols?

        every { DateFormatSymbols.getInstance(locale) } answers {
            mockk {
                dateFormatSymbolsFake = com.ibm.icu.text.DateFormatSymbols.getInstance((arg(0) as Locale))

                every { shortWeekdays } answers {
                    dateFormatSymbolsFake!!.shortWeekdays
                }

                every { weekdays } answers {
                    dateFormatSymbolsFake!!.weekdays
                }
            }
        }

        every { Resources.getSystem() } answers {
            mockk {
                every { getString(any()) } answers {
                    getStringRes(arg(0) as Int)
                }
            }
        }
    }

    @Test
    fun `It fulfills WeekdayFormatter`() {
        val locale = Locale.ENGLISH

        setupMock(locale)

        WeekdayFormatter(context, locale) fulfils WeekdayFormatterContract::class
    }

    @Test
    fun `When formatAbbr is called it maps to abbreviated day names in english`() {
        // Given
        val locale = Locale.ENGLISH

        setupMock(locale)

        val formatter = WeekdayFormatter(context, locale)

        // When
        val formatted = formatter.formatAbbr(setOf(MONDAY.value, WEDNESDAY.value, FRIDAY.value))

        // Then
        formatted mustBe "Mon, Wed, and Fri"
    }

    @Test
    fun `Given formatFull is called it maps to full day names in english`() {
        // Given
        val locale = Locale.ENGLISH

        setupMock(locale)

        val formatter = WeekdayFormatter(context, locale)

        // When
        val formatted = formatter.formatFull(setOf(TUESDAY.value, THURSDAY.value, SATURDAY.value))

        // Then
        formatted mustBe "Tuesday, Thursday, and Saturday"
    }

    @Test
    fun `Given formatFull is called it maps to full day name in english`() {
        // Given
        val locale = Locale.ENGLISH

        setupMock(locale)

        val formatter = WeekdayFormatter(context, locale)

        // When
        val formatted = formatter.formatFull(setOf(FRIDAY.value))

        // Then
        formatted mustBe "Friday"
    }

    @Test
    fun `Given formatAbbr is called it formats to every day in english`() {
        // Given
        val locale = Locale.ENGLISH

        setupMock(locale)

        val formatter = WeekdayFormatter(context, locale)

        // When
        val formatted = formatter.formatAbbr(
            setOf(
                MONDAY.value,
                TUESDAY.value,
                WEDNESDAY.value,
                THURSDAY.value,
                FRIDAY.value,
                SATURDAY.value,
                SUNDAY.value,
            )
        )

        // Then
        formatted mustBe "every day"
    }

    @Test
    fun `Given formatFull is called it formats to every weekday in english`() {
        // Given
        val locale = Locale.ENGLISH

        setupMock(locale)

        val formatter = WeekdayFormatter(context, locale)

        // when
        val formatted = formatter.formatFull(
            setOf(
                MONDAY.value,
                TUESDAY.value,
                WEDNESDAY.value,
                THURSDAY.value,
                FRIDAY.value,
            )
        )

        // then
        formatted mustBe "every weekday"
    }

    @Test
    fun `Given formatFull is called it formats to every weekend in english`() {
        // Given
        val locale = Locale.ENGLISH

        setupMock(locale)

        val formatter = WeekdayFormatter(context, locale)

        // when
        val formatted = formatter.formatFull(setOf(SATURDAY.value, SUNDAY.value))

        // Then
        formatted mustBe "every weekend"
    }

    @Test
    fun `Given formatFull is called it returns empty string`() {
        // Given
        val locale = Locale.ENGLISH

        setupMock(locale)

        val formatter = WeekdayFormatter(context, locale)

        // When
        val formatted = formatter.formatFull(emptySet())

        // Then
        formatted mustBe ""
    }

    @Test
    @Config(qualifiers = "ko")
    fun `When formatAbbr is called it maps to abbreviated day names in korean`() {
        // Given
        val locale = Locale.KOREAN

        setupMock(locale)

        val formatter = WeekdayFormatter(context, locale)

        // When
        val formatted = formatter.formatAbbr(setOf(MONDAY.value, WEDNESDAY.value, FRIDAY.value))

        // Then
        formatted mustBe "월, 수 및 금"
    }

    @Test
    @Config(qualifiers = "ko")
    fun `When formatFull is called it maps to full day names in korean`() {
        // Given
        val locale = Locale.KOREAN

        setupMock(locale)

        val formatter = WeekdayFormatter(context, locale)

        // When
        val formatted = formatter.formatFull(setOf(MONDAY.value, WEDNESDAY.value, FRIDAY.value))

        // Then
        formatted mustBe "월요일, 수요일 및 금요일"
    }

    @Test
    @Config(qualifiers = "ko")
    fun `When formatAbbr is called it maps to every day in korean `() {
        // Given
        val locale = Locale.KOREAN

        setupMock(locale)

        val formatter = WeekdayFormatter(context, locale)

        // When
        val formatted = formatter.formatAbbr(
            setOf(
                MONDAY.value,
                TUESDAY.value,
                WEDNESDAY.value,
                THURSDAY.value,
                FRIDAY.value,
                SATURDAY.value,
                SUNDAY.value
            )
        )

        // Then
        formatted mustBe "매일"
    }

    @Test
    @Config(qualifiers = "ko")
    fun `Given formatFull is called it formats to every weekday in korean`() {
        // Given
        val locale = Locale.KOREAN

        setupMock(locale)

        val formatter = WeekdayFormatter(context, locale)

        // When
        val formatted = formatter.formatFull(
            setOf(
                MONDAY.value,
                TUESDAY.value,
                WEDNESDAY.value,
                THURSDAY.value,
                FRIDAY.value
            )
        )

        // Then
        formatted mustBe "주중"
    }

    @Test
    @Config(qualifiers = "ko")
    fun `Given formatFull is called it formats to every weekend in korean`() {
        // Given
        val locale = Locale.KOREAN

        setupMock(locale)

        val formatter = WeekdayFormatter(context, locale)

        // When
        val formatted = formatter.formatFull(setOf(SUNDAY.value, SATURDAY.value))

        // Then
        formatted mustBe "주말"
    }
}
