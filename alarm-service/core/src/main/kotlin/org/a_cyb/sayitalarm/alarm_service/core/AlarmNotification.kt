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
import android.os.Build
import android.os.Bundle
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import org.a_cyb.sayitalarm.alarm_service.R
import org.a_cyb.sayitalarm.alarm_service.ui.AlarmActivity

object AlarmNotification {

    fun getAlarmAlertNotification(context: Context, alarmBundle: Bundle): Notification {
        createNotificationChannel(
            NotificationManagerCompat.from(context),
            context.getString(R.string.notification_alert_channel_name)
        )

        val activityIntent = Intent(context, AlarmActivity::class.java)
            .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NO_HISTORY)
            .putExtras(alarmBundle)

        val pendingIntent = PendingIntent.getActivity(
            context,
            ALARM_RINGING_NOTIFICATION_ID,
            activityIntent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val notificationBuilder = getNotificationBuilder(
            context,
            context.getString(R.string.notification_content_title)
        )

        return notificationBuilder
            .setFullScreenIntent(pendingIntent, true)
            .build()
    }

    private fun createNotificationChannel(
        manager: NotificationManagerCompat,
        chanelName: String,
    ) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            manager.createNotificationChannel(
                NotificationChannel(
                    ALARM_NOTIFICATION_CHANNEL_ID,
                    chanelName,
                    NotificationManager.IMPORTANCE_HIGH
                )
            )
        }
    }

    private fun getNotificationBuilder(context: Context, contentTitle: String): NotificationCompat.Builder =
        NotificationCompat.Builder(context, ALARM_NOTIFICATION_CHANNEL_ID)
            .setContentTitle(contentTitle)
            .setSmallIcon(R.drawable.ic_notif_small)
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setCategory(NotificationCompat.CATEGORY_ALARM)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            // .setOngoing(true)
            .setAutoCancel(false)
            .setWhen(0)
            .setOnlyAlertOnce(true)

    private const val ALARM_NOTIFICATION_CHANNEL_ID = "siaAlarmNotificationChannel"
    private const val ALARM_RINGING_NOTIFICATION_ID = 818
}
