/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.atom

import kotlin.test.assertTrue
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.a_cyb.sayitalarm.R
import org.a_cyb.sayitalarm.RoborazziTest
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode

@RunWith(AndroidJUnit4::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(sdk = [33])
class IconButtonSpec : RoborazziTest() {

    private fun getString(id: Int) = subjectUnderTest.activity.getString(id)

    @Test
    fun `It renders a IconButtonAdd`() {
        subjectUnderTest.setContent {
            IconButtonAdd {}
        }
    }

    @Test
    fun `Given IconButtonAdd click is called it propagates the given action`() {
        var hasBeenCalled = false

        subjectUnderTest.setContent {
            IconButtonAdd {
                hasBeenCalled = true
            }
        }
        subjectUnderTest
            .onNodeWithContentDescription(getString(R.string.action_add_alarm))
            .performClick()

        assertTrue(hasBeenCalled)
    }

    @Test
    fun `It renders a IconButtonClose`() {
        subjectUnderTest.setContent {
            IconButtonClose {}
        }
    }

    @Test
    fun `Given IconButtonClose click is called it propagates the given action`() {
        var hasBeenCalled = false

        subjectUnderTest.setContent {
            IconButtonClose {
                hasBeenCalled = true
            }
        }
        subjectUnderTest
            .onNodeWithContentDescription(getString(R.string.action_close))
            .performClick()

        assertTrue(hasBeenCalled)
    }

    @Test
    fun `It renders a IconButtonDelete`() {
        subjectUnderTest.setContent {
            IconButtonDelete {}
        }
    }

    @Test
    fun `Given IconButtonDelete click is called it propagates the given action`() {
        var hasBeenCalled = false

        subjectUnderTest.setContent {
            IconButtonDelete {
                hasBeenCalled = true
            }
        }
        subjectUnderTest
            .onNodeWithContentDescription(getString(R.string.action_delete_alarm))
            .performClick()

        assertTrue(hasBeenCalled)
    }

    @Test
    fun `It renders a IconButtonDeleteText`() {
        subjectUnderTest.setContent {
            IconButtonDeleteText {}
        }
    }

    @Test
    fun `Given IconButtonDeleteText click is called it propagates the given action`() {
        var hasBeenCalled = false

        subjectUnderTest.setContent {
            IconButtonDeleteText {
                hasBeenCalled = true
            }
        }
        subjectUnderTest
            .onNodeWithText("Delete")
            .performClick()

        assertTrue(hasBeenCalled)
    }

    @Test
    fun `It renders a IconButtonDone`() {
        subjectUnderTest.setContent {
            IconButtonDone {}
        }
    }

    @Test
    fun `Given IconButtonDone click is called it propagates the given action`() {
        var hasBeenCalled = false

        subjectUnderTest.setContent {
            IconButtonDone {
                hasBeenCalled = true
            }
        }
        subjectUnderTest
            .onNodeWithText(getString(R.string.done))
            .performClick()

        assertTrue(hasBeenCalled)
    }

    @Test
    fun `It renders a IconButtonEdit`() {
        subjectUnderTest.setContent {
            IconButtonEdit {}
        }
    }

    @Test
    fun `Given IconButtonEdit click is called it propagates the given action`() {
        var hasBeenCalled = false

        subjectUnderTest.setContent {
            IconButtonEdit {
                hasBeenCalled = true
            }
        }
        subjectUnderTest
            .onNodeWithContentDescription(getString(R.string.action_edit))
            .performClick()

        assertTrue(hasBeenCalled)
    }

    @Test
    fun `It renders a IconButtonEditText`() {
        subjectUnderTest.setContent {
            IconButtonEditText {}
        }
    }

    @Test
    fun `Given IconButtonEditText click is called it propagates the given action`() {
        var hasBeenCalled = false

        subjectUnderTest.setContent {
            IconButtonEditText {
                hasBeenCalled = true
            }
        }
        subjectUnderTest
            .onNodeWithText(getString(R.string.edit))
            .performClick()

        assertTrue(hasBeenCalled)
    }

    @Test
    fun `It renders a IconButtonSettings`() {
        subjectUnderTest.setContent {
            IconButtonSettings {}
        }
    }

    @Test
    fun `Given IconButtonSettings click is called it propagates the given action`() {
        var hasBeenCalled = false

        subjectUnderTest.setContent {
            IconButtonSettings {
                hasBeenCalled = true
            }
        }

        subjectUnderTest
            .onNodeWithContentDescription(getString(R.string.action_open_settings))
            .performClick()

        assertTrue(hasBeenCalled)
    }

    @Test
    fun `It renders IconButtonNavigateBack`() {
        subjectUnderTest.setContent {
            IconButtonNavigateBack {}
        }
    }

    @Test
    fun `Given IconButtonNavigateBack click is called it propagates the given action`() {
        var hasBeenCalled = false

        subjectUnderTest.setContent {
            IconButtonNavigateBack {
                hasBeenCalled = true
            }
        }

        subjectUnderTest
            .onNodeWithContentDescription(getString(R.string.action_navigate_back))
            .performClick()

        assertTrue(hasBeenCalled)
    }

    @Test
    fun `It renders IconButtonInfo`() {
        subjectUnderTest.setContent {
            IconButtonInfo {}
        }
    }

    @Test
    fun `Given IconButtonInfo click is called it propagates the given action`() {
        var hasBeenCalled = false

        subjectUnderTest.setContent {
            IconButtonInfo {
                hasBeenCalled = true
            }
        }

        subjectUnderTest
            .onNodeWithContentDescription(getString(R.string.action_info))
            .performClick()

        assertTrue(hasBeenCalled)
    }

    @Test
    fun `It renders IconButtonCollapse`() {
        subjectUnderTest.setContent {
            IconButtonCollapse {}
        }
    }

    @Test
    fun `Given IconButtonCollapse click is called it propagates the given action`() {
        var hasBeenCalled = false

        subjectUnderTest.setContent {
            IconButtonCollapse {
                hasBeenCalled = true
            }
        }

        subjectUnderTest
            .onNodeWithContentDescription(getString(R.string.action_collapse))
            .performClick()

        assertTrue(hasBeenCalled)
    }

    @Test
    fun `It renders a IconButtonSaveText`() {
        subjectUnderTest.setContent {
            IconButtonSaveText {}
        }
    }

    @Test
    fun `Given IconButtonSaveText click is called it propagates the given action`() {
        var hasBeenCalled = false

        subjectUnderTest.setContent {
            IconButtonSaveText {
                hasBeenCalled = true
            }
        }

        subjectUnderTest
            .onNodeWithText(getString(R.string.save))
            .performClick()

        assertTrue(hasBeenCalled)
    }

    @Test
    fun `It renders a IconButtonCancelText`() {
        subjectUnderTest.setContent {
            IconButtonCancelText {}
        }
    }

    @Test
    fun `Given IconButtonCancelText click is called it propagates the given action`() {
        var hasBeenCalled = false

        subjectUnderTest.setContent {
            IconButtonCancelText {
                hasBeenCalled = true
            }
        }

        subjectUnderTest
            .onNodeWithText(getString(R.string.cancel))
            .performClick()

        assertTrue(hasBeenCalled)
    }

    @Test
    fun `It renders a IconButtonConfirmText`() {
        subjectUnderTest.setContent {
            IconButtonConfirmText {}
        }
    }

    @Test
    fun `Given IconButtonConfirmText click is called it propagates the given action`() {
        var hasBeenCalled = false

        subjectUnderTest.setContent {
            IconButtonConfirmText {
                hasBeenCalled = true
            }
        }

        subjectUnderTest
            .onNodeWithText(getString(R.string.confirm))
            .performClick()

        assertTrue(hasBeenCalled)
    }

    @Test
    fun `It renders a IconButtonRequestPermission`() {
        subjectUnderTest.setContent {
            IconButtonRequestPermission {}
        }
    }

    @Test
    fun `Given IconButtonRequestPermission click is called it propagates the given action`() {
        var hasBeenCalled = false

        subjectUnderTest.setContent {
            IconButtonRequestPermission {
                hasBeenCalled = true
            }
        }

        subjectUnderTest
            .onNodeWithText(getString(R.string.request_permission))
            .performClick()

        assertTrue(hasBeenCalled)
    }
}
