/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.design_system.screen

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import org.a_cyb.sayitalarm.design_system.R
import org.a_cyb.sayitalarm.design_system.atom.ColumnScreenStandardScrollableNoFooter
import org.a_cyb.sayitalarm.design_system.atom.DividerMedium
import org.a_cyb.sayitalarm.design_system.atom.SpacerMedium
import org.a_cyb.sayitalarm.design_system.atom.SpacerXLarge
import org.a_cyb.sayitalarm.design_system.atom.SpacerXxxxLarge
import org.a_cyb.sayitalarm.design_system.atom.TextButtonCircleExit
import org.a_cyb.sayitalarm.design_system.atom.TextButtonCircleFinish
import org.a_cyb.sayitalarm.design_system.atom.TextDisplayStandardSmall
import org.a_cyb.sayitalarm.design_system.atom.TextTitleSubtleLarge
import org.a_cyb.sayitalarm.design_system.molecule.TextBoxSayItScript
import org.a_cyb.sayitalarm.design_system.molecule.TextBoxStatusHeaderError
import org.a_cyb.sayitalarm.design_system.molecule.TextBoxStatusHeaderFailed
import org.a_cyb.sayitalarm.design_system.molecule.TextBoxStatusHeaderListening
import org.a_cyb.sayitalarm.design_system.molecule.TextBoxStatusHeaderSuccess
import org.a_cyb.sayitalarm.design_system.molecule.TextBoxSttResult
import org.a_cyb.sayitalarm.design_system.molecule.TextBoxWarningBody
import org.a_cyb.sayitalarm.design_system.screen.InProgressState.FAILED
import org.a_cyb.sayitalarm.design_system.screen.InProgressState.LISTENING
import org.a_cyb.sayitalarm.design_system.screen.InProgressState.SUCCESS
import org.a_cyb.sayitalarm.design_system.token.Color
import org.a_cyb.sayitalarm.presentation.contracts.SayItContract
import org.a_cyb.sayitalarm.presentation.contracts.SayItContract.SayItViewModel
import org.a_cyb.sayitalarm.presentation.contracts.command.CommandContract
import org.a_cyb.sayitalarm.presentation.contracts.command.CommandContract.CommandReceiver
import org.a_cyb.sayitalarm.presentation.contracts.command.FinishCommand

enum class InProgressState { LISTENING, SUCCESS, FAILED }

@Composable
fun SayItScreen(viewModel: SayItViewModel) {
    val state = viewModel.state.collectAsState()

    ColumnScreenStandardScrollableNoFooter {
        SpacerXxxxLarge()
        when (val currentState = state.value) {
            is SayItContract.SayItUiState.Initial -> InitialScene()
            is SayItContract.SayItUiState.Listening -> InProcessScene(LISTENING, currentState.sayItUi)
            is SayItContract.SayItUiState.Success -> InProcessScene(SUCCESS, currentState.sayItUi)
            is SayItContract.SayItUiState.Failed -> InProcessScene(FAILED, currentState.sayItUi)
            is SayItContract.SayItUiState.Finished -> FinishScene { viewModel.runCommand(it) }
            is SayItContract.SayItUiState.Error -> ErrorScene(currentState.message) { viewModel.runCommand(it) }
        }
        Spacer(modifier = Modifier.weight(1f))
    }
}

@Composable
private fun ColumnScope.InitialScene() {
    Spacer(Modifier.weight(1f))
    TextTitleSubtleLarge(text = stringResource(id = R.string.loading))
}

@Composable
private fun InProcessScene(state: InProgressState, info: SayItContract.SayItUIInfo) {
    val countString = "${info.currentCount} / ${info.totalCount}"

    when (state) {
        LISTENING -> TextBoxStatusHeaderListening(countString)
        SUCCESS -> TextBoxStatusHeaderSuccess(countString)
        FAILED -> TextBoxStatusHeaderFailed(countString)
    }

    SpacerMedium()
    ProgressCounterBar(current = info.currentCount, total = info.totalCount)
    SpacerXLarge()
    TextBoxSayItScript(text = info.script)
    TextBoxSttResult(text = info.transcript)
}

@Composable
private fun ProgressCounterBar(current: Int, total: Int) {
    val progressRatio by animateFloatAsState(
        targetValue = current / total.toFloat(),
        label = "SayItScriptCounter",
    )

    LinearProgressIndicator(
        progress = { progressRatio },
        color = Color.surface.attention,
    )
}

@Composable
private fun ColumnScope.FinishScene(
    runCommand: (CommandContract.Command<out CommandReceiver>) -> Unit,
) {
    TextDisplayStandardSmall(text = stringResource(id = R.string.say_it))
    SpacerMedium()
    DividerMedium()
    Spacer(Modifier.weight(1f))
    TextButtonCircleFinish { runCommand(FinishCommand) }
}

@Composable
private fun ColumnScope.ErrorScene(
    message: String,
    runCommand: (CommandContract.Command<out CommandReceiver>) -> Unit,
) {
    TextBoxStatusHeaderError()
    SpacerMedium()
    DividerMedium()
    TextBoxWarningBody(text = stringResource(id = R.string.info_say_it_error))
    TextBoxWarningBody(text = "${stringResource(id = R.string.info_say_it_error_name)} $message")
    Spacer(Modifier.weight(1f))
    TextButtonCircleExit { runCommand(FinishCommand) }
}

// @Composable
// private fun ColumnScope.Footer(isOffline: SayItContract.IsOffline) {
//     Spacer(modifier = Modifier.weight(1f))
//     // After implementing support for the only offline STT model, remove this.
//     if (isOffline == SayItContract.IsOffline.False) {
//         Row(Modifier.padding(horizontal = Spacing.m)) {
//             TextBodySubtleMedium(text = stringResource(R.string.info_stt_recognizer_online))
//         }
//     }
//     SpacerSmall()
//     TextBodyStandardSmall(text = stringResource(R.string.say_it))
//     SpacerMedium()
// }
