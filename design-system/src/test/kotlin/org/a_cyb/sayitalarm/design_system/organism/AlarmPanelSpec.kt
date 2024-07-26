/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.design_system.organism

import kotlin.test.assertEquals
import android.content.Context
import android.media.RingtoneManager
import androidx.activity.ComponentActivity
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.assertIsFocused
import androidx.compose.ui.test.assertIsNotFocused
import androidx.compose.ui.test.hasSetTextAction
import androidx.compose.ui.test.isDialog
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithContentDescription
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performImeAction
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
import org.a_cyb.sayitalarm.presentation.AlarmPanelContract.SelectableRepeat
import org.a_cyb.sayitalarm.presentation.AlarmPanelContract.WeeklyRepeatUI
import org.a_cyb.sayitalarm.util.mustBe
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode
import org.robolectric.shadow.api.Shadow
import org.robolectric.shadows.ShadowActivity

@OptIn(ExperimentalRoborazziApi::class)
@RunWith(AndroidJUnit4::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(sdk = [33], qualifiers = RobolectricDeviceQualifiers.ResizableExperimental)
class AlarmPanelSpec {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    @get:Rule
    val roborazziRule = roborazziOf(composeTestRule, RoborazziRule.CaptureType.None)

    private fun stringRes(id: Int) = composeTestRule.activity.getString(id)

    @Test
    fun `It renders AlarmPanel`() {
        with(composeTestRule) {
            setContent {
                AlarmPanel(
                    alarmUI = alarmUI,
                    executor = {}
                )
            }

            onRoot()
                .captureRoboImage()
        }
    }

    @Test
    fun `It displays AlarmUI`() {
        val everyday = alarmUI.weeklyRepeatUI.selectableRepeats
            .map { SelectableRepeat(it.name, it.code, true) }
        val alarmUI = alarmUI.copy(
            weeklyRepeatUI = WeeklyRepeatUI("Everyday", everyday),
            label = "Test Label",
            sayItScripts = listOf("Test script.")
        )

        with(composeTestRule) {
            // When
            setContent {
                AlarmPanel(
                    alarmUI = alarmUI,
                    executor = {}
                )
            }

            // Then
            onNodeWithText("8:00 AM")
                .assertExists()
            onNodeWithText("Everyday")
                .assertExists()
            onNodeWithText("Test Label")
                .assertExists()
            onNodeWithText("Drip")
                .assertExists()
            onNodeWithText("Sound and vibration")
                .assertExists()
            onNodeWithText("Test script.")
                .assertExists()
        }
    }

    @Test
    fun `When TimePanel is clicked it displays PopupPickerTime`() {
        with(composeTestRule) {
            // Given
            setContent {
                AlarmPanel(
                    alarmUI = alarmUI,
                    executor = {}
                )
            }

            // When
            onNodeWithText("8:00 AM")
                .performClick()

            // Then
            onNodeWithText("PM")
                .assertExists()
        }

        captureScreenRoboImage()
    }

    @Test
    fun `When Label row is clicked it gains focus and displays the keyboard`() {
        with(composeTestRule) {
            // Given
            setContent {
                AlarmPanel(
                    alarmUI = alarmUI,
                    executor = {}
                )
            }

            // When
            onNode(hasSetTextAction()) // hasImeAction
                .performClick()

            // Then
            onNode(hasSetTextAction())
                .assertIsFocused()

            // waitUntil {
            //     composeTestRule.activity.window.decorView.rootWindowInsets
            //         .isVisible(android.view.WindowInsets.Type.ime())
            // }

            onRoot()
                .captureRoboImage()
        }
    }

    @Test
    fun `When Label row is focused and tapping outside releases the focus`() {
        with(composeTestRule) {
            // Given
            setContent {
                AlarmPanel(
                    alarmUI = alarmUI,
                    executor = {}
                )
            }

            onNode(hasSetTextAction())
                .performClick()
            onNode(hasSetTextAction())
                .assertIsFocused()

            // When
            onRoot()
                .performClick()

            // Then
            onNode(hasSetTextAction())
                .assertIsNotFocused()
        }
    }

    @Test
    fun `When WeeklyRepeat row edit button is clicked it displays PopupPickerRepeat`() {
        with(composeTestRule) {
            // Given
            setContent {
                AlarmPanel(
                    alarmUI = alarmUI,
                    executor = {}
                )
            }

            // When
            onAllNodesWithContentDescription(stringRes(R.string.action_edit))
                .onFirst()
                .performClick()

            // Then
            onNodeWithText("Monday")
                .assertExists()

            captureScreenRoboImage()
        }
    }

    @Test
    fun `When Ringtone row edit button is clicked it displays PopupPickerRingtone`() {
        var context: Context? = null

        with(composeTestRule) {
            // Given
            setContent {
                context = LocalContext.current

                AlarmPanel(
                    alarmUI = alarmUI,
                    executor = {}
                )
            }

            val launchedActivity = Shadow
                .extract<ShadowActivity>(context as ComponentActivity)

            // When
            onAllNodesWithContentDescription(stringRes(R.string.action_edit))[1]
                .performClick()

            waitForIdle()

            // Then
            assertEquals(
                RingtoneManager.ACTION_RINGTONE_PICKER,
                launchedActivity.nextStartedActivity.action
            )
        }
    }

    @Test
    fun `When AlertType row edit button is clicked it displays PopupPickerStandardWheel`() {
        with(composeTestRule) {
            // Given
            setContent {
                AlarmPanel(
                    alarmUI = alarmUI,
                    executor = {}
                )
            }

            // When
            onAllNodesWithContentDescription(stringRes(R.string.action_edit))[2]
                .performClick()

            // Then
            onNodeWithText("Sound only")
                .assertExists()

            captureScreenRoboImage()
        }
    }

    @Test
    fun `When say it scripts info button is clicked it displays informational text`() {
        with(composeTestRule) {
            // Given
            setContent {
                AlarmPanel(
                    alarmUI = alarmUI,
                    executor = {}
                )
            }

            // When
            onNodeWithContentDescription(stringRes(R.string.action_info))
                .performClick()

            // Then
            onNodeWithText(stringRes(R.string.info_scripts))
                .assertExists()

            onRoot()
                .captureRoboImage()
        }
    }

    @Test
    fun `When say it scripts info text is displayed and IconButtonRowCollapse is clicked it hides info text`() {
        with(composeTestRule) {
            // Given
            setContent {
                AlarmPanel(
                    alarmUI = alarmUI,
                    executor = {}
                )
            }

            onNodeWithContentDescription(stringRes(R.string.action_info))
                .performClick()

            onNodeWithText(stringRes(R.string.info_scripts))
                .assertExists()

            // When
            onNodeWithContentDescription(stringRes(R.string.action_collapse))
                .performClick()

            // Then
            onNodeWithText(stringRes(R.string.info_scripts))
                .assertDoesNotExist()

            onRoot()
                .captureRoboImage()
        }
    }

    @Test
    fun `When say it script panel + button clicked it displays PopupPickerSayItScript`() {
        with(composeTestRule) {
            // Given
            setContent {
                AlarmPanel(
                    alarmUI = alarmUI,
                    executor = {}
                )
            }

            // When
            onNodeWithContentDescription(stringRes(R.string.action_add_script))
                .performClick()

            // Then
            onNodeWithText(stringRes(R.string.info_scripts_only_letter))
                .assertExists()

            captureScreenRoboImage()
        }
    }

    @Test
    fun `When more then one say it scripts is given it displays script in clickable row`() {
        with(composeTestRule) {
            // Given
            val alarmUI = alarmUI.copy(sayItScripts = listOf("Hi, there"))

            // When
            setContent {
                AlarmPanel(
                    alarmUI = alarmUI,
                    executor = {}
                )
            }

            // Then
            onNodeWithText("Hi, there")
                .assertExists()
                .assertHasClickAction()

            onRoot()
                .captureRoboImage()
        }
    }

    @Test
    fun `When say it script row is clicked it displays PopupPickerSayItScript with the script`() {
        // Given
        val alarmUI = alarmUI.copy(sayItScripts = listOf("Hi, there"))

        with(composeTestRule) {
            setContent {
                AlarmPanel(
                    alarmUI = alarmUI,
                    executor = {}
                )
            }

            // When
            onNodeWithText("Hi, there")
                .performClick()

            // Then
            onNodeWithText(stringRes(R.string.info_scripts_only_letter))
                .assertExists()
            onAllNodesWithText("Hi, there")
                .fetchSemanticsNodes()
                .size mustBe 2

            captureScreenRoboImage()
        }
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
                    executor = {}
                )
            }

            onNodeWithText("8:00 AM")
                .performClick()
            onNodeWithText("PM")
                .assertExists()

            // When
            onNodeWithText(stringRes(R.string.cancel))
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
                    executor = {}
                )
            }

            onNodeWithText("8:00 AM")
                .performClick()
            onNodeWithText("PM")
                .assertExists()

            // When
            onNodeWithText(stringRes(R.string.confirm))
                .performClick()

            // Then
            onNode(isDialog())
                .assertDoesNotExist()
            onNodeWithText("PM")
                .assertDoesNotExist()
        }
    }

    @Test
    fun `When PopupPickerSayItScript is displayed and onCancel is clicked it dismiss the picker`() {
        with(composeTestRule) {
            // Given
            setContent {
                AlarmPanel(
                    alarmUI = alarmUI,
                    executor = {}
                )
            }

            onNodeWithContentDescription(stringRes(R.string.action_add_script))
                .performClick()
            onNodeWithText(stringRes(R.string.info_scripts_only_letter))
                .assertExists()

            // When
            onNodeWithText(stringRes(R.string.cancel))
                .performClick()

            // Then
            onNode(isDialog())
                .assertDoesNotExist()
            onNodeWithText(stringRes(R.string.info_scripts_only_letter))
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
                    executor = {}
                )
            }

            onNodeWithContentDescription(stringRes(R.string.action_add_script))
                .performClick()
            onNodeWithText(stringRes(R.string.info_scripts_only_letter))
                .assertExists()

            // When
            onNodeWithText(stringRes(R.string.confirm))
                .performClick()

            // Then
            onNode(isDialog())
                .assertDoesNotExist()
            onNodeWithText(stringRes(R.string.info_scripts_only_letter))
                .assertDoesNotExist()
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
                    executor = { commandExecutor.runCommand(it) }
                )
            }

            // Open PopupPickerTime
            onNodeWithText("8:00 AM")
                .performClick()

            // When
            onNodeWithText(stringRes(R.string.confirm))
                .performClick()
        }

        // Then
        commandExecutor.invokedType mustBe InvokedType.SET_TIME
    }

    @Test
    fun `When Label is focused and onDone ime action is invoked it runs executor setLabel`() {
        // Given
        val commandExecutor = CommandExecutorFake()

        with(composeTestRule) {
            setContent {
                AlarmPanel(
                    alarmUI = alarmUI,
                    executor = { commandExecutor.runCommand(it) }
                )
            }

            // When
            onNode(hasSetTextAction())
                .performClick()
                .performImeAction() // ImeAction.Done
        }

        // Then
        commandExecutor.invokedType mustBe InvokedType.SET_LABEL
    }

    @Test
    fun `When PopupPickerRepeat is displayed and onConfirm is clicked it runs executor setWeeklyRepeat`() {
        // Given
        val commandExecutor = CommandExecutorFake()

        with(composeTestRule) {
            setContent {
                AlarmPanel(
                    alarmUI = alarmUI,
                    executor = { commandExecutor.runCommand(it) }
                )
            }

            // Open PopupPickerRepeat
            onAllNodesWithContentDescription(stringRes(R.string.action_edit))
                .onFirst()
                .performClick()

            // When
            onNodeWithText(stringRes(R.string.confirm))
                .performClick()

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
                    executor = { commandExecutor.runCommand(it) }
                )
            }

            // Open PopupPickerRepeat
            onNodeWithContentDescription(stringRes(R.string.action_add_script))
                .performClick()

            // When
            onNodeWithText(stringRes(R.string.confirm))
                .performClick()

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
                    executor = { commandExecutor.runCommand(it) }
                )
            }

            onAllNodesWithContentDescription(stringRes(R.string.action_edit))[2]
                .performClick()

            // When
            onNodeWithText(stringRes(R.string.confirm))
                .performClick()

            // Then
            commandExecutor.invokedType mustBe InvokedType.SET_ALERT_TYPE
        }
    }

    private val alarmUI = FakeAlarmUIData.defaultAlarmUI
}
