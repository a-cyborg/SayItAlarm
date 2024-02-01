package org.a_cyb.sayitalarm.feature.list

import android.content.res.Configuration
import android.content.res.Resources
import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.ui.test.assertIsOff
import androidx.compose.ui.test.assertIsOn
import androidx.compose.ui.test.assertIsToggleable
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.test.platform.app.InstrumentationRegistry
import org.a_cyb.sayitalarm.R
import org.a_cyb.sayitalarm.core.model.CombinedMinutes
import org.a_cyb.sayitalarm.core.model.WeeklyRepeat
import org.a_cyb.sayitalarm.util.getFormattedClockTime
import org.junit.Rule
import org.junit.Test
import java.util.Calendar
import java.util.Locale

class ListScreenTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    private fun getString(id: Int) = composeTestRule.activity.getString(id)

    private fun getSwitchTestTag(label: String) = "Switch$label"

    @Test
    fun whenListUiStateIsLoading_loadingTextIsDisplayed() {
        composeTestRule.setContent {
            BoxWithConstraints {
                ListScreen(
                    listUiState = ListUiState.Loading,
                    onEditMode = false,
                    onAlarmEnabledStateChange = { _, _ -> },
                    onDeleteAlarm = { _ -> },
                    navigateToEdit = { _ -> },
                )
            }
        }

        composeTestRule.onNodeWithText(
            composeTestRule.activity.resources.getString(R.string.loading)
        )
            .assertExists()
    }

    @Test
    fun whenListUiStateIsSuccess_alarmListItemIsDisplayed() {
        lateinit var formattedTime: String

        composeTestRule.apply {
            setContent {
                ListScreen(
                    listUiState = ListUiState.Success(
                        listOf(
                            getTestAlarmListItem(
                                time = CombinedMinutes(13, 50),
                                label = "TestLabel",
                                weeklyRepeat = WeeklyRepeat.EVERYDAY,
                                enabled = true
                            ),
                        )
                    ),
                    onEditMode = false,
                    onAlarmEnabledStateChange = { _ , _ -> },
                    onDeleteAlarm = { _ -> },
                    navigateToEdit = { _ -> },
                )

                formattedTime = getFormattedClockTime(combinedMin = CombinedMinutes(13, 50))
            }

            onNodeWithText(formattedTime)
                .assertExists()
            onNodeWithText("TestLabel, ${getString(R.string.every_day)}")
                .assertExists()
            onNodeWithTag(getSwitchTestTag("TestLabel"))
                .assertExists()
                .assertIsToggleable()
                .assertIsOn()
        }
    }

    @Test
    fun alarmEnabledState_IsDisplayedOnSwitch() {
        val alarmListItems = List(6) {
            getTestAlarmListItem(
                label = it.toString(),
                enabled = it % 2 == 0
            )
        }

        composeTestRule.apply {
            setContent {
                ListScreen(
                    listUiState = ListUiState.Success(alarmListItems),
                    onEditMode = false,
                    onAlarmEnabledStateChange = { _, _ -> },
                    onDeleteAlarm = { _ -> },
                    navigateToEdit = { _ -> },
                )
            }

            repeat(6) {
                val switchNode = onNodeWithTag(getSwitchTestTag("$it"))

                if (it % 2 == 0) {
                    switchNode.assertIsOn()
                } else {
                    switchNode.assertIsOff()
                }
            }
        }
    }

    @Test
    fun labelAndWeeklyRepeat_IsDisplayed() {
        val alarmListItems = listOf(
            // 1. Expected: "Alarm, Mon, Wed, Fri"
            getTestAlarmListItem(
                label = "Alarm",
                weeklyRepeat = WeeklyRepeat(Calendar.MONDAY, Calendar.WEDNESDAY, Calendar.FRIDAY)
            ),
            // 2. Expected: "Lunch"
            getTestAlarmListItem(
                label = "Lunch",
                weeklyRepeat = WeeklyRepeat.NEVER
            ),
            // 3. Expected: "Wed, Thu, Fri"
            getTestAlarmListItem(
                label = "",
                weeklyRepeat = WeeklyRepeat(Calendar.THURSDAY, Calendar.WEDNESDAY, Calendar.FRIDAY)
            ),
            // 4. Expected: ""
            getTestAlarmListItem(
                label = "",
                weeklyRepeat = WeeklyRepeat.NEVER
            ),
        )

        composeTestRule.apply {
            val testLocal = Locale.US
            val resources: Resources = InstrumentationRegistry.getInstrumentation().targetContext.resources
            val config: Configuration = resources.configuration

            Locale.setDefault(testLocal)
            config.setLocale(testLocal)
            resources.updateConfiguration(config, resources.displayMetrics)

            setContent {
                ListScreen(
                    listUiState = ListUiState.Success(alarmListItems),
                    onEditMode = false,
                    onAlarmEnabledStateChange = { _, _ -> } ,
                    onDeleteAlarm = { _ -> },
                    navigateToEdit = { _ -> },
                )
            }

            onNodeWithText("Alarm, Mon, Wed, Fri").assertExists()
            onNodeWithText("Lunch").assertExists()
            onNodeWithText("Wed, Thu, Fri").assertExists()
            onNodeWithText("").assertExists()
        }
    }

    @Test
    fun whenIsEditMode_editComponentsAreDisplayed() {
        composeTestRule.apply {
            setContent {
                ListScreen(
                    listUiState = ListUiState.Success(listOf(getTestAlarmListItem())),
                    onEditMode = true,
                    onAlarmEnabledStateChange = { _ , _ -> },
                    onDeleteAlarm = { _ -> },
                    navigateToEdit = { _ -> },
                )
            }

            onNodeWithContentDescription(getString(R.string.list_delete_icon_description)).assertExists()
            onNodeWithContentDescription(getString(R.string.list_edit_icon_description)).assertExists()
        }
    }

//    @Test
//    fun whenEditIconIsClicked_editDialogIsDisplayed() {
//        composeTestRule.apply {
//            setContent {
//                ListScreen(
//                    listUiState = ListUiState.Success(listOf(getTestAlarmListItem())),
//                    onEditMode = true,
//                    onAlarmEnabledStateChange = { _ , _ -> },
//                    onDeleteAlarm = { _ -> },
//                    navigateToEdit = { _ -> },
//                )
//            }
//
//            onNodeWithContentDescription(getString(R.string.list_edit_icon_description)).performClick()
//
//            onNodeWithTag("EditScreen").assertExists()
//        }
//    }
}

private fun getTestAlarmListItem(
    id: Int = 1,
    time: CombinedMinutes = CombinedMinutes(8, 30),
    label: String = "TestAlarmListItem",
    weeklyRepeat: WeeklyRepeat = WeeklyRepeat.NEVER,
    enabled: Boolean = true,
) = AlarmListItem(
    id = id,
    time = time,
    label = label,
    weeklyRepeat = weeklyRepeat,
    enabled = enabled
)