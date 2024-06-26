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
import org.a_cyb.sayitalarm.entity.Alarm
import org.a_cyb.sayitalarm.formatter.time.TimeFormatterContract
import org.a_cyb.sayitalarm.formatter.weekday.WeekdayFormatterContract
import org.a_cyb.sayitalarm.presentation.ListContract
import org.a_cyb.sayitalarm.presentation.ListContract.AlarmInfo
import org.a_cyb.sayitalarm.presentation.ListContract.ListState
import org.a_cyb.sayitalarm.presentation.ListContract.ListState.Error
import org.a_cyb.sayitalarm.presentation.ListContract.ListState.Initial
import org.a_cyb.sayitalarm.presentation.ListContract.ListState.InitialError
import org.a_cyb.sayitalarm.presentation.ListContract.ListState.Success
import org.a_cyb.sayitalarm.presentation.ListContract.ListStateWithContent
import org.a_cyb.sayitalarm.presentation.command.CommandContract
import org.a_cyb.sayitalarm.presentation.interactor.InteractorContract

internal class ListViewModel(
    private val interactor: InteractorContract.ListInteractor,
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
        interactor.setEnabled(id, enabled, scope)
    }

    override fun deleteAlarm(id: Long) {
        interactor.deleteAlarm(id, scope)
    }

    override fun <T : CommandContract.CommandReceiver> runCommand(command: CommandContract.Command<T>) {
        @Suppress("UNCHECKED_CAST")
        command.execute(this as T)
    }
}
