/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.atom

import kotlin.test.assertTrue
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.a_cyb.sayitalarm.RoborazziTest
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode

@RunWith(AndroidJUnit4::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(sdk = [33])
class SectionRowSpec : RoborazziTest() {
    @Test
    fun `It renders SectionRow`() {
        subjectUnderTest.setContent {
            SectionRow {}
        }
    }

    fun `Given SectionRowClickable click is called it propagates the given action`() {
        var hasBeenCalled = false

        subjectUnderTest.setContent {
            SectionRowClickable(
                contentDescription = "Test Click",
                onClick = {
                    hasBeenCalled = true
                },
            ) {}
        }

        subjectUnderTest
            .onNodeWithContentDescription("Test Click")
            .performClick()

        assertTrue(hasBeenCalled)
    }
}
