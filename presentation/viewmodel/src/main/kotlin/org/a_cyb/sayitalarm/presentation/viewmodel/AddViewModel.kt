/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.presentation.viewmodel

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.a_cyb.sayitalarm.entity.Alarm
import org.a_cyb.sayitalarm.entity.AlarmType
import org.a_cyb.sayitalarm.entity.AlertType
import org.a_cyb.sayitalarm.entity.Hour
import org.a_cyb.sayitalarm.entity.Label
import org.a_cyb.sayitalarm.entity.Minute
import org.a_cyb.sayitalarm.entity.Ringtone
import org.a_cyb.sayitalarm.entity.SayItScripts
import org.a_cyb.sayitalarm.entity.WeeklyRepeat
import org.a_cyb.sayitalarm.formatter.enum.EnumFormatterContract
import org.a_cyb.sayitalarm.formatter.weekday.WeekdayFormatterContract
import org.a_cyb.sayitalarm.presentation.CommandContract
import org.a_cyb.sayitalarm.presentation.add.AddContract
import org.a_cyb.sayitalarm.presentation.interactor.AddInteractorContract

internal class AddViewModel(
    private val interactor: AddInteractorContract,
    private val weekdayFormatter: WeekdayFormatterContract,
    private val enumFormatter: EnumFormatterContract,
) : AddContract.AddViewModel, ViewModel() {

    private val _state: MutableStateFlow<AddContract.AddState> = MutableStateFlow(AddContract.Initial)
    override val state: StateFlow<AddContract.AddState> = _state

    init {
        updateState(AddContract.AddStateWithContent(getDefaultAlarmUi()))
    }

    private fun updateState(state: AddContract.AddState) {
        scope.launch {
            _state.update {
                state
            }
        }
    }

    private fun getDefaultAlarmUi() =
        AddContract.AlarmUi(
            hour = 8,
            minute = 0,
            weeklyRepeat = weekdayFormatter.formatAbbr(emptySet()),
            label = "",
            alertType = enumFormatter.formatAlertType(AlertType.SOUND_AND_VIBRATE),
            ringtone = getDefaultRingtoneName(),
            sayItScripts = emptyList()
        )

    private fun getDefaultRingtoneName(): String {
        return ""
    }

    override fun save(alarm: Alarm) {
        scope.launch {
            interactor.save(alarm)
        }
    }

    override fun setTime(hour: Hour, minute: Minute) {
    }

    override fun setWeeklyRepeat(weeklyRepeat: WeeklyRepeat) {
        TODO("Not yet implemented")
    }

    override fun setLabel(label: Label) {
        TODO("Not yet implemented")
    }

    override fun setAlertType(alertType: AlertType) {
        TODO("Not yet implemented")
    }

    override fun setRingtone(ringtone: Ringtone) {
        TODO("Not yet implemented")
    }

    override fun setAlarmType(alarmType: AlarmType) {
        TODO("Not yet implemented")
    }

    override fun setScripts(scripts: SayItScripts) {
        TODO("Not yet implemented")
    }

    override fun <T : CommandContract.CommandReceiver> runCommand(command: CommandContract.Command<T>) {
        @Suppress("UNCHECKED_CAST")
        command.execute(this as T)
    }
}