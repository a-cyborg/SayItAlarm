package org.a_cyb.sayitalarm.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import org.a_cyb.sayitalarm.core.database.model.AlarmInstanceEntity

@Dao
interface AlarmInstanceDao {

    // It will return primary key of the inserted row. If the insertion fails it will return -1
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAlarmInstance(instance: AlarmInstanceEntity): Long

    @Query(value =
    """ SELECT * FROM alarm_instances 
        WHERE id = :instanceId 
    """,
    )
    fun getAlarmInstance(instanceId: Int) : Flow<AlarmInstanceEntity?>

//    @Query(value =
//    """
//        SELECT * FROM alarm_instances
//        ORDER BY combinedMinutes ASC
//    """)
//    fun getAlarmEntitiesSortedByCombinedMinute(): Flow<List<AlarmEntity>>
}