package org.a_cyb.sayitalarm.core.data.repository

import kotlinx.coroutines.flow.Flow
import org.a_cyb.sayitalarm.core.model.AlarmInstance

interface AlarmInstanceRepository {
    /**
     * Get an alarmInstance by its id(unique identifier)
     *
     * @param id The unique identifier of the alarmInstance to get.
     * @return The alarmInstance external model corresponding to the provided ID, or null if no alarm found.
     */
    fun getAlarmInstanceById(id: Int): Flow<AlarmInstance?>

    /**
     * Inserts the provided alarm into the database.
     *
     * @param instance The alarm instance to insert.
     * @return The primaryKey (as a String) of the inserted row. Returns -1 if the insertion fails.
     */
    suspend fun insertAlarmInstance(instance: AlarmInstance): Int
}
