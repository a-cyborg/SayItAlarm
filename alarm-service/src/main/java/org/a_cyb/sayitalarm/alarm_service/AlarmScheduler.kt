/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.alarm_service

import kotlinx.coroutines.CoroutineScope
import org.a_cyb.sayitalarm.domain.alarm_service.AlarmServiceContract

class AlarmScheduler : AlarmServiceContract.AlarmScheduler {
    override suspend fun setAlarm(scope: CoroutineScope) {
    }

    override suspend fun cancelAlarm(id: Long, scope: CoroutineScope) {
    }
}