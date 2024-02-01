package org.a_cyb.sayitalarm.core.alarm

import android.content.Context
import org.a_cyb.sayitalarm.core.model.Alarm

interface AlarmScheduler {
    fun setAlarm(context: Context, alarm: Alarm)
    fun cancelAlarm(context: Context, alarm: Alarm)
}