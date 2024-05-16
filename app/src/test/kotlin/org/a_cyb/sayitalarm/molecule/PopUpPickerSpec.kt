/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.molecule

import kotlin.test.assertFalse
import kotlin.test.assertTrue
import androidx.activity.ComponentActivity
import androidx.compose.ui.test.isDialog
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.espresso.Espresso
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.takahirom.roborazzi.RoborazziRule
import com.github.takahirom.roborazzi.captureRoboImage
import org.a_cyb.sayitalarm.R
import org.a_cyb.sayitalarm.atom.TextDisplayStandardSmall
import org.a_cyb.sayitalarm.roborazziOf
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode

@RunWith(AndroidJUnit4::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(sdk = [33])
class PopUpPickerSpec {

    private val colors: List<String> = listOf(
        "Red", "Blue", "Green", "Yellow", "Purple",
        "Orange", "Pink", "Brown", "Black", "White",
        "Gray", "Turquoise", "Maroon",
    )

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    @get:Rule
    val roborazziRule = roborazziOf(composeTestRule, RoborazziRule.CaptureType.None)

    private fun getString(id: Int) = composeTestRule.activity.getString(id)

    @Test
    fun `It renders PopUpPickerStandardWheel`() {
        composeTestRule.setContent {
            PopUpPickerStandardWheel(
                title = "Colors",
                info = "My favorite color is",
                pickerValues = colors,
                pickerInitIdx = 6,
                pickerItemRow = { TextDisplayStandardSmall(it) },
                onDismiss = {},
                onConfirm = { _ -> },
            )
        }

        composeTestRule
            .onNodeWithContentDescription(getString(R.string.component_wheel_picker))
            .assertExists()

        composeTestRule
            .onNode(isDialog())
            .captureRoboImage()
    }

    @Test
    fun `Given PopUpPickerStandardWheel confirm click it propagates the given onConfirm and onDismiss action`() {
        var onConfirmHasBeenCalled = false
        var onCancelHasBeenCalled = false

        // Given
        composeTestRule.setContent {
            PopUpPickerStandardWheel(
                title = "Colors",
                info = "My favorite color is",
                pickerValues = colors,
                pickerItemRow = { TextDisplayStandardSmall(it) },
                onDismiss = {
                    onCancelHasBeenCalled = true
                },
                onConfirm = { _ ->
                    onConfirmHasBeenCalled = true
                },
            )
        }

        // When
        composeTestRule
            .onNodeWithText(getString(R.string.confirm))
            .performClick()

        // Then
        assertTrue(onConfirmHasBeenCalled)
        assertTrue(onCancelHasBeenCalled)
    }

    @Test
    fun `Given PopUpPickerStandardWheel cancel click it propagates the given onDismiss action`() {
        var onConfirmHasBeenCalled = false
        var onCancelHasBeenCalled = false

        // Given
        composeTestRule.setContent {
            PopUpPickerStandardWheel(
                title = "Colors",
                info = "My favorite color is",
                pickerValues = colors,
                pickerItemRow = { TextDisplayStandardSmall(it) },
                onDismiss = {
                    onCancelHasBeenCalled = true
                },
                onConfirm = { _ ->
                    onConfirmHasBeenCalled = true
                },
            )
        }

        // When
        composeTestRule
            .onNodeWithText(getString(R.string.cancel))
            .performClick()

        // Then
        assertFalse(onConfirmHasBeenCalled)
        assertTrue(onCancelHasBeenCalled)
    }

    @Test
    fun `When pressBack is called given PopUpPickerStandardWheel it propagates the given onDismiss action`() {
        var hasBeenCalled = false

        composeTestRule.setContent {
            PopUpPickerStandardWheel(
                title = "Colors",
                info = "My favorite color is",
                pickerValues = colors,
                pickerItemRow = { TextDisplayStandardSmall(it) },
                onDismiss = {
                    hasBeenCalled = true
                },
                onConfirm = { _ -> },
            )
        }

        Espresso.pressBack()

        assertTrue(hasBeenCalled)
    }
}
