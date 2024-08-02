/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.design_system.screen

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.a_cyb.sayitalarm.presentation.AlarmContract.AlarmUiState
import org.a_cyb.sayitalarm.presentation.AlarmContract.AlarmViewModel
import org.a_cyb.sayitalarm.presentation.command.CommandContract

@Preview
@Composable
fun AlarmScreenInitialStatePreview() {
    AlarmScreen(AlarmViewModelFake())
}

private class AlarmViewModelFake(
    state: AlarmUiState = AlarmUiState.Initial
) : AlarmViewModel {
    override val state: StateFlow<AlarmUiState> = MutableStateFlow(state)
    override val currentTime: StateFlow<String> = MutableStateFlow("04:00")

    override fun startSayIt() {}
    override fun <T : CommandContract.CommandReceiver> runCommand(command: CommandContract.Command<T>) {}
}