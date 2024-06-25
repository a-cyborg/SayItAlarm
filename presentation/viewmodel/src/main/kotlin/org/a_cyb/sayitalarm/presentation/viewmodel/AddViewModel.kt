/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.presentation.viewmodel

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import org.a_cyb.sayitalarm.entity.Alarm
import org.a_cyb.sayitalarm.entity.AlarmType
import org.a_cyb.sayitalarm.entity.AlertType
import org.a_cyb.sayitalarm.entity.Hour
import org.a_cyb.sayitalarm.entity.Label
import org.a_cyb.sayitalarm.entity.Minute
import org.a_cyb.sayitalarm.entity.Ringtone
import org.a_cyb.sayitalarm.entity.SayItScripts
import org.a_cyb.sayitalarm.entity.WeeklyRepeat
import org.a_cyb.sayitalarm.formatter.time.TimeFormatterContract
import org.a_cyb.sayitalarm.formatter.weekday.WeekdayFormatterContract
import org.a_cyb.sayitalarm.presentation.CommandContract.Command
import org.a_cyb.sayitalarm.presentation.CommandContract.CommandReceiver
import org.a_cyb.sayitalarm.presentation.add.AddContract
import org.a_cyb.sayitalarm.presentation.add.AddContract.AddState
import org.a_cyb.sayitalarm.presentation.add.AddContract.AddState.Error
import org.a_cyb.sayitalarm.presentation.add.AddContract.AddState.Initial
import org.a_cyb.sayitalarm.presentation.add.AddContract.AddState.Success
import org.a_cyb.sayitalarm.presentation.alarm_panel.AlarmPanelContract.*
import org.a_cyb.sayitalarm.presentation.interactor.InteractorContract
import org.a_cyb.sayitalarm.presentation.viewmodel.mapper.AlarmMapperContract

internal class AddViewModel(
    private val interactor: InteractorContract.AddInteractor,
    private val timeFormatter: TimeFormatterContract,
    private val weeklyRepeatFormatter: WeekdayFormatterContract,
    private val mapper: AlarmMapperContract,
) : AddContract.AddViewModel, ViewModel() {

    private val _state: MutableStateFlow<AddState> = MutableStateFlow(Initial(getDefaultAlarmUI()))
    override val state: StateFlow<AddState> = _state.stateIn(
        scope = scope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = Initial(getDefaultAlarmUI())
    )

    private fun getDefaultAlarmUI(): AlarmUI {
        return mapper.mapToAlarmUI(defaultAlarm)
    }

    private fun updateSuccessOrError(update: AlarmUI.() -> AlarmUI) {
        when (_state.value) {
            is Success, is Initial -> Success(_state.value.alarmUI.update())
            is Error -> Error(_state.value.alarmUI.update())
        }.updateState()
    }

    private fun AddState.updateState() {
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
        val codes = filter { it.selected }
            .map { it.code }

        return weeklyRepeatFormatter.formatAbbr(codes.toSet())
    }

    override fun setLabel(label: String) {
        updateSuccessOrError {
            copy(label = label)
        }
    }

    override fun setAlertType(alertTypeUI: AlertTypeUI) {
        updateSuccessOrError {
            copy(alertTypeUI = alertTypeUI)
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
            interactor.save(
                mapper.mapToAlarm((_state.value as Success).alarmUI),
                scope
            )
        }
    }

    override fun <T : CommandReceiver> runCommand(command: Command<T>) {
        @Suppress("UNCHECKED_CAST")
        command.execute(this as T)
    }

    companion object Default {
        private val HOUR = Hour(8)
        private val MINUTE = Minute(0)
        private val WEEKLY_REPEAT = WeeklyRepeat()
        private val LABEL = Label("")
        private const val ENABLED = true
        private val ALERT_TYPE = AlertType.SOUND_AND_VIBRATE
        private val RINGTONE = Ringtone("")
        private val ALARM_TYPE = AlarmType.SAY_IT
        private val SAY_IT_SCRIPTS = SayItScripts()

        private val defaultAlarm: Alarm
            get() = Alarm(
                hour = HOUR,
                minute = MINUTE,
                weeklyRepeat = WEEKLY_REPEAT,
                label = LABEL,
                enabled = ENABLED,
                alertType = ALERT_TYPE,
                ringtone = RINGTONE,
                alarmType = ALARM_TYPE,
                sayItScripts = SAY_IT_SCRIPTS
            )
    }
}
