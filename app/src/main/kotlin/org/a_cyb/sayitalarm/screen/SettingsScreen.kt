/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import org.a_cyb.sayitalarm.BuildConfig
import org.a_cyb.sayitalarm.R
import org.a_cyb.sayitalarm.atom.ColumnScreenStandardScrollable
import org.a_cyb.sayitalarm.atom.IconButtonEdit
import org.a_cyb.sayitalarm.atom.IconButtonNavigateBack
import org.a_cyb.sayitalarm.atom.PanelStandard
import org.a_cyb.sayitalarm.atom.SpacerLarge
import org.a_cyb.sayitalarm.atom.SpacerXLarge
import org.a_cyb.sayitalarm.molecule.PanelItemStandard
import org.a_cyb.sayitalarm.molecule.PanelItemWithPopupPickerStandardWheel
import org.a_cyb.sayitalarm.molecule.TextRowWarning
import org.a_cyb.sayitalarm.molecule.TopAppBarGlobal
import org.a_cyb.sayitalarm.presentation.CommandContract
import org.a_cyb.sayitalarm.presentation.CommandContract.CommandReceiver
import org.a_cyb.sayitalarm.presentation.settings.SetSnoozeCommand
import org.a_cyb.sayitalarm.presentation.settings.SetThemeCommand
import org.a_cyb.sayitalarm.presentation.settings.SetTimeOutCommand
import org.a_cyb.sayitalarm.presentation.settings.SettingsContract
import org.a_cyb.sayitalarm.presentation.settings.SettingsContract.InitialError
import org.a_cyb.sayitalarm.presentation.settings.SettingsContract.SettingsStateWithContent
import org.a_cyb.sayitalarm.presentation.settings.SettingsContract.SettingsViewModel

@Composable
fun SettingsScreen(viewModel: SettingsViewModel) {
    val state = viewModel.state.collectAsState()

    ColumnScreenStandardScrollable {
        SettingsTopAppBar(onNavigateBack = {})
        SpacerLarge()
        when (state.value) {
            is SettingsStateWithContent -> {
                val settings = (state.value as SettingsStateWithContent)
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

            is InitialError -> {
                TextRowWarning(text = stringResource(R.string.info_settings_initialize_error))
            }
        }
        SpacerXLarge()
        InfoPanel()
    }
}

@Composable
private fun SettingsTopAppBar(onNavigateBack: () -> Unit) {
    TopAppBarGlobal(
        title = stringResource(id = R.string.settings),
        firstIcon = { IconButtonNavigateBack { onNavigateBack() } },
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
fun InfoPanel() {
    PanelStandard(
        { PanelItemAbout() },
        { PanelItemLicense() },
        { PanelItemVersion() },
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
fun PanelItemVersion() {
    PanelItemStandard(
        valueLabel = stringResource(id = R.string.version),
        value = BuildConfig.VERSION_NAME,
    )
}
