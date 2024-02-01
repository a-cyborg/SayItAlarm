package org.a_cyb.sayitalarm.core.data.repository

import kotlinx.coroutines.flow.Flow
import org.a_cyb.sayitalarm.core.model.Alarm

interface AlarmRepository {
    /**
     * Get all alarms as a flow stream.
     *
     * @return Flow of List of Alarm
     */
    fun getAlarms(): Flow<List<Alarm>>

    /**
     * Get all alarms as a flowable stream.
     * sorted in ascending order by alarm time.
     *
     * @return Flow of List of Alarm
     */
    fun getAlarmSortedByTime(): Flow<List<Alarm>>

    /**
     * Get an alarm by its id(unique identifier)
     *
     * @param id The unique identifier of the alarm to get.
     * @return The alarm external model corresponding to the provided ID, or null if no alarm found.
     */
    fun getAlarmById(id: Int): Flow<Alarm?>

    /**
     * Inserts the provided alarm into the database.
     *
     * @param alarm The alarm instance to insert.
     * @return The primaryKey (as a String) of the inserted row. Returns -1 if the insertion fails.
     */
    suspend fun insertAlarm(alarm: Alarm): String

    /**
     * Update the [Alarm.enabled] status of an alarm.
     *
     * @param id The unique identifier of the alarm to update.
     * @param enabled The new `enabled` status to set for the alarm.
     * @return The number of row that were updated. Returns 0 if the update fails.
     */
    suspend fun updateAlarm(id: Int, enabled: Boolean): Int

    /**
     * Deletes the alarm of alarmId from the database and returns the number of deleted rows.
     *
     * @param alarmId The unique identifier of the alarm to delete.
     * @return The number of rows deleted. Returns 0 if the deletion fails.
     */
    suspend fun deleteAlarm(alarmId: Int): Int
}