package org.a_cyb.sayitalarm.core.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.a_cyb.sayitalarm.core.data.model.asEntity
import org.a_cyb.sayitalarm.core.database.dao.AlarmInstanceDao
import org.a_cyb.sayitalarm.core.database.model.asExternalModel
import org.a_cyb.sayitalarm.core.model.AlarmInstance
import javax.inject.Inject


/**
 * [AlarmInstanceRepository] implementation disk storage backed by Room database.
 */
class ImplAlarmInstanceRepository @Inject constructor(
    private val instanceDao: AlarmInstanceDao,
) : AlarmInstanceRepository {

    override fun getAlarmInstanceById(id: Int): Flow<AlarmInstance?> =
        instanceDao
            .getAlarmInstance(id)
            .map { it?.asExternalModel() }

    override suspend fun insertAlarm(instance: AlarmInstance): Int =
        instanceDao
            .insertAlarm(instance.asEntity())
            .toInt()
}