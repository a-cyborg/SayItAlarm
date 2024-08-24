/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.design_system.atom

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.isDialog
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.test.espresso.Espresso
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.takahirom.roborazzi.RoborazziRule
import com.github.takahirom.roborazzi.captureRoboImage
import junit.framework.TestCase.assertTrue
import org.a_cyb.sayitalarm.design_system.R
import org.a_cyb.sayitalarm.design_system.molecule.TopAppBarSmall
import org.a_cyb.sayitalarm.design_system.roborazziOf
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode

@RunWith(AndroidJUnit4::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(sdk = [33])
class DialogSpec {
    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    @get:Rule
    val roborazziRule = roborazziOf(composeTestRule, RoborazziRule.CaptureType.None)

    private fun getString(id: Int) = composeTestRule.activity.getString(id)

    @Test
    fun `It renders a DialogStandardFillMax`() {
        composeTestRule.setContent {
            DialogStandardFillMax(
                topAppBar = {
                    TopAppBarSmall(
                        title = "Dialog",
                        firstIcon = { IconButtonClose {} },
                        secondIcon = {},
                    )
                },
                onDismiss = { },
            ) {}
        }

        with(composeTestRule) {
            onNodeWithText("Dialog").assertExists()
            onNodeWithContentDescription(getString(R.string.action_close)).assertExists()

            onNode(isDialog()).captureRoboImage()
        }
    }

    @Test
    fun `When pressBack is called given dialogStandardFillMax it propagates the given onDismiss action`() {
        var hasBeenCalled = false

        composeTestRule.setContent {
            DialogStandardFillMax(
                topAppBar = {
                    TopAppBarSmall(
                        title = "Add Alarm",
                        firstIcon = { IconButtonClose {} },
                        secondIcon = { TextButtonSave {} },
                    )
                },
                onDismiss = {
                    hasBeenCalled = true
                },
            ) {}
        }

        Espresso.pressBack()

        assertTrue(hasBeenCalled)
    }

    @Test
    fun `It renders a DialogStandardFitContent`() {
        composeTestRule.setContent {
            DialogStandardFitContent(onDismiss = {}) {
                TextTitleStandardLarge(text = "Dialog")
            }
        }

        composeTestRule.onNodeWithText("Dialog").assertExists()
        composeTestRule.onNode(isDialog()).captureRoboImage()
    }

    @Test
    fun `When pressBack is called given dialogStandardFitContent it propagates the given onDismiss action`() {
        var hasBeenCalled = false

        composeTestRule.setContent {
            DialogStandardFitContent(
                onDismiss = {
                    hasBeenCalled = true
                },
            ) {
                TextTitleStandardLarge(text = "Dialog")
            }
        }

        Espresso.pressBack()

        assertTrue(hasBeenCalled)
    }
}
