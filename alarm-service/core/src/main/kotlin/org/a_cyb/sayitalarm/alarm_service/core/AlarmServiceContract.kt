/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.alarm_service.core

import kotlinx.coroutines.flow.SharedFlow
import org.a_cyb.sayitalarm.entity.AlertType
import org.a_cyb.sayitalarm.entity.Ringtone

interface AlarmServiceContract {
    val serviceEvent: SharedFlow<ServiceEvent>

    fun startRinging(ringtone: Ringtone, alertType: AlertType)
    fun stopRinging()
    fun stopService()

    sealed interface ServiceEvent {
        data class AudioVibePlayerError(val exception: Exception) : ServiceEvent
    }
}
