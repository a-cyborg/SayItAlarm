package org.a_cyb.sayitalarm.core.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import org.a_cyb.sayitalarm.core.alarm.AlarmOptionalFeature
import org.a_cyb.sayitalarm.core.alarm.AlarmTerminator
import org.a_cyb.sayitalarm.core.model.Alarm
import org.a_cyb.sayitalarm.core.model.CombinedMinutes
import org.a_cyb.sayitalarm.core.model.WeeklyRepeat

@Entity(tableName = "alarms")
data class AlarmEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val combinedMinutes: Int,
    @ColumnInfo(defaultValue = "1")
    val enabled: Boolean,
    val weeklyRepeat: WeeklyRepeat,
    @ColumnInfo(defaultValue = "")
    val label: String,
    @ColumnInfo(defaultValue = "0")
    val vibrate: Boolean,
    @ColumnInfo(defaultValue = "")
    val ringtone: String,
    val alarmTerminator: AlarmTerminator,
    val alarmOptionalFeature: AlarmOptionalFeature,
)

fun AlarmEntity.asExternalModel() = Alarm (
    id = id,
    combinedMinutes = CombinedMinutes(combinedMinutes),
    enabled = enabled,
    weeklyRepeat = weeklyRepeat,
    label = label,
    vibrate = vibrate,
    ringtone = ringtone,
    alarmTerminator = alarmTerminator,
    alarmOptionalFeature = alarmOptionalFeature,
)