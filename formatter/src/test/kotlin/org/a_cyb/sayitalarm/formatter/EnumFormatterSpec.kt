/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.formatter

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.a_cyb.sayitalarm.entity.AlarmType
import org.a_cyb.sayitalarm.entity.AlertType
import org.a_cyb.sayitalarm.formatter.enum.AlarmTypeFormatter
import org.a_cyb.sayitalarm.formatter.enum.AlertTypeFormatter
import org.a_cyb.sayitalarm.util.mustBe
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class EnumFormatterSpec {

    @Test
    fun `Given AlertTypeFormatter when format is called it maps to formatted string`() {
        // Given
        val context: Context = ApplicationProvider.getApplicationContext()
        val formatter = AlertTypeFormatter(context)

        // When & Then
        formatter.format(AlertType.SOUND_ONLY) mustBe "Sound only"
        formatter.format(AlertType.SOUND_AND_VIBRATE) mustBe "Sound and vibration"
        formatter.format(AlertType.VIBRATE_ONLY) mustBe "Vibration only"
    }

    @Test
    fun `Given AlarmTypeFormatter when format is called it maps to formatted string`() {
        // Given
        val context: Context = ApplicationProvider.getApplicationContext()
        val formatter = AlarmTypeFormatter(context)

        // When & Then
        formatter.format(AlarmType.SAY_IT) mustBe "Turn off the alarm by reading scripts out."
        formatter.format(AlarmType.TYPING) mustBe "Turn off the alarm by typing."
        formatter.format(AlarmType.PUSH_BUTTON) mustBe "Turn off the alarm by pushing button."
    }
}
