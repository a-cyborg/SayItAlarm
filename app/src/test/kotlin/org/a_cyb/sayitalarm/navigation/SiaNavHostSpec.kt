/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.navigation

import android.app.Application
import android.content.Context
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.assertHasNoClickAction
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.testing.TestNavHostController
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.runTest
import org.a_cyb.sayitalarm.design_system.R
import org.a_cyb.sayitalarm.presentation.contracts.AddContract
import org.a_cyb.sayitalarm.presentation.contracts.AddContract.AddState
import org.a_cyb.sayitalarm.presentation.contracts.EditContract
import org.a_cyb.sayitalarm.presentation.contracts.ListContract
import org.a_cyb.sayitalarm.presentation.contracts.SettingsContract
import org.a_cyb.sayitalarm.presentation.viewmodel.AddViewModel
import org.a_cyb.sayitalarm.presentation.viewmodel.EditViewModel
import org.a_cyb.sayitalarm.presentation.viewmodel.ListViewModel
import org.a_cyb.sayitalarm.presentation.viewmodel.SettingsViewModel
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.GlobalContext
import org.koin.core.context.loadKoinModules
import org.koin.core.context.startKoin
import org.koin.dsl.bind
import org.koin.dsl.module
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode

@RunWith(AndroidJUnit4::class)
@Config(application = TestApplication::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
class SiaNavHostSpec {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Before
    fun setup() {
        setupViewModelMockk()

        composeTestRule.setContent {
            val navController = TestNavHostController(LocalContext.current)
            navController.navigatorProvider.addNavigator(ComposeNavigator())

            SiaNavHost(navController = navController)
        }
    }

    private val context: Context = InstrumentationRegistry.getInstrumentation().targetContext

    private fun getStringRes(id: Int) = context.getString(id)

    @Test
    fun `It start from ListScreen`() {
        with(composeTestRule) {
            onNodeWithText(getStringRes(R.string.edit)).assertExists()
                .assertHasClickAction()
            onNodeWithContentDescription(getStringRes(R.string.action_open_settings)).assertExists()
                .assertHasClickAction()
            onNodeWithContentDescription(getStringRes(R.string.action_add_alarm)).assertExists()
                .assertHasClickAction()
        }
    }

    @Test
    fun `When add icon button is clicked, it navigate to AddScreen`() = runTest {
        with(composeTestRule) {
            // When
            onNodeWithContentDescription(getStringRes(R.string.action_add_alarm)).performClick()

            // Then
            onNodeWithText(getStringRes(R.string.add)).assertExists()
        }
    }

    @Test
    fun `When it is on the AddScreen and the navigateBack button is clicked, it returns to the ListScreen`() {
        with(composeTestRule) {
            // Given
            onNodeWithContentDescription(getStringRes(R.string.action_add_alarm)).performClick()

            // When
            onNodeWithContentDescription(getStringRes(R.string.action_navigate_back)).performClick()

            // Then
            onNodeWithText(getStringRes(R.string.edit)).assertExists()
            onNodeWithContentDescription(getStringRes(R.string.action_open_settings)).assertExists()
        }
    }

    @Test
    fun `When settings icon button is clicked, it navigate to the SettingsScreen`() {
        with(composeTestRule) {
            // When
            onNodeWithContentDescription(getStringRes(R.string.action_open_settings)).performClick()

            // Then
            onNodeWithText(getStringRes(R.string.settings)).assertExists().assertHasNoClickAction()
            onNodeWithText(getStringRes(R.string.about)).assertExists().assertHasNoClickAction()
            onNodeWithText(getStringRes(R.string.licenses)).assertExists().assertHasNoClickAction()
        }
    }

    @Test
    fun `When it is on the SettingsScreen and the navigateBack button is clicked, it returns to the ListScreen`() {
        with(composeTestRule) {
            // Given
            onNodeWithContentDescription(getStringRes(R.string.action_open_settings)).performClick()

            // When
            onNodeWithContentDescription(getStringRes(R.string.action_navigate_back)).performClick()

            // Then
            onNodeWithText(getStringRes(R.string.edit)).assertExists()
            onNodeWithContentDescription(getStringRes(R.string.action_add_alarm)).assertExists()
        }
    }

    @Test
    fun `When on the ListScreen in edit mode and the edit button is clicked, it navigates to the the EditScreen`() {
        with(composeTestRule) {
            // Given
            onNodeWithText(getStringRes(R.string.edit)).performClick() // Activate edit mode.

            // When
            onNodeWithContentDescription(getStringRes(R.string.action_edit)).performClick()

            // Then
            onNodeWithText(getStringRes(R.string.edit)).assertExists().assertHasNoClickAction()
            onNodeWithText(getStringRes(R.string.confirm)).assertExists().assertHasClickAction()
        }
    }

    @Test
    fun `When on the EditScreen and the navigateBack button is clicked, it returns to the ListScreen`() {
        with(composeTestRule) {
            // Given
            onNodeWithText(getStringRes(R.string.edit)).performClick()
            onNodeWithContentDescription(getStringRes(R.string.action_edit)).performClick()

            // When
            onNodeWithContentDescription(getStringRes(R.string.action_navigate_back)).performClick()

            // Then
            onNodeWithContentDescription(getStringRes(R.string.action_open_settings)).assertExists()
            onNodeWithContentDescription(getStringRes(R.string.action_add_alarm)).assertExists()
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
        val addState = MutableStateFlow(AddState.Initial(FakeAlarmUIData.defaultAlarmUI))
        val editState = MutableStateFlow(EditContract.EditViewModel.EditState.Initial)
        val settingsState = MutableStateFlow(SettingsContract.SettingsState.Initial)
        val offlineState = MutableStateFlow(true)

        every { listViewModelFake.state } returns listState
        every { listViewModelFake.isOfflineAvailable } returns offlineState
        every { addViewModelFake.state } returns addState
        every { editViewModelFake.state } returns editState
        every { settingsViewModelFake.state } returns settingsState

        val viewmodelModule = module {
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
