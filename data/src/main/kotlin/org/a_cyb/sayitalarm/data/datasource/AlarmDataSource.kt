/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.data.datasource

import kotlin.coroutines.CoroutineContext
import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import org.a_cyb.sayitalarm.data.model.AlarmEntity
import org.acyb.sayitalarm.database.AlarmQueries
import org.acyb.sayitalarm.database.Alarm as DTO

class AlarmDataSource(
    private val alarmQueries: AlarmQueries,
    private val coroutineContext: CoroutineContext,
) : DataSourceContract.AlarmDataSource {

    override fun getAllByTimeAsc(): Flow<Result<List<AlarmEntity>>> {
        return alarmQueries.getAllByTimeAsc()
            .asFlow()
            .mapToList(coroutineContext)
            .map(::toSuccessResult)
            .catch { emit(Result.failure(it)) }
    }

    private fun toSuccessResult(alarms: List<DTO>): Result<List<AlarmEntity>> {
        return Result.success(
            alarms.map { it.toAlarmEntity() }
        )
    }

    override suspend fun getById(id: Long): Result<AlarmEntity> {
        val alarm = alarmQueries.getById(id)
            .executeAsOneOrNull()

        return if (alarm == null) {
            Result.failure(IllegalStateException())
        } else {
            Result.success(alarm.toAlarmEntity())
        }
    }

    override suspend fun insert(alarm: AlarmEntity) {
        alarmQueries.insert(
            alarm.hour,
            alarm.minute,
            alarm.weeklyRepeat,
            alarm.label,
            alarm.enabled,
            alarm.alertType,
            alarm.ringtone,
            alarm.alarmType,
            alarm.sayItScripts,
        )
    }

    override suspend fun update(alarm: AlarmEntity) {
        alarmQueries.update(
            hour = alarm.hour,
            minute = alarm.minute,
            weeklyRepeat = alarm.weeklyRepeat,
            label = alarm.label,
            enabled = alarm.enabled,
            alertType = alarm.alertType,
            ringtone = alarm.ringtone,
            alarmType = alarm.alarmType,
            sayItScripts = alarm.sayItScripts,
            id = alarm.id,
        )
    }

    override suspend fun updateEnabled(id: Long, enabled: Boolean) {
        alarmQueries.updateEnabled(
            enabled = enabled,
            id = id
        )
    }

    override suspend fun delete(id: Long) {
        alarmQueries.delete(id)
    }

    private fun DTO.toAlarmEntity(): AlarmEntity {
        return AlarmEntity(
            id = this.id,
            hour = this.hour,
            minute = this.minute,
            weeklyRepeat = this.weeklyRepeat,
            label = this.label,
            enabled = this.enabled,
            alertType = this.alertType,
            ringtone = this.ringtone,
            alarmType = this.alarmType,
            sayItScripts = this.sayItScripts,
        )
    }
}
