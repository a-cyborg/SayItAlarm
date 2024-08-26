/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.di

import kotlin.test.AfterTest
import kotlin.test.assertNotNull
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.a_cyb.sayitalarm.presentation.AddContract
import org.a_cyb.sayitalarm.presentation.AlarmContract
import org.a_cyb.sayitalarm.presentation.EditContract
import org.a_cyb.sayitalarm.presentation.ListContract
import org.a_cyb.sayitalarm.presentation.SayItContract
import org.a_cyb.sayitalarm.presentation.SettingsContract
import org.a_cyb.sayitalarm.presentation.viewmodel.AddViewModel
import org.a_cyb.sayitalarm.presentation.viewmodel.AlarmViewModel
import org.a_cyb.sayitalarm.presentation.viewmodel.EditViewModel
import org.a_cyb.sayitalarm.presentation.viewmodel.ListViewModel
import org.a_cyb.sayitalarm.presentation.viewmodel.SayItViewModel
import org.a_cyb.sayitalarm.presentation.viewmodel.SettingsViewModel
import org.a_cyb.sayitalarm.util.fulfils
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf
import org.koin.java.KoinJavaComponent.getOrNull
import org.koin.mp.KoinPlatform.stopKoin

@RunWith(AndroidJUnit4::class)
class AppModuleSpec {

    @AfterTest
    fun tearDown() {
        stopKoin()
    }

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun `It injects AddViewModel`() {
        // When
        val viewModel: AddContract.AddViewModel? = getOrNull(AddViewModel::class.java)

        // Then
        assertNotNull(viewModel)
    }

    @Test
    fun `It injects AlarmViewModel`() {
        // When
        val viewModel: AlarmContract.AlarmViewModel? = getOrNull(AlarmViewModel::class.java)

        // Then
        assertNotNull(viewModel)
    }

    @Test
    fun `It injects EditViewModel`() {
        // Given
        val alarmId: Long = 3
        val viewModel: EditViewModel? = getOrNull(EditContract.EditViewModel::class.java) { parametersOf(alarmId) }

        // Then
        assertNotNull(viewModel)
    }

    @Test
    fun `It injects ListViewModel`() {
        // Given
        val viewModel: ListContract.ListViewModel? = getOrNull(ListContract.ListViewModel::class.java)

        // Then
        assertNotNull(viewModel)
    }

    @Test
    fun `It injects SayItViewModel`() {
        // When
        val viewModel: SayItContract.SayItViewModel? = getOrNull(SayItContract.SayItViewModel::class.java)

        // Then
        assertNotNull(viewModel)
    }

    @Test
    fun `It injects SettingsViewModel`() {
        // When
        val viewModel: SettingsContract.SettingsViewModel? = getOrNull(SettingsContract.SettingsViewModel::class.java)

        // Then
        assertNotNull(viewModel)
    }

    @Test
    fun `It injects viewModel with compose extension`() {
        composeTestRule.setContent {
            val addViewModel: AddViewModel = koinViewModel()
            val alarmViewModel: AlarmViewModel = koinViewModel()
            val alarmId: Long = 3
            val editViewModel: EditViewModel = koinViewModel(parameters = { parametersOf(alarmId) })
            val listViewModel: ListViewModel = koinViewModel()
            val sayItViewModel: SayItViewModel = koinViewModel()
            val settingsViewModel: SettingsViewModel = koinViewModel()

            addViewModel fulfils AddContract.AddViewModel::class
            alarmViewModel fulfils AlarmContract.AlarmViewModel::class
            editViewModel fulfils EditContract.EditViewModel::class
            listViewModel fulfils ListContract.ListViewModel::class
            sayItViewModel fulfils SayItContract.SayItViewModel::class
            settingsViewModel fulfils SettingsContract.SettingsViewModel::class
        }
    }
}
