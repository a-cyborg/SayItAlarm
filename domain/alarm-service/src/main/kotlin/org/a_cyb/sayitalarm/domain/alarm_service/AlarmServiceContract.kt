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
        fun startSayIt()
        fun startSnooze()
    }

    interface AlarmServiceController {
        val alarmState: StateFlow<AlarmServiceState>

        fun onServiceBind(serviceContract: AlarmService)
        fun onServiceDisconnected()
        fun startSayIt()
        fun startSnooze()

        sealed interface AlarmServiceState {
            data object Initial : AlarmServiceState
            data object Ringing : AlarmServiceState
            data object RunningSayIt : AlarmServiceState
            data object Completed : AlarmServiceState
            data object Error : AlarmServiceState
        }
    }
}
