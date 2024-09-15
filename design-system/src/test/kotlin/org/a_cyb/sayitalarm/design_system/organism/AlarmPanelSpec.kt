/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.design_system.organism

import android.content.Context
import android.media.RingtoneManager
import androidx.activity.ComponentActivity
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.assert
import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.assertIsFocused
import androidx.compose.ui.test.hasScrollAction
import androidx.compose.ui.test.hasSetTextAction
import androidx.compose.ui.test.isDialog
import androidx.compose.ui.test.isSelectable
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithContentDescription
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onParent
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.takahirom.roborazzi.ExperimentalRoborazziApi
import com.github.takahirom.roborazzi.RobolectricDeviceQualifiers
import com.github.takahirom.roborazzi.RoborazziRule
import com.github.takahirom.roborazzi.captureRoboImage
import com.github.takahirom.roborazzi.captureScreenRoboImage
import org.a_cyb.sayitalarm.design_system.FakeAlarmUIData
import org.a_cyb.sayitalarm.design_system.R
import org.a_cyb.sayitalarm.design_system.organism.CommandExecutorFake.InvokedType
import org.a_cyb.sayitalarm.design_system.roborazziOf
import org.a_cyb.sayitalarm.presentation.contracts.AlarmPanelContract.WeeklyRepeatUI
import org.a_cyb.sayitalarm.util.mustBe
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode
import org.robolectric.shadow.api.Shadow
import org.robolectric.shadows.ShadowActivity
import kotlin.test.assertEquals

@OptIn(ExperimentalRoborazziApi::class)
@RunWith(AndroidJUnit4::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(sdk = [33], qualifiers = RobolectricDeviceQualifiers.ResizableExperimental)
class AlarmPanelSpec {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    @get:Rule
    val roborazziRule = roborazziOf(composeTestRule, RoborazziRule.CaptureType.None)

    private val alarmUI = FakeAlarmUIData.defaultAlarmUI

    private fun getString(id: Int) = composeTestRule.activity.getString(id)

    @Test
    fun `It displays AlarmUI`() {
        // Given
        val alarmUI = alarmUI.copy(
            weeklyRepeatUI = WeeklyRepeatUI("Everyday", alarmUI.weeklyRepeatUI.selectableRepeats),
        )

        // When
        composeTestRule.setContent {
            AlarmPanel(alarmUI = alarmUI) {}
        }

        // Then
        with(composeTestRule) {
            onNodeWithText(getString(R.string.label)).assertExists()
            onNodeWithText(getString(R.string.repeat)).assertExists()
            onNodeWithText(getString(R.string.ringtone)).assertExists()
            onNodeWithText(getString(R.string.alert_type)).assertExists()
            onNodeWithText(alarmUI.timeUI.formattedTime).assertExists()
            onNodeWithText(alarmUI.weeklyRepeatUI.formatted).assertExists()
            onNodeWithText(alarmUI.ringtoneUI.title).assertExists()
            onNodeWithText("Sound and vibration").assertExists()
            onNodeWithText(getString(R.string.info_scripts)).assertExists()
            onAllNodesWithContentDescription(getString(R.string.action_edit)).fetchSemanticsNodes().size mustBe 4
            onNodeWithContentDescription(getString(R.string.action_info)).assertExists().assertHasClickAction()

            onRoot().captureRoboImage()
        }
    }

    @Test
    fun `When TimePanel is clicked, it displays PopupPickerTime`() {
        // Given
        composeTestRule.setContent { AlarmPanel(alarmUI = alarmUI) {} }

        // When
        composeTestRule.onNodeWithText(alarmUI.timeUI.formattedTime).performClick()

        // Then
        composeTestRule.onNodeWithText("PM").assertExists()
        composeTestRule.onNodeWithText(getString(R.string.confirm)).assertExists().assertHasClickAction()
        composeTestRule.onNodeWithText(getString(R.string.cancel)).assertExists().assertHasClickAction()

        captureScreenRoboImage()
    }

    @Test
    fun `When Label row edit button is clicked, it displays PopupPickerLabel`() {
        // Given
        composeTestRule.setContent { AlarmPanel(alarmUI = alarmUI) {} }

        // When
        composeTestRule
            .onAllNodesWithContentDescription(getString(R.string.action_edit))[0].performClick()

        // Then
        composeTestRule.onNode(hasSetTextAction()).assertExists().assertIsFocused()
        composeTestRule.onNodeWithText(getString(R.string.confirm)).assertExists().assertHasClickAction()
        composeTestRule.onNodeWithText(getString(R.string.cancel)).assertExists().assertHasClickAction()

        captureScreenRoboImage()
    }

    @Test
    fun `When WeeklyRepeat row edit button is clicked it displays PopupPickerRepeat`() {
        // Given
        composeTestRule.setContent { AlarmPanel(alarmUI = alarmUI) {} }

        // When
        composeTestRule
            .onAllNodesWithContentDescription(getString(R.string.action_edit))[1].performClick()

        // Then
        with(composeTestRule) {
            alarmUI.weeklyRepeatUI.selectableRepeats.forEach {
                onNodeWithText(it.name).assertExists()
            }
            onAllNodes(isSelectable()).fetchSemanticsNodes().size mustBe 7
            onNodeWithText(getString(R.string.confirm)).assertExists()
            onNodeWithText(getString(R.string.cancel)).assertExists()
        }
        captureScreenRoboImage()
    }

    @Test
    fun `When Ringtone row edit button is clicked, it displays PopupPickerRingtone`() {
        // Given
        var context: Context? = null

        composeTestRule.setContent {
            context = LocalContext.current
            AlarmPanel(alarmUI = alarmUI) {}
        }

        val launchedActivity = Shadow.extract<ShadowActivity>(context as ComponentActivity)

        // When
        composeTestRule
            .onAllNodesWithContentDescription(getString(R.string.action_edit))[2].performClick()
        composeTestRule.waitForIdle()

        // Then
        assertEquals(RingtoneManager.ACTION_RINGTONE_PICKER, launchedActivity.nextStartedActivity.action)
    }

    @Test
    fun `When AlertType row edit button is clicked, it displays PopupPickerStandardWheel`() {
        // Given
        composeTestRule.setContent { AlarmPanel(alarmUI = alarmUI) {} }

        // When
        composeTestRule
            .onAllNodesWithContentDescription(getString(R.string.action_edit))[3].performClick()

        // Then
        with(composeTestRule) {
            onNodeWithText("Sound only").assertExists()
            onNodeWithText("Sound only").onParent().assert(hasScrollAction())
            onNodeWithText(getString(R.string.confirm)).assertExists().assertHasClickAction()
            onNodeWithText(getString(R.string.cancel)).assertExists().assertHasClickAction()
        }
        captureScreenRoboImage()
    }

    @Test
    fun `When say it scripts info button is clicked it displays informational text`() {
        // Given
        composeTestRule.setContent {
            AlarmPanel(alarmUI = alarmUI.copy(sayItScripts = listOf("Say it script"))) {}
        }

        // When
        composeTestRule.onNodeWithContentDescription(getString(R.string.action_info)).performClick()

        // Then
        composeTestRule.onNodeWithText(getString(R.string.info_scripts)).assertExists()
        composeTestRule.onNodeWithContentDescription(getString(R.string.action_collapse)).assertExists()
            .assertHasClickAction()
    }

    @Test
    fun `When say it scripts info text is displayed and IconButtonRowCollapse is clicked it hides info text`() {
        // Given
        composeTestRule.setContent { AlarmPanel(alarmUI = alarmUI) {} }
        composeTestRule.onNodeWithText(getString(R.string.info_scripts)).assertExists()

        // When
        composeTestRule.onNodeWithContentDescription(getString(R.string.action_collapse)).performClick()

        // Then
        composeTestRule.onNodeWithText(getString(R.string.info_scripts)).assertDoesNotExist()
    }

    @Test
    fun `When say it script panel + button is clicked, it displays PopupPickerSayItScript`() {
        // Given
        composeTestRule.setContent { AlarmPanel(alarmUI = alarmUI) {} }

        // When
        composeTestRule.onNodeWithContentDescription(getString(R.string.action_add_script)).performClick()

        // Then
        with(composeTestRule) {
            onAllNodesWithText(getString(R.string.info_scripts_only_letter)).fetchSemanticsNodes().size mustBe 2
            onNode(hasSetTextAction()).assertExists()
            onNodeWithText(getString(R.string.confirm)).assertExists()
            onNodeWithText(getString(R.string.cancel)).assertExists()
            onNodeWithText(getString(R.string.delete)).assertExists()
        }
        captureScreenRoboImage()
    }

    @Test
    fun `When more then one sayItScripts is given, it displays scripts in clickable row`() {
        // Given
        val sayItScripts = listOf("This is the first test script", "This is the second test script")

        // When
        composeTestRule.setContent {
            AlarmPanel(alarmUI = alarmUI.copy(sayItScripts = sayItScripts)) { }
        }

        // Then
        sayItScripts.forEach {
            composeTestRule.onNodeWithText(it).assertExists().assertHasClickAction()
        }

        captureScreenRoboImage()
    }

    @Test
    fun `When sayItScript row is clicked, it displays PopupPickerSayItScript with the script`() {
        // Given
        val sayItScripts = listOf("This is the first test script", "This is the second test script")
        composeTestRule.setContent {
            AlarmPanel(alarmUI = alarmUI.copy(sayItScripts = sayItScripts)) { }
        }

        // When
        composeTestRule.onNodeWithText(sayItScripts[0]).performClick()

        // Then
        composeTestRule.onNodeWithText(getString(R.string.info_scripts_only_letter)).assertExists()

        captureScreenRoboImage()
    }

    /*
     * Dismiss popup picker logic
     * */
    @Test
    fun `When PopupPickerTime is displayed and onCancel is clicked it dismiss the picker`() {
        with(composeTestRule) {
            // Given
            setContent {
                AlarmPanel(
                    alarmUI = alarmUI,
                    executor = {},
                )
            }

            onNodeWithText("8:00 AM")
                .performClick()
            onNodeWithText("PM")
                .assertExists()

            // When
            onNodeWithText(getString(R.string.cancel))
                .performClick()

            // Then
            onNode(isDialog())
                .assertDoesNotExist()
            onNodeWithText("PM")
                .assertDoesNotExist()
        }
    }

    @Test
    fun `When PopupPickerTime is displayed and onConfirm is clicked it dismiss the picker`() {
        with(composeTestRule) {
            // Given
            setContent {
                AlarmPanel(
                    alarmUI = alarmUI,
                    executor = {},
                )
            }

            onNodeWithText("8:00 AM")
                .performClick()
            onNodeWithText("PM")
                .assertExists()

            // When
            onNodeWithText(getString(R.string.confirm))
                .performClick()

            // Then
            onNode(isDialog())
                .assertDoesNotExist()
            onNodeWithText("PM")
                .assertDoesNotExist()
        }
    }

    @Test
    fun `When PopupPickerSayItScript is displayed and onConfirm is clicked it dismiss the picker`() {
        with(composeTestRule) {
            // Given
            setContent {
                AlarmPanel(
                    alarmUI = alarmUI,
                    executor = {},
                )
            }

            onNodeWithContentDescription(getString(R.string.action_add_script)).performClick()

            // When
            onNodeWithText(getString(R.string.confirm)).performClick()

            // Then
            onNode(isDialog()).assertDoesNotExist()
        }
    }

    /*
     *   Command execute tests
     * */
    @Test
    fun `When PopupPickerTime is displayed and onConfirm is clicked it runs executor setTimeOut`() {
        // Given
        val commandExecutor = CommandExecutorFake()

        with(composeTestRule) {
            setContent {
                AlarmPanel(
                    alarmUI = alarmUI,
                    executor = { commandExecutor.runCommand(it) },
                )
            }

            // Open PopupPickerTime
            onNodeWithText("8:00 AM")
                .performClick()

            // When
            onNodeWithText(getString(R.string.confirm))
                .performClick()
        }

        // Then
        commandExecutor.invokedType mustBe InvokedType.SET_TIME
    }

    @Test
    fun `When PopupPickerRepeat is displayed and onConfirm is clicked it runs executor setWeeklyRepeat`() {
        // Given
        val commandExecutor = CommandExecutorFake()

        with(composeTestRule) {
            setContent {
                AlarmPanel(
                    alarmUI = alarmUI,
                    executor = { commandExecutor.runCommand(it) },
                )
            }

            // Open PopupPickerRepeat
            onAllNodesWithContentDescription(getString(R.string.action_edit))[1].performClick()

            // When
            onNodeWithText(getString(R.string.confirm)).performClick()

            // Then
            commandExecutor.invokedType mustBe InvokedType.SET_WEEKLY_REPEAT
        }
    }

    @Test
    fun `When PopupPickerSayItScript is displayed and onConfirm is clicked it runs executor setScripts`() {
        // Given
        val commandExecutor = CommandExecutorFake()

        with(composeTestRule) {
            setContent {
                AlarmPanel(
                    alarmUI = alarmUI,
                    executor = { commandExecutor.runCommand(it) },
                )
            }

            onNodeWithContentDescription(getString(R.string.action_add_script)).performClick()

            // When
            onNodeWithText(getString(R.string.confirm)).performClick()

            // Then
            commandExecutor.invokedType mustBe InvokedType.SET_SCRIPTS
        }
    }

    @Test
    fun `When PopupPickerAlertType is displayed and onConfirm is clicked it runs executor setAlertType`() {
        // Given
        val commandExecutor = CommandExecutorFake()

        with(composeTestRule) {
            setContent {
                AlarmPanel(
                    alarmUI = alarmUI,
                    executor = { commandExecutor.runCommand(it) },
                )
            }

            onAllNodesWithContentDescription(getString(R.string.action_edit))[3]
                .performClick()

            // When
            onNodeWithText(getString(R.string.confirm)).performClick()

            // Then
            commandExecutor.invokedType mustBe InvokedType.SET_ALERT_TYPE
        }
    }
}
