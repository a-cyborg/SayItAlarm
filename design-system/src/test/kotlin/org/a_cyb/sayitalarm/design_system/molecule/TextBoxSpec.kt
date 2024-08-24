/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.design_system.molecule

import androidx.compose.ui.test.onNodeWithText
import org.a_cyb.sayitalarm.design_system.R
import org.a_cyb.sayitalarm.design_system.RoborazziTest
import org.junit.Test

class TextBoxSpec : RoborazziTest() {

    private fun getString(id: Int) = subjectUnderTest.activity.getString(id)

    @Test
    fun `It renders TextBoxTitleAndInfo`() {
        subjectUnderTest.setContent {
            TextBoxTitleAndInfo(
                title = "Title",
                info = "Information",
            )
        }

        subjectUnderTest.onNodeWithText("Title").assertExists()
        subjectUnderTest.onNodeWithText("Information").assertExists()
    }

    @Test
    fun `It renders TextBoxInfo`() {
        subjectUnderTest.setContent {
            TextBoxInfo(text = "TextBoxInfo")
        }

        subjectUnderTest.onNodeWithText("TextBoxInfo").assertExists()
    }

    @Test
    fun `It renders TextBoxWarningTitle`() {
        subjectUnderTest.setContent {
            TextBoxWarningTitle(text = "TextBoxWarningTitle")
        }

        subjectUnderTest.onNodeWithText("TextBoxWarningTitle").assertExists()
    }

    @Test
    fun `It renders TextBoxWarningBody`() {
        subjectUnderTest.setContent {
            TextBoxWarningBody(text = "TextBoxWarningBody")
        }

        subjectUnderTest.onNodeWithText("TextBoxWarningBody").assertExists()
    }

    @Test
    fun `It renders TextBoxSayItScript`() {
        subjectUnderTest.setContent {
            TextBoxSayItScript(text = "TextSayItScript")
        }

        subjectUnderTest.onNodeWithText("TextSayItScript").assertExists()
    }

    @Test
    fun `It renders TextBoxSttResult`() {
        subjectUnderTest.setContent {
            TextBoxSttResult(text = "TextSttResult")
        }

        subjectUnderTest.onNodeWithText("TextSttResult").assertExists()
    }

    @Test
    fun `It renders TextBoxStatusHeaderReady`() {
        // Given
        val count = "3/7"

        // When
        subjectUnderTest.setContent {
            TextBoxStatusHeaderReady(count = count)
        }

        // Then
        subjectUnderTest.onNodeWithText(getString(R.string.ready)).assertExists()
        subjectUnderTest.onNodeWithText(count).assertExists()
    }

    @Test
    fun `It renders TextBoxStatusHeaderListening`() {
        // Given
        val count = "3/7"

        // When
        subjectUnderTest.setContent {
            TextBoxStatusHeaderListening(count = count)
        }

        // Then
        subjectUnderTest.onNodeWithText(getString(R.string.listening)).assertExists()
        subjectUnderTest.onNodeWithText(count).assertExists()
    }

    @Test
    fun `It renders TextBoxStatusHeaderSuccess`() {
        // Given
        val count = "3/7"

        // When
        subjectUnderTest.setContent {
            TextBoxStatusHeaderSuccess(count = count)
        }

        // Then
        subjectUnderTest.onNodeWithText(getString(R.string.success)).assertExists()
        subjectUnderTest.onNodeWithText(count).assertExists()
    }

    @Test
    fun `It renders TextBoxStatusHeaderFailed`() {
        // Given
        val count = "3/7"

        // When
        subjectUnderTest.setContent {
            TextBoxStatusHeaderFailed(count = count)
        }

        // Then
        subjectUnderTest.onNodeWithText(getString(R.string.failed)).assertExists()
        subjectUnderTest.onNodeWithText(count).assertExists()
    }

    @Test
    fun `It renders TextBoxStatusHeaderError`() {
        // When
        subjectUnderTest.setContent {
            TextBoxStatusHeaderError()
        }

        // Then
        subjectUnderTest.onNodeWithText(getString(R.string.error)).assertExists()
    }
}
