/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.domain.alarm_service

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.StateFlow

sealed interface AlarmServiceContract {

    interface AlarmScheduler {
        suspend fun setAlarm(scope: CoroutineScope)
        suspend fun cancelAlarm(id: Long, scope: CoroutineScope)
    }

    interface AlarmService {
        fun ringAlarm()
        fun startSayIt()
        fun startSnooze()
        fun stopService()
    }

    interface AlarmServiceController {
        val alarmState: StateFlow<AlarmServiceState>

        fun onServiceBind(serviceContract: AlarmService)
        fun onServiceDisconnected()
        fun startSayIt()
        fun startSnooze()
        fun terminate()

        sealed interface AlarmServiceState {
            data object Initial : AlarmServiceState
            data object Ringing : AlarmServiceState
            data object RunningSayIt : AlarmServiceState
            data object Completed : AlarmServiceState
            data object Error : AlarmServiceState
        }
    }

    interface SayItRecognizer {
        fun startSayItRecognizer()
        fun stopSayItRecognizer()
    }
}
