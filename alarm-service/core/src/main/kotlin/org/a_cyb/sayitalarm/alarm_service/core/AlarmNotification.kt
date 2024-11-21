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

private const val ALARM_NOTIFICATION_CHANNEL_ID = "siaAlarmNotificationChannel"
private const val ALARM_ALERT_PENDING_INTENT_REQUEST_CODE = 818

fun getFullScreenNotificationForAlarm(context: Context): Notification {
    NotificationManagerCompat.from(context)
        .createNotificationChannel(
            NotificationChannel(
                ALARM_NOTIFICATION_CHANNEL_ID,
                context.getString(R.string.notification_alert_channel_name),
                NotificationManager.IMPORTANCE_HIGH,
            ),
        )

    val activityIntent = Intent(context, AlarmActivity::class.java)
        .setFlags(
            Intent.FLAG_ACTIVITY_NEW_TASK
                or Intent.FLAG_ACTIVITY_CLEAR_TASK
                or Intent.FLAG_ACTIVITY_NO_HISTORY,
        )

    val pendingIntent = PendingIntent.getActivity(
        context,
        ALARM_ALERT_PENDING_INTENT_REQUEST_CODE,
        activityIntent,
        PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT,
    )

    return getAlarmNotificationBuilder(context, context.getString(R.string.notification_content_title))
        .setFullScreenIntent(pendingIntent, true)
        .build()
}

private fun getAlarmNotificationBuilder(context: Context, contentTitle: String): NotificationCompat.Builder =
    NotificationCompat.Builder(context, ALARM_NOTIFICATION_CHANNEL_ID)
        .setContentTitle(contentTitle)
        .setSmallIcon(R.drawable.ic_notif_small)
        .setPriority(NotificationCompat.PRIORITY_MAX)
        .setCategory(NotificationCompat.CATEGORY_ALARM)
        .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
        .setAutoCancel(false)
        .setWhen(0)
        .setOnlyAlertOnce(true)
