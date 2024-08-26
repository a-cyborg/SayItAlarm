/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.alarm_service.core

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.a_cyb.sayitalarm.domain.alarm_service.AlarmServiceContract
import org.a_cyb.sayitalarm.domain.alarm_service.AlarmServiceContract.AlarmServiceController
import org.a_cyb.sayitalarm.domain.alarm_service.AlarmServiceContract.AlarmServiceController.ControllerState
import org.a_cyb.sayitalarm.domain.repository.RepositoryContract
import org.a_cyb.sayitalarm.entity.Alarm
import org.a_cyb.sayitalarm.entity.Settings
import org.a_cyb.sayitalarm.presentation.viewmodel.SettingsViewModel

class AlarmServiceController(
    private val alarmRepository: RepositoryContract.AlarmRepository,
    private val settingsRepository: RepositoryContract.SettingsRepository,
    private val alarmScheduler: AlarmServiceContract.AlarmScheduler,
    private val scope: CoroutineScope,
) : AlarmServiceController {

    private var alarmService: AlarmServiceContract.AlarmService? = null
    private var alarm: Alarm? = null
    private var settings: Settings? = null

    private val _controllerState: MutableStateFlow<ControllerState> = MutableStateFlow(ControllerState.Initial)
    override val controllerState: StateFlow<ControllerState> = _controllerState.asStateFlow()

    override fun onServiceBind(service: AlarmServiceContract.AlarmService, alarmId: Long) {
        alarmService = service

        scope.launch { setAlarmAndSettings(alarmId, this) }
            .invokeOnCompletion { startRingAlarm() }
    }

    private suspend fun setAlarmAndSettings(alarmId: Long, scope: CoroutineScope) {
        alarm = alarmRepository
            .getAlarm(alarmId, scope)
            .await()
            .getOrNull()

        settings = settingsRepository
            .getSettings()
            .first()
            .getOrNull() ?: SettingsViewModel.getDefaultSettings()
    }

    private fun startRingAlarm() {
        if (alarm == null) {
            // If the alarm is no longer in the database, terminate the application.
            alarmService!!.stopService()
        } else {
            alarmService!!.ringAlarm(alarm!!.ringtone, alarm!!.alertType)
            ControllerState.Ringing(alarm!!.label).update()
        }
    }

    override fun onServiceDisconnected() {
        alarmService = null
        ControllerState.Error.update()
    }

    override fun startSayIt() {
        runActionOrUpdateError {
            alarmService!!.startSayIt()
            ControllerState.RunningSayIt(alarm!!.sayItScripts).update()
        }
    }

    override fun scheduleNextAlarm(scope: CoroutineScope) {
        if (alarm != null) {
            scope.launch {
                if (alarm!!.weeklyRepeat.weekdays.isEmpty()) {
                    alarmRepository.update(alarm!!.copy(enabled = false), this)
                }
                alarmScheduler.scheduleAlarms()
            }
        }
    }

    override fun startSnooze() {
        runActionOrUpdateError {
            alarmScheduler.scheduleSnooze(alarm!!.id, settings!!.snooze)
            alarmService?.startSnooze()
        }
    }

    override fun terminate() {
        alarmService?.stopService()
    }

    private fun runActionOrUpdateError(action: () -> Unit) {
        when (alarmService == null) {
            false -> action()
            true -> ControllerState.Error.update()
        }
    }

    private fun ControllerState.update() {
        _controllerState.update { this }
    }
}