/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.design_system.molecule

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.isDialog
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.espresso.Espresso
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.takahirom.roborazzi.RobolectricDeviceQualifiers
import com.github.takahirom.roborazzi.RoborazziRule
import com.github.takahirom.roborazzi.captureRoboImage
import kotlin.test.assertFalse
import kotlin.test.assertTrue
import org.a_cyb.sayitalarm.design_system.R
import org.a_cyb.sayitalarm.design_system.atom.TextDisplayStandardSmall
import org.a_cyb.sayitalarm.design_system.roborazziOf
import org.a_cyb.sayitalarm.util.test_utils.createAddActivityToRobolectricRule
import org.a_cyb.sayitalarm.util.test_utils.mustBe
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode

@RunWith(AndroidJUnit4::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(sdk = [33], qualifiers = RobolectricDeviceQualifiers.ResizableExperimental)
class PopupPickerSpec {

    private val colors: List<String> = listOf(
        "Red", "Blue", "Green", "Yellow", "Purple",
        "Orange", "Pink", "Brown", "Black", "White",
        "Gray", "Turquoise", "Maroon",
    )

    @get:Rule(order = 1)
    val addActivityRule = createAddActivityToRobolectricRule()

    @get:Rule(order = 2)
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    @get:Rule(order = 3)
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
                onCancel = {},
                onConfirm = { _ -> },
            )
        }

        composeTestRule
            .onNodeWithContentDescription(getString(R.string.action_component_wheel_picker))
            .assertExists()

        composeTestRule
            .onNode(isDialog())
            .captureRoboImage()
    }

    @Test
    fun `Given PopUpPickerStandardWheel confirm button is clicked it propagates the onConfirm and onDismiss action`() {
        var onConfirmHasBeenCalled = false
        var onCancelHasBeenCalled = false

        // Given
        composeTestRule.setContent {
            PopUpPickerStandardWheel(
                title = "Colors",
                info = "My favorite color is",
                pickerValues = colors,
                pickerItemRow = { TextDisplayStandardSmall(it) },
                onCancel = {
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
    fun `Given PopUpPickerStandardWheel cancel button is clicked it propagates the onDismiss action`() {
        var onConfirmHasBeenCalled = false
        var onCancelHasBeenCalled = false

        // Given
        composeTestRule.setContent {
            PopUpPickerStandardWheel(
                title = "Colors",
                info = "My favorite color is",
                pickerValues = colors,
                pickerItemRow = { TextDisplayStandardSmall(it) },
                onCancel = {
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
    fun `When back button is clicked given PopUpPickerStandardWheel runs the onDismiss action`() {
        var hasBeenCalled = false

        composeTestRule.setContent {
            PopUpPickerStandardWheel(
                title = "Colors",
                info = "My favorite color is",
                pickerValues = colors,
                pickerItemRow = { TextDisplayStandardSmall(it) },
                onCancel = {
                    hasBeenCalled = true
                },
                onConfirm = { _ -> },
            )
        }

        Espresso.pressBack()

        assertTrue(hasBeenCalled)
    }

    // @Test
    // fun `It renders PopupPickerTime`() {
    //     composeTestRule.setContent {
    //         PopupPickerTime(
    //             hour = 8,
    //             minute = 0,
    //             onConfirm = { _, _ -> },
    //             onCancel = {},
    //         )
    //     }
    //
    //     composeTestRule
    //         .onNode(isDialog())
    //         .captureRoboImage()
    // }

    @Test
    fun `Given PopupPickerTime confirm button is clicked it propagates the onConfirm and onDismiss action`() {
        var onConfirmHasBeenCalled = false
        var onDismissHasBeenCalled = false

        // Given
        composeTestRule.setContent {
            PopupPickerTime(
                hour = 8,
                minute = 0,
                onConfirm = { _, _ -> onConfirmHasBeenCalled = true },
                onCancel = { onDismissHasBeenCalled = true },
            )
        }

        // When
        composeTestRule
            .onNodeWithText(getString(R.string.confirm))
            .performClick()

        // Then
        onConfirmHasBeenCalled mustBe true
        onDismissHasBeenCalled mustBe true
    }

    @Test
    fun `Given PopupPickerTime cancel button is clicked it propagates the onDismiss action`() {
        var onConfirmHasBeenCalled = false
        var onDismissHasBeenCalled = false

        // Given
        composeTestRule.setContent {
            PopupPickerTime(
                hour = 8,
                minute = 0,
                onConfirm = { _, _ -> onConfirmHasBeenCalled = true },
                onCancel = { onDismissHasBeenCalled = true },
            )
        }

        // When
        composeTestRule
            .onNodeWithText(getString(R.string.cancel))
            .performClick()

        // Then
        onConfirmHasBeenCalled mustBe false
        onDismissHasBeenCalled mustBe true
    }

    @Test
    fun `When back button is clicked given PopupPickerTime runs the onDismiss action`() {
        var onConfirmHasBeenCalled = false
        var onDismissHasBeenCalled = false

        // Given
        composeTestRule.setContent {
            PopupPickerTime(
                hour = 8,
                minute = 0,
                onConfirm = { _, _ -> onConfirmHasBeenCalled = true },
                onCancel = { onDismissHasBeenCalled = true },
            )
        }

        // When
        Espresso.pressBack()

        // Then
        onConfirmHasBeenCalled mustBe false
        onDismissHasBeenCalled mustBe true
    }

    // @Test
    // fun `It renders PopupPickerRepeat`() {
    //     val selectableRepeats = FakeAlarmUIData.defaultSelectableRepeats
    //
    //     composeTestRule.setContent {
    //         PopupPickerRepeat(
    //             title = stringResource(id = R.string.repeat),
    //             selectableRepeats = selectableRepeats,
    //             onConfirm = { _ -> },
    //             onCancel = {}
    //         )
    //     }
    //
    //     composeTestRule
    //         .onNode(isDialog())
    //         .captureRoboImage()
    // }
}
