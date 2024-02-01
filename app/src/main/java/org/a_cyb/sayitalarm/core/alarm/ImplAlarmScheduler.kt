package org.a_cyb.sayitalarm.core.alarm

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import org.a_cyb.sayitalarm.core.model.Alarm
import org.a_cyb.sayitalarm.util.TAG

const val ALARM_ID_EXTRA = "alarmId"
const val FIRE_ALARM_ACTION = "org.a_cyb.sayitalarm.RING_ALARM"

object ImplAlarmScheduler : AlarmScheduler {

    @SuppressLint("ScheduleExactAlarm")
    override fun setAlarm(context: Context, alarm: Alarm) {
        Log.i(TAG, "setAlarm: Setting a new alarm for [${alarm.id}]")
        val alarmManager = context.getSystemService(AlarmManager::class.java)

        val now = System.currentTimeMillis()

        val intent = Intent(context, AlarmStateManager::class.java)
            .apply {
                action = FIRE_ALARM_ACTION
                putExtra("alarmId", alarm.id)
            }

        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            now + 5_000,
            PendingIntent.getBroadcast(
                context,
                alarm.hashCode(),
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
        )
    }

    @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
    override fun cancelAlarm(context: Context, alarm: Alarm) {
        context.getSystemService(AlarmManager::class.java)
            .cancelAll()
    }
}

/*
class ImplAlarmScheduler2(
    @ApplicationContext private val context: Context,
) : AlarmScheduler {

    private val alarmManager = context.getSystemService(AlarmManager::class.java)

    @SuppressLint("MissingPermission", "ScheduleExactAlarm")
    override fun setAlarm(alarm: Alarm) {
        Log.i(TAG, "setAlarm: Setting a new alarm for [${alarm.id}]")

        val now = System.currentTimeMillis()

        val intent = Intent(context, AlarmReceiver::class.java)
            .apply {
                putExtra("alarmId", "Extra_alarm_id")
            }

        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            now + 5000,

            PendingIntent.getBroadcast(
                context,
                alarm.hashCode(),
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
        )
    }

    override fun cancelAlarm(alarm: Alarm) {
        alarmManager.cancel(
            PendingIntent.getBroadcast(
                context,
                alarm.hashCode(),
                Intent(),
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
        )
    }

    companion object {
        const val ALARM_ALERT_ACTION = "org.a_cyb.sayitalarm.alarm_firing"
    }
}

 */



/* Before try to open the service.
//        val intent = Intent(context, AlarmService::class.java).apply {
//            putExtra("alarmId", "Alarm Id test id = ${alarm.id}")
//        }

//            PendingIntent.getService(
//                context,
//                alarm.hashCode(),
//                intent,
//                PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
//            )
 */