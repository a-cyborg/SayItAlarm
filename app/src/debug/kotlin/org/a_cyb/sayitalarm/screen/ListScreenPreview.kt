/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.screen

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.a_cyb.sayitalarm.presentation.command.CommandContract
import org.a_cyb.sayitalarm.presentation.ListContract

@Preview
@Composable
fun ListScreenPreview() {
    ListScreen(viewModel = ListViewModelFake())
}

@Preview
@Composable
fun ListScreenInitPreview() {
    ListScreen(viewModel = ListViewModelFake(ListContract.Success(listOf())))
}

private val success = ListContract.Success(
    listOf(
        ListContract.AlarmInfo(
            id = 1,
            time = "6:00 AM",
            labelAndWeeklyRepeat = "Wake Up, every weekday",
            enabled = true
        ),
        ListContract.AlarmInfo(
            id = 2,
            time = "8:30 PM",
            labelAndWeeklyRepeat = "Workout, Mon, Wed, and Fri",
            enabled = true
        ),
        ListContract.AlarmInfo(
            id = 3,
            time = "9:00 AM",
            labelAndWeeklyRepeat = "Passion Hour, every weekend",
            enabled = false
        ),
    )
)

private class ListViewModelFake(
    initState: ListContract.ListState = success,
) : ListContract.ListViewModel {

    override val state: StateFlow<ListContract.ListState> = MutableStateFlow(initState)
    override fun setEnabled(id: Long, enabled: Boolean) {}
    override fun deleteAlarm(id: Long) {}
    override fun <T : CommandContract.CommandReceiver> runCommand(command: CommandContract.Command<T>) {}
}
