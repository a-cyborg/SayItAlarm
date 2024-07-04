/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.domain.interactor

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import org.a_cyb.sayitalarm.domain.alarm_service.AlarmServiceContract
import org.a_cyb.sayitalarm.domain.repository.RepositoryContract
import org.a_cyb.sayitalarm.entity.Alarm

class ListInteractor(
    private val alarmRepository: RepositoryContract.AlarmRepository,
    private val alarmScheduler: AlarmServiceContract.AlarmScheduler,
) : InteractorContract.ListInteractor {

    override fun getAllAlarms(): Flow<Result<List<Alarm>>> =
        alarmRepository.getAllAlarms()

    override fun setEnabled(id: Long, enabled: Boolean, scope: CoroutineScope) {
        scope.launch {
            alarmRepository.updateEnabled(id, enabled, this)

            setAlarmSchedule(id, enabled, scope)
        }
    }

    private suspend fun setAlarmSchedule(id: Long, enabled: Boolean, scope: CoroutineScope) {
        if (enabled) {
            alarmScheduler.setAlarm(scope)
        } else {
            alarmScheduler.cancelAlarm(id, scope)
        }
    }

    override fun deleteAlarm(id: Long, scope: CoroutineScope) {
        scope.launch {
            alarmRepository.delete(id, this)

            alarmScheduler.cancelAlarm(id, this)
        }
    }
}
