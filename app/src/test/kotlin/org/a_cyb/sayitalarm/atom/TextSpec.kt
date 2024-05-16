/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.atom

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.a_cyb.sayitalarm.RoborazziTest
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode

@RunWith(AndroidJUnit4::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(sdk = [33])
class TextSpec : RoborazziTest() {

    @Test
    fun `It renders TextDisplayStandardLarge`() {
        subjectUnderTest.setContent {
            TextDisplayStandardLarge(text = "DisplayStandardLarge")
        }
    }

    @Test
    fun `It renders TextDisplayStandardSmall`() {
        subjectUnderTest.setContent {
            TextDisplayStandardSmall(text = "DisplayStandardSmall")
        }
    }

    @Test
    fun `It renders TextHeadlineStandardLarge`() {
        subjectUnderTest.setContent {
            TextHeadlineStandardLarge(text = "HeadlineStandardLarge")
        }
    }

    @Test
    fun `It renders TextHeadlineStandardSmall`() {
        subjectUnderTest.setContent {
            TextHeadlineStandardSmall(text = "HeadlineStandardSmall")
        }
    }

    @Test
    fun `It renders TextTitleStandardLarge`() {
        subjectUnderTest.setContent {
            TextTitleStandardLarge(text = "TitleStandardLarge")
        }
    }

    @Test
    fun `It renders TextTitleAttentionMedium`() {
        subjectUnderTest.setContent {
            TextTitleAttentionMedium(text = "TitleAttentionMedium")
        }
    }

    @Test
    fun `It renders TextTitleWarningMedium`() {
        subjectUnderTest.setContent {
            TextTitleWarningMedium(text = "TitleWarningMedium")
        }
    }

    @Test
    fun `It renders TextTitleSubtleMedium`() {
        subjectUnderTest.setContent {
            TextTitleSubtleMedium(text = "TitleSubtleMedium")
        }
    }

    @Test
    fun `It renders TextLabelAttentionLarge`() {
        subjectUnderTest.setContent {
            TextLabelAttentionLarge(text = "LabelAttentionLarge")
        }
    }

    @Test
    fun `It renders TextBodyStandardLarge`() {
        subjectUnderTest.setContent {
            TextBodyStandardLarge(text = "BodyStandardLarge")
        }
    }

    @Test
    fun `It renders TextBodySubtleMedium`() {
        subjectUnderTest.setContent {
            TextBodySubtleMedium(text = "BodySubtleMedium")
        }
    }

    @Test
    fun `It renders TextBodyStandardSmall`() {
        subjectUnderTest.setContent {
            TextBodyStandardSmall(text = "BodyStandardSmall")
        }
    }
}
