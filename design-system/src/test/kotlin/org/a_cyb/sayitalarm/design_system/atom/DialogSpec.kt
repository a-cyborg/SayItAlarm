/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.design_system.atom

import androidx.activity.ComponentActivity
import androidx.compose.runtime.Composable
import androidx.compose.ui.test.assert
import androidx.compose.ui.test.hasScrollAction
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onParent
import androidx.test.espresso.Espresso
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.takahirom.roborazzi.ExperimentalRoborazziApi
import com.github.takahirom.roborazzi.RobolectricDeviceQualifiers
import com.github.takahirom.roborazzi.RoborazziRule
import com.github.takahirom.roborazzi.captureScreenRoboImage
import junit.framework.TestCase.assertTrue
import org.a_cyb.sayitalarm.design_system.R
import org.a_cyb.sayitalarm.design_system.molecule.TopAppBarSmall
import org.a_cyb.sayitalarm.design_system.roborazziOf
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode

@OptIn(ExperimentalRoborazziApi::class)
@RunWith(AndroidJUnit4::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(sdk = [33], qualifiers = RobolectricDeviceQualifiers.Pixel7)
class DialogSpec {
    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    @get:Rule
    val roborazziRule = roborazziOf(composeTestRule, RoborazziRule.CaptureType.None)

    private fun getString(id: Int) = composeTestRule.activity.getString(id)

    @Composable
    private fun DialogTestTopAppBar() {
        TopAppBarSmall(title = "Dialog", firstIcon = { IconButtonClose {} }, secondIcon = {})
    }

    @Test
    fun `It renders a DialogStandardFillMax`() {
        // Given & When
        composeTestRule.setContent {
            DialogStandardFillMax(
                onDismiss = {},
                topAppBar = { DialogTestTopAppBar() }
            ) {
                TextTitleStandardMedium(text = "DialogStandardFillMax")
            }
        }

        // Then
        composeTestRule.onNodeWithText("Dialog").assertExists() // Title of DialogTestTopAppBar
        composeTestRule.onNodeWithText("DialogStandardFillMax").assertExists()
        captureScreenRoboImage()
    }

    @Test
    fun `When DialogStandardFillMax is displayed and pressBack is called, it triggers onDismiss action`() {
        // Given
        var hasBeenCalled = false

        composeTestRule.setContent {
            DialogStandardFillMax(
                topAppBar = {},
                onDismiss = { hasBeenCalled = true },
            ) {}
        }

        // When
        Espresso.pressBack()

        // Then
        assertTrue(hasBeenCalled)
    }

    @Test
    fun `It renders a DialogStandardFillMaxScrollable`() {
        // Given & When
        composeTestRule.setContent {
            DialogStandardFillMaxScrollable(
                onDismiss = {},
                topAppBar = { DialogTestTopAppBar() },
            ) {
                TextTitleStandardMedium(text = "DialogStandardFillMaxScrollable")
            }
        }

        // Then
        with(composeTestRule) {
            onNodeWithText("Dialog").assertExists()
            onNodeWithContentDescription(getString(R.string.action_close)).assertExists()
            onNodeWithText("DialogStandardFillMaxScrollable").assertExists()
            onNodeWithText("DialogStandardFillMaxScrollable").onParent().assert(hasScrollAction())
        }

        captureScreenRoboImage()
    }

    @Test
    fun `When DialogStandardFillMaxScrollable is displayed and pressBack is called, it triggers onDismiss action`() {
        // Given
        var hasBeenCalled = false

        composeTestRule.setContent {
            DialogStandardFillMaxScrollable(
                topAppBar = {},
                onDismiss = { hasBeenCalled = true },
            ) {}
        }

        // When
        Espresso.pressBack()

        // Then
        assertTrue(hasBeenCalled)
    }

    @Test
    fun `It renders a DialogStandardFitContent`() {
        // Given & When
        composeTestRule.setContent {
            DialogStandardFitContent(onDismiss = {}) {
                TextTitleStandardMedium(text = "DialogStandardFitContent")
            }
        }

        // Then
        composeTestRule.onNodeWithText("DialogStandardFitContent").assertExists()
        captureScreenRoboImage()
    }

    @Test
    fun `When dialogStandardFitContent is displayed and pressBack is called, it triggers onDismiss action`() {
        // Given
        var hasBeenCalled = false

        composeTestRule.setContent {
            DialogStandardFitContent(onDismiss = { hasBeenCalled = true }) {
                TextTitleStandardLarge(text = "Dialog")
            }
        }

        // When
        Espresso.pressBack()

        // Then
        assertTrue(hasBeenCalled)
    }

    @Test
    fun `It renders a DialogStandardFitContentScrollable`() {
        // Given & When
        composeTestRule.setContent {
            DialogStandardFitContentScrollable(onDismiss = {}) {
                TextTitleStandardMedium(text = "DialogStandardFitContentScrollable")
            }
        }

        // Then
        composeTestRule.onNodeWithText("DialogStandardFitContentScrollable").assertExists()
        composeTestRule.onNodeWithText("DialogStandardFitContentScrollable").onParent().assert(hasScrollAction())
        captureScreenRoboImage()
    }

    @Test
    fun `When dialogStandardFitContentScrollable is displayed and pressBack is called, it triggers onDismiss action`() {
        // Given
        var hasBeenCalled = false

        composeTestRule.setContent {
            DialogStandardFitContentScrollable(onDismiss = { hasBeenCalled = true }) {
                TextTitleStandardLarge(text = "Dialog")
            }
        }

        // When
        Espresso.pressBack()

        // Then
        assertTrue(hasBeenCalled)
    }
}
