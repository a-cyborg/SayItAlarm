/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.alarm_service.core.navigation

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.testing.TestNavHostController
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.MutableStateFlow
import org.a_cyb.sayitalarm.design_system.R
import org.a_cyb.sayitalarm.presentation.contracts.AlarmContract
import org.a_cyb.sayitalarm.presentation.contracts.AlarmContract.AlarmUiState
import org.a_cyb.sayitalarm.presentation.contracts.SayItContract
import org.a_cyb.sayitalarm.presentation.contracts.SayItContract.SayItUIInfo
import org.a_cyb.sayitalarm.presentation.viewmodel.AlarmViewModel
import org.a_cyb.sayitalarm.presentation.viewmodel.SayItViewModel
import org.a_cyb.sayitalarm.util.test_utils.createAddActivityToRobolectricRule
import org.a_cyb.sayitalarm.util.test_utils.createKoinExternalResourceRule
import org.junit.After
import org.junit.Rule
import org.junit.runner.RunWith
import org.koin.core.context.loadKoinModules
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.bind
import org.koin.dsl.module
import org.robolectric.annotation.GraphicsMode
import kotlin.test.Test

@RunWith(AndroidJUnit4::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
class SiaAlarmServiceNavHostSpec {

    private val context: Context = InstrumentationRegistry.getInstrumentation().targetContext

    @get:Rule(order = 1)
    val addActivityRule = createAddActivityToRobolectricRule()

    @get:Rule(order = 2)
    val composeTestRule = createComposeRule()

    @get:Rule(order = 3)
    val koinTestRule = createKoinExternalResourceRule(module { })

    @After
    fun teatDown() {
        clearAllMocks()
    }

    private fun setupMock(alarmViewModelState: AlarmUiState = AlarmUiState.Initial) {
        val alarmViewModel: AlarmViewModel = mockk(relaxed = true)
        val sayItViewModel: SayItViewModel = mockk(relaxed = true)

        every { alarmViewModel.state } returns MutableStateFlow(alarmViewModelState)
        every { alarmViewModel.currentTime } returns MutableStateFlow("")
        every { sayItViewModel.state } returns MutableStateFlow(SayItContract.SayItUiState.Listening(sayItUiInfo))

        val viewmodelModule = module {
            viewModel { alarmViewModel } bind AlarmContract.AlarmViewModel::class
            viewModel { sayItViewModel } bind SayItContract.SayItViewModel::class
        }

        loadKoinModules(viewmodelModule)
    }

    @Composable
    private fun SetupNavHost() {
        val navController = TestNavHostController(LocalContext.current)
        navController.navigatorProvider.addNavigator(ComposeNavigator())

        SiaAlarmServiceNavHost(navController = navController)
    }

    private fun getStringRes(id: Int) = context.getString(id)

    @Test
    fun `It starts from AlarmScreen`() {
        // Given
        setupMock()

        with(composeTestRule) {
            // When
            setContent { SetupNavHost() }

            // Then
            onNodeWithText(getStringRes(R.string.say_it)).assertExists()
            onNodeWithText(getStringRes(R.string.loading)).assertExists()
        }
    }

    @Test
    fun `When SayIt button is clicked it navigate to SayItScreen`() {
        // Given
        setupMock(AlarmUiState.Ringing("Label"))

        with(composeTestRule) {
            setContent { SetupNavHost() }

            // When
            onNodeWithText(getStringRes(R.string.say_it)).performClick()

            // Then
            onNodeWithText(sayItUiInfo.script).assertExists()
        }
    }

    private val sayItUiInfo =
        SayItUIInfo(
            "I embrace this hour with enthusiasm.",
            "I embrace",
            1,
            3,
        )
}
