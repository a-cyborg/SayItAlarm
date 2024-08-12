/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.design_system.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import org.a_cyb.sayitalarm.design_system.R
import org.a_cyb.sayitalarm.design_system.atom.ColumnScreenVerticalCenter
import org.a_cyb.sayitalarm.design_system.atom.IconButtonSayIt
import org.a_cyb.sayitalarm.design_system.atom.IconButtonSnoozeText
import org.a_cyb.sayitalarm.design_system.atom.SpacerSmall
import org.a_cyb.sayitalarm.design_system.atom.SpacerXLarge
import org.a_cyb.sayitalarm.design_system.atom.TextDisplayStandardLarge
import org.a_cyb.sayitalarm.design_system.atom.TextTitleStandardLarge
import org.a_cyb.sayitalarm.design_system.molecule.animateCircleBorder
import org.a_cyb.sayitalarm.design_system.token.Brush
import org.a_cyb.sayitalarm.design_system.token.Sizing
import org.a_cyb.sayitalarm.presentation.AlarmContract.AlarmUiState.Completed
import org.a_cyb.sayitalarm.presentation.AlarmContract.AlarmUiState.Error
import org.a_cyb.sayitalarm.presentation.AlarmContract.AlarmUiState.Initial
import org.a_cyb.sayitalarm.presentation.AlarmContract.AlarmUiState.Ringing
import org.a_cyb.sayitalarm.presentation.AlarmContract.AlarmUiState.VoiceInputProcessing
import org.a_cyb.sayitalarm.presentation.AlarmContract.AlarmViewModel
import org.a_cyb.sayitalarm.presentation.command.StartSayItCommand

@Composable
fun AlarmScreen(
    viewModel: AlarmViewModel
) {
    val state = viewModel.state.collectAsState()
    val time = viewModel.currentTime.collectAsState()

    ColumnScreenVerticalCenter {
        when (state.value) {
            is Initial ->
                LoadingScreen()

            is Ringing ->
                RingingScreen(
                    time = time.value,
                    label = (state.value as Ringing).label,
                    onSayItButtonClick = { viewModel.runCommand(StartSayItCommand) },
                    onSnoozeButtonClick = {},
                )

            is VoiceInputProcessing -> {}
            is Completed -> {}
            is Error -> {}
        }
    }
}

@Composable
fun ColumnScope.LoadingScreen() {
    TextDisplayStandardLarge(text = stringResource(id = R.string.say_it))
    Spacer(Modifier.weight(1f))

    LoadingCircleBox()
    Spacer(Modifier.weight(1f))
}

@Composable
private fun LoadingCircleBox() {
    Box(
        modifier = Modifier
            .size(Sizing.CircleButton.Large)
            .animateCircleBorder(Brush.sweepGradientGray),
        contentAlignment = Alignment.Center
    ) {
        TextTitleStandardLarge(text = stringResource(id = R.string.loading))
    }
}

@Composable
fun ColumnScope.RingingScreen(
    time: String,
    label: String,
    onSayItButtonClick: () -> Unit,
    onSnoozeButtonClick: () -> Unit,
) {
    TextDisplayStandardLarge(text = time)
    SpacerSmall()

    TextTitleStandardLarge(text = label)
    Spacer(Modifier.weight(1f))
    SpacerXLarge()

    IconButtonSayIt(onClick = onSayItButtonClick)
    Spacer(Modifier.weight(1f))

    IconButtonSnoozeText(onClick = onSnoozeButtonClick)
}
