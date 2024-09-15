/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.design_system.screen

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import org.a_cyb.sayitalarm.design_system.R
import org.a_cyb.sayitalarm.design_system.atom.BoxAnimatedCircleBorder
import org.a_cyb.sayitalarm.design_system.atom.ColumnScreenVerticalCenter
import org.a_cyb.sayitalarm.design_system.atom.SpacerSmall
import org.a_cyb.sayitalarm.design_system.atom.SpacerXLarge
import org.a_cyb.sayitalarm.design_system.atom.TextButtonCircleSayIt
import org.a_cyb.sayitalarm.design_system.atom.TextButtonSnooze
import org.a_cyb.sayitalarm.design_system.atom.TextDisplayStandardLarge
import org.a_cyb.sayitalarm.design_system.atom.TextDisplayStandardSmall
import org.a_cyb.sayitalarm.design_system.atom.TextTitleStandardLarge
import org.a_cyb.sayitalarm.design_system.token.Brush
import org.a_cyb.sayitalarm.presentation.contracts.AlarmContract.AlarmUiState.Error
import org.a_cyb.sayitalarm.presentation.contracts.AlarmContract.AlarmUiState.Initial
import org.a_cyb.sayitalarm.presentation.contracts.AlarmContract.AlarmUiState.Ringing
import org.a_cyb.sayitalarm.presentation.contracts.AlarmContract.AlarmUiState.Stopped
import org.a_cyb.sayitalarm.presentation.contracts.AlarmContract.AlarmViewModel
import org.a_cyb.sayitalarm.presentation.contracts.command.SnoozeCommand
import org.a_cyb.sayitalarm.presentation.contracts.command.StartSayItCommand

@Composable
fun AlarmScreen(
    viewModel: AlarmViewModel,
    navigateToSayIt: () -> Unit,
) {
    val state = viewModel.state.collectAsState()
    val time = viewModel.currentTime.collectAsState()

    ColumnScreenVerticalCenter {
        when (state.value) {
            is Initial -> LoadingScreen()
            is Ringing ->
                RingingScreen(
                    time = time.value,
                    label = (state.value as Ringing).label,
                    onSayItButtonClick = {
                        viewModel.runCommand(StartSayItCommand)
                        navigateToSayIt()
                    },
                    onSnoozeButtonClick = {
                        viewModel.runCommand(SnoozeCommand)
                    },
                )

            is Error -> TextDisplayStandardLarge(text = "Error🌛")
            is Stopped -> {}
        }
    }
}

@Composable
fun ColumnScope.LoadingScreen() {
    TextDisplayStandardSmall(text = stringResource(id = R.string.say_it))
    Spacer(Modifier.weight(1f))
    LoadingCircleBox()
    Spacer(Modifier.weight(1f))
}

@Composable
private fun LoadingCircleBox() {
    BoxAnimatedCircleBorder(brush = Brush.sweepGradientGray) {
        TextTitleStandardLarge(
            text = stringResource(id = R.string.loading),
        )
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

    TextButtonCircleSayIt(onClick = onSayItButtonClick)
    Spacer(Modifier.weight(1f))

    TextButtonSnooze(onClick = onSnoozeButtonClick)
}
