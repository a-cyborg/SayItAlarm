/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.design_system.screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import org.a_cyb.sayitalarm.design_system.R
import org.a_cyb.sayitalarm.design_system.atom.ColumnScreenStandardScrollable
import org.a_cyb.sayitalarm.design_system.atom.IconButtonEdit
import org.a_cyb.sayitalarm.design_system.atom.IconButtonNavigateBack
import org.a_cyb.sayitalarm.design_system.atom.PanelStandard
import org.a_cyb.sayitalarm.design_system.atom.SpacerLarge
import org.a_cyb.sayitalarm.design_system.atom.SpacerXLarge
import org.a_cyb.sayitalarm.design_system.molecule.PanelItemStandard
import org.a_cyb.sayitalarm.design_system.molecule.PanelItemWithPopupPickerStandardWheel
import org.a_cyb.sayitalarm.design_system.molecule.TextRowWarning
import org.a_cyb.sayitalarm.design_system.molecule.TopAppBarMedium
import org.a_cyb.sayitalarm.presentation.SettingsContract
import org.a_cyb.sayitalarm.presentation.SettingsContract.SettingsState.Error
import org.a_cyb.sayitalarm.presentation.SettingsContract.SettingsState.Initial
import org.a_cyb.sayitalarm.presentation.SettingsContract.SettingsState.Success
import org.a_cyb.sayitalarm.presentation.SettingsContract.SettingsViewModel
import org.a_cyb.sayitalarm.presentation.command.CommandContract
import org.a_cyb.sayitalarm.presentation.command.CommandContract.CommandReceiver
import org.a_cyb.sayitalarm.presentation.command.SetSnoozeCommand
import org.a_cyb.sayitalarm.presentation.command.SetThemeCommand
import org.a_cyb.sayitalarm.presentation.command.SetTimeOutCommand

@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel,
    navigateToList: () -> Unit,
    versionName: String,
) {
    val state = viewModel.state.collectAsState()

    ColumnScreenStandardScrollable {
        SettingSTopAppBar(onNavigateBack = navigateToList)
        SpacerLarge()
        when (state.value) {
            is Success -> {
                val settings = (state.value as Success).settingsUI
                SettingsPanel(
                    currentTimeOut = settings.timeOut,
                    currentSnooze = settings.snooze,
                    currentTheme = settings.theme,
                    timeOuts = viewModel.timeOuts,
                    snoozes = viewModel.snoozes,
                    themes = viewModel.themes,
                    execute = { viewModel.runCommand(it) }
                )
            }

            Error -> {
                TextRowWarning(text = stringResource(R.string.info_settings_initialize_error))
            }

            Initial -> {}
        }
        SpacerXLarge()
        InfoPanel(versionName)
    }
}

@Composable
private fun SettingSTopAppBar(onNavigateBack: () -> Unit) {
    TopAppBarMedium(
        title = stringResource(id = R.string.settings),
        navigationIcon = { IconButtonNavigateBack { onNavigateBack() } },
    )
}

@Composable
fun SettingsPanel(
    currentTimeOut: SettingsContract.TimeInput,
    currentSnooze: SettingsContract.TimeInput,
    currentTheme: String,
    timeOuts: List<SettingsContract.TimeInput>,
    snoozes: List<SettingsContract.TimeInput>,
    themes: List<String>,
    execute: (CommandContract.Command<out CommandReceiver>) -> Unit,
) {
    PanelStandard(
        {
            PanelItemTimeOut(
                timeOuts = timeOuts.map { it.formatted },
                currentIdx = timeOuts.indexOf(currentTimeOut),
                onConfirm = { execute(SetTimeOutCommand(timeOuts[it].input)) }
            )
        },
        {
            PanelItemSnooze(
                snoozes = snoozes.map { it.formatted },
                currentIdx = snoozes.indexOf(currentSnooze),
                onConfirm = { execute(SetSnoozeCommand(snoozes[it].input)) }
            )
        },
        {
            PanelItemTheme(
                themes = themes,
                currentIdx = themes.indexOf(currentTheme),
                onConfirm = { execute(SetThemeCommand(themes[it])) }
            )
        },
    )
}

@Composable
fun InfoPanel(versionName: String) {
    PanelStandard(
        { PanelItemAbout() },
        { PanelItemLicense() },
        { PanelItemVersion(versionName) },
    )
}

@Composable
fun PanelItemTimeOut(
    timeOuts: List<String>,
    currentIdx: Int,
    onConfirm: (Int) -> Unit,
) {
    PanelItemWithPopupPickerStandardWheel(
        title = stringResource(id = R.string.timeout),
        info = stringResource(id = R.string.info_timeout),
        values = timeOuts,
        selectedItemIdx = currentIdx,
        popUpPickerOnConfirm = { onConfirm(it) }
    )
}

@Composable
fun PanelItemSnooze(
    snoozes: List<String>,
    currentIdx: Int,
    onConfirm: (Int) -> Unit,
) {
    PanelItemWithPopupPickerStandardWheel(
        title = stringResource(id = R.string.snooze),
        info = stringResource(id = R.string.info_snooze),
        values = snoozes,
        selectedItemIdx = currentIdx,
        popUpPickerOnConfirm = { onConfirm(it) }
    )
}

@Composable
fun PanelItemTheme(
    themes: List<String>,
    currentIdx: Int,
    onConfirm: (Int) -> Unit,
) {
    PanelItemWithPopupPickerStandardWheel(
        title = stringResource(id = R.string.theme),
        values = themes,
        selectedItemIdx = currentIdx,
        popUpPickerOnConfirm = { onConfirm(it) }
    )
}

@Composable
fun PanelItemAbout() {
    var showText by remember { mutableStateOf(false) }

    PanelItemStandard(valueLabel = stringResource(id = R.string.about)) {
        IconButtonEdit { showText = true }
    }
}

@Composable
fun PanelItemLicense() {
    var showText by remember { mutableStateOf(false) }

    PanelItemStandard(valueLabel = stringResource(id = R.string.license)) {
        IconButtonEdit { showText = true }
    }

    if (showText) {
    }
}

@Composable
fun PanelItemVersion(versionName: String) {
    PanelItemStandard(
        valueLabel = stringResource(id = R.string.version),
        value = versionName,
    )
}
