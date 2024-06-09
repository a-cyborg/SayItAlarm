/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.formatter

import java.util.Locale
import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.a_cyb.sayitalarm.entity.Hour
import org.a_cyb.sayitalarm.entity.Minute
import org.a_cyb.sayitalarm.formatter.time.TimeFormatter
import org.a_cyb.sayitalarm.formatter.time.TimeFormatterContract
import org.a_cyb.sayitalarm.util.fulfils
import org.a_cyb.sayitalarm.util.mustBe
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.shadows.ShadowSettings

@RunWith(AndroidJUnit4::class)
class TimeFormatterSpec {

    private val context: Context = ApplicationProvider.getApplicationContext()

    @Test
    fun `It fulfils TimeFormatterContract`() {
        TimeFormatter(context) fulfils TimeFormatterContract::class
    }

    @Test
    fun `When format is called it maps to a 24-hour time format string`() {
        // Given
        ShadowSettings.set24HourTimeFormat(true)
        val formatter = TimeFormatter(context)
        val hour = Hour(23)
        val minutes = Minute(50)

        // When
        val formattedTime = formatter.format(hour, minutes)

        // Then
        formattedTime mustBe "23:50"
    }

    @Test
    fun `When format is called with less then 12 hours it maps to a zero padded 24-hour time format string`() {
        // Given
        ShadowSettings.set24HourTimeFormat(true)
        val formatter = TimeFormatter(context)
        val hour = Hour(4)
        val minutes = Minute(0)

        // When
        val formattedTime = formatter.format(hour, minutes)

        // Then
        formattedTime mustBe "04:00"
    }

    @Test
    fun `When format is called with a hour before noon hour it maps to a 12-hour format string in english`() {
        // Given
        ShadowSettings.set24HourTimeFormat(false)
        val formatter = TimeFormatter(context)
        val hour = Hour(8)
        val minutes = Minute(2)

        // When
        val formattedTime = formatter.format(hour, minutes)

        // Then
        formattedTime mustBe "8:02 AM"
    }

    @Test
    fun `When format is called with a hour after noon hour it maps to a 12-hour format string in english`() {
        // Given
        ShadowSettings.set24HourTimeFormat(false)
        val formatter = TimeFormatter(context)
        val hour = Hour(23)
        val minutes = Minute(33)

        // When
        val formattedTime = formatter.format(hour, minutes)

        // Then
        formattedTime mustBe "11:33 PM"
    }

    @Test
    fun `When format is called with a hour before noon hour it maps to a 12-hour format string in korean`() {
        // Given
        ShadowSettings.set24HourTimeFormat(false)
        Locale.setDefault(Locale.KOREAN)

        val formatter = TimeFormatter(context)
        val hour = Hour(8)
        val minutes = Minute(2)

        // When
        val formattedTime = formatter.format(hour, minutes)

        // Then
        formattedTime mustBe "8:02 오전"
    }

    @Test
    fun `When format is called with a hour after noon hour it maps to a 12-hour format string in korean`() {
        // Given
        ShadowSettings.set24HourTimeFormat(false)
        Locale.setDefault(Locale.KOREAN)

        val formatter = TimeFormatter(context)
        val hour = Hour(23)
        val minutes = Minute(33)

        // When
        val formattedTime = formatter.format(hour, minutes)

        // Then
        formattedTime mustBe "11:33 오후"
    }
}
