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