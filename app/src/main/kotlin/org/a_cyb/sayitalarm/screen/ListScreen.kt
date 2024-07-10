/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import org.a_cyb.sayitalarm.R
import org.a_cyb.sayitalarm.atom.ColumnScreenStandard
import org.a_cyb.sayitalarm.atom.DividerStandard
import org.a_cyb.sayitalarm.atom.IconButtonAdd
import org.a_cyb.sayitalarm.atom.IconButtonDelete
import org.a_cyb.sayitalarm.atom.IconButtonDone
import org.a_cyb.sayitalarm.atom.IconButtonEdit
import org.a_cyb.sayitalarm.atom.IconButtonEditText
import org.a_cyb.sayitalarm.atom.IconButtonSettings
import org.a_cyb.sayitalarm.atom.SpacerLarge
import org.a_cyb.sayitalarm.atom.SpacerMedium
import org.a_cyb.sayitalarm.atom.SpacerSmall
import org.a_cyb.sayitalarm.atom.SpacerXSmall
import org.a_cyb.sayitalarm.atom.SwitchStandard
import org.a_cyb.sayitalarm.atom.TextHeadlineStandardLarge
import org.a_cyb.sayitalarm.atom.TextTitleStandardMedium
import org.a_cyb.sayitalarm.molecule.ListItemStandard
import org.a_cyb.sayitalarm.molecule.TextRowInfo
import org.a_cyb.sayitalarm.molecule.TopAppBarMedium
import org.a_cyb.sayitalarm.presentation.ListContract
import org.a_cyb.sayitalarm.presentation.ListContract.ListState.Error
import org.a_cyb.sayitalarm.presentation.ListContract.ListState.InitialError
import org.a_cyb.sayitalarm.presentation.ListContract.ListState.Success
import org.a_cyb.sayitalarm.presentation.ListContract.ListViewModel
import org.a_cyb.sayitalarm.presentation.command.CommandContract
import org.a_cyb.sayitalarm.presentation.command.CommandContract.CommandReceiver
import org.a_cyb.sayitalarm.presentation.command.DeleteAlarmCommand
import org.a_cyb.sayitalarm.presentation.command.SetEnabledCommand
import org.a_cyb.sayitalarm.screen.ListScreenMode.EDIT
import org.a_cyb.sayitalarm.screen.ListScreenMode.VIEW
import org.a_cyb.sayitalarm.token.Color

enum class ListScreenMode { EDIT, VIEW }

@Composable
fun ListScreen(
    viewModel: ListViewModel,
    navigateToAdd: () -> Unit,
    navigateToEdit: (Long) -> Unit,
    navigateToSettings: () -> Unit,
) {
    val state = viewModel.state.collectAsState()

    var mode by rememberSaveable { mutableStateOf(VIEW) }

    LaunchedEffect(Unit) {
        mode = VIEW
    }

    ColumnScreenStandard {
        ListTopAppBar(
            screenMode = mode,
            onEditClick = { mode = EDIT },
            onDoneClick = { mode = VIEW },
            onAddClick = navigateToAdd,
            onSettingsClick = navigateToSettings
        )
        SpacerLarge()
        when (state.value) {
            is Success -> {
                val alarms = (state.value as Success).alarmData

                if (alarms.isEmpty()) {
                    TextRowInfo(text = stringResource(id = R.string.info_list_no_alarm))
                }

                LazyColumn(modifier = Modifier.background(Color.surface.standard)) {
                    items(alarms) {
                        AlarmListItem(
                            alarmInfo = it,
                            screenMode = mode,
                            navigateToEdit = { navigateToEdit(it.id) },
                            executor = { command -> viewModel.runCommand(command) }
                        )
                    }
                }
            }

            is InitialError, is Error -> {
                TextRowInfo(text = stringResource(id = R.string.info_list_initialize_error))
            }

            else -> {}
        }
    }
}

@Composable
private fun ListTopAppBar(
    screenMode: ListScreenMode,
    onEditClick: () -> Unit,
    onDoneClick: () -> Unit,
    onAddClick: () -> Unit,
    onSettingsClick: () -> Unit,
) {
    TopAppBarMedium(
        title = "SayIt",
        actions = {
            when (screenMode) {
                VIEW -> IconButtonEditText { onEditClick() }
                EDIT -> IconButtonDone { onDoneClick() }
            }
            IconButtonAdd { onAddClick() }
            IconButtonSettings { onSettingsClick() }
        }
    )
}

@Composable
private fun AlarmListItem(
    alarmInfo: ListContract.AlarmInfo,
    screenMode: ListScreenMode,
    navigateToEdit: () -> Unit,
    executor: (CommandContract.Command<out CommandReceiver>) -> Unit,
) {
    ListItemStandard(
        beforeContent = {
            if (screenMode == EDIT) IconButtonDelete {
                executor(DeleteAlarmCommand(alarmInfo.id))
            }
        },
        content = {
            AlarmInfo(
                time = alarmInfo.time,
                labelAndRepeat = alarmInfo.labelAndWeeklyRepeat
            )
        },
        afterContent = {
            when (screenMode) {
                EDIT -> {
                    IconButtonEdit { navigateToEdit() }
                }

                VIEW -> {
                    SwitchStandard(checked = alarmInfo.enabled) { toggled ->
                        executor(SetEnabledCommand(alarmInfo.id, toggled))
                    }
                    SpacerMedium()
                }
            }
        }
    )
    DividerStandard()
    SpacerLarge()
}

@Composable
private fun RowScope.AlarmInfo(time: String, labelAndRepeat: String) {
    Column(
        modifier = Modifier.weight(1f),
        verticalArrangement = Arrangement.Center,
    ) {
        TextHeadlineStandardLarge(text = time)
        SpacerSmall()
        TextTitleStandardMedium(text = labelAndRepeat)
        SpacerXSmall()
    }
}
