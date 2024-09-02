/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.design_system.screen

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithContentDescription
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onParent
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollToNode
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.takahirom.roborazzi.ExperimentalRoborazziApi
import com.github.takahirom.roborazzi.RobolectricDeviceQualifiers
import com.github.takahirom.roborazzi.RoborazziRule
import com.github.takahirom.roborazzi.captureRoboImage
import com.github.takahirom.roborazzi.captureScreenRoboImage
import kotlinx.coroutines.test.runTest
import org.a_cyb.sayitalarm.design_system.R
import org.a_cyb.sayitalarm.design_system.roborazziOf
import org.a_cyb.sayitalarm.design_system.screen.SettingsViewModelFake.ExecutedCommand
import org.a_cyb.sayitalarm.presentation.SettingsContract.SettingsState.Error
import org.a_cyb.sayitalarm.presentation.SettingsContract.SettingsState.Success
import org.a_cyb.sayitalarm.presentation.SettingsContract.SettingsUI
import org.a_cyb.sayitalarm.presentation.SettingsContract.TimeInput
import org.a_cyb.sayitalarm.util.mustBe
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode

@OptIn(ExperimentalRoborazziApi::class)
@RunWith(AndroidJUnit4::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(sdk = [33], qualifiers = RobolectricDeviceQualifiers.Pixel7)
class SettingsScreenSpec {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    @get:Rule
    val roborazziRule = roborazziOf(composeTestRule, RoborazziRule.CaptureType.None)

    private val versionName = "1.0"
    private val successSate = Success(
        SettingsUI(
            timeOut = TimeInput(180, "3 hr"),
            snooze = TimeInput(15, "15 min"),
            theme = "Light",
        )
    )

    private fun getString(id: Int) = composeTestRule.activity.getString(id)

    @Test
    fun `When the viewModel is in success state, it renders SettingsUI`() = runTest {
        // Given & When
        composeTestRule.setContent {
            SettingsScreen(SettingsViewModelFake(successSate), versionName) {}
        }

        // Then
        with(composeTestRule) {
            onNodeWithText("3 hr").assertExists()
            onNodeWithText("15 min").assertExists()
            onNodeWithText("Light").assertExists()
            onAllNodesWithContentDescription(getString(R.string.action_edit)).fetchSemanticsNodes().size mustBe 5
            onRoot().captureRoboImage()
        }
    }

    @Test
    fun `When viewModel is in error state, it displays error message and InfoPanel`() {
        // Given & When
        composeTestRule.setContent {
            SettingsScreen(SettingsViewModelFake(Error), versionName) {}
        }

        // Then
        with(composeTestRule) {
            onNodeWithText(getString(R.string.info_settings_initialize_error)).assertExists()
            onNodeWithText(getString(R.string.about)).assertExists()
            onNodeWithText(getString(R.string.licenses)).assertExists()
            onNodeWithText(getString(R.string.version)).assertExists()

            onNodeWithText(getString(R.string.timeout)).assertDoesNotExist()
            onNodeWithText(getString(R.string.snooze)).assertDoesNotExist()
            onNodeWithText(getString(R.string.theme)).assertDoesNotExist()

            onRoot().captureRoboImage()
        }
    }

    @Test
    fun `When PanelItemTimeOut edit button is clicked, it displays PopUpPickerStandardWheel`() {
        // Given
        composeTestRule.setContent {
            SettingsScreen(SettingsViewModelFake(successSate), versionName) {}
        }

        // When
        composeTestRule.onAllNodesWithContentDescription(getString(R.string.action_edit))[0].performClick()

        // Then
        composeTestRule.onNodeWithContentDescription(getString(R.string.action_component_wheel_picker)).assertExists()
        captureScreenRoboImage()
    }

    @Test
    fun `When panelItemSnooze edit button is clicked, it displays PopUpPickerStandardWheel`() {
        // Given
        composeTestRule.setContent {
            SettingsScreen(SettingsViewModelFake(successSate), versionName) {}
        }

        // When
        composeTestRule.onAllNodesWithContentDescription(getString(R.string.action_edit))[1].performClick()

        // Then
        composeTestRule.onNodeWithText(getString(R.string.info_snooze)).assertExists()
        composeTestRule.onNodeWithContentDescription(getString(R.string.action_component_wheel_picker)).assertExists()
        captureScreenRoboImage()
    }

    @Test
    fun `When panelItemTheme edit button is clicked, it displays PopUpPickerStandardWheel`() {
        // Given
        composeTestRule.setContent {
            SettingsScreen(SettingsViewModelFake(successSate), versionName) {}
        }

        // When
        composeTestRule.onAllNodesWithContentDescription(getString(R.string.action_edit))[2].performClick()

        // Then
        composeTestRule.onNodeWithText("Dark").assertExists()
        composeTestRule.onNodeWithContentDescription(getString(R.string.action_component_wheel_picker)).assertExists()
        captureScreenRoboImage()
    }

    @Test
    fun `When panelItemAbout edit button is clicked, it displays AboutDialog`() {
        // Given
        composeTestRule.setContent {
            SettingsScreen(SettingsViewModelFake(successSate), versionName) {}
        }

        // When
        composeTestRule.onAllNodesWithContentDescription(getString(R.string.action_edit))[3].performClick()

        // Then
        with(composeTestRule) {
            onNodeWithText(getString(R.string.info_about)).assertExists()
            onNodeWithText(getString(R.string.email)).assertExists().assertHasClickAction()
            onNodeWithText(getString(R.string.google_play)).assertExists().assertHasClickAction()
            onNodeWithText(getString(R.string.github)).assertExists().assertHasClickAction()
        }
        captureScreenRoboImage()
    }

    @Test
    fun `When navigateBack button is clicked, executes navigateToList`() {
        // Given
        var hasBennCalled = false
        composeTestRule.setContent {
            SettingsScreen(SettingsViewModelFake(successSate), versionName) {
                hasBennCalled = true
            }
        }

        // When
        composeTestRule.onNodeWithContentDescription(getString(R.string.action_navigate_back)).performClick()

        // Then
        hasBennCalled mustBe true
    }

    /*
    * Command execution test
    * */

    @Test
    fun `When timeOutPopUpPicker is displayed and confirm is clicked, it executes setTimeOutCommand`() = runTest {
        // Given
        val viewModel = SettingsViewModelFake(successSate)
        composeTestRule.setContent { SettingsScreen(viewModel, versionName) {} }
        composeTestRule.onAllNodesWithContentDescription(getString(R.string.action_edit))[0].performClick()

        // When
        composeTestRule.onNodeWithText(getString(R.string.confirm)).performClick()

        // Then
        viewModel.executed mustBe ExecutedCommand.SET_TIMEOUT
    }

    @Test
    fun `When snooze popUpPicker is displayed and confirm is clicked, it executes setSnoozeCommand`() = runTest {
        // Given
        val viewModel = SettingsViewModelFake(successSate)
        composeTestRule.setContent { SettingsScreen(viewModel, versionName) {} }
        composeTestRule.onAllNodesWithContentDescription(getString(R.string.action_edit))[1].performClick()

        // When
        composeTestRule.onNodeWithText(getString(R.string.confirm)).performClick()

        // Then
        viewModel.executed mustBe ExecutedCommand.SET_SNOOZE
    }

    @Test
    fun `When AboutDialog is displayed and email is clicked, it executes sendEmailCommand`() {
        // Given
        val viewModel = SettingsViewModelFake(successSate)
        with(composeTestRule) {
            setContent { SettingsScreen(viewModel, versionName) {} }
            onAllNodesWithContentDescription(getString(R.string.action_edit))[3].performClick()
            onNodeWithText(getString(R.string.info_about)).onParent()
                .performScrollToNode(hasText(getString(R.string.email)))
        }

        // When
        composeTestRule.onNodeWithText(getString(R.string.email)).performClick()

        // Then
        viewModel.executed mustBe ExecutedCommand.SEND_EMAIL
    }

    @Test
    fun `When AboutDialog is displayed and GooglePlay is clicked, it executes openGooglePlay`() {
        // Given
        val viewModel = SettingsViewModelFake(successSate)
        with(composeTestRule) {
            setContent { SettingsScreen(viewModel, versionName) {} }
            onAllNodesWithContentDescription(getString(R.string.action_edit))[3].performClick()
            onNodeWithText(getString(R.string.info_about)).onParent()
                .performScrollToNode(hasText(getString(R.string.google_play)))
        }

        // When
        composeTestRule.onNodeWithText(getString(R.string.google_play)).performClick()

        // Then
        viewModel.executed mustBe ExecutedCommand.OPEN_GOOGLE_PLAY
    }

    @Test
    fun `When AboutDialog is displayed and GitHub is clicked, it executes openGitHub`() {
        // Given
        val viewModel = SettingsViewModelFake(successSate)
        with(composeTestRule) {
            setContent { SettingsScreen(viewModel, versionName) {} }
            onAllNodesWithContentDescription(getString(R.string.action_edit))[3].performClick()
            onNodeWithText(getString(R.string.info_about)).onParent()
                .performScrollToNode(hasText(getString(R.string.github)))
        }

        // When
        composeTestRule.onNodeWithText(getString(R.string.github)).performClick()

        // Then
        viewModel.executed mustBe ExecutedCommand.OPEN_GITHUB
    }
}
