/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.design_system.screen

import androidx.compose.foundation.layout.Spacer
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import org.a_cyb.sayitalarm.design_system.atom.ColumnScreenVerticalCenter
import org.a_cyb.sayitalarm.design_system.atom.IconButtonSayIt
import org.a_cyb.sayitalarm.design_system.atom.IconButtonSnoozeText
import org.a_cyb.sayitalarm.design_system.atom.SpacerSmall
import org.a_cyb.sayitalarm.design_system.atom.SpacerXLarge
import org.a_cyb.sayitalarm.design_system.atom.SpacerXxxxLarge
import org.a_cyb.sayitalarm.design_system.atom.TextDisplayStandardLarge
import org.a_cyb.sayitalarm.design_system.atom.TextTitleStandardLarge
import org.a_cyb.sayitalarm.presentation.AlarmContract
import org.a_cyb.sayitalarm.presentation.command.StartSayItCommand

@Composable
fun AlarmScreen(
    viewModel: AlarmContract.AlarmViewModel
) {
    val state = viewModel.state.collectAsState()
    val time = viewModel.currentTime.collectAsState()

    when (state.value) {
        is AlarmContract.AlarmUiState.Initial -> RingingScreen(
            time = time.value,
            label = "" /*TODO*/,
            onSayItButtonClick = { viewModel.runCommand(StartSayItCommand) },
            onSnoozeButtonClick = {},
        )

        else -> {}
    }
}

@Composable
fun RingingScreen(
    time: String,
    label: String,
    onSayItButtonClick: () -> Unit,
    onSnoozeButtonClick: () -> Unit,
) {
    ColumnScreenVerticalCenter {
        SpacerXxxxLarge()

        TextDisplayStandardLarge(
            text = time
        )
        SpacerSmall()

        TextTitleStandardLarge(
            text = label
        )
        Spacer(Modifier.weight(1f))
        SpacerXLarge()

        IconButtonSayIt(onClick = onSayItButtonClick)
        Spacer(Modifier.weight(1f))

        IconButtonSnoozeText(onClick = onSnoozeButtonClick)
        SpacerXxxxLarge()
    }
}
