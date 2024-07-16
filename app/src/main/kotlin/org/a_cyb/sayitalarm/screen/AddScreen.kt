/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.res.stringResource
import org.a_cyb.sayitalarm.R
import org.a_cyb.sayitalarm.atom.ColumnScreenStandard
import org.a_cyb.sayitalarm.atom.IconButtonNavigateBack
import org.a_cyb.sayitalarm.atom.IconButtonSaveText
import org.a_cyb.sayitalarm.atom.SpacerMedium
import org.a_cyb.sayitalarm.molecule.PermissionLauncher
import org.a_cyb.sayitalarm.molecule.TextRowWarning
import org.a_cyb.sayitalarm.molecule.TopAppBarMedium
import org.a_cyb.sayitalarm.organism.AlarmPanel
import org.a_cyb.sayitalarm.presentation.AddContract
import org.a_cyb.sayitalarm.presentation.AddContract.AddState.Error
import org.a_cyb.sayitalarm.presentation.command.SaveCommand

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
            }
        )
        SpacerMedium()

        if (state.value is Error) {
            TextRowWarning(text = stringResource(id = R.string.info_add_and_edit_initialize_error))
        }

        AlarmPanel(
            alarmUI = state.value.alarmUI,
            executor = { viewModel.runCommand(it) }
        )
    }
}

@Composable
private fun AddTopAppBar(
    onCancel: () -> Unit,
    onSave: () -> Unit,
) {
    TopAppBarMedium(
        title = stringResource(id = R.string.add),
        navigationIcon = {
            IconButtonNavigateBack(onCancel)
        },
        actions = {
            IconButtonSaveText(onSave)
        }
    )
}
