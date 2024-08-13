/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.alarm_service.core

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.a_cyb.sayitalarm.domain.alarm_service.AlarmServiceContract
import org.a_cyb.sayitalarm.domain.alarm_service.AlarmServiceContract.AlarmServiceController
import org.a_cyb.sayitalarm.domain.alarm_service.AlarmServiceContract.AlarmServiceController.AlarmServiceState
import org.a_cyb.sayitalarm.domain.repository.RepositoryContract
import org.a_cyb.sayitalarm.entity.Alarm
import org.a_cyb.sayitalarm.entity.Settings
import org.a_cyb.sayitalarm.presentation.viewmodel.SettingsViewModel

class AlarmServiceController(
    private val alarmRepository: RepositoryContract.AlarmRepository,
    private val settingsRepository: RepositoryContract.SettingsRepository,
    private val dispatcher: CoroutineDispatcher,
) : AlarmServiceController {

    private var alarmService: AlarmServiceContract.AlarmService? = null
    private var alarm: Alarm? = null
    private var settings: Settings? = null

    private val _alarmState: MutableStateFlow<AlarmServiceState> = MutableStateFlow(AlarmServiceState.Initial)
    override val alarmState: StateFlow<AlarmServiceState> = _alarmState.asStateFlow()

    override fun onServiceBind(service: AlarmServiceContract.AlarmService, alarmId: Long) {
        alarmService = service

        val scope = CoroutineScope(Job() + dispatcher)
        scope
            .launch { setAlarmAndSettings(alarmId, this) }
            .invokeOnCompletion {
                scope.cancel()
                startRingAlarm()
            }
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
            // TODO: Push a notification about the canceled alarm.
            alarmService!!.stopService()
        } else {
            alarmService!!.ringAlarm(alarm!!.ringtone, alarm!!.alertType)
            _alarmState.value = AlarmServiceState.Ringing(alarm!!.label)
        }
    }

    override fun onServiceDisconnected() {
        alarmService = null
        _alarmState.value = AlarmServiceState.Error
    }

    override fun startSayIt() {
        runActionOrUpdateError {
            alarmService!!.startSayIt()
            _alarmState.value = AlarmServiceState.RunningSayIt(alarm!!.sayItScripts)
        }
    }

    override fun startSnooze() {
        runActionOrUpdateError {
            alarmService?.startSnooze(settings!!.snooze)
        }
    }

    override fun terminate() {
        alarmService?.stopService()
    }

    private fun runActionOrUpdateError(action: (AlarmServiceContract.AlarmService.() -> Unit)) {
        if (alarmService != null) {
            alarmService!!.action()
        } else {
            _alarmState.value = AlarmServiceState.Error
        }
    }
}