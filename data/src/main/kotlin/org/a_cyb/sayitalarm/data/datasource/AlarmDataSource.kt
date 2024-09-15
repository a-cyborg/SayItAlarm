/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.data.datasource

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import org.acyb.sayitalarm.database.AlarmQueries
import org.acyb.sayitalarm.database.Alarm as DTO

class AlarmDataSource(
    private val alarmQueries: AlarmQueries,
) : DataSourceContract.AlarmDataSource {

    override fun getAllByTimeAsc(dispatcher: CoroutineDispatcher): Flow<Result<List<DTO>>> =
        alarmQueries.getAllByTimeAsc()
            .asFlow()
            .mapToList(dispatcher)
            .map { Result.success(it) }
            .catch { emit(Result.failure(it)) }

    override suspend fun getById(id: Long): Result<DTO> =
        alarmQueries
            .getById(id)
            .executeAsOneOrNull()
            .resolveToResult()

    private fun DTO?.resolveToResult(): Result<DTO> =
        if (this != null) {
            Result.success(this)
        } else {
            Result.failure(IllegalStateException())
        }

    override suspend fun getAllEnabled(): List<DTO> =
        alarmQueries
            .getAllEnabledAlarm()
            .executeAsList()

    override suspend fun insert(alarmDto: DTO) {
        alarmQueries.insert(
            alarmDto.hour,
            alarmDto.minute,
            alarmDto.weeklyRepeat,
            alarmDto.label,
            alarmDto.enabled,
            alarmDto.alertType,
            alarmDto.ringtone,
            alarmDto.alarmType,
            alarmDto.sayItScripts,
        )
    }

    override suspend fun update(alarmDto: DTO) {
        alarmQueries.update(
            hour = alarmDto.hour,
            minute = alarmDto.minute,
            weeklyRepeat = alarmDto.weeklyRepeat,
            label = alarmDto.label,
            enabled = alarmDto.enabled,
            alertType = alarmDto.alertType,
            ringtone = alarmDto.ringtone,
            alarmType = alarmDto.alarmType,
            sayItScripts = alarmDto.sayItScripts,
            id = alarmDto.id,
        )
    }

    override suspend fun updateEnabled(id: Long, enabled: Boolean) {
        alarmQueries.updateEnabled(
            enabled = enabled,
            id = id,
        )
    }

    override suspend fun delete(id: Long) {
        alarmQueries.delete(id)
    }
}
