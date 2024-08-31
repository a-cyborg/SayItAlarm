/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.design_system.atom

import kotlin.test.assertTrue
import android.content.ClipboardManager
import android.content.Context
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import org.a_cyb.sayitalarm.design_system.R
import org.a_cyb.sayitalarm.design_system.RoborazziTest
import org.a_cyb.sayitalarm.util.mustBe
import org.junit.Test

class TextButtonSpec : RoborazziTest() {

    private fun getString(id: Int) = subjectUnderTest.activity.getString(id)

    @Test
    fun `When TextButtonDelete is clicked, it executes the given action`() {
        var hasBeenCalled = false

        subjectUnderTest.setContent {
            TextButtonDone {
                hasBeenCalled = true
            }
        }
        subjectUnderTest
            .onNodeWithText(getString(R.string.done))
            .performClick()

        assertTrue(hasBeenCalled)
    }

    @Test
    fun `When TextButtonDone is clicked, it executes the given action`() {
        var hasBeenCalled = false

        subjectUnderTest.setContent {
            TextButtonDone {
                hasBeenCalled = true
            }
        }
        subjectUnderTest
            .onNodeWithText(getString(R.string.done))
            .performClick()

        assertTrue(hasBeenCalled)
    }

    @Test
    fun `When TextButtonEdit is clicked, it executes the given action`() {
        var hasBeenCalled = false

        subjectUnderTest.setContent {
            TextButtonEdit {
                hasBeenCalled = true
            }
        }
        subjectUnderTest
            .onNodeWithText(getString(R.string.edit))
            .performClick()

        assertTrue(hasBeenCalled)
    }

    @Test
    fun `When TextButtonSave is clicked, it executes the given action`() {
        var hasBeenCalled = false

        subjectUnderTest.setContent {
            TextButtonSave {
                hasBeenCalled = true
            }
        }

        subjectUnderTest
            .onNodeWithText(getString(R.string.save))
            .performClick()

        assertTrue(hasBeenCalled)
    }

    @Test
    fun `When TextButtonCancel is clicked, it executes the given action`() {
        var hasBeenCalled = false

        subjectUnderTest.setContent {
            TextButtonCancel {
                hasBeenCalled = true
            }
        }

        subjectUnderTest
            .onNodeWithText(getString(R.string.cancel))
            .performClick()

        assertTrue(hasBeenCalled)
    }

    @Test
    fun `When TextButtonConfirm is clicked, it executes the given action`() {
        var hasBeenCalled = false

        subjectUnderTest.setContent {
            TextButtonConfirm {
                hasBeenCalled = true
            }
        }

        subjectUnderTest
            .onNodeWithText(getString(R.string.confirm))
            .performClick()

        assertTrue(hasBeenCalled)
    }

    @Test
    fun `When TextButtonSnooze is clicked, it executes the given action`() {
        var hasBeenCalled = false

        subjectUnderTest.setContent {
            TextButtonSnooze {
                hasBeenCalled = true
            }
        }

        subjectUnderTest
            .onNodeWithText(getString(R.string.snooze))
            .performClick()

        assertTrue(hasBeenCalled)
    }

    @Test
    fun `When TextButtonRequestPermission is clicked, it executes the given action`() {
        var hasBeenCalled = false

        subjectUnderTest.setContent {
            TextButtonRequestPermission {
                hasBeenCalled = true
            }
        }

        subjectUnderTest
            .onNodeWithText(getString(R.string.request_permission))
            .performClick()

        assertTrue(hasBeenCalled)
    }

    @Test
    fun `When TextButtonCircleSayIt is clicked, it executes the given action`() {
        var hasBeenCalled = false

        subjectUnderTest.setContent {
            TextButtonCircleSayIt {
                hasBeenCalled = true
            }
        }

        subjectUnderTest
            .onNodeWithText(getString(R.string.say_it))
            .performClick()

        assertTrue(hasBeenCalled)
    }

    @Test
    fun `When TextButtonCircleStart is clicked, it executes the given action`() {
        var hasBeenCalled = false

        subjectUnderTest.setContent {
            TextButtonCircleStart {
                hasBeenCalled = true
            }
        }

        subjectUnderTest
            .onNodeWithText(getString(R.string.start))
            .performClick()

        assertTrue(hasBeenCalled)
    }

    @Test
    fun `When TextButtonCircleTryAgain is clicked, it executes the given action`() {
        var hasBeenCalled = false

        subjectUnderTest.setContent {
            TextButtonCircleTryAgain {
                hasBeenCalled = true
            }
        }

        subjectUnderTest
            .onNodeWithText(getString(R.string.try_again))
            .performClick()

        assertTrue(hasBeenCalled)
    }

    @Test
    fun `When TextButtonCircleFinish is clicked, it executes the given action`() {
        // Given
        var hasBeenCalled = false

        subjectUnderTest.setContent {
            TextButtonCircleFinish { hasBeenCalled = true }
        }

        // When
        subjectUnderTest.onNodeWithText(getString(R.string.finish)).performClick()

        // Then
        assertTrue(hasBeenCalled)
    }

    @Test
    fun `When TextButtonCircleExit is clicked, it executes the given action`() {
        // Given
        var hasBeenCalled = false

        subjectUnderTest.setContent {
            TextButtonCircleExit { hasBeenCalled = true }
        }

        // When
        subjectUnderTest.onNodeWithText(getString(R.string.exit)).performClick()

        // Then
        assertTrue(hasBeenCalled)
    }

    @Test
    fun `When TextButtonCopy is clicked, it sets primaryClip`() {
        lateinit var clipboardManager: ClipboardManager
        val testText = "copyTestString"

        subjectUnderTest.setContent {
            clipboardManager = LocalContext.current.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager

            TextButtonCopy(copyString = testText)
        }

        // When
        subjectUnderTest.onNodeWithText(getString(R.string.copy)).performClick()

        // Then
        clipboardManager.hasPrimaryClip() mustBe true
        clipboardManager.primaryClip!!.getItemAt(0)!!.text mustBe testText
    }

    @Test
    fun `When TextButtonEmail is clicked, it executes the given action`() {
        // Given
        var hasBeenCalled = false

        subjectUnderTest.setContent {
            TextButtonEmail { hasBeenCalled = true }
        }

        // When
        subjectUnderTest.onNodeWithText(getString(R.string.email)).performClick()

        // Then
        assertTrue(hasBeenCalled)
    }

    @Test
    fun `When TextButtonGooglePlay is clicked, it executes the given action`() {
        // Given
        var hasBeenCalled = false

        subjectUnderTest.setContent {
            TextButtonGooglePlay { hasBeenCalled = true }
        }

        // When
        subjectUnderTest.onNodeWithText(getString(R.string.google_play)).performClick()

        // Then
        assertTrue(hasBeenCalled)
    }

    @Test
    fun `When TextButtonGitHub is clicked, it executes the given action`() {
        // Given
        var hasBeenCalled = false

        subjectUnderTest.setContent {
            TextButtonGitHub { hasBeenCalled = true }
        }

        // When
        subjectUnderTest.onNodeWithText(getString(R.string.github)).performClick()

        // Then
        assertTrue(hasBeenCalled)
    }
}