/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.data

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import org.a_cyb.sayitalarm.data.datasource.DataSourceContract
import org.a_cyb.sayitalarm.data.model.toAlarm
import org.a_cyb.sayitalarm.data.model.toDTO
import org.a_cyb.sayitalarm.domain.repository.RepositoryContract
import org.a_cyb.sayitalarm.entity.Alarm
import org.acyb.sayitalarm.database.Alarm as AlarmDTO

class AlarmRepository(
    private val dataSource: DataSourceContract.AlarmDataSource,
    private val dispatcher: CoroutineDispatcher,
) : RepositoryContract.AlarmRepository {

    override fun getAllAlarms(): Flow<Result<List<Alarm>>> {
        return dataSource
            .getAllByTimeAsc(dispatcher)
            .map(::map)
    }

    private fun map(result: Result<List<AlarmDTO>>): Result<List<Alarm>> =
        result.map { listOfEntity ->
            listOfEntity.map { dto ->
                dto.toAlarm()
            }
        }

    override fun getAlarm(id: Long, scope: CoroutineScope): Deferred<Result<Alarm>> =
        scope.async(context = dispatcher) {
            dataSource.getById(id)
                .map { it.toAlarm() }
        }

    override fun getAllEnabledAlarm(scope: CoroutineScope): Deferred<List<Alarm>> =
        scope.async(context = dispatcher) {
            dataSource
                .getAllEnabled()
                .map { it.toAlarm() }
        }

    override fun save(alarm: Alarm, scope: CoroutineScope) {
        scope.launch(context = dispatcher) {
            dataSource.insert(alarm.toDTO())
        }
    }

    override fun update(alarm: Alarm, scope: CoroutineScope) {
        scope.launch(context = dispatcher) {
            dataSource.update(alarm.toDTO())
        }
    }

    override fun updateEnabled(id: Long, enabled: Boolean, scope: CoroutineScope) {
        scope.launch(context = dispatcher) {
            dataSource.updateEnabled(id, enabled)
        }
    }

    override fun delete(id: Long, scope: CoroutineScope) {
        scope.launch(context = dispatcher) {
            dataSource.delete(id)
        }
    }
}
