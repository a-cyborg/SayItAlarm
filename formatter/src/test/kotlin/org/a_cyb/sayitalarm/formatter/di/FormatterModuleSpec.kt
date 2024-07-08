/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.formatter.di

import kotlin.test.assertNotNull
import java.util.Locale
import android.content.Context
import android.icu.text.DateFormatSymbols
import android.icu.text.NumberFormat
import android.icu.text.RelativeDateTimeFormatter
import android.icu.util.ULocale
import android.text.format.DateFormat
import android.text.format.DateFormat.getTimeFormat
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import org.a_cyb.sayitalarm.formatter.duration.DurationFormatterContract
import org.a_cyb.sayitalarm.formatter.enum.EnumFormatterContract
import org.a_cyb.sayitalarm.formatter.time.TimeFormatterContract
import org.a_cyb.sayitalarm.formatter.weekday.WeekdayFormatterContract
import org.junit.Test
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.koinApplication

class FormatterModuleSpec {

    @Test
    fun `It injects DurationFormatter`() {
        // Mockk
        mockkStatic(RelativeDateTimeFormatter::class, NumberFormat::class, ULocale::class)
        every { ULocale.forLocale(any()) } answers { mockk {} }
        every { NumberFormat.getInstance(any() as Locale) } answers { mockk {} }
        every { RelativeDateTimeFormatter.getInstance(any(), any(), any(), any()) } answers { mockk() }

        // Given
        val koinApp = koinApplication {
            modules(
                formatterModule,
            )
        }

        // When
        val formatter = koinApp.koin.get<DurationFormatterContract>()

        // Then
        assertNotNull(formatter)

        clearAllMocks()
    }

    @Test
    fun `It injects AlertTypeFormatter`() {
        // Given
        val context: Context = mockk()
        val koinApp = koinApplication {
            androidContext(context)
            modules(
                formatterModule,
            )
        }

        // When
        val formatter = koinApp.koin.get<EnumFormatterContract.AlertTypeFormatter>()

        // Then
        assertNotNull(formatter)
    }

    @Test
    fun `It injects AlarmTypeFormatter`() {
        // Given
        val context: Context = mockk(relaxed = true)
        val koinApp = koinApplication {
            androidContext(context)
            modules(
                formatterModule,
            )
        }

        // When
        val formatter = koinApp.koin.get<EnumFormatterContract.AlarmTypeFormatter>()

        // Then
        assertNotNull(formatter)
    }

    @Test
    fun `It injects TimeFormatter`() {
        // Mockk
        mockkStatic(DateFormat::class)
        every { getTimeFormat(any()) } answers { mockk() }

        // Given
        val context: Context = mockk(relaxed = true)
        val koinApp = koinApplication {
            androidContext(context)
            modules(
                formatterModule,
            )
        }

        // When
        val formatter = koinApp.koin.get<TimeFormatterContract>()

        // Then
        assertNotNull(formatter)

        clearAllMocks()
    }

    @Test
    fun `It injects WeekdayFormatter`() {
        // Mockk
        mockkStatic(ULocale::class, DateFormatSymbols::class)
        every { ULocale.forLocale(any()) } answers { mockk {} }
        every { DateFormatSymbols.getInstance(any() as Locale) } answers { mockk() }

        // Given
        val koinApp = koinApplication {
            modules(
                formatterModule,
            )
        }

        // When
        val formatter = koinApp.koin.get<WeekdayFormatterContract>()

        // Then
        assertNotNull(formatter)

        clearAllMocks()
    }
}