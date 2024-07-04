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
import org.a_cyb.sayitalarm.entity.Hour
import org.a_cyb.sayitalarm.entity.Minute
import org.a_cyb.sayitalarm.entity.SayItScripts
import org.a_cyb.sayitalarm.organism.FakeAlarmUIData
import org.a_cyb.sayitalarm.presentation.AddContract
import org.a_cyb.sayitalarm.presentation.AddContract.AddState
import org.a_cyb.sayitalarm.presentation.AddContract.AddState.*
import org.a_cyb.sayitalarm.presentation.AlarmPanelContract
import org.a_cyb.sayitalarm.presentation.command.CommandContract

@Preview
@Composable
fun AddScreenPreview() {
    AddScreen(viewModel = AddViewModelFake())
}

private class AddViewModelFake : AddContract.AddViewModel {

    override val state: StateFlow<AddState> =
        MutableStateFlow(Success(FakeAlarmUIData.defaultAlarmUI))

    override fun setTime(hour: Hour, minute: Minute) {}
    override fun setWeeklyRepeat(selectableRepeats: List<AlarmPanelContract.SelectableRepeat>) {}
    override fun setLabel(label: String) {}
    override fun setAlertType(alertTypeName: String) {}
    override fun setRingtone(ringtoneUI: AlarmPanelContract.RingtoneUI) {}
    override fun setScripts(scripts: SayItScripts) {}
    override fun save() {}

    override fun <T : CommandContract.CommandReceiver> runCommand(command: CommandContract.Command<T>) {}
}