/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.alarm_service

import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.content.pm.ServiceInfo
import android.os.Build
import android.os.Bundle
import android.os.IBinder

class AlertService : Service() {

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {

        val bundle = intent.extras ?: Bundle()

        val notification = AlarmNotification
            .getAlarmAlertNotification(this, bundle)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            startForeground(
                FOREGROUND_ID,
                notification,
                ServiceInfo.FOREGROUND_SERVICE_TYPE_MICROPHONE
            )
        } else {
            startForeground(
                FOREGROUND_ID,
                notification
            )
        }

        (getSystemService(NotificationManager::class.java) as NotificationManager)
            .notify(FOREGROUND_ID, notification)

        return START_NOT_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    companion object {
        const val FOREGROUND_ID = 300
    }
}