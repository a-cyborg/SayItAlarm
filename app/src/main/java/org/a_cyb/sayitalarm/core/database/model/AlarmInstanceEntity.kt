package org.a_cyb.sayitalarm.core.database.model

import androidx.core.net.toUri
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import org.a_cyb.sayitalarm.core.model.AlarmInstance

@Entity(tableName = "alarm_instances")
data class AlarmInstanceEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val year: Int,
    val month: Int,
    val day: Int,
    val hour: Int,
    val minute: Int,
    @ColumnInfo(defaultValue = "")
    val label: String?,
    @ColumnInfo(defaultValue = "0")
    val vibrate: Boolean,
    val ringtone: String,
    val associatedAlarmId: Int?,
    val alarmState: Int,
)

fun AlarmInstanceEntity.asExternalModel() = AlarmInstance(
    id = id,
    year = year,
    month = month,
    day = day,
    hour = hour,
    minute = minute,
    label = label,
    vibrate = vibrate,
    ringtone = ringtone?.toUri(),
    associatedAlarmId = associatedAlarmId,
    alarmState = alarmState,
)