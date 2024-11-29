/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.domain.alarm_service

import kotlinx.coroutines.flow.StateFlow
import org.a_cyb.sayitalarm.entity.AlertType
import org.a_cyb.sayitalarm.entity.Ringtone

interface AlarmControllerContract {
    val alarmControllerState: StateFlow<AlarmControllerState>

    fun startAlarm(ringtone: Ringtone, alertType: AlertType)
    fun stopAlarm()
    fun stopService()

    sealed interface AlarmControllerState {
        data object Initial : AlarmControllerState
        data class ServiceConnected(val alarmId: Long) : AlarmControllerState
        data object ServiceDisconnected : AlarmControllerState
        data class AudioPlayerError(val message: String) : AlarmControllerState
        data object ServiceStateCollectionError : AlarmControllerState
    }
}
