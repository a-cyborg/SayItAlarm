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
import org.a_cyb.sayitalarm.entity.Snooze

sealed interface AlarmServiceContract {

    interface AlarmScheduler {
        suspend fun setAlarm(scope: CoroutineScope)
        suspend fun cancelAlarm(id: Long, scope: CoroutineScope)
    }

    interface AlarmService {
        fun ringAlarm(ringtone: Ringtone, alertType: AlertType)
        fun startSayIt()
        fun startSnooze(snooze: Snooze)
        fun stopService()
    }

    interface AlarmServiceController {
        val alarmState: StateFlow<AlarmServiceState>

        fun onServiceBind(service: AlarmService, alarmId: Long)
        fun onServiceDisconnected()
        fun startSayIt()
        fun startSnooze()
        fun terminate()

        sealed interface AlarmServiceState {
            data object Initial : AlarmServiceState
            data class Ringing(val label: Label) : AlarmServiceState
            data class RunningSayIt(val scripts: SayItScripts) : AlarmServiceState
            data object Completed : AlarmServiceState
            data object Error : AlarmServiceState
        }
    }

    interface SayItRecognizer {
        fun startSayItRecognizer()
        fun stopSayItRecognizer()
    }
}
