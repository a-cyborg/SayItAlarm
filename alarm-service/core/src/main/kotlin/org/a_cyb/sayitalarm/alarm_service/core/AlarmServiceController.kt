/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.alarm_service.core

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.a_cyb.sayitalarm.domain.alarm_service.AlarmServiceContract
import org.a_cyb.sayitalarm.domain.alarm_service.AlarmServiceContract.AlarmServiceController
import org.a_cyb.sayitalarm.domain.alarm_service.AlarmServiceContract.AlarmServiceController.AlarmServiceState

class AlarmServiceController : AlarmServiceController {

    private var alarmService: AlarmServiceContract.AlarmService? = null

    private val _alarmState: MutableStateFlow<AlarmServiceState> = MutableStateFlow(AlarmServiceState.Initial)
    override val alarmState: StateFlow<AlarmServiceState> = _alarmState.asStateFlow()

    override fun onServiceBind(service: AlarmServiceContract.AlarmService) {
        alarmService = service
        _alarmState.value = AlarmServiceState.Ringing
    }

    override fun onServiceDisconnected() {
        alarmService = null
        _alarmState.value = AlarmServiceState.Error
    }

    override fun startSayIt() {
        if (alarmService != null) {
            alarmService!!.startSayIt()
            _alarmState.value = AlarmServiceState.RunningSayIt
        } else {
            _alarmState.value = AlarmServiceState.Error
        }
    }

    override fun startSnooze() {}

    override fun terminate() {
        alarmService?.stopService()
    }
}