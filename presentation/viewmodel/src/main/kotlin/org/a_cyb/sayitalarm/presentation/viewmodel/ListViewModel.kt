/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.presentation.viewmodel

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.a_cyb.sayitalarm.alarm_service.AlarmSchedulerContract
import org.a_cyb.sayitalarm.entity.Alarm
import org.a_cyb.sayitalarm.formatter.time.TimeFormatterContract
import org.a_cyb.sayitalarm.formatter.weekday.WeekdayFormatterContract
import org.a_cyb.sayitalarm.presentation.CommandContract
import org.a_cyb.sayitalarm.presentation.interactor.ListInteractorContract
import org.a_cyb.sayitalarm.presentation.list.ListContract
import org.a_cyb.sayitalarm.presentation.list.ListContract.AlarmInfo
import org.a_cyb.sayitalarm.presentation.list.ListContract.Error
import org.a_cyb.sayitalarm.presentation.list.ListContract.Initial
import org.a_cyb.sayitalarm.presentation.list.ListContract.InitialError
import org.a_cyb.sayitalarm.presentation.list.ListContract.ListState
import org.a_cyb.sayitalarm.presentation.list.ListContract.ListStateWithContent
import org.a_cyb.sayitalarm.presentation.list.ListContract.Success

internal class ListViewModel(
    private val interactor: ListInteractorContract,
    private val alarmScheduler: AlarmSchedulerContract,
    private val timeFormatter: TimeFormatterContract,
    private val weekdayFormatter: WeekdayFormatterContract,
) : ListContract.ListViewModel, ViewModel() {

    private val _state: MutableStateFlow<ListState> = MutableStateFlow(Initial)
    override val state: StateFlow<ListState> = _state

    init {
        interactor.alarms
            .onEach(::updateState)
            .launchIn(scope)
    }

    private fun updateState(result: Result<List<Alarm>>) {
        _state.update { result.toUiState() }
    }

    private fun Result<List<Alarm>>.toUiState(): ListState {
        return getOrNull()
            ?.let(::toSuccess) ?: toError()
    }

    private fun toSuccess(alarms: List<Alarm>): ListState {
        return Success(alarms.map { it.toAlarmInfo() })
    }

    private fun Alarm.toAlarmInfo(): AlarmInfo {
        return AlarmInfo(
            id = id,
            time = timeFormatter.format(hour, minute),
            labelAndWeeklyRepeat = "${label.label}, ${weekdayFormatter.formatAbbr(weeklyRepeat.weekdays)}",
            enabled = enabled
        )
    }

    private fun toError(): ListState {
        return if (_state.value is ListStateWithContent) {
            Error((_state.value as ListStateWithContent).alarmData)
        } else {
            InitialError
        }
    }

    override fun setEnabled(id: Long, enabled: Boolean) {
        scope.launch {
            interactor.setEnabled(id, enabled, this) // First alarm db updated and after that  scheduler should run.
            alarmScheduler.setAlarm(this)
        }
    }

    override fun deleteAlarm(id: Long) {
        scope.launch {
            interactor.deleteAlarm(id, this)
            alarmScheduler.cancelAlarm(id, this)
        }
    }

    override fun <T : CommandContract.CommandReceiver> runCommand(command: CommandContract.Command<T>) {
        @Suppress("UNCHECKED_CAST")
        command.execute(this as T)
    }
}
