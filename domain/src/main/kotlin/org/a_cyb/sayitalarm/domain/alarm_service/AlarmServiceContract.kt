/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.domain.alarm_service

import kotlinx.coroutines.CoroutineScope

sealed interface AlarmServiceContract {

    interface AlarmScheduler : AlarmServiceContract {
        suspend fun setAlarm(scope: CoroutineScope)
        suspend fun cancelAlarm(id: Long, scope: CoroutineScope)
    }
}
