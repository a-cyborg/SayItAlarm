/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.domain.interactor

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.a_cyb.sayitalarm.domain.alarm_service.AlarmControllerContract
import org.a_cyb.sayitalarm.domain.alarm_service.AlarmControllerContract.AlarmControllerState
import org.a_cyb.sayitalarm.domain.alarm_service.AlarmSchedulerContract
import org.a_cyb.sayitalarm.domain.interactor.InteractorContract.AlarmInteractor
import org.a_cyb.sayitalarm.domain.interactor.InteractorContract.AlarmInteractor.AlarmInteractorEvent.ERROR_ALARM_NOT_FOUND
import org.a_cyb.sayitalarm.domain.interactor.InteractorContract.AlarmInteractor.AlarmInteractorEvent.ERROR_AUDIO_PLAYER
import org.a_cyb.sayitalarm.domain.interactor.InteractorContract.AlarmInteractor.AlarmInteractorEvent.ERROR_SERVICE_DISCONNECTED
import org.a_cyb.sayitalarm.domain.repository.RepositoryContract
import org.a_cyb.sayitalarm.entity.Alarm
import org.a_cyb.sayitalarm.entity.Label
import org.a_cyb.sayitalarm.entity.Settings
import org.a_cyb.sayitalarm.entity.Snooze

class AlarmInteractor(
    private val alarmController: AlarmControllerContract,
    private val alarmScheduler: AlarmSchedulerContract,
    private val alarmRepository: RepositoryContract.AlarmRepository,
    private val settingsRepository: RepositoryContract.SettingsRepository,
    private val ioDispatcher: CoroutineDispatcher,
) : AlarmInteractor {

    private lateinit var alarm: Alarm
    private lateinit var settings: Settings

    private val _label = MutableSharedFlow<Result<Label>>()
    override val label: SharedFlow<Result<Label>> = _label.asSharedFlow()

    private val _event = MutableSharedFlow<AlarmInteractor.AlarmInteractorEvent>()
    override val event: SharedFlow<AlarmInteractor.AlarmInteractorEvent> = _event.asSharedFlow()

    override fun startAlarm(scope: CoroutineScope) {
        scope.launch(ioDispatcher) {
            alarmController.alarmControllerState
                .onEach { handleControllerEvent(it, this) }
                .collect()
        }
    }

    private fun handleControllerEvent(controllerState: AlarmControllerState, scope: CoroutineScope) =
        when (controllerState) {
            is AlarmControllerState.Initial -> {}

            is AlarmControllerState.ServiceConnected ->
                startAlarm(controllerState.alarmId, scope)

            is AlarmControllerState.ServiceDisconnected ->
                emitEvent(ERROR_SERVICE_DISCONNECTED, scope)

            is AlarmControllerState.AudioPlayerError ->
                emitEvent(ERROR_AUDIO_PLAYER, scope)

            is AlarmControllerState.ServiceStateCollectionError ->
                emitEvent(ERROR_SERVICE_DISCONNECTED, scope)
        }

    private fun startAlarm(alarmId: Long, scope: CoroutineScope) {
        scope.launch(ioDispatcher) {
            setAlarmAndSettings(alarmId, scope)
        }.invokeOnCompletion {
            if (::alarm.isInitialized) {
                alarmController
                    .startAlarm(alarm.ringtone, alarm.alertType)

                scope.launch {
                    _label.emit(Result.success(alarm.label))
                }
            } else {
                // If the alarm is not found in the database, terminate the app.
                alarmController.stopService()
            }
        }
    }

    private suspend fun setAlarmAndSettings(alarmId: Long, scope: CoroutineScope) {
        alarmRepository
            .getAlarm(alarmId, scope)
            .await()
            .onSuccess { alarm = it }
            .onFailure { emitEvent(ERROR_ALARM_NOT_FOUND, scope) }

        settingsRepository
            .getSettings()
            .first()
            .onSuccess { settings = it }
    }

    private fun emitEvent(event: AlarmInteractor.AlarmInteractorEvent, scope: CoroutineScope) {
        scope.launch {
            _event.emit(event)
        }
    }

    override fun stopAlarm() {
        alarmController.stopAlarm()
    }

    override fun snooze() {
        val snoozeMin = if (::settings.isInitialized) settings.snooze else Snooze(5)

        runBlocking {
            alarmScheduler.scheduleSnooze(alarm.id, snoozeMin)
            alarmController.stopService()
        }
    }
}
