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
import org.a_cyb.sayitalarm.entity.CombinedMinutes
import org.a_cyb.sayitalarm.util.fulfils
import org.a_cyb.sayitalarm.util.mustBe
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.shadows.ShadowSettings
import tech.antibytes.kfixture.fixture
import tech.antibytes.kfixture.kotlinFixture

@RunWith(AndroidJUnit4::class)
class TimeFormatterSpec {

    private val context: Context = ApplicationProvider.getApplicationContext()
    private val fixture = kotlinFixture()

    @Test
    fun `It fulfils TimeFormatterContract`() {
        TimeFormatter(context) fulfils TimeFormatterContract::class
    }

    @Test
    fun `It formats combinedMinutes in 24 hour format`() {
        // Given
        ShadowSettings.set24HourTimeFormat(true)
        val formatter = TimeFormatter(context)

        // When
        val minutes = CombinedMinutes(fixture.fixture(range = 0..1439))
        val formattedTime = formatter.formatTime(minutes)

        // Then
        formattedTime mustBe String.format("%02d:%02d", minutes.hour, minutes.minute)
    }

    @Test
    fun `Given local Eng It formats combinedMinutes in 12 hour with AM`() {
        // Given
        ShadowSettings.set24HourTimeFormat(false)
        val formatter = TimeFormatter(context, Locale.ENGLISH)

        // When
        val minutes = CombinedMinutes(fixture.fixture(range = 0..719))
        val formattedTime = formatter.formatTime(minutes)

        // Then
        formattedTime mustBe String.format("%d:%02d AM", minutes.hour, minutes.minute)
    }

    @Test
    fun `Given local Eng it formats combinedMinutes in 12 hour with PM`() {
        // Given
        ShadowSettings.set24HourTimeFormat(false)
        val formatter = TimeFormatter(context, Locale.ENGLISH)

        // When
        val minutes = CombinedMinutes(fixture.fixture(range = 720..1439))
        val formattedTime = formatter.formatTime(minutes)

        // Then
        formattedTime mustBe String.format("%d:%02d PM", minutes.hour % 12, minutes.minute)
    }

    @Test
    fun `It formats in duration format`() {
    }


}