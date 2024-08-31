/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.design_system.atom

import org.a_cyb.sayitalarm.design_system.RoborazziTest
import org.junit.Test

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
    fun `It renders TextDisplaySubtleSmall`() {
        subjectUnderTest.setContent {
            TextDisplaySubtleSmall(text = "DisplayStandardSmall")
        }
    }

    @Test
    fun `It renders TextDisplayAttentionSmall`() {
        subjectUnderTest.setContent {
            TextDisplayAttentionSmall(text = "DisplayAttentionSmall")
        }
    }

    @Test
    fun `It renders TextDisplayDangerSmall`() {
        subjectUnderTest.setContent {
            TextDisplayDangerSmall(text = "DisplayDangerSmall")
        }
    }

    @Test
    fun `It renders TextDisplayWarningSmall`() {
        subjectUnderTest.setContent {
            TextDisplayWarningSmall(text = "DisplayWarningSmall")
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
    fun `It renders TextTitleAttentionLarge`() {
        subjectUnderTest.setContent {
            TextTitleAttentionLarge(text = "TitleAttentionLarge")
        }
    }

    @Test
    fun `It renders TextTitleSubtleLarge`() {
        subjectUnderTest.setContent {
            TextTitleSubtleLarge(text = "TitleSubtleLarge")
        }
    }

    @Test
    fun `It renders TextTitleWarningLarge`() {
        subjectUnderTest.setContent {
            TextTitleWarningLarge(text = "TitleWarningLarge")
        }
    }

    @Test
    fun `It renders TextTitleStandardMedium`() {
        subjectUnderTest.setContent {
            TextTitleStandardMedium(text = "TitleStandardLarge")
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
    fun `It renders TextTitleDangerMedium`() {
        subjectUnderTest.setContent {
            TextTitleDangerMedium(text = "TitleWarningMedium")
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
    fun `It renders TextBodySubtleMediumUnderline`() {
        subjectUnderTest.setContent {
            TextBodySubtleMediumUnderline(text = "BodySubtleMediumUnderline")
        }
    }

    @Test
    fun `It renders TextBodyStandardSmall`() {
        subjectUnderTest.setContent {
            TextBodyStandardSmall(text = "BodyStandardSmall")
        }
    }

    @Test
    fun `It renders TextBodyStandardSmallUnderline`() {
        subjectUnderTest.setContent {
            TextBodySubtleMediumUnderline(text = "BodySubtleMediumUnderline")
        }
    }
}
