package org.a_cyb.sayitalarm.core.alarm

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import org.a_cyb.sayitalarm.core.model.AlarmInstance
import org.a_cyb.sayitalarm.util.TAG

interface AlarmScheduler {
    fun setAlarm(context: Context, instance: AlarmInstance)
    fun cancelAlarm(context: Context, instance: AlarmInstance)
}

const val FIRE_ALARM_ACTION = "org.a_cyb.sayitalarm.FIRE_ALARM"

const val ALARM_ID_EXTRA = "org.a_cyb.sayitalarm.ALARM_ID_EXTRA"
object AlarmManagerScheduler : AlarmScheduler {

    override fun setAlarm(context: Context, instance: AlarmInstance) {
        Log.i(TAG, "setAlarm: Set a new alarm for the instance:[$instance]")

        val intent = Intent(context, AlarmReceiver::class.java).apply {
                action = FIRE_ALARM_ACTION
                putExtra(ALARM_ID_EXTRA, instance.associatedAlarmId)
        }
        val pendingIntent = PendingIntent.getBroadcast(
                context,
                instance.hashCode(),
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        context.getSystemService(AlarmManager::class.java)
            .setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                instance.alarmTime.timeInMillis,
                pendingIntent
            )
    }

    override fun cancelAlarm(context: Context, instance: AlarmInstance) {
        val intent = Intent(context, AlarmReceiver::class.java).apply {
            putExtra(ALARM_ID_EXTRA, instance.associatedAlarmId)
        }
        val pendingIntent = PendingIntent.getBroadcast(
                context,
                instance.hashCode(),
                intent,
                PendingIntent.FLAG_NO_CREATE or PendingIntent.FLAG_IMMUTABLE
        )

        context.getSystemService(AlarmManager::class.java)
            .cancel(pendingIntent)

        pendingIntent.cancel()
    }
}