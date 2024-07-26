/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.design_system.atom

import androidx.compose.ui.test.onNodeWithText
import org.a_cyb.sayitalarm.design_system.RoborazziTest
import org.junit.Test

class BoxSpec : RoborazziTest() {
    @Test
    fun `It renders BoxVerticalFading with given content`() {
        subjectUnderTest.setContent {
            BoxVerticalFading {
                TextTitleStandardLarge(text = "BoxVerticalFading")
            }
        }

        subjectUnderTest.onNodeWithText("BoxVerticalFading")
            .assertExists()
    }
}
