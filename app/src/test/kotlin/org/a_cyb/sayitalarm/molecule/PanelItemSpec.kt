/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.molecule

import androidx.activity.ComponentActivity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performClick
import androidx.test.espresso.Espresso
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.takahirom.roborazzi.RoborazziRule
import com.github.takahirom.roborazzi.captureRoboImage
import org.a_cyb.sayitalarm.R
import org.a_cyb.sayitalarm.atom.DialogStandardFitContent
import org.a_cyb.sayitalarm.atom.IconButtonEdit
import org.a_cyb.sayitalarm.atom.TextDisplayStandardLarge
import org.a_cyb.sayitalarm.atom.TextTitleDangerMedium
import org.a_cyb.sayitalarm.roborazziOf
import org.a_cyb.sayitalarm.util.mustBe
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
    fun `Given PanelRowSpec displays without a value`() {
        composeTestRule.setContent {
            PanelItemStandard(
                valueLabel = stringResource(id = R.string.about),
                afterContent = { IconButtonEdit {} },
            )
        }
    }

    @Test
    fun `Given PanelRowSpec displays without an afterContent`() {
        composeTestRule.setContent {
            PanelItemStandard(
                valueLabel = stringResource(id = R.string.version),
                value = "1.0",
            )
        }
    }

    @Test
    fun `It renders PanelItemWithPopUpPicker`() {
        composeTestRule.setContent {
            PanelItemWithPopupPickerStandardWheel(
                title = getString(id = R.string.timeout),
                info = getString(id = R.string.info_timeout),
                values = listOf("3hr"),
                selectedItemIdx = 0,
                popUpPickerOnConfirm = { _ -> }
            )
        }

        composeTestRule.onRoot()
            .captureRoboImage()
    }

    @Test
    fun `Given PanelItemWithPopupPicker IconButtonEdit is clicked it displays PopUpPicker`() {
        // Given
        composeTestRule.setContent {
            PanelItemWithPopupPickerStandardWheel(
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
        composeTestRule.onNodeWithContentDescription(getString(R.string.action_component_wheel_picker))
            .assertExists()
    }

    @Test
    fun `Given PanelItemPopupPicker editButton is clicked it displays popupPicker`() {
        // Given
        composeTestRule.setContent {
            PanelItemWithPopupPicker(valueLabel = "Test", value = "") { onDismiss ->
                DialogStandardFitContent(onDismiss = onDismiss) {
                    TextDisplayStandardLarge(text = "PopupPicker")
                }
            }
        }

        // When
        composeTestRule.onNodeWithContentDescription(getString(R.string.action_edit))
            .performClick()

        // Then
        composeTestRule.onNodeWithText("PopupPicker").assertExists()
    }

    @Test
    fun `Given PanelItemPopupPicker when popupPicker is displayed and dismiss actions received it runs onDismiss action`() {
        // Given
        val text = "PopupPicker"

        composeTestRule.setContent {
            PanelItemWithPopupPicker(valueLabel = "Test", value = "") { onDismiss ->
                DialogStandardFitContent(onDismiss = onDismiss) {
                    TextDisplayStandardLarge(text = text)
                }
            }
        }
        composeTestRule.onNodeWithContentDescription(getString(R.string.action_edit))
            .performClick()
        composeTestRule.onNodeWithText(text)
            .assertExists()

        // When
        Espresso.pressBack()

        // Then
        composeTestRule.onNodeWithText(text).assertDoesNotExist()
    }

    @Test
    fun `When PanelItemStandardClickable is clicked it runs onClick action`() {
        var hasBeenCalled = false

        with(composeTestRule) {
            // Given
            setContent {
                PanelItemStandardClickable(
                    valueLabel = "Test",
                    onClick = { hasBeenCalled = true }
                )
            }

            // When
            onNodeWithText("Test")
                .performClick()

            // Then
            hasBeenCalled mustBe true
        }
    }

    @Test
    fun `When PanelItemClickableBordered is clicked it runs onClick action`() {
        var hasBeenCalled: Boolean = false

        with(composeTestRule) {
            // Given
            setContent {
                PanelItemClickableBordered(
                    contentDescription = "",
                    onClick = { hasBeenCalled = true },
                ) {
                    TextTitleDangerMedium(text = "Test")
                }
            }

            // When
            onNodeWithText("Test")
                .performClick()

            // Then
            hasBeenCalled mustBe true
        }
    }
}
