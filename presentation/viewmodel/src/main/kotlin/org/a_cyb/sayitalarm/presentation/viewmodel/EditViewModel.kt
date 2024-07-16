/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.presentation.viewmodel

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.takeWhile
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.a_cyb.sayitalarm.domain.interactor.InteractorContract
import org.a_cyb.sayitalarm.entity.Alarm
import org.a_cyb.sayitalarm.entity.Hour
import org.a_cyb.sayitalarm.entity.Minute
import org.a_cyb.sayitalarm.entity.SayItScripts
import org.a_cyb.sayitalarm.presentation.AlarmPanelContract.AlarmUI
import org.a_cyb.sayitalarm.presentation.AlarmPanelContract.RingtoneUI
import org.a_cyb.sayitalarm.presentation.AlarmPanelContract.SelectableRepeat
import org.a_cyb.sayitalarm.presentation.EditContract
import org.a_cyb.sayitalarm.presentation.EditContract.EditViewModel.EditState
import org.a_cyb.sayitalarm.presentation.EditContract.EditViewModel.EditState.Error
import org.a_cyb.sayitalarm.presentation.EditContract.EditViewModel.EditState.Initial
import org.a_cyb.sayitalarm.presentation.EditContract.EditViewModel.EditState.Success
import org.a_cyb.sayitalarm.presentation.command.CommandContract
import org.a_cyb.sayitalarm.presentation.viewmodel.converter.AlarmUIConverterContract
import org.a_cyb.sayitalarm.presentation.viewmodel.mapper.AlarmMapperContract

class EditViewModel(
    private val alarmId: Long,
    private val interactor: InteractorContract.EditInteractor,
    private val mapper: AlarmMapperContract,
    private val converter: AlarmUIConverterContract,
) : EditContract.EditViewModel, ViewModel() {

    private val _state: MutableStateFlow<EditState> = MutableStateFlow(Initial)
    override val state: StateFlow<EditState> = _state.asStateFlow()

    init {
        scope.launch {
            interactor.alarm
                .takeWhile { _state.value == Initial }
                .map(::toState)
                .collect()
        }

        interactor.getAlarm(alarmId, scope)
    }

    private fun toState(result: Result<Alarm>) {
        result
            .onSuccess { Success(it.toAlarmUI()).updateState() }
            .onFailure { Error.updateState() }
    }

    private fun Alarm.toAlarmUI(): AlarmUI {
        return mapper.mapToAlarmUI(this)
    }

    private fun EditState.updateState() {
        _state.update { this }
    }

    override fun setTime(hour: Hour, minute: Minute) {
        updateSuccessOrError {
            copy(
                timeUI = converter.convertAsTimeUi(hour, minute)
            )
        }
    }

    private fun updateSuccessOrError(change: AlarmUI.() -> AlarmUI) {
        when (_state.value) {
            is Success -> Success((_state.value as Success).alarmUI.change())
            else -> Error
        }.updateState()
    }

    override fun setWeeklyRepeat(selectableRepeats: List<SelectableRepeat>) {
        updateSuccessOrError {
            copy(
                weeklyRepeatUI = converter.convertAsWeeklyRepeatUi(selectableRepeats)
            )
        }
    }

    override fun setLabel(label: String) {
        updateSuccessOrError {
            copy(label = label)
        }
    }

    override fun setAlertType(alertTypeName: String) {
        updateSuccessOrError {
            copy(
                alertTypeUI = converter.convertToAlertTypeUi(
                    (_state.value as Success).alarmUI.alertTypeUI.selectableAlertType,
                    alertTypeName
                )
            )
        }
    }

    override fun setRingtone(ringtoneUI: RingtoneUI) {
        updateSuccessOrError {
            copy(ringtoneUI = ringtoneUI)
        }
    }

    override fun setScripts(scripts: SayItScripts) {
        updateSuccessOrError {
            copy(sayItScripts = scripts.scripts)
        }
    }

    override fun save() {
        if (_state.value is Success) {
            val alarm = (_state.value as Success).alarmUI.toAlarm()

            interactor.update(alarm, scope)
        } else {
            Error.updateState()
        }
    }

    private fun AlarmUI.toAlarm(): Alarm {
        return mapper
            .mapToAlarm(this)
            .copy(id = alarmId)
    }

    override fun <T : CommandContract.CommandReceiver> runCommand(command: CommandContract.Command<T>) {
        @Suppress("UNCHECKED_CAST")
        command.execute(this as T)
    }
}
