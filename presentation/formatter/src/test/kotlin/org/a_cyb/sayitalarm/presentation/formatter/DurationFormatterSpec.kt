/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.presentation.formatter

import kotlin.time.Duration
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.minutes
import java.util.Locale
import android.icu.text.NumberFormat
import android.icu.text.RelativeDateTimeFormatter
import android.icu.util.ULocale
import com.ibm.icu.text.DisplayContext
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.unmockkStatic
import org.a_cyb.sayitalarm.presentation.formatter.duration.DurationFormatter
import org.a_cyb.sayitalarm.presentation.formatter.duration.DurationFormatterContract
import org.a_cyb.sayitalarm.util.fulfils
import org.a_cyb.sayitalarm.util.mustBe
import org.junit.After
import org.junit.Before
import org.junit.Test
import com.ibm.icu.text.NumberFormat as IcuNumberFormat
import com.ibm.icu.text.RelativeDateTimeFormatter as IcuRelativeDateTimeFormatter
import com.ibm.icu.util.ULocale as IcuULocale

class DurationFormatterSpec {

    @Before
    fun setup() {
        mockkStatic(RelativeDateTimeFormatter::class, NumberFormat::class, ULocale::class)
    }

    @After
    fun teatDown() {
        unmockkStatic(RelativeDateTimeFormatter::class, NumberFormat::class, ULocale::class)
    }

    private fun setupMock(locale: Locale) {
        var icuLocale: IcuULocale? = null
        var icuNumberFormat: IcuNumberFormat? = null

        every { ULocale.forLocale(any()) } answers {
            mockk {
                icuLocale = IcuULocale.forLocale(arg(0) as Locale)
            }
        }

        every { NumberFormat.getInstance(locale) } answers {
            mockk {
                icuNumberFormat = IcuNumberFormat.getInstance(arg(0) as Locale)
            }
        }

        every { RelativeDateTimeFormatter.getInstance(any(), any(), any(), any()) } answers {
            mockk {
                val formatterFake = IcuRelativeDateTimeFormatter.getInstance(
                    icuLocale,
                    icuNumberFormat,
                    IcuRelativeDateTimeFormatter.Style.valueOf(
                        (arg(2) as RelativeDateTimeFormatter.Style).name
                    ),
                    DisplayContext.CAPITALIZATION_NONE,
                )

                every { format(any(), any(), any()) } answers {
                    formatterFake.format(
                        arg(0) as Double,
                        IcuRelativeDateTimeFormatter.Direction.valueOf(
                            (arg(1) as RelativeDateTimeFormatter.Direction).name
                        ),
                        IcuRelativeDateTimeFormatter.RelativeUnit.valueOf(
                            (arg(2) as RelativeDateTimeFormatter.RelativeUnit).name
                        )
                    )
                }
            }
        }
    }

    @Test
    fun `It fulfills DurationFormatContract`() {
        val locale = Locale.ENGLISH
        setupMock(locale)

        DurationFormatter(Locale.ENGLISH) fulfils DurationFormatterContract::class
    }

    @Test
    fun `When format is called with less then an hour duration it maps to a minute-only string in english`() {
        // Given
        val locale = Locale.ENGLISH

        setupMock(locale)

        val duration = 30.minutes

        // When
        val (short, long) = DurationFormatter(locale).format(duration)

        // Then
        short mustBe "30 min"
        long mustBe "30 minutes"
    }

    @Test
    fun `When format is called with an exact hour duration it maps to a hour-only string in english`() {
        // Given
        val locale = Locale.ENGLISH

        setupMock(locale)

        val duration = 3.hours

        // When
        val (short, long) = DurationFormatter(locale).format(duration)

        // Then
        short mustBe "3 hr"
        long mustBe "3 hours"
    }

    @Test
    fun `When format is called with duration more then an hour it maps to a hour and minutes string in english`() {
        // Given
        val locale = Locale.ENGLISH

        setupMock(locale)

        val duration = 3.hours.plus(33.minutes)

        // When
        val (short, long) = DurationFormatter(locale).format(duration)

        // Then
        short mustBe "3 hr 33 min"
        long mustBe "3 hours 33 minutes"
    }

    @Test
    fun `When format is called with 0 duration it maps to an zero minutes string in english`() {
        // Given
        val locale = Locale.ENGLISH

        setupMock(locale)

        val duration = Duration.ZERO

        // When
        val (short, long) = DurationFormatter(locale).format(duration)

        // Then
        short mustBe "0 min"
        long mustBe "0 minutes"
    }

    @Test
    fun `When format is called with less then an hour duration it maps to a minute-only string in korean`() {
        // Given
        val locale = Locale.KOREAN

        setupMock(locale)

        val duration = 30.minutes

        // When
        val (short, long) = DurationFormatter(locale).format(duration)

        // Then
        short mustBe "30분"
        long mustBe "30분"
    }

    @Test
    fun `When format is called with an exact hour duration it maps to a hour-only string in korean`() {
        // Given
        val locale = Locale.KOREAN

        setupMock(locale)

        val duration = 3.hours

        // When
        val (short, long) = DurationFormatter(locale).format(duration)

        // Then
        short mustBe "3시간"
        long mustBe "3시간"
    }

    @Test
    fun `When format is called with duration more then an hour it maps to a hour and minutes string in korean`() {
        // Given
        val locale = Locale.KOREAN

        setupMock(locale)

        val duration = 3.hours.plus(33.minutes)

        // When
        val (short, long) = DurationFormatter(locale).format(duration)

        // Then
        short mustBe "3시간 33분"
        long mustBe "3시간 33분"
    }

    @Test
    fun `When format is called with 0 duration it maps to an zero minutes string in korean`() {
        // Given
        val locale = Locale.KOREAN

        setupMock(locale)

        val duration = Duration.ZERO

        // When
        val (short, long) = DurationFormatter(locale).format(duration)

        // Then
        short mustBe "0분"
        long mustBe "0분"
    }
}
