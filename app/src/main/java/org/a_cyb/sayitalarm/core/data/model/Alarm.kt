package org.a_cyb.sayitalarm.core.data.model

import org.a_cyb.sayitalarm.core.database.model.AlarmEntity
import org.a_cyb.sayitalarm.core.model.Alarm

fun Alarm.asEntity() = AlarmEntity(
    id = id.toInt(),
    combinedMinutes = combinedMinutes.value,
    enabled = enabled,
    weeklyRepeat = weeklyRepeat,
    label = label,
    vibrate = vibrate,
    ringtone = ringtone.toString(),
    alarmTerminator = alarmTerminator,
    alarmOptionalFeature = alarmOptionalFeature,
)