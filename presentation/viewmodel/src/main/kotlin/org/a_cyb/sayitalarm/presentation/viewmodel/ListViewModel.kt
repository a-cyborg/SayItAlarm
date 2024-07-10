/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.presentation.viewmodel

import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import org.a_cyb.sayitalarm.domain.interactor.InteractorContract
import org.a_cyb.sayitalarm.entity.Alarm
import org.a_cyb.sayitalarm.entity.Label
import org.a_cyb.sayitalarm.entity.WeeklyRepeat
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

class ListViewModel(
    private val interactor: InteractorContract.ListInteractor,
    private val timeFormatter: TimeFormatterContract,
    private val weekdayFormatter: WeekdayFormatterContract,
) : ListContract.ListViewModel, ViewModel() {

    override val state: StateFlow<ListState> = interactor.alarms
        .map(::toState)
        .stateIn(
            scope = scope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = Initial
        )

    private fun toState(result: Result<List<Alarm>>): ListState =
        result.getOrNull()
            ?.let(::toSuccess) ?: toError()

    private fun toError(): ListState =
        when (state.value is ListStateWithContent) {
            true -> Error((state.value as ListStateWithContent).alarmData)
            else -> InitialError
        }

    private fun toSuccess(alarms: List<Alarm>): ListState =
        Success(
            alarms.map(::toAlarmInfo)
        )

    private fun toAlarmInfo(alarm: Alarm): AlarmInfo =
        AlarmInfo(
            id = alarm.id,
            time = timeFormatter.format(alarm.hour, alarm.minute),
            labelAndWeeklyRepeat = formatLabelAndWeeklyRepeat(alarm.label, alarm.weeklyRepeat),
            enabled = alarm.enabled
        )

    private fun formatLabelAndWeeklyRepeat(label: Label, weeklyRepeat: WeeklyRepeat): String =
        when {
            label.label.isEmpty() && weeklyRepeat.weekdays.isEmpty() -> ""
            label.label.isEmpty() -> weekdayFormatter.formatAbbr(weeklyRepeat.weekdays)
            weeklyRepeat.weekdays.isEmpty() -> label.label
            else -> "${label.label}, ${weekdayFormatter.formatAbbr(weeklyRepeat.weekdays)}"
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
