/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.design_system.atom

import androidx.compose.ui.test.assert
import androidx.compose.ui.test.hasScrollAction
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onParent
import org.a_cyb.sayitalarm.design_system.RoborazziTest
import org.junit.Test

class CardSpec : RoborazziTest() {

    @Test
    fun `It renders CardStandard with given content`() {
        // Given & When
        subjectUnderTest.setContent {
            CardStandard {
                TextDisplayStandardLarge(text = "CardStandard")
            }
        }

        // Then
        subjectUnderTest.onNodeWithText("CardStandard").assertExists()
    }

    @Test
    fun `It renders CardStandardCentered with given content`() {
        // Given & When
        subjectUnderTest.setContent {
            CardStandardCentered {
                TextDisplayStandardLarge(text = "CardStandardCentered")
            }
        }

        // Then
        subjectUnderTest.onNodeWithText("CardStandardCentered").assertExists()
    }

    @Test
    fun `It renders CardStandardCenteredScrollable with given content`() {
        // Given & When
        subjectUnderTest.setContent {
            CardStandardCenteredScrollable {
                TextDisplayStandardLarge(text = "CardStandardCenteredScrollable")
            }
        }

        // Then
        subjectUnderTest.onNodeWithText("CardStandardCenteredScrollable").assertExists()
        subjectUnderTest.onNodeWithText("CardStandardCenteredScrollable").onParent().assert(hasScrollAction())
    }

    @Test
    fun `It renders CardStandardFillMax with given content`() {
        // Given & When
        subjectUnderTest.setContent {
            CardStandardFillMax {
                TextDisplayStandardLarge(text = "CardStandardFillMax")
            }
        }

        // Then
        subjectUnderTest.onNodeWithText("CardStandardFillMax").assertExists()
    }
}
