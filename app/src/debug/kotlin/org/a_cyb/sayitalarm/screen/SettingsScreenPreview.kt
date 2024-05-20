/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.screen

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import org.a_cyb.sayitalarm.entity.Theme
import org.a_cyb.sayitalarm.presentation.SettingsContract

@Preview
@Composable
fun SettingsScreenPreview() {
    val settingsStateWithContent = SettingsContract.SettingsStateWithContent(
        timeOut = SettingsContract.ValidTimeInput(180),
        snooze = SettingsContract.ValidTimeInput(15),
        theme = Theme.LIGHT,
    )

    SettingsScreen(
        viewModel = SettingsViewModelFake(settingsStateWithContent),
    )
}

@Preview
@Composable
fun SettingsScreenErrorStatePreview() {
    val settingsStateWithContent = SettingsContract.Error(
        SettingsContract.SettingsError.INITIAL_SETTINGS_UNRESOLVED,
    )

    SettingsScreen(
        viewModel = SettingsViewModelFake(settingsStateWithContent),
    )
}