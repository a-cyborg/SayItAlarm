package org.a_cyb.sayitalarm.core.alarm

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Binder
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import org.a_cyb.sayitalarm.core.alarm.util.AlarmAlertWakeLock
import org.a_cyb.sayitalarm.feature.alarm.AlarmActivity
import org.a_cyb.sayitalarm.util.TAG

class AlarmService : Service() {

    private val serviceBinder: Binder = Binder()

    override fun onBind(intent: Intent?): IBinder? {
        Log.d(TAG, "onBind: [***] called")
        return serviceBinder
    }

    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "onCreate: [***] called")
    }


    override fun onDestroy() {
        Log.d(TAG, "onDestroy: [***] Called")
        AlarmAlertWakeLock.releaseWakeLock()
        super.onDestroy()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d(TAG, "onStartCommand: [***] Called | devExtra = ${intent?.getStringExtra("alarmId")}")
        // Debug wake lock test (If the screen is turn on)
        AlarmAlertWakeLock.wakePersistentlyUntilRelease(this)
//        AlarmNotification.showAlertNotification(this, getRandomAlarm())

//        val destinationIntent = Intent(this, AlarmActivity::class.java).apply {
//            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
//        }
//        showNotification(this)

//        startActivity(destinationIntent)

//        stopSelf()

        return START_NOT_STICKY
    }

    companion object {
        const val ALARM_SNOOZE_ACTION = "org.a_cyb.sayitalarm.ALARM_SNOOZE"
        const val ALARM_DISMISS_ACTION = "org.a_cyb.sayitalarm.ALARM_DISMISS"
        const val ALARM_DONE_ACTION = "org.a_cyb.sayitalarm.ALARM_DONE"
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun showNotification(service: Service) {
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
        val notificationManager = service.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)

        // FullScreenIntent
        val destinationIntent = Intent(service, AlarmActivity::class.java)
        val fullScreenIntent =
            PendingIntent.getActivity(
                service,
                0,
                destinationIntent,
                PendingIntent.
                FLAG_IMMUTABLE
            )

        // Content intent
        val contentIntent =
            PendingIntent.getActivity(
                service,
                0,
                destinationIntent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )

        val notificationBuilder = NotificationCompat.Builder(service, chanelId)
            .setSmallIcon(androidx.core.R.drawable.ic_call_answer)
            .setContentTitle("Say It Alarm🐒")
            .setContentText("Dev Mode")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(contentIntent)
            .setFullScreenIntent(fullScreenIntent, true)

//        with(NotificationManagerCompat.from(service)) {
//            if (ActivityCompat.checkSelfPermission(
//                    service,
//                    Manifest.permission.POST_NOTIFICATIONS
//                ) != PackageManager.PERMISSION_GRANTED
//            ) {
//                Log.d(TAG, "showNotification: [***] Permission asked ")
//                // TODO: Consider calling
//                //    ActivityCompat#requestPermissions
//                // here to request the missing permissions, and then overriding
//                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//                //                                          int[] grantResults)
//                // to handle the case where the user grants the permission. See the documentation
//                // for ActivityCompat#requestPermissions for more details.
//                return
//            }
//            notify(3, notificationBuilder.build())
//        }
        service.startForeground(0 ,notificationBuilder.build())
    }
}
