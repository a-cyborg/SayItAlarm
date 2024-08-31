/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.design_system.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import org.a_cyb.sayitalarm.design_system.R
import org.a_cyb.sayitalarm.design_system.atom.ColumnScreenStandardScrollable
import org.a_cyb.sayitalarm.design_system.atom.DialogStandardFillMaxScrollable
import org.a_cyb.sayitalarm.design_system.atom.DividerStandard
import org.a_cyb.sayitalarm.design_system.atom.IconButtonClose
import org.a_cyb.sayitalarm.design_system.atom.IconButtonEdit
import org.a_cyb.sayitalarm.design_system.atom.IconButtonNavigateBack
import org.a_cyb.sayitalarm.design_system.atom.PanelStandard
import org.a_cyb.sayitalarm.design_system.atom.SpacerLarge
import org.a_cyb.sayitalarm.design_system.atom.SpacerMedium
import org.a_cyb.sayitalarm.design_system.atom.SpacerXLarge
import org.a_cyb.sayitalarm.design_system.atom.TextButtonCopy
import org.a_cyb.sayitalarm.design_system.atom.TextButtonEmail
import org.a_cyb.sayitalarm.design_system.atom.TextButtonGitHub
import org.a_cyb.sayitalarm.design_system.atom.TextButtonGooglePlay
import org.a_cyb.sayitalarm.design_system.molecule.PanelItemStandard
import org.a_cyb.sayitalarm.design_system.molecule.PanelItemWithPopupPickerStandardWheel
import org.a_cyb.sayitalarm.design_system.molecule.TextBoxStandardBody
import org.a_cyb.sayitalarm.design_system.molecule.TextBoxWarningTitle
import org.a_cyb.sayitalarm.design_system.molecule.TopAppBarLarge
import org.a_cyb.sayitalarm.design_system.token.Spacing
import org.a_cyb.sayitalarm.presentation.SettingsContract
import org.a_cyb.sayitalarm.presentation.SettingsContract.SettingsState.Error
import org.a_cyb.sayitalarm.presentation.SettingsContract.SettingsState.Initial
import org.a_cyb.sayitalarm.presentation.SettingsContract.SettingsState.Success
import org.a_cyb.sayitalarm.presentation.SettingsContract.SettingsViewModel
import org.a_cyb.sayitalarm.presentation.command.CommandContract
import org.a_cyb.sayitalarm.presentation.command.CommandContract.CommandReceiver
import org.a_cyb.sayitalarm.presentation.command.OpenGitHubCommand
import org.a_cyb.sayitalarm.presentation.command.OpenGooglePlayCommand
import org.a_cyb.sayitalarm.presentation.command.SendEmailCommand
import org.a_cyb.sayitalarm.presentation.command.SetSnoozeCommand
import org.a_cyb.sayitalarm.presentation.command.SetThemeCommand
import org.a_cyb.sayitalarm.presentation.command.SetTimeOutCommand

@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel,
    versionName: String,
    navigateToList: () -> Unit,
) {
    val state = viewModel.state.collectAsState()

    ColumnScreenStandardScrollable {
        SettingsTopAppBar(onNavigateBack = navigateToList)
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
                TextBoxWarningTitle(text = stringResource(R.string.info_settings_initialize_error))
            }

            Initial -> {}
        }
        SpacerXLarge()
        InfoPanel(
            versionName = versionName,
            contact = viewModel.contact,
            execute = { viewModel.runCommand(it) }
        )
    }
}

@Composable
private fun SettingsTopAppBar(onNavigateBack: () -> Unit) {
    TopAppBarLarge(
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
        }
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
fun InfoPanel(
    versionName: String,
    contact: SettingsContract.Contact,
    execute: (CommandContract.Command<out CommandReceiver>) -> Unit,
) {
    PanelStandard(
        { PanelItemAbout(contact, execute) },
        { PanelItemLicense() },
        { PanelItemVersion(versionName) },
    )
}

@Composable
private fun PanelItemAbout(
    contact: SettingsContract.Contact,
    execute: (CommandContract.Command<out CommandReceiver>) -> Unit
) {
    var isShowAbout by remember { mutableStateOf(false) }

    PanelItemStandard(valueLabel = stringResource(id = R.string.about)) {
        IconButtonEdit { isShowAbout = true }
    }

    if (isShowAbout) {
        AboutDialog(
            onDisMiss = { isShowAbout = false },
            contact = contact,
            execute = execute
        )
    }
}

@Composable
private fun AboutDialog(
    onDisMiss: () -> Unit,
    contact: SettingsContract.Contact,
    execute: (CommandContract.Command<out CommandReceiver>) -> Unit,
) {
    DialogStandardFillMaxScrollable(
        onDismiss = onDisMiss,
        topAppBar = { AboutDialogTopBar { onDisMiss() } }
    ) {
        TextBoxStandardBody(text = stringResource(id = R.string.info_about))
        SpacerMedium()
        DividerStandard()
        ContactRow(copyString = contact.email) { TextButtonEmail { execute(SendEmailCommand) } }
        ContactRow(copyString = contact.googlePlayUrl) { TextButtonGooglePlay { execute(OpenGooglePlayCommand) } }
        ContactRow(copyString = contact.githubUrl) { TextButtonGitHub { execute(OpenGitHubCommand) } }
    }
}

@Composable
private fun AboutDialogTopBar(navigateBack: () -> Unit) {
    TopAppBarLarge(
        title = stringResource(id = R.string.about),
        navigationIcon = { IconButtonClose(onClick = navigateBack) },
    )
}

@Composable
private fun ContactRow(
    copyString: String,
    contactContents: @Composable () -> Unit,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .padding(horizontal = Spacing.m)
            .fillMaxWidth()
    ) {
        contactContents()
        Spacer(Modifier.weight(1f))
        TextButtonCopy(copyString = copyString)
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
