/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.domain.interactor

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import org.a_cyb.sayitalarm.domain.alarm_service.AlarmServiceContract
import org.a_cyb.sayitalarm.domain.repository.RepositoryContract
import org.a_cyb.sayitalarm.entity.Alarm

class ListInteractor(
    private val alarmRepository: RepositoryContract.AlarmRepository,
    private val alarmScheduler: AlarmServiceContract.AlarmScheduler,
) : InteractorContract.ListInteractor {

    private val _alarms: MutableSharedFlow<Result<List<Alarm>>> = MutableSharedFlow()
    override val alarms: SharedFlow<Result<List<Alarm>>> = _alarms

    override fun load(scope: CoroutineScope) {
        scope.launch {
            alarmRepository
                .load(this)
                .await()
                .emitResult()
        }
    }

    private suspend fun Result<List<Alarm>>.emitResult() {
        this
            .onSuccess { _alarms.emit(Result.success(it)) }
            .onFailure { _alarms.emit(Result.failure(it)) }
    }

    override fun setEnabled(id: Long, enabled: Boolean, scope: CoroutineScope) {
        scope.launch {
            alarmRepository
                .updateEnabled(id, enabled, this)

            setAlarmSchedule(id, enabled, scope)

            load(this)
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

            load(this)
        }
    }
}
