/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.screen

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithContentDescription
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.takahirom.roborazzi.ExperimentalRoborazziApi
import com.github.takahirom.roborazzi.RobolectricDeviceQualifiers
import com.github.takahirom.roborazzi.RoborazziRule
import com.github.takahirom.roborazzi.captureRoboImage
import com.github.takahirom.roborazzi.captureScreenRoboImage
import kotlinx.coroutines.test.runTest
import org.a_cyb.sayitalarm.R
import org.a_cyb.sayitalarm.presentation.SettingsContract.SettingsState.Error
import org.a_cyb.sayitalarm.presentation.SettingsContract.SettingsState.Success
import org.a_cyb.sayitalarm.presentation.SettingsContract.SettingsUI
import org.a_cyb.sayitalarm.presentation.SettingsContract.TimeInput
import org.a_cyb.sayitalarm.roborazziOf
import org.a_cyb.sayitalarm.screen.SettingsViewModelFake.ExecutedCommand
import org.a_cyb.sayitalarm.util.mustBe
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode

@OptIn(ExperimentalRoborazziApi::class)
@RunWith(AndroidJUnit4::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(sdk = [33], qualifiers = RobolectricDeviceQualifiers.ResizableExperimental)
class SettingsScreenSpec {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    @get:Rule
    val roborazziRule = roborazziOf(composeTestRule, RoborazziRule.CaptureType.None)

    private val successSate = Success(
        SettingsUI(
            timeOut = TimeInput(180, "3 hr"),
            snooze = TimeInput(15, "15 min"),
            theme = "Light",
        )
    )

    private fun getString(id: Int) = composeTestRule.activity.getString(id)

    @Test
    fun `When the viewModel is in success state it renders SettingsUI`() = runTest {
        // Given
        val viewModel = SettingsViewModelFake(this, successSate)

        with(composeTestRule) {
            // When
            setContent {
                SettingsScreen(
                    viewModel = viewModel,
                    navigateToList = {}
                )
            }

            // Then
            onNodeWithText("3 hr")
                .assertExists()
            onNodeWithText("15 min")
                .assertExists()
            onNodeWithText("Light")
                .assertExists()

            // Verify all edit button is displayed
            onAllNodesWithContentDescription(getString(R.string.action_edit))
                .fetchSemanticsNodes()
                .size mustBe 5

            onRoot()
                .captureRoboImage()
        }
    }

    @Test
    fun `When viewModel is in error state it displays error message`() = runTest {
        // Given
        val viewModel = SettingsViewModelFake(this, Error)

        // When
        with(composeTestRule) {
            setContent {
                SettingsScreen(
                    viewModel = viewModel,
                    navigateToList = {}
                )
            }

            // Then
            onNodeWithText(getString(R.string.info_settings_initialize_error))
                .assertExists()

            onRoot()
                .captureRoboImage()
        }
    }

    @Test
    fun `When the viewModel is in Error state it displays only info panel`() = runTest {
        // Given
        val viewModel = SettingsViewModelFake(this, Error)

        with(composeTestRule) {
            setContent {
                // When
                SettingsScreen(
                    viewModel = viewModel,
                    navigateToList = {}
                )
            }

            onNodeWithText(getString(R.string.timeout))
                .assertDoesNotExist()
            onNodeWithText(getString(R.string.snooze))
                .assertDoesNotExist()
            onNodeWithText(getString(R.string.theme))
                .assertDoesNotExist()

            onNodeWithText(getString(R.string.about))
                .assertExists()
            onNodeWithText(getString(R.string.license))
                .assertExists()
            onNodeWithText(getString(R.string.version))
                .assertExists()
        }
    }

    @Test
    fun `When PanelItemTimeOut edit button is clicked it displays PopUpPickerStandardWheel`() = runTest {
        // Given
        val viewModel = SettingsViewModelFake(this, successSate)

        with(composeTestRule) {
            setContent {
                SettingsScreen(
                    viewModel = viewModel,
                    navigateToList = {},
                )
            }

            // When
            onAllNodesWithContentDescription(getString(R.string.action_edit))[0]
                .performClick()

            // Then
            onNodeWithContentDescription(getString(R.string.action_component_wheel_picker))
                .assertExists()

            captureScreenRoboImage()
        }
    }

    @Test
    fun `When panelItemSnooze edit button is clicked it displays PopUpPickerStandardWheel`() = runTest {
        // Given
        val viewModel = SettingsViewModelFake(this, successSate)

        with(composeTestRule) {
            setContent {
                SettingsScreen(
                    viewModel = viewModel,
                    navigateToList = {}
                )
            }

            // When
            onAllNodesWithContentDescription(getString(R.string.action_edit))[1]
                .performClick()

            // Then
            onNodeWithText(getString(R.string.info_snooze))
                .assertExists()
            onNodeWithContentDescription(getString(R.string.action_component_wheel_picker))
                .assertExists()

            captureScreenRoboImage()
        }
    }

    @Test
    fun `Given panelItemTheme edit button click it displays PopUpPickerStandardWheel`() = runTest {
        // Given
        val viewModel = SettingsViewModelFake(this, successSate)

        composeTestRule.setContent {
            SettingsScreen(
                viewModel = viewModel,
                navigateToList = {}
            )
        }

        with(composeTestRule) {
            // When
            onAllNodesWithContentDescription(getString(R.string.action_edit))[2]
                .performClick()

            // Then
            onNodeWithText("Dark").assertExists()
            onNodeWithContentDescription(getString(R.string.action_component_wheel_picker)).assertExists()

            captureScreenRoboImage()
        }
    }

    /*
    * Command execution test
    * */

    @Test
    fun `When timeOut PopUpPicker is displayed and confirm is clicked it executes setTimeOutCommand`() = runTest {
        // Given
        val viewModel = SettingsViewModelFake(this, successSate)

        with(composeTestRule) {
            setContent {
                SettingsScreen(
                    viewModel = viewModel,
                    navigateToList = {}
                )
            }

            onAllNodesWithContentDescription(getString(R.string.action_edit))[0]
                .performClick()

            // When
            onNodeWithText(getString(R.string.confirm))
                .performClick()

            // Then
            viewModel.executed mustBe ExecutedCommand.SET_TIMEOUT
        }
    }

    @Test
    fun `When snooze popUpPicker is displayed and confirm click it executes setSnoozeCommand`() = runTest {
        // Given
        val viewModel = SettingsViewModelFake(this, successSate)

        with(composeTestRule) {
            setContent {
                SettingsScreen(
                    viewModel = viewModel,
                    navigateToList = {}
                )
            }

            onAllNodesWithContentDescription(getString(R.string.action_edit))[1]
                .performClick()

            // When
            onNodeWithText(getString(R.string.confirm))
                .performClick()

            // Then
            viewModel.executed mustBe ExecutedCommand.SET_SNOOZE
        }
    }
}
