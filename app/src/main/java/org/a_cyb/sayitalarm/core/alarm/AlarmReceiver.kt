package org.a_cyb.sayitalarm.core.alarm

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.PowerManager
import android.util.Log
import org.a_cyb.sayitalarm.core.alarm.util.AlarmAlertWakeLock
import org.a_cyb.sayitalarm.core.alarm.util.SiaAsyncHandler
import org.a_cyb.sayitalarm.core.model.Alarm
import org.a_cyb.sayitalarm.core.model.AlarmInstance
import org.a_cyb.sayitalarm.util.TAG

class AlarmReceiver: BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent?) {
        Log.d(TAG, "onReceive: intent = $intent")

        val pendingResult = goAsync()
        val wakeLock = AlarmAlertWakeLock
            .getPartialWakeLock(context)
            .apply(PowerManager.WakeLock::acquire)

        SiaAsyncHandler.post {
            handleIntent(context, intent)
            pendingResult.finish()
            wakeLock.release()
        }
    }

    companion object {
        fun handleIntent(context: Context, intent: Intent?) {
            val alarmId = intent?.getStringExtra(ALARM_ID_EXTRA)
            Log.i(TAG, "handleIntent: Received broadcast intent from the system with alarmId = $alarmId")

            when (intent?.action) {
                CHANGE_STATE_ACTION -> {

                }
                FIRE_ALARM_ACTION ->  {
                    AlarmNotification.showAlarmNotification(
                        context,
                        AlarmInstance(associatedAlarmId = Alarm.INVALID_ID, alarmState = 0) // DEV MODE
                    )
                }
            }
        }

        fun changeSate(instanceId: Int) {
            Log.d(TAG, "changeSate: [***] Will change State of instance $instanceId")
        }

        const val CHANGE_STATE_ACTION = "sia_alarm_change_sate"
    }
}