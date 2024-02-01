package org.a_cyb.sayitalarm.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import org.a_cyb.sayitalarm.core.database.model.AlarmEntity

@Dao
interface AlarmDao {
    @Query(value =
    """ 
        SELECT * FROM alarms
    """)
    fun getAlarmEntities(): Flow<List<AlarmEntity>>

    @Query(value =
    """ 
        SELECT * FROM alarms 
        ORDER BY combinedMinutes ASC 
    """)
    fun getAlarmEntitiesSortedByCombinedMinute(): Flow<List<AlarmEntity>>

    @Query(value =
    """ 
        SELECT * FROM alarms 
        WHERE id = :alarmId 
    """)
    fun getAlarm(alarmId: Int) : Flow<AlarmEntity?>

    // It will return primaryKey of the inserted row. If the insertion fails it will return -1
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAlarm(alarm: AlarmEntity): Long

    @Query(value =
    """ 
        DELETE FROM alarms 
        WHERE id = :alarmId 
    """)
    fun deleteAlarm(alarmId: Int): Int

    @Query(value =
    """ 
        UPDATE alarms 
        SET enabled = :enabled 
        WHERE id = :alarmId
    """)
    suspend fun updateAlarmEnabled(alarmId: Int, enabled: Boolean): Int
}