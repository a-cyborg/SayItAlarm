/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.molecule

import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.a_cyb.sayitalarm.RoborazziTest
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode

@RunWith(AndroidJUnit4::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(sdk = [33])
class TextRowSpec : RoborazziTest() {

    private fun getString(id: Int) = subjectUnderTest.activity.getString(id)

    @Test
    fun `It renders TextRowTitleAndInfo`() {
        subjectUnderTest.setContent {
            TextRowTitleAndInfo(
                title = "Title",
                info = "Information",
            )
        }

        subjectUnderTest.onNodeWithText("Title").assertExists()
        subjectUnderTest.onNodeWithText("Information").assertExists()
    }

    @Test
    fun `It renders TextRowInfo`() {
        subjectUnderTest.setContent {
            TextRowInfo(text = "TextRowInfo")
        }

        subjectUnderTest.onNodeWithText("TextRowInfo")
            .assertExists()
    }

    @Test
    fun `It renders TextRowWarning`() {
        subjectUnderTest.setContent {
            TextRowWarning(text = "TextRowWarning")
        }

        subjectUnderTest.onNodeWithText("TextRowWarning")
            .assertExists()
    }
}
