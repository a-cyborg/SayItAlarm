/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.domain.interactor

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import org.a_cyb.sayitalarm.domain.alarm_service.AlarmSchedulerContract
import org.a_cyb.sayitalarm.domain.repository.RepositoryContract
import org.a_cyb.sayitalarm.entity.Alarm

class EditInteractor(
    private val alarmRepository: RepositoryContract.AlarmRepository,
    private val alarmScheduler: AlarmSchedulerContract,
) : InteractorContract.EditInteractor {

    private val _alarm: MutableSharedFlow<Result<Alarm>> = MutableSharedFlow()
    override val alarm = _alarm.asSharedFlow()

    override fun getAlarm(id: Long, scope: CoroutineScope) {
        scope.launch {
            alarmRepository.getAlarm(id, scope)
                .await()
                .let { _alarm.emit(it) }
        }
    }

    override fun update(alarm: Alarm, scope: CoroutineScope) {
        scope.launch {
            alarmRepository.update(alarm, scope)
            alarmScheduler.scheduleAlarms()
        }
    }
}
