package org.a_cyb.sayitalarm.core.alarm

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import org.a_cyb.sayitalarm.R
import org.a_cyb.sayitalarm.core.model.AlarmInstance
import org.a_cyb.sayitalarm.feature.alarm.AlarmActivity
import org.a_cyb.sayitalarm.util.IsBuildVersionOOrLater
import org.a_cyb.sayitalarm.util.TAG
import org.a_cyb.sayitalarm.util.getFormattedClockTime

internal object AlarmNotification {
    /**
     * Notification channel containing all alarm notifications.
     */
    private const val ALARM_NOTIFICATION_CHANNEL_ID = "siaAlarmNotification"
    private const val ALARM_FIRING_NOTIFICATION_ID = 818

    @Synchronized
    fun showAlarmNotification(
        context: Context,
        instance: AlarmInstance,
    ) {
        Log.i(TAG, "[***] showAlertNotification: Display notification for alarm instance + [${instance}]")

        val stringResources: (Int) -> String = context::getString

        val builder: NotificationCompat.Builder = NotificationCompat
            .Builder(context, ALARM_NOTIFICATION_CHANNEL_ID)
                .setContentTitle(instance.label ?: stringResources(R.string.notification_content_title))
                .setContentText(getFormattedClockTime(context, instance.alarmTime))
                .setOngoing(true)
                .setAutoCancel(false)
                .setDefaults(NotificationCompat.DEFAULT_LIGHTS)
                .setWhen(0)
                .setCategory(NotificationCompat.CATEGORY_ALARM)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setLocalOnly(true)
                .setSmallIcon(androidx.core.R.drawable.notification_bg)
                .setFullScreenIntent(
                    PendingIntent.getActivity(
                        context,
                        ALARM_FIRING_NOTIFICATION_ID,
                        Intent(context, AlarmActivity::class.java).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK),
                        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE,
                    ),
                    true
                )

        val notificationManager = NotificationManagerCompat.from(context)

        if (IsBuildVersionOOrLater) {
            notificationManager.createNotificationChannel(
                NotificationChannel(
                    ALARM_NOTIFICATION_CHANNEL_ID,
                    context.getString(R.string.notification_alarm_firing_channel_name),
                    NotificationManager.IMPORTANCE_HIGH
                )
            )
        }

        cancelAlarmFiringNotification(context)
        notificationManager.notify(ALARM_FIRING_NOTIFICATION_ID, builder.build())
    }

    @Synchronized
    fun cancelAlarmFiringNotification(context: Context) {
        with(NotificationManagerCompat.from(context)) {
            cancel(ALARM_FIRING_NOTIFICATION_ID)
        }
    }
}


/* Backup 30.Jan working draft
    @Synchronized
    fun showAlarmNotification(context: Context, alarmId: String) {
        Log.i(TAG, "[***] showAlertNotification: Display notification for alarm instance + [${alarmId}]")

        val intentOfActivity = Intent(context, AlarmActivity::class.java)
            .apply { flags = Intent.FLAG_ACTIVITY_NEW_TASK }

        val alertPendingIntent = PendingIntent
            .getActivity(
                context,
                NOTIFICATION_RING_ALARM_REQUEST_CODE,
                intentOfActivity,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )

        val builder: NotificationCompat.Builder =
            NotificationCompat.Builder(context, ALARM_HIGH_PRIORITY_NOTIFICATION_CHANNEL_ID)
                .setContentTitle(context.getString(R.string.notification_high_priority_alert_title))
                .setContentText("DEBUG_CONTENT_TEXT")
                .setAutoCancel(false)
                .setDefaults(NotificationCompat.DEFAULT_LIGHTS)
                .setWhen(0)
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setCategory(NotificationCompat.CATEGORY_ALARM)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setLocalOnly(true)
                .setSmallIcon(androidx.core.R.drawable.ic_call_answer)
                .setFullScreenIntent(alertPendingIntent, true)
//                .setColor()
//                .setShowWhen(false)

        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        NotificationManagerCompat
            .from(context)
            .apply {
                if (IsBuildVersionOOrLater) {
                    createNotificationChannel(
                        NotificationChannel(
                            ALARM_HIGH_PRIORITY_NOTIFICATION_CHANNEL_ID,
                            context.getString(R.string.notification_alarm_alert_channel_name),
                            NotificationManager.IMPORTANCE_HIGH
                        )
                    )
                }
            }
            // Ring alarm with notification and we used static id to have only one notification
            // for this app but it might be changed in the future.
            .notify(NOTIFICATION_DEFAULT_NOTIFICATION_ID, builder.build())
    }

    // TODO: Clear Notification (If any has)

    private const val NOTIFICATION_DEFAULT_NOTIFICATION_ID = 340
    private const val NOTIFICATION_RING_ALARM_REQUEST_CODE = 343

    private const val ALARM_FIRING_NOTIFICATION_ID = Int.MAX_VALUE - 7

    /**
     * Notification channel containing all high priority notifications.
     */
    private const val ALARM_HIGH_PRIORITY_NOTIFICATION_CHANNEL_ID = "sia_notification_alert_channel_id"

 */



/* Before Order then 30.Jan.
//        val notificationManager = NotificationManagerCompat.from(context)
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            val channel = NotificationChannel(
//                NOTIFICATION_HIGH_PRIORITY_ALERT_CHANNEL_ID,
//                context.getString(R.string.notification_channel_name_alarm),
//                NotificationManager.IMPORTANCE_HIGH
//            )
//
//            notificationManager.createNotificationChannel(channel)
//
//        }
//
//        notificationManager.notify(NOTIFICATION_DEFAULT_NOTIFICATION_ID, builder.build())
 */