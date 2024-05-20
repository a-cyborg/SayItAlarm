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
import org.a_cyb.sayitalarm.atom.PanelRowStandard
import org.a_cyb.sayitalarm.atom.PanelStandard
import org.a_cyb.sayitalarm.atom.SpacerLarge
import org.a_cyb.sayitalarm.atom.SpacerXLarge
import org.a_cyb.sayitalarm.atom.TextTitleStandardLarge
import org.a_cyb.sayitalarm.entity.Theme
import org.a_cyb.sayitalarm.molecule.PopUpPickerStandardWheel
import org.a_cyb.sayitalarm.molecule.TextRowTimeDuration
import org.a_cyb.sayitalarm.molecule.TextRowWarning
import org.a_cyb.sayitalarm.molecule.TopAppBarGlobal
import org.a_cyb.sayitalarm.presentation.CommandContract
import org.a_cyb.sayitalarm.presentation.CommandContract.CommandReceiver
import org.a_cyb.sayitalarm.presentation.SetSnoozeCommand
import org.a_cyb.sayitalarm.presentation.SetThemeCommand
import org.a_cyb.sayitalarm.presentation.SetTimeOutCommand
import org.a_cyb.sayitalarm.presentation.SettingsContract.Error
import org.a_cyb.sayitalarm.presentation.SettingsContract.SettingsStateWithContent
import org.a_cyb.sayitalarm.presentation.SettingsContract.SettingsViewModel

@Composable
private fun SettingsTopAppBar(onNavigateBack: () -> Unit) {
    TopAppBarGlobal(
        title = stringResource(id = R.string.settings),
        firstIcon = { IconButtonNavigateBack { onNavigateBack() } },
    )
}

@Suppress("MagicNumber")
@Composable
fun PanelItemTimeOut(value: Int, execute: (CommandContract.Command<out CommandReceiver>) -> Unit) {
    val timeOuts = (30..300).toList()
    var showPopUpPicker by remember { mutableStateOf(false) }

    PanelRowStandard(
        valueLabel = stringResource(id = R.string.timeout),
        value = stringResource(id = R.string.minute_short, value),
    ) {
        IconButtonEdit { showPopUpPicker = true }
    }

    if (showPopUpPicker) {
        PopUpPickerStandardWheel(
            title = stringResource(id = R.string.timeout),
            info = stringResource(id = R.string.info_timeout),
            pickerValues = (30..300).toList(),
            pickerInitIdx = timeOuts.indexOf(value),
            pickerItemRow = { TextRowTimeDuration(minutes = it) },
            onDismiss = { showPopUpPicker = false },
            onConfirm = { execute(SetTimeOutCommand(it)) },
        )
    }
}

@Suppress("MagicNumber")
@Composable
fun PanelItemSnooze(
    snoozes: List<String>,
    value: Int,
    execute: (CommandContract.Command<out CommandReceiver>) -> Unit,
) {
    val snoozes = (5..60).toList()
    var showPopUpPicker by remember { mutableStateOf(false) }

    PanelRowStandard(
        valueLabel = stringResource(id = R.string.snooze),
        value = stringResource(id = R.string.minute_short, value),
    ) {
        IconButtonEdit { showPopUpPicker = true }
    }

    if (showPopUpPicker) {
        PopUpPickerStandardWheel(
            title = stringResource(id = R.string.snooze),
            info = stringResource(id = R.string.info_snooze),
            pickerValues = snoozes,
            pickerInitIdx = snoozes.indexOf(value),
            pickerItemRow = { TextRowTimeDuration(minutes = it) },
            onDismiss = { showPopUpPicker = false },
            onConfirm = { execute(SetSnoozeCommand(it)) },
        )
    }
}

@Composable
fun PanelItemTheme(value: Theme, execute: (CommandContract.Command<out CommandReceiver>) -> Unit) {
    val themes = Theme.entries.map { it.name.toCamelCase() }
    var displayPopUpPicker by remember { mutableStateOf(false) }

    PanelRowStandard(
        valueLabel = stringResource(id = R.string.theme),
        value = value.name.toCamelCase(),
    ) {
        IconButtonEdit { displayPopUpPicker = true }
    }

    if (displayPopUpPicker) {
        PopUpPickerStandardWheel(
            title = stringResource(id = R.string.theme),
            pickerValues = themes,
            pickerInitIdx = themes.indexOf(value.name.toCamelCase()),
            pickerItemRow = { TextTitleStandardLarge(it) },
            onDismiss = { displayPopUpPicker = false },
            onConfirm = { execute(SetThemeCommand(Theme.valueOf(it.uppercase()))) },
        )
    }
}

private fun String.toCamelCase() = this.lowercase().replaceFirstChar(Char::titlecase)

@Composable
fun SettingsPanel(
    currentTimeOut: Int,
    currentSnooze: Int,
    currentTheme: Theme,
    snoozes: List<String>,
    executor: (CommandContract.Command<out CommandReceiver>) -> Unit,
) {
    PanelStandard(
        panelItems = listOf(
            { PanelItemTimeOut(value = currentTimeOut, execute = executor) },
            { PanelItemSnooze(snoozes = snoozes, value = currentSnooze, execute = executor) },
            { PanelItemTheme(value = currentTheme, execute = executor) },
        ),
    )
}

@Composable
fun PanelItemAbout() {
    var showText by remember { mutableStateOf(false) }

    PanelRowStandard(valueLabel = stringResource(id = R.string.about)) {
        IconButtonEdit { showText = true }
    }
}

@Composable
fun PanelItemVersion() {
    PanelRowStandard(
        valueLabel = stringResource(id = R.string.version),
        value = BuildConfig.VERSION_NAME,
    )
}

@Composable
fun InfoPanel() {
    PanelStandard(
        panelItems = listOf(
            { PanelItemAbout() },
            { PanelItemVersion() },
        ),
    )
}

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
                    currentTimeOut = settings.timeOut.input,
                    currentSnooze = settings.snooze.input,
                    currentTheme = settings.theme,
                    executor = { viewModel.runCommand(it) },
                    snoozes = viewModel.snoozes
                )
            }

            is Error -> {
                val errorMessage = (state.value as Error).error.name
                TextRowWarning(text = errorMessage)
            }
        }
        SpacerXLarge()
        InfoPanel()
    }
}
