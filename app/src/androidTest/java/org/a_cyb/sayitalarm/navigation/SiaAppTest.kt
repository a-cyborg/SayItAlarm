package org.a_cyb.sayitalarm.navigation

import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import org.a_cyb.sayitalarm.MainActivity
import org.a_cyb.sayitalarm.R
import org.junit.Rule
import org.junit.Test

class SiaAppTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    private fun getString(id: Int) = composeTestRule.activity.getString(id)

    @Test
    fun whenCreated_topBarIsDisplayed() {
        composeTestRule.apply {
            onNodeWithText(getString(R.string.app_name))

            onNodeWithText(getString(R.string.sia_app_topbar_edit_icon_text))
                .assertExists()
                .assertHasClickAction()

            onNodeWithContentDescription(getString(R.string.sia_app_topbar_add_icon_description))
                .assertExists()
                .assertHasClickAction()

            onNodeWithContentDescription(getString(R.string.sia_app_topbar_settings_icon_description))
                .assertExists()
                .assertHasClickAction()
        }
    }

    @Test
    fun whenIsEditMode_doneIconIsDisplayed() {
        composeTestRule.apply {
            onNodeWithText(getString(R.string.sia_app_topbar_edit_icon_text))
                .performClick()

            onNodeWithText(getString(R.string.sia_app_topbar_done_icon_text))
                .assertExists()
        }
    }

    @Test
    fun whenTopBarSettingsIconIsClicked_settingsDialogIsDisplayed() {
        composeTestRule.apply {
            onNodeWithContentDescription(getString(R.string.sia_app_topbar_settings_icon_description))
                .performClick()

            // Check that one of the settings is actually displayed.
            onNodeWithText(getString(R.string.settings_topbar_title))
                .assertExists()
        }
    }

    @Test
    fun whenSettingsDialogDismissed_listScreenIsDisplayed() {
        composeTestRule.apply {
            // Navigate the settings dialog, then close it.
            onNodeWithContentDescription(getString(R.string.sia_app_topbar_settings_icon_description))
                .performClick()
            onNodeWithContentDescription(getString(R.string.topbar_dialog_close_icon_description))
                .performClick()

            // Check that the list screen is displayed.
            onNode(hasTestTag("ListScreen"))
                .assertExists()
        }
    }

    @Test
    fun whenTopBarAddIconIsClicked_addDialogIsDisplayed() {
        composeTestRule.apply {
            onNodeWithContentDescription(getString(R.string.sia_app_topbar_add_icon_description))
                .performClick()

            // Check that one of the add screen component is actually displayed.
            onNodeWithText(getString(R.string.add_dialog_topbar_title))
                .assertExists()
        }
    }

    @Test
    fun whenAddDialogDismissed_listScreenIsDisplayed() {
        composeTestRule.apply {
            // Navigate the add dialog, then close it.
            onNodeWithContentDescription(getString(R.string.sia_app_topbar_add_icon_description))
                .performClick()
            onNodeWithContentDescription(getString(R.string.topbar_dialog_close_icon_description))
                .performClick()

            // Check that the list screen is displayed.
            onNode(hasTestTag("ListScreen"))
                .assertExists()
        }
    }
}

/* TODO: Think about edit mode is on and show edit components(DeleteButton, Edit RightArrow)
        Is Shown Tests on ListScreen test
@Test
fun whenTopBarEditIconIsClicked_editModeComponentsAreDisplayed() {
    composeTestRule.apply {
        onNodeWithText(getString(R.string.sia_app_topbar_edit_icon_text))
            .performClick()

    }
}
 */

