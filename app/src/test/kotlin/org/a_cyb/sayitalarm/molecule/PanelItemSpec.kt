/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.molecule

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.isRoot
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.takahirom.roborazzi.RoborazziRule
import com.github.takahirom.roborazzi.captureRoboImage
import org.a_cyb.sayitalarm.R
import org.a_cyb.sayitalarm.roborazziOf
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode

@RunWith(AndroidJUnit4::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(sdk = [33])
class PanelItemSpec {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    @get:Rule
    val roborazziRule = roborazziOf(composeTestRule, RoborazziRule.CaptureType.None)

    private fun getString(id: Int) = composeTestRule.activity.getString(id)

    @Test
    fun `It renders PanelItemWithPopUpPicker`() {
        composeTestRule.setContent {
            PanelItemWithPopUpPicker(
                title = getString(id = R.string.timeout),
                info = getString(id = R.string.info_timeout),
                values = listOf("3hr"),
                selectedItemIdx = 0,
                popUpPickerOnConfirm = { _ -> }
            )
        }

        composeTestRule.onNode(isRoot())
            .captureRoboImage()
    }

    @Test
    fun `Given PanelItemWithPopUpPicker IconButtonEdit is clicked it displays PopUpPicker`() {
        // Given
        composeTestRule.setContent {
            PanelItemWithPopUpPicker(
                title = getString(id = R.string.timeout),
                info = getString(id = R.string.info_timeout),
                values = listOf("3hr"),
                selectedItemIdx = 0,
                popUpPickerOnConfirm = { _ -> }
            )
        }

        // When
        composeTestRule.onNodeWithContentDescription(getString(R.string.action_edit))
            .performClick()

        // Then
        composeTestRule.onNodeWithContentDescription(getString(R.string.component_wheel_picker))
            .assertExists()
    }
}
