/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.alarm_service

import android.app.Service
import android.content.Intent
import android.os.IBinder

class AlarmRingService : Service() {
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {

        return START_NOT_STICKY
    }

    companion object {
        private const val ALARM_NOTIFICATION_CHANNEL_ID = "siaAlarmNotificationChannel"
        private const val ALARM_RINGINT_NOTIFICATION_ID = 818
    }

}