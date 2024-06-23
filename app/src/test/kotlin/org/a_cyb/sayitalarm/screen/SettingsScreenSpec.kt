/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.screen

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.isRoot
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithContentDescription
import androidx.compose.ui.test.onLast
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.takahirom.roborazzi.RobolectricDeviceQualifiers
import com.github.takahirom.roborazzi.RoborazziRule
import com.github.takahirom.roborazzi.captureRoboImage
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.a_cyb.sayitalarm.R
import org.a_cyb.sayitalarm.presentation.settings.SettingsContract
import org.a_cyb.sayitalarm.roborazziOf
import org.a_cyb.sayitalarm.util.mustBe
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(AndroidJUnit4::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(sdk = [33], qualifiers = RobolectricDeviceQualifiers.ResizableExperimental)
class SettingsScreenSpec {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    @get:Rule
    val roborazziRule = roborazziOf(composeTestRule, RoborazziRule.CaptureType.None)

    private val stateWithContent = SettingsContract.SettingsStateWithContent(
        timeOut = SettingsContract.TimeInput(180, "3 hr"),
        snooze = SettingsContract.TimeInput(15, "15 min"),
        theme = "Light",
    )

    private fun getString(id: Int) = composeTestRule.activity.getString(id)

    @Test
    fun `Given the viewModel state settingsStateWithContent it displays all content`() = runTest {
        // Given
        val viewModel = SettingsViewModelFake(this, stateWithContent)

        // When
        composeTestRule.setContent {
            SettingsScreen(viewModel = viewModel)
        }

        // Then
        with(composeTestRule) {
            onNodeWithText(getString(R.string.settings)).assertExists()
            onNodeWithContentDescription(getString(R.string.action_navigate_back)).assertExists()

            onNodeWithText(getString(R.string.timeout)).assertExists()
            onNodeWithText(getString(R.string.snooze)).assertExists()
            onNodeWithText(getString(R.string.theme)).assertExists()
            onNodeWithText(getString(R.string.about)).assertExists()
            onNodeWithText(getString(R.string.license)).assertExists()
            onNodeWithText(getString(R.string.version)).assertExists()

            onNodeWithText("3 hr").assertExists()
            onNodeWithText("15 min").assertExists()
            onNodeWithText("Light").assertExists()

            onAllNodesWithContentDescription(getString(R.string.action_edit)).fetchSemanticsNodes().size mustBe 5

            onNode(isRoot()).captureRoboImage()
        }
    }

    @Test
    fun `Given panelItemTimeOut edit button is clicked it displays PopUpPickerStandardWheel`() = runTest {
        // Given
        val viewModel = SettingsViewModelFake(this, stateWithContent)

        composeTestRule.setContent {
            SettingsScreen(viewModel = viewModel)
        }

        with(composeTestRule) {
            // When
            onAllNodesWithContentDescription(getString(R.string.action_edit))[0].performClick()

            // Then
            onNodeWithText(getString(R.string.info_timeout)).assertExists()
            onNodeWithContentDescription(getString(R.string.action_component_wheel_picker)).assertExists()

            onAllNodes(isRoot()).onLast().captureRoboImage()
        }
    }

    @Test
    fun `Given timeOut popUpPicker is displayed and onConfirm is clicked it executes setTimeOutCommand`() = runTest {
        // Given
        val viewModel = SettingsViewModelFake(this, stateWithContent)

        composeTestRule.setContent {
            SettingsScreen(viewModel = viewModel)
        }

        with(composeTestRule) {
            onAllNodesWithContentDescription(getString(R.string.action_edit))[0].performClick()

            // When
            onNodeWithText(getString(R.string.confirm)).performClick()
            advanceUntilIdle()

            // Then
            viewModel.executed mustBe SettingsViewModelFake.ExecutedCommand.SET_TIMEOUT
        }
    }

    @Test
    fun `Given panelItemSnooze edit button click it displays PopUpPickerStandardWheel`() = runTest {
        // Given
        val viewModel = SettingsViewModelFake(this, stateWithContent)

        composeTestRule.setContent {
            SettingsScreen(viewModel = viewModel)
        }

        with(composeTestRule) {
            // When
            onAllNodesWithContentDescription(getString(R.string.action_edit))[1]
                .performClick()

            // Then
            onNodeWithText(getString(R.string.info_snooze)).assertExists()
            onNodeWithContentDescription(getString(R.string.action_component_wheel_picker)).assertExists()

            onAllNodes(isRoot()).onLast().captureRoboImage()
        }
    }

    fun `When snooze popUpPicker is displayed and onConfirm click it executes setSnoozeCommand`() = runTest {
        // Given
        val viewModel = SettingsViewModelFake(this, stateWithContent)

        composeTestRule.setContent {
            SettingsScreen(viewModel = viewModel)
        }

        with(composeTestRule) {
            onAllNodesWithContentDescription(getString(R.string.action_edit))[1].performClick()

            // When
            onNodeWithText(getString(R.string.confirm)).performClick()
            advanceUntilIdle()

            // Then
            viewModel.executed mustBe SettingsViewModelFake.ExecutedCommand.SET_SNOOZE
        }
    }

    @Test
    fun `Given panelItemTheme edit button click it displays PopUpPickerStandardWheel`() = runTest {
        // Given
        val viewModel = SettingsViewModelFake(this, stateWithContent)

        composeTestRule.setContent {
            SettingsScreen(viewModel = viewModel)
        }

        with(composeTestRule) {
            // When
            onAllNodesWithContentDescription(getString(R.string.action_edit))[2]
                .performClick()

            // Then
            onNodeWithText("Dark").assertExists()
            onNodeWithContentDescription(getString(R.string.action_component_wheel_picker)).assertExists()

            onAllNodes(isRoot()).onLast().captureRoboImage()
        }
    }

    fun `When theme popUpPicker is displayed and onConfirm click it executes setSnoozeCommand`() = runTest {
        // Given
        val viewModel = SettingsViewModelFake(this, stateWithContent)

        composeTestRule.setContent {
            SettingsScreen(viewModel = viewModel)
        }

        with(composeTestRule) {
            onAllNodesWithContentDescription(getString(R.string.action_edit))[2].performClick()

            // When
            onNodeWithText(getString(R.string.confirm)).performClick()
            advanceUntilIdle()

            // Then
            viewModel.executed mustBe SettingsViewModelFake.ExecutedCommand.SET_THEME
        }
    }

    @Test
    fun `Given the viewModel state Error it displays info panel`() = runTest {
        // Given
        val viewModel = SettingsViewModelFake(this, SettingsContract.InitialError)

        composeTestRule.setContent {
            // When
            SettingsScreen(viewModel = viewModel)
        }

        // Then
        with(composeTestRule) {
            onNodeWithText(getString(R.string.timeout)).assertDoesNotExist()
            onNodeWithText(getString(R.string.snooze)).assertDoesNotExist()
            onNodeWithText(getString(R.string.theme)).assertDoesNotExist()

            onNodeWithText(getString(R.string.about)).assertExists()
            onNodeWithText(getString(R.string.license)).assertExists()
            onNodeWithText(getString(R.string.version)).assertExists()
        }
    }

    @Test
    fun `Given viewModel state error it displays error message`() = runTest {
        // Given
        val state = SettingsContract.InitialError
        val viewModel = SettingsViewModelFake(this, state)

        // When
        composeTestRule.setContent {
            SettingsScreen(viewModel = viewModel)
        }

        with(composeTestRule) {
            // Then
            onNodeWithText(getString(R.string.info_settings_initialize_error))
                .assertExists()

            onNode(isRoot()).captureRoboImage()
        }
    }
}
