/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.domain.interactor

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import org.a_cyb.sayitalarm.domain.alarm_service.AlarmSchedulerContract
import org.a_cyb.sayitalarm.domain.repository.RepositoryContract
import org.a_cyb.sayitalarm.entity.Alarm

class ListInteractor(
    private val alarmRepository: RepositoryContract.AlarmRepository,
    private val alarmScheduler: AlarmSchedulerContract,
) : InteractorContract.ListInteractor {

    override val alarms: Flow<Result<List<Alarm>>>
        get() = alarmRepository.getAllAlarms()

    override fun setEnabled(id: Long, enabled: Boolean, scope: CoroutineScope) {
        scope.launch {
            alarmRepository.updateEnabled(id, enabled, this)

            setAlarmSchedule(id, enabled)
        }
    }

    private fun setAlarmSchedule(id: Long, enabled: Boolean) {
        if (enabled) {
            alarmScheduler.scheduleAlarms()
        } else {
            alarmScheduler.cancelAlarm(id)
        }
    }

    override fun deleteAlarm(id: Long, scope: CoroutineScope) {
        scope.launch {
            alarmRepository.delete(id, this)
            alarmScheduler.cancelAlarm(id)
        }
    }
}
