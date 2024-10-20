/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.design_system.atom

import androidx.activity.ComponentActivity
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.assertIsOn
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.takahirom.roborazzi.RobolectricDeviceQualifiers
import com.github.takahirom.roborazzi.RoborazziRule
import com.github.takahirom.roborazzi.captureRoboImage
import kotlin.test.assertFalse
import org.a_cyb.sayitalarm.design_system.roborazziOf
import org.a_cyb.sayitalarm.util.test_utils.createAddActivityToRobolectricRule
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode

@RunWith(AndroidJUnit4::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(sdk = [33], qualifiers = RobolectricDeviceQualifiers.ResizableExperimental)
class SwitchSpec {

    @get:Rule(order = 1)
    val addActivityRule = createAddActivityToRobolectricRule()

    @get:Rule(order = 2)
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    @get:Rule(order = 3)
    val roborazziRule = roborazziOf(composeTestRule, RoborazziRule.CaptureType.None)

    @Test
    fun `It renders checked SwitchStandard`() {
        composeTestRule.setContent {
            SwitchStandard(checked = true) {}
        }

        composeTestRule.onRoot().captureRoboImage()
    }

    @Test
    fun `It renders unchecked SwitchStandard`() {
        composeTestRule.setContent {
            SwitchStandard(checked = false) {}
        }

        composeTestRule.onRoot().captureRoboImage()
    }

    @Test
    fun `Given checked SwitchStandard click is called it propagates the given action`() {
        var checked = true

        composeTestRule.setContent {
            SwitchStandard(checked = checked) {
                checked = it
            }
        }

        composeTestRule.onNode(SemanticsMatcher.expectValue(SemanticsProperties.Role, Role.Switch))
            .assertIsOn()
            .performClick()

        assertFalse(checked)
    }
}
