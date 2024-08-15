/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.domain.interactor

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.a_cyb.sayitalarm.domain.alarm_service.AlarmServiceContract
import org.a_cyb.sayitalarm.domain.repository.RepositoryContract
import org.a_cyb.sayitalarm.entity.Alarm

class AddInteractor(
    private val alarmRepository: RepositoryContract.AlarmRepository,
    private val alarmScheduler: AlarmServiceContract.AlarmScheduler,
) : InteractorContract.AddInteractor {

    override fun save(alarm: Alarm, scope: CoroutineScope) {
        scope.launch {
            alarmRepository.save(alarm, this)
            alarmScheduler.scheduleAlarms(this)
        }
    }
}
