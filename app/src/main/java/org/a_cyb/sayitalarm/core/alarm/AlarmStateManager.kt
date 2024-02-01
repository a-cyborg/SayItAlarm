package org.a_cyb.sayitalarm.core.alarm

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.PowerManager
import android.util.Log
import org.a_cyb.sayitalarm.core.alarm.util.AlarmAlertWakeLock
import org.a_cyb.sayitalarm.core.alarm.util.SiaAsyncHandler
import org.a_cyb.sayitalarm.core.model.AlarmInstance
import org.a_cyb.sayitalarm.util.TAG

class AlarmStateManager: BroadcastReceiver() {

//    @Inject
//    lateinit var userDataRepo: UserDataRepository

    override fun onReceive(context: Context, intent: Intent?) {
//        Log.d(TAG, "onReceive: alarmId = ${intent?.getStringExtra("alarmId")}")
        Log.d(TAG, "onReceive: intent = ${intent}")

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
            /*
            if (intent?.getStringExtra("alarmId") == null) {
                Log.i(TAG, "handleIntent: received null intent $intent")
                return
            }
             */
            when (intent?.action) {
                CHANGE_STATE_ACTION -> {

                }
                FIRE_ALARM_ACTION ->  {
                    Log.d(TAG, "handleIntent: [***] call : showAlarmNotification() ")
                    AlarmNotification.showAlarmNotification(
                        context,
                        AlarmInstance(alarmState = 0)
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

/* Before
    @RequiresApi(Build.VERSION_CODES.O)
    private fun showNotification(context: Context) {
        val chanelId = "SayIt_channel"

        // Create Notification Channel
        val channel = NotificationChannel(
            chanelId,
            "SayItAlarm Channel",
            NotificationManager.IMPORTANCE_HIGH
        ).apply {
            description = "Say it alarm notification"
        }

        // Register the channel with the system
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)

        // FullScreenIntent
        val destinationIntent = Intent(context, AlarmActivity::class.java)
        val fullScreenIntent =
            PendingIntent.getActivity(
                context,
                0,
                destinationIntent,
                PendingIntent.
                FLAG_IMMUTABLE
            )

        val notificationBuilder = NotificationCompat.Builder(context, chanelId)
            .setSmallIcon(androidx.core.R.drawable.ic_call_answer)
            .setContentTitle("Say It Alarm🐒")
            .setContentText("Dev Mode")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setFullScreenIntent(fullScreenIntent, true)

        with(NotificationManagerCompat.from(context)) {
            if (ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                Log.d(TAG, "showNotification: [***] Permission asked ")
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return
            }
            notify(3, notificationBuilder.build())
        }
    }
 */