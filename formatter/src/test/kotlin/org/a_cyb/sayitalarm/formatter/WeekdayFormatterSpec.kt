/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.formatter

import java.util.Calendar.FRIDAY
import java.util.Calendar.MONDAY
import java.util.Calendar.SATURDAY
import java.util.Calendar.SUNDAY
import java.util.Calendar.THURSDAY
import java.util.Calendar.TUESDAY
import java.util.Calendar.WEDNESDAY
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
import org.a_cyb.sayitalarm.formatter.weekday.WeekdayFormatter
import org.a_cyb.sayitalarm.formatter.weekday.WeekdayFormatterContract
import org.a_cyb.sayitalarm.util.fulfils
import org.a_cyb.sayitalarm.util.mustBe
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config

@RunWith(AndroidJUnit4::class)
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
        val formatted = formatter.formatAbbr(setOf(MONDAY, WEDNESDAY, FRIDAY))

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
        val formatted = formatter.formatFull(setOf(TUESDAY, THURSDAY, SATURDAY))

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
        val formatted = formatter.formatFull(setOf(FRIDAY))

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
            setOf(MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY, SUNDAY)
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
        val formatted = formatter.formatFull(setOf(FRIDAY, THURSDAY, WEDNESDAY, TUESDAY, MONDAY))

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
        val formatted = formatter.formatFull(setOf(SATURDAY, SUNDAY))

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
        val formatted = formatter.formatAbbr(setOf(MONDAY, WEDNESDAY, FRIDAY))

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
        val formatted = formatter.formatFull(setOf(MONDAY, WEDNESDAY, FRIDAY))

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
            setOf(MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY, SUNDAY)
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
        val formatted = formatter.formatFull(setOf(MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY))

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
        val formatted = formatter.formatFull(setOf(SUNDAY, SATURDAY))

        // Then
        formatted mustBe "주말"
    }
}
