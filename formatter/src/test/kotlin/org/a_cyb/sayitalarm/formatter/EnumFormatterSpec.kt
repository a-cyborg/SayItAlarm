/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.formatter

import android.content.Context
import android.content.res.Resources
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.unmockkStatic
import org.a_cyb.sayitalarm.entity.AlarmType
import org.a_cyb.sayitalarm.entity.AlertType
import org.a_cyb.sayitalarm.formatter.enum.AlarmTypeFormatter
import org.a_cyb.sayitalarm.formatter.enum.AlertTypeFormatter
import org.a_cyb.sayitalarm.util.mustBe
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class EnumFormatterSpec {

    @Before
    fun setup() {
        mockkStatic(Resources::class)
        setupMockk()
    }

    @After
    fun teatDown() {
        unmockkStatic(Resources::class)
    }

    private fun setupMockk() {
        val context: Context = ApplicationProvider.getApplicationContext()

        every { Resources.getSystem() } answers {
            mockk {
                every { getString(any()) } answers {
                    context.getString(arg(0) as Int)
                }
            }
        }
    }

    @Test
    fun `Given AlertTypeFormatter when format is called it maps to formatted string`() {
        // Given
        val formatter = AlertTypeFormatter()

        // When & Then
        formatter.format(AlertType.SOUND_ONLY) mustBe "Sound only"
        formatter.format(AlertType.SOUND_AND_VIBRATE) mustBe "Sound and vibration"
        formatter.format(AlertType.VIBRATE_ONLY) mustBe "Vibration only"
    }

    @Test
    fun `Given AlarmTypeFormatter when format is called it maps to formatted string`() {
        // Given
        val formatter = AlarmTypeFormatter()

        // When & Then
        formatter.format(AlarmType.SAY_IT) mustBe "Turn off the alarm by reading scripts out."
        formatter.format(AlarmType.TYPING) mustBe "Turn off the alarm by typing."
        formatter.format(AlarmType.PUSH_BUTTON) mustBe "Turn off the alarm by pushing button."
    }
}
