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
import org.a_cyb.sayitalarm.formatter.enum.EnumFormatter
import org.a_cyb.sayitalarm.util.mustBe
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config

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
    fun `Given AlertType format is called it maps to display name in english`() {
        // Given
        val formatter = EnumFormatter()

        // When & Then
        formatter.format(AlertType.SOUND_ONLY) mustBe "Sound only"
        formatter.format(AlertType.SOUND_AND_VIBRATE) mustBe "Sound and vibration"
        formatter.format(AlertType.VIBRATE_ONLY) mustBe "Vibration only"
    }

    @Config(qualifiers = "ko")
    @Test
    fun `Given AlertType format is called it maps to display name in korean`() {
        // Given
        val formatter = EnumFormatter()

        // When & Then
        formatter.format(AlertType.SOUND_ONLY) mustBe "벨소리 모드"
        formatter.format(AlertType.SOUND_AND_VIBRATE) mustBe "벨소리와 진동 모드"
        formatter.format(AlertType.VIBRATE_ONLY) mustBe "진동 모드"
    }

    @Test
    fun `Given AlarmType format is called it maps to display name in english`() {
        // Given
        val formatter = EnumFormatter()

        // When & Then
        formatter.format(AlarmType.SAY_IT) mustBe "Turn off the alarm by reading scripts out."
        formatter.format(AlarmType.TYPING) mustBe "Turn off the alarm by typing."
        formatter.format(AlarmType.PUSH_BUTTON) mustBe "Turn off the alarm by pushing button."
    }

    @Config(qualifiers = "ko")
    @Test
    fun `Given AlarmType format is called it maps to display name in korean`() {
        // Given
        val formatter = EnumFormatter()

        // When & Then
        formatter.format(AlarmType.SAY_IT) mustBe "스크립트를 읽어 알람 끄기."
        formatter.format(AlarmType.TYPING) mustBe "타이핑으로 알람 끄기."
        formatter.format(AlarmType.PUSH_BUTTON) mustBe "버튼을 눌러 알람 끄기."
    }
}
