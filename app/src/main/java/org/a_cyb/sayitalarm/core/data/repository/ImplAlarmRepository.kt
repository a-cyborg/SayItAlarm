package org.a_cyb.sayitalarm.core.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.a_cyb.sayitalarm.core.data.model.asEntity
import org.a_cyb.sayitalarm.core.database.dao.AlarmDao
import org.a_cyb.sayitalarm.core.database.model.AlarmEntity
import org.a_cyb.sayitalarm.core.database.model.asExternalModel
import org.a_cyb.sayitalarm.core.model.Alarm
import javax.inject.Inject

/**
 * [AlarmRepository] implementation disk storage backed by Room database.
 */
class ImplAlarmRepository @Inject constructor(
    private val alarmDao: AlarmDao,
) : AlarmRepository {

    override fun getAlarms(): Flow<List<Alarm>> =
        alarmDao
            .getAlarmEntities()
            .map { it.map(AlarmEntity::asExternalModel) }

    override fun getAlarmSortedByTime(): Flow<List<Alarm>> =
        alarmDao
            .getAlarmEntitiesSortedByCombinedMinute()
            .map { it.map(AlarmEntity::asExternalModel) }

    override fun getAlarmById(id: Int): Flow<Alarm?> =
        alarmDao
            .getAlarm(id)
            .map { it?.asExternalModel() }

    override suspend fun insertAlarm(alarm: Alarm): String =
        alarmDao
            .insertAlarm(alarm.asEntity())
            .toString()

    override suspend fun deleteAlarm(alarmId: Int): Int =
        alarmDao
            .deleteAlarm(alarmId)

    override suspend fun updateAlarm(id: Int, enabled: Boolean): Int =
        alarmDao
            .updateAlarmEnabled(id, enabled)
}