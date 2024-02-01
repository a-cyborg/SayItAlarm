package org.a_cyb.sayitalarm.feature.add

import android.media.RingtoneManager
import androidx.activity.ComponentActivity
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import org.a_cyb.sayitalarm.R
import org.a_cyb.sayitalarm.util.getFormattedClockTime
import org.junit.Rule
import org.junit.Test

class AddDialogTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    private fun getString(id: Int) = composeTestRule.activity.getString(id)

    @Test
    fun whenCreated_topBarIsDisplayed() {
        composeTestRule.apply {
            // Setup.
            setContent {
                AddDialog(
                    addUiState = AddUiSate(),
                    onDismiss = { },
                    onConfirm = { },
                    onUpdateTime = { _, _ -> },
                    onUpdateWeeklyRepeat = { _ -> } ,
                    onUpdateLabel = { _ -> },
                    onUpdateRingtone = { _ -> },
                    onUpdateSayItText = { _ -> }
                )
            }

            onNodeWithText(getString(R.string.add_dialog_topbar_title)).assertExists()
            onNodeWithText(getString(R.string.save))
                .assertExists()
                .assertHasClickAction()
            onNodeWithContentDescription(getString(R.string.topbar_dialog_close_icon_description))
                .assertExists()
                .assertHasClickAction()
        }
    }

    @Test
    fun whenCreated_allDefaultValuesAreDisplayed() {
        val defaultState = AddUiSate()

        lateinit var formattedTime: String
        lateinit var defaultRingtoneTitle: String

        composeTestRule.apply {
            // Setup
            setContent {
                AddDialog(
                    addUiState = AddUiSate(),
                    onDismiss = { },
                    onConfirm = { },
                    onUpdateTime = { _, _ -> },
                    onUpdateWeeklyRepeat = { _ -> },
                    onUpdateLabel = { _ -> },
                    onUpdateRingtone = { _ -> },
                    onUpdateSayItText = { _ -> }
                )

                formattedTime = getFormattedClockTime(defaultState.time)

                defaultRingtoneTitle = RingtoneManager
                    .getRingtone(LocalContext.current, defaultState.ringtone)
                    .getTitle(LocalContext.current)
            }

            onNodeWithTag("AlarmPanel").assertExists()
            onNodeWithText(formattedTime).assertExists()
            onNodeWithText(getString(R.string.never)).assertExists() // WeeklyRepeat
            onNodeWithText(getString(R.string.alarm_panel_label_section_textfield_placeholder)).assertExists() // Label section placeholder
            onNodeWithText(defaultRingtoneTitle).assertExists()
            onNodeWithText(getString(R.string.alarm_panel_sayit_section_textfield_placeholder)).assertExists()
        }
    }
}