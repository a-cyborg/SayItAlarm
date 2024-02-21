package org.a_cyb.sayitalarm.core.designsystem.component

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsFocused
import androidx.compose.ui.test.assertIsNotFocused
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import org.a_cyb.sayitalarm.R
import org.a_cyb.sayitalarm.core.alarm.NoOptionalFeature
import org.a_cyb.sayitalarm.core.alarm.VoiceRecognitionTerminator
import org.a_cyb.sayitalarm.core.model.Alarm
import org.a_cyb.sayitalarm.feature.add.AddUiSate
import org.a_cyb.sayitalarm.util.getLocalizedFullWeekdaysMap
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class AlarmPanelTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    private lateinit var localizedFullWeekdayNames: Map<Int, String>

    @Before
    fun setup() {
        composeTestRule.setContent {
            val defaultAlarm = AddUiSate()
            AlarmPanel(
                alarm = Alarm(
                    combinedMinutes = defaultAlarm.time,
                    label = defaultAlarm.label,
                    weeklyRepeat = defaultAlarm.weeklyRepeat,
                    enabled = true,
                    vibrate = false,    // Not implemented.
                    ringtone = defaultAlarm.ringtone.toString(),
                    alarmTerminator = VoiceRecognitionTerminator(defaultAlarm.sayItText),
                    alarmOptionalFeature = NoOptionalFeature,
                ),
                onUpdateAlarmTime = { _, _ -> },
                onUpdateWeeklyRepeat = { _ -> },
                onUpdateLabel = { _ -> },
                onUpdateRingtone = { _ -> },
                onUpdateSayItText = { _ -> }
            )
        }

        localizedFullWeekdayNames = getLocalizedFullWeekdaysMap()
    }

    private fun getString(id: Int) = composeTestRule.activity.getString(id)

    @Test
    fun whenCreated_allSectionsAreDisplayed()  {
        composeTestRule.apply {
            // TimeSection
            onNodeWithContentDescription(getString(R.string.alarm_panel_timepicker_open_button_description))
                .assertExists()
                .assertHasClickAction()

            // WeeklyRepeatSection
            onNodeWithText(getString(R.string.alarm_panel_weeklyrepeat_session_title))
                .assertExists()
            onNodeWithContentDescription(getString(R.string.alarm_panel_weeklyrepeat_picker_open_icon_description))
                .assertExists()
                .assertHasClickAction()

            // LabelSection
            onNodeWithText(getString(R.string.alarm_panel_label_session_title))
                .assertExists()
            onNodeWithText(getString(R.string.alarm_panel_label_section_textfield_placeholder))
                .assertExists()
                .assertIsEnabled()
                .assertHasClickAction()

            // RingtoneSection
            onNodeWithText(getString(R.string.alarm_panel_ringtone_section_title))
                .assertExists()
            onNodeWithContentDescription(getString(R.string.alarm_panel_ringtone_picker_open_icon_description))
                .assertExists()
                .assertHasClickAction()

            // SayItTextSection
            onNodeWithText(getString(R.string.alarm_panel_say_it_section_title))
                .assertExists()
            onNodeWithText(getString(R.string.alarm_panel_sayit_section_textfield_placeholder))
                .assertExists()
                .assertIsEnabled()
                .assertHasClickAction()
            onNodeWithText(getString(R.string.alarm_panel_sayit_section_add_new_button_text))
                .assertExists()
                .assertHasClickAction()
        }
    }

    @Test
    fun whenTimeSectionIsClicked_TimePickerIsShown() {
        composeTestRule.apply {
            val timePickerTitle =
                onNodeWithText(getString(R.string.alarm_panel_time_picker_title))

            timePickerTitle.assertDoesNotExist()

            onNodeWithContentDescription(getString(R.string.alarm_panel_timepicker_open_button_description))
                .performClick()

            // Check that one of the TimePicker component is actually displayed.
            timePickerTitle.assertExists()
        }
    }

    @Test
    fun whenWeeklyRepeatSessionIsClicked_WeeklyRepeatPickerIsShown() {
        composeTestRule.apply {
            val weeklyRepeatPickerTitle =
                onNodeWithText(getString(R.string.alarm_panel_weeklyrepeat_picker_title))

            weeklyRepeatPickerTitle.assertDoesNotExist()

            onNodeWithContentDescription(getString(R.string.alarm_panel_weeklyrepeat_picker_open_icon_description))
                .performClick()

            weeklyRepeatPickerTitle.assertExists()
        }
    }

    @Test
    fun whenLabelSectionTextFieldIsClicked_getFocus() {
        composeTestRule.apply {
            val labelTextField =
                onNodeWithText(getString(R.string.alarm_panel_label_section_textfield_placeholder))

            labelTextField.assertIsNotFocused()
            labelTextField.performClick()
            labelTextField.assertIsFocused()
        }
    }

    @Test
    fun whenRingtoneSectionIsClicked_RingtonePickerIsShown() { /* TODO */ }

    @Test
    fun whenSayItTextSectionTextFieldIsClicked_getFocus() {
        composeTestRule.apply {
            val sayItTextField =
               onNodeWithText(getString(R.string.alarm_panel_sayit_section_textfield_placeholder))

            sayItTextField.assertIsNotFocused()
            sayItTextField.performClick()
            sayItTextField.assertIsFocused()
        }
    }

    @Test
    fun whenSayItTextSectionInfoButtonClicked_longInfoIsShown() {
        composeTestRule.apply {
            val longInfoText =
                onNodeWithText(getString(R.string.alarm_panel_sayit_text_long_information))

            longInfoText.assertDoesNotExist()

            onNodeWithContentDescription(getString(R.string.alarm_panel_sayit_section_show_info_icon_description))
                .performClick()

            longInfoText.assertExists()

            // Hide button
            onNodeWithContentDescription(getString(R.string.alarm_panel_sayit_section_hide_info_icon_description))
                .performClick()

            longInfoText.assertDoesNotExist()
        }
    }

    @Test
    fun whenSayItTextSectionAddNewTextButtonClicked_newTextFieldIsCreated() { /* TODO */ }

    @Test
    fun whenWeeklyRepeatPickerIsDisplayed_allAvailableOptionsAreShown() {
        composeTestRule.apply {
            // Open weekly repeat picker.
            onNodeWithContentDescription(getString(R.string.alarm_panel_weeklyrepeat_picker_open_icon_description))
                .performClick()


            localizedFullWeekdayNames.values.forEach { fullDayName ->
                onNodeWithText("${getString(R.string.every)} $fullDayName")
                    .assertExists()
            }
        }
    }

    // TODO: After picker selected or set value and dismissed should show selected value.
}