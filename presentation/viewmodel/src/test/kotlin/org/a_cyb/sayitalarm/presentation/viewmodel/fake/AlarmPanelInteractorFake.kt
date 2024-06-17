/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.presentation.viewmodel.fake

import kotlinx.coroutines.CoroutineScope
import org.a_cyb.sayitalarm.entity.Alarm
import org.a_cyb.sayitalarm.presentation.interactor.InteractorContract

class AlarmPanelInteractorFake : InteractorContract.AlarmPanelInteractor {
    private val alarms = TestAlarms().getAlarms()

    override fun fetchAlarm(id: Long, scope: CoroutineScope): Alarm {
        return alarms.first { it.id == id }
    }
}