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
import org.a_cyb.sayitalarm.design_system.token.Color
import org.a_cyb.sayitalarm.presentation.SayItContract
import org.a_cyb.sayitalarm.presentation.SayItContract.SayItState
import org.a_cyb.sayitalarm.presentation.SayItContract.SayItState.Error
import org.a_cyb.sayitalarm.presentation.SayItContract.SayItState.Finished
import org.a_cyb.sayitalarm.presentation.SayItContract.SayItState.Initial
import org.a_cyb.sayitalarm.presentation.SayItContract.SayItState.Processing
import org.a_cyb.sayitalarm.presentation.SayItContract.SttStatus
import org.a_cyb.sayitalarm.presentation.command.CommandContract

@Preview
@Composable
fun SayItScreenPreview_Processing_Ready() {
    Color.useDarkTheme()
    val state = Processing(sayItInfo)
    SayItScreen(viewModel = SayItViewModelFake(state))
}

@Preview
@Composable
fun SayItScreenPreview_Processing_Listening() {
    val state = Processing(sayItInfo.copy(status = SttStatus.LISTENING))
    SayItScreen(viewModel = SayItViewModelFake(state))
}

@Preview
@Composable
fun SayItScreenPreview_Processing_Success() {
    val state = Processing(sayItInfo.copy(status = SttStatus.SUCCESS))
    SayItScreen(viewModel = SayItViewModelFake(state))
}

@Preview
@Composable
fun SayItScreenPreview_Processing_Failed() {
    val state = Processing(sayItInfo.copy(status = SttStatus.FAILED))
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
    SayItScreen(viewModel = SayItViewModelFake(Error))
}

@Preview
@Composable
fun SayItScreenPreview_Initial() {
    SayItScreen(viewModel = SayItViewModelFake(Initial))
}

private class SayItViewModelFake(state: SayItState) : SayItContract.SayItViewModel {
    override val state: StateFlow<SayItState> = MutableStateFlow(state)
    override val isOffline: StateFlow<SayItContract.IsOffline> = MutableStateFlow(SayItContract.IsOffline.False)

    override fun processScript() {}
    override fun finish() {}
    override fun <T : CommandContract.CommandReceiver> runCommand(command: CommandContract.Command<T>) {}
}

private val sayItInfo = SayItContract.SayItInfo(
    script = "I embrace this hour with enthusiasm.",
    sttResult = "I embrace this",
    status = SttStatus.READY,
    count = SayItContract.Count(3, 7)
)
