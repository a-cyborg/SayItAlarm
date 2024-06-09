/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.presentation.viewmodel.fake

import kotlinx.coroutines.CoroutineScope
import org.a_cyb.sayitalarm.alarm_service.AlarmSchedulerContract

class AlarmSchedulerFake : AlarmSchedulerContract {

    private var _invokedType = InvokedType.NONE
    val invokedType: InvokedType
        get() = _invokedType

    override suspend fun setAlarm(scope: CoroutineScope) {
        _invokedType = InvokedType.SET_ALARM
    }

    override suspend fun cancelAlarm(id: Long, scope: CoroutineScope) {
        _invokedType = InvokedType.CANCEL_ALARM
    }

    enum class InvokedType {
        SET_ALARM,
        CANCEL_ALARM,
        NONE
    }
}