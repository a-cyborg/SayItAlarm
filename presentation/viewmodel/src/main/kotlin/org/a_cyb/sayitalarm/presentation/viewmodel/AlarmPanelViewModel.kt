/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.presentation.viewmodel

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
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
import org.a_cyb.sayitalarm.presentation.alarm_panel.AlarmPanelContract
import org.a_cyb.sayitalarm.presentation.alarm_panel.AlarmPanelContract.*
import org.a_cyb.sayitalarm.presentation.interactor.InteractorContract
import org.a_cyb.sayitalarm.ringtone_manager.RingtoneManagerContract

class AlarmPanelViewModel(
    alarmId: Long = 0,
    override val scope: CoroutineScope,
    private val interactor: InteractorContract.AlarmPanelInteractor,
    private val ringtoneManager: RingtoneManagerContract,
    private val weekdayFormatter: WeekdayFormatterContract,
    private val enumFormatter: EnumFormatterContract,
) : AlarmPanelContract.AlarmPanelViewModel {

    private val _alarm: MutableStateFlow<Alarm> = MutableStateFlow(getDefaultAlarm())
    private val alarm: StateFlow<Alarm> = _alarm.asStateFlow()

    private val _alarmUI: MutableSharedFlow<AlarmUI> = MutableSharedFlow()
    override val alarmUI: SharedFlow<AlarmUI> = _alarmUI.asSharedFlow()

    init {
        alarm
            .onEach(::emitAlarmUI)
            .launchIn(scope)

        initAlarm(alarmId)
    }

    private fun emitAlarmUI(alarm: Alarm) {
        scope.launch {
            _alarmUI.emit(alarm.toAlarmUI())
        }
    }

    private fun Alarm.toAlarmUI(): AlarmUI {
        return AlarmUI(
            hour = this.hour.hour,
            minute = this.minute.minute,
            weeklyRepeat = weekdayFormatter.formatAbbr(this.weeklyRepeat.weekdays),
            alertType = enumFormatter.format(this.alertType),
            label = this.label.label,
            ringtone = ringtoneManager.getRingtoneTitle(this.ringtone.ringtone),
            sayItScripts = this.sayItScripts.scripts
        )
    }

    private fun initAlarm(id: Long) {
        if (id != 0L) {
            interactor
                .fetchAlarm(id, scope)
                .emitAlarm()
        }
    }

    private fun Alarm.emitAlarm() {
        scope.launch {
            _alarm.update { this@emitAlarm }
        }
    }

    override fun setTime(hour: Hour, minute: Minute) {
        _alarm.value
            .copy(hour = hour, minute = minute)
            .emitAlarm()
    }

    override fun setWeeklyRepeat(weeklyRepeat: WeeklyRepeat) {
        _alarm.value
            .copy(weeklyRepeat = weeklyRepeat)
            .emitAlarm()
    }

    override fun setLabel(label: Label) {
        _alarm.value
            .copy(label = label)
            .emitAlarm()
    }

    override fun setAlertType(alertType: AlertType) {
        _alarm.value
            .copy(alertType = alertType)
            .emitAlarm()
    }

    override fun setRingtone(ringtone: Ringtone) {
        _alarm.value
            .copy(ringtone = ringtone)
            .emitAlarm()
    }

    override fun setScripts(scripts: SayItScripts) {
        _alarm.value
            .copy(sayItScripts = scripts)
            .emitAlarm()
    }

    override fun getAlarm(): Alarm {
        return alarm.value
    }

    private fun getDefaultAlarm(): Alarm {
        return Alarm(
            hour = Hour(8),
            minute = Minute(0),
            weeklyRepeat = WeeklyRepeat(),
            label = Label(""),
            enabled = true,
            alertType = AlertType.SOUND_AND_VIBRATE,
            ringtone = Ringtone(ringtoneManager.getDefaultRingtoneTitle()),
            alarmType = AlarmType.SAY_IT,
            sayItScripts = SayItScripts()
        )
    }

    override fun <T : CommandContract.CommandReceiver> runCommand(command: CommandContract.Command<T>) {
        @Suppress("UNCHECKED_CAST")
        command.execute(this as T)
    }
}
