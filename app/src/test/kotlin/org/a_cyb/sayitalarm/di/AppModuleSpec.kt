/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.di

import kotlin.test.AfterTest
import kotlin.test.assertNotNull
import androidx.activity.ComponentActivity
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.a_cyb.sayitalarm.presentation.EditContract
import org.a_cyb.sayitalarm.presentation.ListContract
import org.a_cyb.sayitalarm.presentation.SettingsContract
import org.a_cyb.sayitalarm.presentation.viewmodel.AddViewModel
import org.a_cyb.sayitalarm.presentation.viewmodel.EditViewModel
import org.a_cyb.sayitalarm.presentation.viewmodel.ListViewModel
import org.a_cyb.sayitalarm.presentation.viewmodel.SettingsViewModel
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.androidx.compose.koinViewModel
import org.koin.core.context.stopKoin
import org.koin.core.parameter.parametersOf
import org.koin.java.KoinJavaComponent.getOrNull

@RunWith(AndroidJUnit4::class)
class AppModuleSpec {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    @AfterTest
    fun tearDown() {
        stopKoin()
        composeTestRule.activityRule.scenario.recreate()
    }

    @Test
    fun `It injects AddVieWModel`() {
        // When
        val viewModel: AddViewModel? = getOrNull(AddViewModel::class.java)

        // Then
        assertNotNull(viewModel)
    }

    @Test
    fun `It injects EditViewModel`() {
        // Given
        val alarmId: Long = 3
        val viewModel: EditViewModel? =
            getOrNull(EditContract.EditViewModel::class.java) { parametersOf(alarmId) }

        // Then
        assertNotNull(viewModel)
    }

    @Test
    fun `It injects ListViewModel`() {
        // Given
        val viewModel: ListContract.ListViewModel? =
            getOrNull(ListContract.ListViewModel::class.java)

        // Then
        assertNotNull(viewModel)
    }

    @Test
    fun `It injects SettingsViewModel`() {
        // When
        val viewModel: SettingsContract.SettingsViewModel? =
            getOrNull(SettingsContract.SettingsViewModel::class.java)

        // Then
        assertNotNull(viewModel)
    }

    @Test
    fun `It injects viewModel with compose extension`() {
        composeTestRule.setContent {
            val addViewModel: AddViewModel = koinViewModel()
            val alarmId: Long = 3
            val editViewModel: EditViewModel = koinViewModel(parameters = { parametersOf(alarmId) })
            val listViewModel: ListViewModel = koinViewModel()
            val settingsViewModel: SettingsViewModel = koinViewModel()

            assertNotNull(addViewModel)
            assertNotNull(editViewModel)
            assertNotNull(listViewModel)
            assertNotNull(settingsViewModel)
        }
    }
}
