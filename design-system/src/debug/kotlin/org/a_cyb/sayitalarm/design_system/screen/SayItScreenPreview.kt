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
import org.a_cyb.sayitalarm.presentation.contracts.SayItContract
import org.a_cyb.sayitalarm.presentation.contracts.SayItContract.SayItUIInfo
import org.a_cyb.sayitalarm.presentation.contracts.SayItContract.SayItUiState
import org.a_cyb.sayitalarm.presentation.contracts.SayItContract.SayItUiState.Finished
import org.a_cyb.sayitalarm.presentation.contracts.SayItContract.SayItUiState.Initial
import org.a_cyb.sayitalarm.presentation.contracts.command.CommandContract

@Preview
@Composable
fun SayItScreenPreview_InitialScene() {
    val state = Initial
    SayItScreen(viewModel = SayItViewModelFake(state))
}

private val sayItInfo =
    SayItUIInfo(
        "I embrace this hour with enthusiasm.",
        "I embrace",
        1,
        3,
    )

@Preview
@Composable
fun SayItScreenPreview_InProgressScene_Listening() {
    val state = SayItUiState.Listening(sayItInfo)
    SayItScreen(viewModel = SayItViewModelFake(state))
}

@Preview
@Composable
fun SayItScreenPreview_InProgressScene_Success() {
    val state = SayItUiState.Success(
        sayItInfo.copy(transcript = sayItInfo.script),
    )
    SayItScreen(viewModel = SayItViewModelFake(state))
}

@Preview
@Composable
fun SayItScreenPreview_InProgressScene_Failed() {
    val state = SayItUiState.Failed(sayItInfo)
    SayItScreen(viewModel = SayItViewModelFake(state))
}

@Preview
@Composable
fun SayItScreenPreview_Finished() {
    SayItScreen(viewModel = SayItViewModelFake(Finished))
}

@Preview
@Composable
fun SayItScreenPreview_Error() {
    val state = SayItUiState.Error("SERVICE_DISCONNECTED")
    SayItScreen(viewModel = SayItViewModelFake(state))
}

private class SayItViewModelFake(state: SayItUiState) : SayItContract.SayItViewModel {
    override val state: StateFlow<SayItUiState> = MutableStateFlow(state)
    // override val isOffline: StateFlow<SayItContract.IsOffline> = MutableStateFlow(SayItContract.IsOffline.True)

    override fun processScript() {}
    override fun finish() {}
    override fun <T : CommandContract.CommandReceiver> runCommand(command: CommandContract.Command<T>) {}
}
