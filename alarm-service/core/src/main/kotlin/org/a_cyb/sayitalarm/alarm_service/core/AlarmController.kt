/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.alarm_service.core

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.a_cyb.sayitalarm.alarm_service.core.AlarmServiceContract.ServiceEvent
import org.a_cyb.sayitalarm.domain.alarm_service.AlarmControllerContract
import org.a_cyb.sayitalarm.domain.alarm_service.AlarmControllerContract.AlarmControllerState
import org.a_cyb.sayitalarm.entity.AlertType
import org.a_cyb.sayitalarm.entity.Ringtone

class AlarmController(dispatcher: CoroutineDispatcher) : AlarmControllerContract {

    private val controllerScope = CoroutineScope(dispatcher + SupervisorJob())
    private val _alarmControllerState = MutableStateFlow<AlarmControllerState>(AlarmControllerState.Initial)
    override val alarmControllerState: StateFlow<AlarmControllerState> = _alarmControllerState.asStateFlow()
    private lateinit var alarmService: AlarmServiceContract

    internal fun connectService(service: AlarmServiceContract, alarmId: Long) {
        alarmService = service
        updateState(AlarmControllerState.ServiceConnected(alarmId))
        collectServiceEvent()
    }

    private fun collectServiceEvent() {
        controllerScope.launch {
            alarmService.serviceEvent
                .onEach(::handleEvent)
                .catch { updateState(AlarmControllerState.ServiceStateCollectionError) }
                .collect()
        }
    }

    override fun startAlarm(ringtone: Ringtone, alertType: AlertType) {
        checkConnectionAndRunActions {
            startRinging(ringtone, alertType)
        }
    }

    override fun stopAlarm() {
        checkConnectionAndRunActions {
            stopRinging()
        }
    }

    override fun stopService() {
        controllerScope.cancel()
        checkConnectionAndRunActions {
            stopService()
        }
    }

    private fun handleEvent(serviceEvent: ServiceEvent) {
        when (serviceEvent) {
            is ServiceEvent.AudioVibePlayerError -> {
                updateState(AlarmControllerState.AudioPlayerError("${serviceEvent.exception.message}"))
            }
        }
    }

    private fun checkConnectionAndRunActions(action: AlarmServiceContract.() -> Unit) {
        if (::alarmService.isInitialized) {
            alarmService.action()
        } else {
            updateState(AlarmControllerState.ServiceDisconnected)
        }
    }

    internal fun serviceDisconnected() {
        updateState(AlarmControllerState.ServiceDisconnected)
    }

    private fun updateState(alarmControllerState: AlarmControllerState) {
        controllerScope.launch {
            _alarmControllerState.update { alarmControllerState }
        }
    }
}
