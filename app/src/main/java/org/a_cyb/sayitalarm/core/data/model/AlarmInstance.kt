package org.a_cyb.sayitalarm.core.data.model

import org.a_cyb.sayitalarm.core.database.model.AlarmInstanceEntity
import org.a_cyb.sayitalarm.core.model.AlarmInstance

fun AlarmInstance.asEntity() = AlarmInstanceEntity(
    id = id,
    year = year,
    month = month,
    day = day,
    hour = hour,
    minute = minute,
    label = label,
    vibrate = vibrate,
    ringtone = ringtone.toString(),
    associatedAlarmId = associatedAlarmId,
    alarmState = alarmState,
)