/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.domain.alarm_service

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.StateFlow

sealed interface AlarmServiceContract {

    interface AlarmScheduler : AlarmServiceContract {
        suspend fun setAlarm(scope: CoroutineScope)
        suspend fun cancelAlarm(id: Long, scope: CoroutineScope)
    }

    interface AlertServiceContract {
        fun startSayIt()
        fun startSnooze()
    }

    interface AlertServiceControllerContract {
        val alertServiceState: StateFlow<AlertState>

        fun onServiceBind(serviceContract: AlertServiceContract)
        fun onServiceDisconnected()
        fun startSayIt()
        fun startSnooze()

        sealed interface AlertState {
            data object Initial : AlertState
            data object Ringing : AlertState
            data object Error : AlertState
        }
    }
}
