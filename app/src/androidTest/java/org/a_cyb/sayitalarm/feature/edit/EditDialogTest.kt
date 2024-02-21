package org.a_cyb.sayitalarm.feature.edit

import android.media.RingtoneManager
import android.net.Uri
import androidx.activity.ComponentActivity
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import kotlinx.coroutines.flow.MutableStateFlow
import org.a_cyb.sayitalarm.R
import org.a_cyb.sayitalarm.core.alarm.NoOptionalFeature
import org.a_cyb.sayitalarm.core.alarm.VoiceRecognitionTerminator
import org.a_cyb.sayitalarm.core.model.Alarm
import org.a_cyb.sayitalarm.core.model.CombinedMinutes
import org.a_cyb.sayitalarm.core.model.WeeklyRepeat
import org.a_cyb.sayitalarm.feature.add.AddUiSate
import org.a_cyb.sayitalarm.util.getFormattedClockTime
import org.junit.Rule
import org.junit.Test

class EditDialogTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    private fun getString(id: Int) = composeTestRule.activity.getString(id)
    
    @Test
    fun whenIsCreated_topAppBarIsDisplayed() {
        composeTestRule.apply {
            setContent {
                EditDialog(
                    onDismiss = { -> },
                    editUiState = EditUiState.Loading,
                    onUpdateTime = { _, _ -> },
                    onUpdateWeeklyRepeat = { _ -> },
                    onUpdateLabel = { _ -> },
                    onUpdateRingtone = { _ -> },
                    onUpdateSayItText = { _ -> },
                    onDelete = { -> },
                    onSave = { -> },
                    onCancel = { -> }
                )
            }

            onNodeWithText(getString(R.string.edit_topbar_title))
                .assertExists()
            onNodeWithContentDescription(getString(R.string.topbar_dialog_close_icon_description))
                .assertExists()
            onNodeWithText(getString(R.string.save))
                .assertExists()
        }
    }

    @Test
    fun whenEditStateIsSuccess_fetchedAlarmValuesAreDisplayed() {
        lateinit var formattedTime: String
        lateinit var defaultRingtoneTitle: String

        composeTestRule.apply {
            val fetchedAlarm = Alarm(
                combinedMinutes = CombinedMinutes(13, 13),
                weeklyRepeat = WeeklyRepeat.EVERYDAY,
                label = "Morning",
                enabled = true,
                vibrate = false,
                ringtone = AddUiSate().ringtone.toString(), // TODO: change to get uri with string.
                alarmTerminator = VoiceRecognitionTerminator(listOf("I am grateful.")),
                alarmOptionalFeature = NoOptionalFeature,
            )
            setContent {
                EditDialog(
                    onDismiss = { /*TODO*/ },
                    editUiState = EditUiState.Success(fetchedAlarm, MutableStateFlow(fetchedAlarm)),
                    onUpdateTime = { _, _ -> },
                    onUpdateWeeklyRepeat = { _ -> },
                    onUpdateLabel = { _ -> },
                    onUpdateRingtone = { _ -> },
                    onUpdateSayItText = { _ -> },
                    onDelete = { -> },
                    onSave = { -> },
                    onCancel = { -> }
                )

                formattedTime = getFormattedClockTime(fetchedAlarm.combinedMinutes)

                defaultRingtoneTitle = RingtoneManager
                    .getRingtone(LocalContext.current, Uri.parse(fetchedAlarm.ringtone))
                    .getTitle(LocalContext.current)
            }

            onNodeWithTag("AlarmPanel").assertExists()
            onNodeWithText(formattedTime).assertExists()
            onNodeWithText(getString(R.string.every_day)).assertExists() // WeeklyRepeat
            onNodeWithText("Morning").assertExists() // Label section placeholder
            onNodeWithText(defaultRingtoneTitle).assertExists()
            onNodeWithText("I am grateful.").assertExists()
        }
    }
}