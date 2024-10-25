/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.di

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.a_cyb.sayitalarm.presentation.contracts.AddContract
import org.a_cyb.sayitalarm.presentation.contracts.AlarmContract
import org.a_cyb.sayitalarm.presentation.contracts.EditContract
import org.a_cyb.sayitalarm.presentation.contracts.ListContract
import org.a_cyb.sayitalarm.presentation.contracts.SayItContract
import org.a_cyb.sayitalarm.presentation.contracts.SettingsContract
import org.a_cyb.sayitalarm.presentation.viewmodel.AddViewModel
import org.a_cyb.sayitalarm.presentation.viewmodel.AlarmViewModel
import org.a_cyb.sayitalarm.presentation.viewmodel.EditViewModel
import org.a_cyb.sayitalarm.presentation.viewmodel.ListViewModel
import org.a_cyb.sayitalarm.presentation.viewmodel.SayItViewModel
import org.a_cyb.sayitalarm.presentation.viewmodel.SettingsViewModel
import org.a_cyb.sayitalarm.util.test_utils.createAddActivityToRobolectricRule
import org.a_cyb.sayitalarm.util.test_utils.createKoinExternalResourceRule
import org.a_cyb.sayitalarm.util.test_utils.fulfils
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@RunWith(AndroidJUnit4::class)
class AppModuleComposeSpec {

    @get:Rule(order = 1)
    val koinTestRule = createKoinExternalResourceRule(appModule)

    @get:Rule(order = 2)
    val addActivityRule = createAddActivityToRobolectricRule()

    @get:Rule(order = 3)
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun `Injects viewModels with compose extension`() {
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
