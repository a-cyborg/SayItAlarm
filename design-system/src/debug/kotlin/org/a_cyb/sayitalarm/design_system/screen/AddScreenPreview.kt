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
import org.a_cyb.sayitalarm.design_system.organism.FakeAlarmUIData
import org.a_cyb.sayitalarm.entity.Hour
import org.a_cyb.sayitalarm.entity.Minute
import org.a_cyb.sayitalarm.entity.SayItScripts
import org.a_cyb.sayitalarm.presentation.contracts.AddContract
import org.a_cyb.sayitalarm.presentation.contracts.AddContract.AddState
import org.a_cyb.sayitalarm.presentation.contracts.AddContract.AddState.Success
import org.a_cyb.sayitalarm.presentation.contracts.AlarmPanelContract
import org.a_cyb.sayitalarm.presentation.contracts.command.CommandContract

@Preview
@Composable
fun AddScreenPreview() {
    AddScreen(
        viewModel = AddViewModelFake(),
        navigateToList = {},
    )
}

private class AddViewModelFake : AddContract.AddViewModel {
    override val state: StateFlow<AddState> = MutableStateFlow(Success(FakeAlarmUIData.defaultAlarmUI))

    override fun setTime(hour: Hour, minute: Minute) {}
    override fun setWeeklyRepeat(selectableRepeats: List<AlarmPanelContract.SelectableRepeat>) {}
    override fun setLabel(label: String) {}
    override fun setAlertType(alertTypeName: String) {}
    override fun setRingtone(ringtoneUI: AlarmPanelContract.RingtoneUI) {}
    override fun setScripts(scripts: SayItScripts) {}
    override fun save() {}

    override fun <T : CommandContract.CommandReceiver> runCommand(command: CommandContract.Command<T>) {}
}
