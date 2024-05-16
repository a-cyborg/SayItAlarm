/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.atom

import kotlin.test.assertFalse
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.assertIsOn
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.a_cyb.sayitalarm.RoborazziTest
import org.a_cyb.sayitalarm.app.atom.SwitchStandard
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode

@RunWith(AndroidJUnit4::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(sdk = [33])
class SwitchSpec : RoborazziTest() {

    @Test
    fun `It renders checked SwitchStandard`() {
        subjectUnderTest.setContent {
            SwitchStandard(checked = true) {}
        }
    }

    @Test
    fun `It renders unchecked SwitchStandard`() {
        subjectUnderTest.setContent {
            SwitchStandard(checked = false) {}
        }
    }

    @Test
    fun `Given checked SwitchStandard click is called it propagates the given action`() {
        var checked = true

        subjectUnderTest.setContent {
            SwitchStandard(checked = checked) {
                checked = it
            }
        }

        subjectUnderTest.onNode(SemanticsMatcher.expectValue(SemanticsProperties.Role, Role.Switch))
            .assertIsOn()
            .performClick()

        assertFalse(checked)
    }
}
