/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.di

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.a_cyb.sayitalarm.MainActivity
import org.a_cyb.sayitalarm.presentation.contracts.AddContract
import org.a_cyb.sayitalarm.presentation.contracts.AlarmContract
import org.a_cyb.sayitalarm.presentation.contracts.EditContract
import org.a_cyb.sayitalarm.presentation.contracts.ListContract
import org.a_cyb.sayitalarm.presentation.contracts.SayItContract
import org.a_cyb.sayitalarm.presentation.contracts.SettingsContract
import org.a_cyb.sayitalarm.presentation.viewmodel.AddViewModel
import org.a_cyb.sayitalarm.presentation.viewmodel.AlarmViewModel
import org.a_cyb.sayitalarm.presentation.viewmodel.EditViewModel
import org.a_cyb.sayitalarm.util.test_utils.createKoinExternalResourceRule
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.core.parameter.parametersOf
import org.koin.java.KoinJavaComponent.getOrNull
import kotlin.test.assertNotNull

@RunWith(AndroidJUnit4::class)
class AppModuleSpec {

    @get:Rule(order = 1)
    val koinTestRule = createKoinExternalResourceRule(appModule)

    @get:Rule(order = 2)
    val composeTestRule = createAndroidComposeRule<MainActivity>()

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
}
