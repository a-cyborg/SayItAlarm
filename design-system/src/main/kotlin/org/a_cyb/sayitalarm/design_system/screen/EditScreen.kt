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
import org.a_cyb.sayitalarm.design_system.atom.TextButtonConfirm
import org.a_cyb.sayitalarm.design_system.atom.IconButtonNavigateBack
import org.a_cyb.sayitalarm.design_system.atom.SpacerMedium
import org.a_cyb.sayitalarm.design_system.molecule.TextBoxWarningTitle
import org.a_cyb.sayitalarm.design_system.molecule.TopAppBarLarge
import org.a_cyb.sayitalarm.design_system.organism.AlarmPanel
import org.a_cyb.sayitalarm.presentation.contracts.EditContract
import org.a_cyb.sayitalarm.presentation.contracts.EditContract.EditViewModel.EditState.Error
import org.a_cyb.sayitalarm.presentation.contracts.EditContract.EditViewModel.EditState.Initial
import org.a_cyb.sayitalarm.presentation.contracts.EditContract.EditViewModel.EditState.Success
import org.a_cyb.sayitalarm.presentation.contracts.command.SaveCommand

@Composable
fun EditScreen(
    viewModel: EditContract.EditViewModel,
    navigateToList: () -> Unit,
) {

    val state = viewModel.state.collectAsState()

    ColumnScreenStandard {
        EditTopAppBar(
            onCancel = navigateToList,
            onConfirm = {
                viewModel.runCommand(SaveCommand)
                navigateToList()
            }
        )
        SpacerMedium()

        when (state.value) {
            is Success -> {
                AlarmPanel(
                    alarmUI = (state.value as Success).alarmUI,
                    executor = { viewModel.runCommand(it) }
                )
            }

            Error -> {
                TextBoxWarningTitle(stringResource(id = R.string.info_add_and_edit_initialize_error))
            }

            Initial -> {}
        }

    }
}

@Composable
private fun EditTopAppBar(
    onCancel: () -> Unit,
    onConfirm: () -> Unit,
) {
    TopAppBarLarge(
        title = stringResource(id = R.string.edit),
        navigationIcon = { IconButtonNavigateBack(onCancel) },
        actions = { TextButtonConfirm(onConfirm) }
    )
}
