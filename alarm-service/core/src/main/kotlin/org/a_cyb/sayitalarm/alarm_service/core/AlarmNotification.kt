/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.alarm_service.core

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

object AlarmNotification {

    /**
     * Alert notification
     */
    fun getAlarmAlertNotification(context: Context): Notification {
        createNotificationChannel(
            NotificationManagerCompat.from(context),
            context.getString(R.string.notification_alert_channel_name),
        )

        val activityIntent = Intent(context, AlarmActivity::class.java)
            .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NO_HISTORY)

        val pendingIntent = PendingIntent.getActivity(
            context,
            ALARM_ALERT_PENDING_INTENT_REQUEST_CODE,
            activityIntent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT,
        )

        val notificationBuilder = getAlertNotificationBuilder(
            context,
            context.getString(R.string.notification_content_title),
        )

        return notificationBuilder
            .setFullScreenIntent(pendingIntent, true)
            .build()
    }

    private fun getAlertNotificationBuilder(context: Context, contentTitle: String): NotificationCompat.Builder =
        NotificationCompat.Builder(context, ALARM_NOTIFICATION_CHANNEL_ID)
            .setContentTitle(contentTitle)
            .setSmallIcon(R.drawable.ic_notif_small)
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setCategory(NotificationCompat.CATEGORY_ALARM)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setAutoCancel(false)
            .setWhen(0)
            .setOnlyAlertOnce(true)

    /**
     * Scheduling notification.
     */
    fun getPostBootSchedulingNotification(context: Context): Notification {
        createNotificationChannel(
            NotificationManagerCompat.from(context),
            context.getString(R.string.notification_schedule_channel_name),
        )

        val notificationBuilder = NotificationCompat.Builder(context, ALARM_NOTIFICATION_CHANNEL_ID)
            .setContentTitle(context.getString(R.string.notification_content_title))
            .setContentText(context.getString(R.string.notification_content_text_schedule))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setCategory(NotificationCompat.CATEGORY_STATUS)
            .setSmallIcon(R.drawable.ic_notif_small)

        return notificationBuilder.build()
    }

    private fun createNotificationChannel(manager: NotificationManagerCompat, chanelName: String) {
        manager.createNotificationChannel(
            NotificationChannel(
                ALARM_NOTIFICATION_CHANNEL_ID,
                chanelName,
                NotificationManager.IMPORTANCE_HIGH,
            ),
        )
    }

    private const val ALARM_NOTIFICATION_CHANNEL_ID = "siaAlarmNotificationChannel"
    private const val ALARM_ALERT_PENDING_INTENT_REQUEST_CODE = 818
}
