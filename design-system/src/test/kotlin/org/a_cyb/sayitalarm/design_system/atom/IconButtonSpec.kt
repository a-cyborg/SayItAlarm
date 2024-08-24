/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.design_system.atom

import kotlin.test.assertTrue
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.performClick
import org.a_cyb.sayitalarm.design_system.R
import org.a_cyb.sayitalarm.design_system.RoborazziTest
import org.junit.Test

class IconButtonSpec : RoborazziTest() {

    private fun getString(id: Int) = subjectUnderTest.activity.getString(id)

    @Test
    fun `When IconButtonAdd is clicked, it executes the given action`() {
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
    fun `When IconButtonClose is clicked, it executes the given action`() {
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
    fun `When IconButtonDelete is clicked, it executes the given action`() {
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
    fun `When IconButtonEdit is clicked, it executes the given action`() {
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
    fun `When IconButtonSettings is clicked, it executes the given action`() {
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
    fun `When IconButtonNavigateBack is clicked, it executes the given action`() {
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
    fun `When IconButtonInfo is clicked, it executes the given action`() {
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
    fun `When IconButtonCollapse is clicked, it executes the given action`() {
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
}
