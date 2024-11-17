/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.domain.alarm_service

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.StateFlow
import org.a_cyb.sayitalarm.entity.AlertType
import org.a_cyb.sayitalarm.entity.Label
import org.a_cyb.sayitalarm.entity.Ringtone
import org.a_cyb.sayitalarm.entity.SayItScripts

interface AlarmServiceContract {
    fun ringAlarm(ringtone: Ringtone, alertType: AlertType)
    fun startSayIt()
    fun startSnooze()
    fun stopService()
}

interface AlarmServiceControllerContract {
    val controllerState: StateFlow<ControllerState>

    fun onServiceBind(service: AlarmServiceContract, alarmId: Long)
    fun onServiceDisconnected()
    fun startSayIt()
    fun startSnooze()
    fun scheduleNextAlarm(scope: CoroutineScope)
    fun terminate()

    sealed interface ControllerState {
        data object Initial : ControllerState
        data class Ringing(val label: Label) : ControllerState
        data class RunningSayIt(val sayItScripts: SayItScripts) : ControllerState
        data object Error : ControllerState
    }
}
