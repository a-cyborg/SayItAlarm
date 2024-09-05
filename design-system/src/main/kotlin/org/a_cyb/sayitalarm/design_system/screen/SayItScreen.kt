/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.design_system.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
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
import org.a_cyb.sayitalarm.design_system.atom.SpacerSmall
import org.a_cyb.sayitalarm.design_system.atom.SpacerXLarge
import org.a_cyb.sayitalarm.design_system.atom.SpacerXxxxLarge
import org.a_cyb.sayitalarm.design_system.atom.TextBodyStandardSmall
import org.a_cyb.sayitalarm.design_system.atom.TextBodySubtleMedium
import org.a_cyb.sayitalarm.design_system.atom.TextButtonCircleExit
import org.a_cyb.sayitalarm.design_system.atom.TextButtonCircleFinish
import org.a_cyb.sayitalarm.design_system.atom.TextButtonCircleStart
import org.a_cyb.sayitalarm.design_system.atom.TextButtonCircleTryAgain
import org.a_cyb.sayitalarm.design_system.atom.TextDisplayStandardSmall
import org.a_cyb.sayitalarm.design_system.atom.TextTitleSubtleLarge
import org.a_cyb.sayitalarm.design_system.atom.TextTitleWarningLarge
import org.a_cyb.sayitalarm.design_system.molecule.TextBoxSayItScript
import org.a_cyb.sayitalarm.design_system.molecule.TextBoxStatusHeaderError
import org.a_cyb.sayitalarm.design_system.molecule.TextBoxStatusHeaderFailed
import org.a_cyb.sayitalarm.design_system.molecule.TextBoxStatusHeaderListening
import org.a_cyb.sayitalarm.design_system.molecule.TextBoxStatusHeaderReady
import org.a_cyb.sayitalarm.design_system.molecule.TextBoxStatusHeaderSuccess
import org.a_cyb.sayitalarm.design_system.molecule.TextBoxSttResult
import org.a_cyb.sayitalarm.design_system.molecule.TextBoxWarningBody
import org.a_cyb.sayitalarm.design_system.molecule.animateSlowFlickering
import org.a_cyb.sayitalarm.design_system.token.Color
import org.a_cyb.sayitalarm.design_system.token.Spacing
import org.a_cyb.sayitalarm.presentation.SayItContract
import org.a_cyb.sayitalarm.presentation.SayItContract.Count
import org.a_cyb.sayitalarm.presentation.SayItContract.SayItInfo
import org.a_cyb.sayitalarm.presentation.SayItContract.SayItState.Error
import org.a_cyb.sayitalarm.presentation.SayItContract.SayItState.Finished
import org.a_cyb.sayitalarm.presentation.SayItContract.SayItState.Initial
import org.a_cyb.sayitalarm.presentation.SayItContract.SayItState.Processing
import org.a_cyb.sayitalarm.presentation.SayItContract.SayItViewModel
import org.a_cyb.sayitalarm.presentation.SayItContract.SttStatus
import org.a_cyb.sayitalarm.presentation.command.CommandContract
import org.a_cyb.sayitalarm.presentation.command.CommandContract.CommandReceiver
import org.a_cyb.sayitalarm.presentation.command.FinishCommand
import org.a_cyb.sayitalarm.presentation.command.ProcessScriptCommand

@Composable
fun SayItScreen(
    viewModel: SayItViewModel,
) {
    val state = viewModel.state.collectAsState()
    val isOffline = viewModel.isOffline.collectAsState()

    ColumnScreenStandardScrollableNoFooter {
        SpacerXxxxLarge()
        when (state.value) {
            is Initial -> InitialScreen()
            is Processing -> {
                ProcessingScreen(
                    info = (state.value as Processing).info,
                    runCommand = { viewModel.runCommand(it) }
                )
            }

            is Finished -> FinishScreen { viewModel.runCommand(it) }
            is Error -> ErrorScreen { viewModel.runCommand(it) }
        }
        Footer(isOffline.value)
    }
}

@Composable
private fun ColumnScope.InitialScreen() {
    Spacer(Modifier.weight(1f))
    TextTitleSubtleLarge(text = stringResource(id = R.string.loading))
}

@Composable
private fun ColumnScope.FinishScreen(
    runCommand: (CommandContract.Command<out CommandReceiver>) -> Unit,
) {
    TextDisplayStandardSmall(text = stringResource(id = R.string.say_it))
    SpacerMedium()
    DividerMedium()
    Spacer(Modifier.weight(1f))
    TextButtonCircleFinish { runCommand(FinishCommand) }
}

@Composable
private fun ColumnScope.ErrorScreen(
    runCommand: (CommandContract.Command<out CommandReceiver>) -> Unit,
) {
    TextBoxStatusHeaderError()
    SpacerMedium()
    DividerMedium()
    SpacerXLarge()
    TextBoxWarningBody(text = stringResource(id = R.string.info_say_it_error))
    Spacer(Modifier.weight(1f))
    TextButtonCircleExit { runCommand(FinishCommand) }
}

@Composable
private fun ColumnScope.ProcessingScreen(
    info: SayItInfo,
    runCommand: (CommandContract.Command<out CommandReceiver>) -> Unit,
) {
    ProcessingScreenHeader(status = info.status, count = info.count)
    TextBoxSayItScript(text = info.script)
    TextBoxSttResult(text = info.sttResult)
    Spacer(modifier = Modifier.weight(1f))
    ProcessingActionAndAnimation(
        status = info.status,
        runCommand = { runCommand(it) }
    )
    SpacerXLarge()
}

@Composable
private fun ProcessingScreenHeader(status: SttStatus, count: Count) {
    val counter by animateFloatAsState(
        targetValue = count.current / count.total.toFloat(),
        label = "SayItScriptCounter"
    )

    StatusHeader(
        status = status,
        countAsText = "${count.current}/${count.total}"
    )
    SpacerMedium()
    LinearProgressIndicator(
        progress = { counter },
        color = Color.surface.attention,
    )
}

@Composable
private fun StatusHeader(status: SttStatus, countAsText: String) {
    when (status) {
        SttStatus.READY -> TextBoxStatusHeaderReady(count = countAsText)
        SttStatus.LISTENING -> TextBoxStatusHeaderListening(count = countAsText)
        SttStatus.SUCCESS -> TextBoxStatusHeaderSuccess(count = countAsText)
        SttStatus.FAILED -> TextBoxStatusHeaderFailed(count = countAsText)
    }
}

@Composable
private fun ProcessingActionAndAnimation(
    status: SttStatus,
    runCommand: (CommandContract.Command<out CommandReceiver>) -> Unit,
) {
    AnimateScaleInAndOut(status == SttStatus.READY) {
        StartButton { runCommand(it) }
    }
    AnimateScaleInAndOut(visible = status == SttStatus.LISTENING) {
        Box(modifier = Modifier.animateSlowFlickering()) {
            TextTitleWarningLarge(text = stringResource(id = R.string.listening))
        }
    }
    AnimateScaleInAndOut(status == SttStatus.SUCCESS) {
        StartButton { runCommand(it) }
    }
    AnimateScaleInAndOut(status == SttStatus.FAILED) {
        TextButtonCircleTryAgain { runCommand(ProcessScriptCommand) }
    }
}

@Composable
private fun AnimateScaleInAndOut(visible: Boolean, content: @Composable () -> Unit) {
    AnimatedVisibility(
        visible = visible,
        enter = scaleIn(),
        exit = scaleOut(),
    ) {
        content()
    }
}

@Composable
private fun StartButton(runCommand: (CommandContract.Command<out CommandReceiver>) -> Unit) {
    SpacerMedium()
    TextButtonCircleStart { runCommand(ProcessScriptCommand) }
    SpacerMedium()
}

@Composable
private fun ColumnScope.Footer(isOffline: SayItContract.IsOffline) {
    Spacer(modifier = Modifier.weight(1f))
    // After implementing support for the only offline STT model, remove this.
    if (isOffline == SayItContract.IsOffline.False) {
        Row(Modifier.padding(horizontal = Spacing.m)) {
            TextBodySubtleMedium(text = stringResource(R.string.info_stt_recognizer_online))
        }
    }
    SpacerSmall()
    TextBodyStandardSmall(text = stringResource(R.string.say_it))
    SpacerMedium()
}
