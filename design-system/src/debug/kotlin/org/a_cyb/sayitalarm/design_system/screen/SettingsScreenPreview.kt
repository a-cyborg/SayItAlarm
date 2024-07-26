/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.design_system.screen

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import org.a_cyb.sayitalarm.presentation.SettingsContract

@Preview
@Composable
fun SettingsScreenPreview() {
    SettingsScreen(
        viewModel = SettingsViewModelFake(),
        navigateToList = {},
        versionName = "1.0",
    )
}

@Preview
@Composable
fun SettingsScreenErrorStatePreview() {
    val settingsStateWithContent = SettingsContract.SettingsState.Error

    SettingsScreen(
        viewModel = SettingsViewModelFake(settingsStateWithContent),
        navigateToList = {},
        versionName = "1.0",
    )
}