/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.presentation.viewmodel

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.takeWhile
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.a_cyb.sayitalarm.domain.interactor.InteractorContract
import org.a_cyb.sayitalarm.entity.Alarm
import org.a_cyb.sayitalarm.entity.Hour
import org.a_cyb.sayitalarm.entity.Minute
import org.a_cyb.sayitalarm.entity.SayItScripts
import org.a_cyb.sayitalarm.presentation.formatter.time.TimeFormatterContract
import org.a_cyb.sayitalarm.presentation.formatter.weekday.WeekdayFormatterContract
import org.a_cyb.sayitalarm.presentation.AlarmPanelContract.AlarmUI
import org.a_cyb.sayitalarm.presentation.AlarmPanelContract.AlertTypeUI
import org.a_cyb.sayitalarm.presentation.AlarmPanelContract.RingtoneUI
import org.a_cyb.sayitalarm.presentation.AlarmPanelContract.SelectableAlertType
import org.a_cyb.sayitalarm.presentation.AlarmPanelContract.SelectableRepeat
import org.a_cyb.sayitalarm.presentation.AlarmPanelContract.TimeUI
import org.a_cyb.sayitalarm.presentation.AlarmPanelContract.WeeklyRepeatUI
import org.a_cyb.sayitalarm.presentation.EditContract
import org.a_cyb.sayitalarm.presentation.EditContract.EditViewModel.EditState
import org.a_cyb.sayitalarm.presentation.EditContract.EditViewModel.EditState.Error
import org.a_cyb.sayitalarm.presentation.EditContract.EditViewModel.EditState.Initial
import org.a_cyb.sayitalarm.presentation.EditContract.EditViewModel.EditState.Success
import org.a_cyb.sayitalarm.presentation.command.CommandContract
import org.a_cyb.sayitalarm.presentation.viewmodel.mapper.AlarmMapperContract

class EditViewModel(
    private val alarmId: Long,
    private val interactor: InteractorContract.EditInteractor,
    private val timeFormatter: TimeFormatterContract,
    private val weeklyRepeatFormatter: WeekdayFormatterContract,
    private val mapper: AlarmMapperContract,
) : EditContract.EditViewModel, ViewModel() {

    private val _state: MutableStateFlow<EditState> = MutableStateFlow(Initial)
    override val state: StateFlow<EditState> = _state.stateIn(
        scope = scope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = Initial
    )

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
                timeUI = TimeUI(
                    hour.hour,
                    minute.minute,
                    timeFormatter.format(hour, minute)
                )
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
                weeklyRepeatUI = WeeklyRepeatUI(
                    selectableRepeats.format(),
                    selectableRepeats
                )
            )
        }
    }

    private fun List<SelectableRepeat>.format(): String {
        val codes = filter { it.selected }.map { it.code }

        return weeklyRepeatFormatter.formatAbbr(codes.toSet())
    }

    override fun setLabel(label: String) {
        updateSuccessOrError {
            copy(label = label)
        }
    }

    override fun setAlertType(alertTypeName: String) {
        updateSuccessOrError {
            copy(alertTypeUI = getUpdatedAlertTypeUI(alertTypeName))
        }
    }

    private fun getUpdatedAlertTypeUI(chosenName: String): AlertTypeUI {
        val selectableAlertTypes = (_state.value as Success).alarmUI
            .alertTypeUI.selectableAlertType
            .map {
                SelectableAlertType(it.name, it.name == chosenName)
            }

        return AlertTypeUI(selectableAlertTypes)
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
