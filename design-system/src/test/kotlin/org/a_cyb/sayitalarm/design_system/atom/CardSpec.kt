/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.design_system.atom

import androidx.compose.ui.test.onNodeWithText
import org.a_cyb.sayitalarm.design_system.RoborazziTest
import org.junit.Test

class CardSpec : RoborazziTest() {

    @Test
    fun `It renders CardStandard with given content`() {
        subjectUnderTest.setContent {
            CardStandard {
                TextDisplayStandardLarge(text = "CardStandard")
            }
        }

        subjectUnderTest.onNodeWithText("CardStandard")
            .assertExists()
    }

    @Test
    fun `It renders CardStandardCentered with given content`() {
        subjectUnderTest.setContent {
            CardStandardCentered {
                TextDisplayStandardLarge(text = "CardStandardCentered")
            }
        }

        subjectUnderTest.onNodeWithText("CardStandardCentered")
            .assertExists()
    }
}
