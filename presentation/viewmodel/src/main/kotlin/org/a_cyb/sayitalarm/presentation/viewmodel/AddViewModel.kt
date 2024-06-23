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
import org.a_cyb.sayitalarm.presentation.CommandContract.Command
import org.a_cyb.sayitalarm.presentation.CommandContract.CommandReceiver
import org.a_cyb.sayitalarm.presentation.add.AddContract
import org.a_cyb.sayitalarm.presentation.add.AddContract.AddState
import org.a_cyb.sayitalarm.presentation.add.AddContract.AddState.Error
import org.a_cyb.sayitalarm.presentation.add.AddContract.AddState.Initial
import org.a_cyb.sayitalarm.presentation.add.AddContract.AddState.Success
import org.a_cyb.sayitalarm.presentation.alarm_panel.AlarmPanelContract.AlarmUI
import org.a_cyb.sayitalarm.presentation.interactor.InteractorContract
import org.a_cyb.sayitalarm.presentation.viewmodel.mapper.AlarmMapperContract

internal class AddViewModel(
    private val interactor: InteractorContract.AddInteractor,
    private val mapper: AlarmMapperContract,
) : AddContract.AddViewModel, ViewModel() {

    private val _state: MutableStateFlow<AddState> = MutableStateFlow(Initial)
    override val state: StateFlow<AddState> = _state.stateIn(
        scope = scope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = Initial
    )

    init {
        setDefaultAlarmUI()
    }

    private fun setDefaultAlarmUI() {
        Success(mapper.mapToAlarmUI(defaultAlarm))
            .updateState()
    }

    override fun setTime(hour: Hour, minute: Minute) {
        updateSuccessOrError {
            copy(time = mapper.mapToTimeUI(hour, minute))
        }
    }

    override fun setWeeklyRepeat(weeklyRepeat: WeeklyRepeat) {
        updateSuccessOrError {
            copy(weeklyRepeat = mapper.mapToWeeklyRepeatUI(weeklyRepeat))
        }
    }

    override fun setLabel(label: Label) {
        updateSuccessOrError {
            copy(label = label.label)
        }
    }

    override fun setAlertType(alertType: AlertType) {
        updateSuccessOrError {
            copy(alertType = mapper.mapToAlertTypeUI(alertType))
        }
    }

    override fun setRingtone(ringtone: Ringtone) {
        updateSuccessOrError {
            copy(ringtone = mapper.mapToRingtoneUI(ringtone))
        }
    }

    override fun setScripts(scripts: SayItScripts) {
        updateSuccessOrError {
            copy(sayItScripts = scripts.scripts)
        }
    }

    override fun save() {
        getAlarmUIOrNull()
            ?.let { interactor.save(mapper.mapToAlarm(it), scope) }
            ?: run { Error.updateState() }
    }

    private fun updateSuccessOrError(update: AlarmUI.() -> AlarmUI) {
        val alarmUI = getAlarmUIOrNull()
        if (alarmUI == null) Error else Success(alarmUI.update())
            .updateState()
    }

    private fun getAlarmUIOrNull(): AlarmUI? {
        return (_state.value as? Success)?.alarmUI
    }

    private fun AddState.updateState() {
        _state.update { this }
    }

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

    override fun <T : CommandReceiver> runCommand(command: Command<T>) {
        @Suppress("UNCHECKED_CAST")
        command.execute(this as T)
    }

    companion object Default {
        val HOUR = Hour(8)
        val MINUTE = Minute(0)
        val WEEKLY_REPEAT = WeeklyRepeat()
        val LABEL = Label("")
        val ENABLED = true
        val ALERT_TYPE = AlertType.SOUND_AND_VIBRATE
        val RINGTONE = Ringtone("")
        val ALARM_TYPE = AlarmType.SAY_IT
        val SAY_IT_SCRIPTS = SayItScripts()
    }
}
