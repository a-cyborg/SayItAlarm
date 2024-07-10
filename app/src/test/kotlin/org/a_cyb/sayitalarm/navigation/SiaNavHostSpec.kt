/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.navigation

import androidx.activity.ComponentActivity
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.hasSetTextAction
import androidx.compose.ui.test.junit4.ComposeTestRule
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performImeAction
import androidx.compose.ui.test.performTextInput
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.testing.TestNavHostController
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.a_cyb.sayitalarm.R
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.GraphicsMode

@RunWith(AndroidJUnit4::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
class SiaNavHostSpec {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    @Before
    fun setup() {
        composeTestRule.setContent {
            val navController = TestNavHostController(LocalContext.current)
            navController.navigatorProvider.addNavigator(ComposeNavigator())

            SiaNavHost(navController = navController)
        }
    }

    @After
    fun tearDown() {
        composeTestRule.activityRule.scenario.recreate()
    }

    private fun getStringRes(id: Int) = composeTestRule.activity.getString(id)

    @Test
    fun `It start from ListScreen`() {
        with(composeTestRule) {
            onNodeWithText(getStringRes(R.string.edit))
                .assertHasClickAction()
                .assertExists()

            onNodeWithContentDescription(getStringRes(R.string.action_open_settings))
                .assertHasClickAction()
                .assertExists()
        }
    }

    @Test
    fun `When add icon button is clicked it navigate to AddScreen`() {
        with(composeTestRule) {
            // When
            onNodeWithContentDescription(getStringRes(R.string.action_add_alarm))
                .performClick()

            // Then
            onNodeWithText(getStringRes(R.string.add))
                .assertExists()
        }
    }

    @Test
    fun `When it is in AddScreen and navigateBack button is clicked it returns to ListScreen`() {
        with(composeTestRule) {
            // Given
            onNodeWithContentDescription(getStringRes(R.string.action_add_alarm))
                .performClick()

            onNodeWithText(getStringRes(R.string.add))
                .assertExists()

            // When
            onNodeWithContentDescription(getStringRes(R.string.action_navigate_back))
                .performClick()

            // Then
            onNodeWithText(getStringRes(R.string.edit))
                .assertHasClickAction()
                .assertExists()
        }
    }

    @Test
    fun `When settings icon button is clicked it navigate to SettingsScreen`() {
        with(composeTestRule) {
            // When
            onNodeWithContentDescription(getStringRes(R.string.action_open_settings))
                .performClick()

            // Then
            onNodeWithText(getStringRes(R.string.settings))
                .assertExists()
            onNodeWithText(getStringRes(R.string.about))
                .assertExists()
            onNodeWithText(getStringRes(R.string.license))
                .assertExists()
        }
    }

    @Test
    fun `When it is in SettingsScreen and navigateBack button is clicked it returns to ListScreen`() {
        with(composeTestRule) {
            // Given
            onNodeWithContentDescription(getStringRes(R.string.action_open_settings))
                .performClick()

            onNodeWithText(getStringRes(R.string.settings))
                .assertExists()

            // When
            onNodeWithContentDescription(getStringRes(R.string.action_navigate_back))
                .performClick()

            // Then
            onNodeWithText(getStringRes(R.string.edit))
                .assertHasClickAction()
                .assertExists()
        }
    }

    @Test
    fun `When alarm list row edit button is clicked it navigate to EditScreen`() {
        with(composeTestRule) {
            // Given
            insertAlarm(this)
            onNodeWithText(getStringRes(R.string.edit)).performClick()

            // When
            onNodeWithContentDescription(getStringRes(R.string.action_edit))
                .performClick()

            // Then
            onNodeWithText(getStringRes(R.string.confirm))
                .assertExists()
        }
    }

    @Test
    fun `When it is in EditScreen and navigateBack button is clicked it returns to ListScreen`() {
        with(composeTestRule) {
            // Given
            insertAlarm(this)
            onNodeWithText(getStringRes(R.string.edit))
                .performClick()  // Enter edit mode
            onNodeWithContentDescription(getStringRes(R.string.action_edit))
                .performClick() // Edit screen

            // When
            onNodeWithContentDescription(getStringRes(R.string.action_navigate_back))
                .performClick()

            // Then
            onNodeWithContentDescription(getStringRes(R.string.action_open_settings))
                .assertExists()
        }
    }

    private fun insertAlarm(composeTestRule: ComposeTestRule) {
        with(composeTestRule) {
            onNodeWithContentDescription(getStringRes(R.string.action_add_alarm))
                .performClick()
            onNode(hasSetTextAction())
                .performTextInput("TestLabel")
            onNode(hasSetTextAction())
                .performImeAction()  // Done
            onNodeWithText(getStringRes(R.string.save))
                .performClick()  // Save
        }
    }
}
