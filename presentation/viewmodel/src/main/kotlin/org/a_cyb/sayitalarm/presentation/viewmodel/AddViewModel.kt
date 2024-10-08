/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.presentation.viewmodel

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import org.a_cyb.sayitalarm.domain.interactor.InteractorContract
import org.a_cyb.sayitalarm.entity.Alarm
import org.a_cyb.sayitalarm.entity.AlarmType
import org.a_cyb.sayitalarm.entity.AlertType
import org.a_cyb.sayitalarm.entity.Hour
import org.a_cyb.sayitalarm.entity.Label
import org.a_cyb.sayitalarm.entity.Minute
import org.a_cyb.sayitalarm.entity.Ringtone
import org.a_cyb.sayitalarm.entity.SayItScripts
import org.a_cyb.sayitalarm.entity.WeeklyRepeat
import org.a_cyb.sayitalarm.presentation.contracts.AddContract
import org.a_cyb.sayitalarm.presentation.contracts.AddContract.AddState
import org.a_cyb.sayitalarm.presentation.contracts.AddContract.AddState.Initial
import org.a_cyb.sayitalarm.presentation.contracts.AddContract.AddState.Success
import org.a_cyb.sayitalarm.presentation.contracts.AlarmPanelContract.AlarmUI
import org.a_cyb.sayitalarm.presentation.contracts.AlarmPanelContract.RingtoneUI
import org.a_cyb.sayitalarm.presentation.contracts.AlarmPanelContract.SelectableRepeat
import org.a_cyb.sayitalarm.presentation.contracts.command.CommandContract.Command
import org.a_cyb.sayitalarm.presentation.contracts.command.CommandContract.CommandReceiver
import org.a_cyb.sayitalarm.presentation.viewmodel.converter.AlarmUiConverterContract
import org.a_cyb.sayitalarm.presentation.viewmodel.mapper.AlarmMapperContract

class AddViewModel(
    private val interactor: InteractorContract.AddInteractor,
    private val mapper: AlarmMapperContract,
    private val converter: AlarmUiConverterContract,
) : AddContract.AddViewModel, ViewModel() {

    private val _state: MutableStateFlow<AddState> = MutableStateFlow(Initial(getDefaultAlarmUI()))
    override val state: StateFlow<AddState> = _state.asStateFlow()

    private fun getDefaultAlarmUI(): AlarmUI = mapper.mapToAlarmUI(defaultAlarm)

    override fun setTime(hour: Hour, minute: Minute) {
        updateStateWithCopy {
            copy(
                timeUI = converter.convertAsTimeUi(hour, minute),
            )
        }
    }

    private fun updateStateWithCopy(valueCopy: AlarmUI.() -> AlarmUI) {
        _state.update {
            Success(_state.value.alarmUI.valueCopy())
        }
    }

    override fun setWeeklyRepeat(selectableRepeats: List<SelectableRepeat>) {
        updateStateWithCopy {
            copy(
                weeklyRepeatUI = converter.convertAsWeeklyRepeatUi(selectableRepeats),
            )
        }
    }

    override fun setLabel(label: String) {
        updateStateWithCopy {
            copy(label = label)
        }
    }

    override fun setAlertType(alertTypeName: String) {
        updateStateWithCopy {
            copy(
                alertTypeUI = converter.convertToAlertTypeUi(
                    _state.value.alarmUI.alertTypeUI.selectableAlertType,
                    alertTypeName,
                ),
            )
        }
    }

    override fun setRingtone(ringtoneUI: RingtoneUI) {
        updateStateWithCopy {
            copy(ringtoneUI = ringtoneUI)
        }
    }

    override fun setScripts(scripts: SayItScripts) {
        updateStateWithCopy {
            copy(sayItScripts = scripts.scripts)
        }
    }

    override fun save() {
        val alarmUi: AlarmUI? = when (_state.value) {
            is Success -> (_state.value as Success).alarmUI
            is Initial -> (_state.value as Initial).alarmUI
            else -> null
        }

        alarmUi?.let { interactor.save(mapper.mapToAlarm(it), scope) }
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
                sayItScripts = SAY_IT_SCRIPTS,
            )
    }
}
