/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.navigation

import android.app.Application
import androidx.activity.ComponentActivity
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.testing.TestNavHostController
import androidx.test.ext.junit.runners.AndroidJUnit4
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.test.runTest
import org.a_cyb.sayitalarm.design_system.R
import org.a_cyb.sayitalarm.presentation.AddContract
import org.a_cyb.sayitalarm.presentation.AddContract.AddState
import org.a_cyb.sayitalarm.presentation.EditContract
import org.a_cyb.sayitalarm.presentation.ListContract
import org.a_cyb.sayitalarm.presentation.SettingsContract
import org.a_cyb.sayitalarm.presentation.viewmodel.AddViewModel
import org.a_cyb.sayitalarm.presentation.viewmodel.EditViewModel
import org.a_cyb.sayitalarm.presentation.viewmodel.ListViewModel
import org.a_cyb.sayitalarm.presentation.viewmodel.SettingsViewModel
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.GlobalContext
import org.koin.core.context.GlobalContext.startKoin
import org.koin.core.context.loadKoinModules
import org.koin.dsl.bind
import org.koin.dsl.module
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode

@RunWith(AndroidJUnit4::class)
@Config(application = TestApplication::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
class SiaNavHostSpec {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    @Before
    fun setup() {
        setupViewModelMockk()

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
    fun `When add icon button is clicked it navigate to AddScreen`() = runTest {
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

    private fun setupViewModelMockk() {
        val listViewModelFake: ListViewModel = mockk(relaxed = true)
        val addViewModelFake: AddViewModel = mockk(relaxed = true)
        val editViewModelFake: EditViewModel = mockk(relaxed = true)
        val settingsViewModelFake: SettingsViewModel = mockk(relaxed = true)

        val listState = MutableStateFlow<ListContract.ListState>(
            ListContract.ListState.Success(
                listOf(
                    ListContract.AlarmInfo(
                        id = 1,
                        time = "6:00 AM",
                        labelAndWeeklyRepeat = "Wake Up, every weekday",
                        enabled = true
                    )
                )
            )
        )
        every { listViewModelFake.state } returns
            listState.asStateFlow()
        every { addViewModelFake.state } returns
            MutableStateFlow(AddState.Initial(FakeAlarmUIData.defaultAlarmUI)).asStateFlow()
        every { editViewModelFake.state } returns
            MutableStateFlow(EditContract.EditViewModel.EditState.Initial).asStateFlow()
        every { settingsViewModelFake.state } returns
            MutableStateFlow(SettingsContract.SettingsState.Initial).asStateFlow()

        val viewmodelModule =
            module {
                viewModel { listViewModelFake } bind ListContract.ListViewModel::class
                viewModel { addViewModelFake } bind AddContract.AddViewModel::class
                viewModel { editViewModelFake } bind EditContract.EditViewModel::class
                viewModel { settingsViewModelFake } bind SettingsContract.SettingsViewModel::class
            }

        loadKoinModules(viewmodelModule)
    }
}

class TestApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        if (GlobalContext.getOrNull() == null) {
            startKoin {}
        }
    }
}
