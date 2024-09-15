/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.design_system.screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.res.stringResource
import org.a_cyb.sayitalarm.design_system.R
import org.a_cyb.sayitalarm.design_system.atom.ColumnScreenStandard
import org.a_cyb.sayitalarm.design_system.atom.IconButtonNavigateBack
import org.a_cyb.sayitalarm.design_system.atom.SpacerMedium
import org.a_cyb.sayitalarm.design_system.atom.TextButtonSave
import org.a_cyb.sayitalarm.design_system.molecule.PermissionLauncher
import org.a_cyb.sayitalarm.design_system.molecule.TextBoxWarningTitle
import org.a_cyb.sayitalarm.design_system.molecule.TopAppBarLarge
import org.a_cyb.sayitalarm.design_system.organism.AlarmPanel
import org.a_cyb.sayitalarm.presentation.contracts.AddContract
import org.a_cyb.sayitalarm.presentation.contracts.AddContract.AddState.Error
import org.a_cyb.sayitalarm.presentation.contracts.command.SaveCommand

@Composable
fun AddScreen(
    viewModel: AddContract.AddViewModel,
    navigateToList: () -> Unit,
) {
    val state = viewModel.state.collectAsState()

    PermissionLauncher()

    ColumnScreenStandard {
        AddTopAppBar(
            onCancel = navigateToList,
            onSave = {
                viewModel.runCommand(SaveCommand)
                navigateToList()
            },
        )
        SpacerMedium()

        if (state.value is Error) {
            TextBoxWarningTitle(text = stringResource(id = R.string.info_add_and_edit_initialize_error))
        }

        AlarmPanel(
            alarmUI = state.value.alarmUI,
            executor = { viewModel.runCommand(it) },
        )
    }
}

@Composable
private fun AddTopAppBar(
    onCancel: () -> Unit,
    onSave: () -> Unit,
) {
    TopAppBarLarge(
        title = stringResource(id = R.string.add),
        navigationIcon = {
            IconButtonNavigateBack(onCancel)
        },
        actions = {
            TextButtonSave(onSave)
        },
    )
}
