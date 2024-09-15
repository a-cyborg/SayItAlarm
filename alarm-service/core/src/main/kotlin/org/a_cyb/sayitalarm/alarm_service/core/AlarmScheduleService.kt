/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.alarm_service.core

import android.app.Notification
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.content.pm.ServiceInfo
import android.os.Build
import android.os.IBinder
import org.a_cyb.sayitalarm.domain.alarm_service.AlarmServiceContract
import org.koin.android.ext.android.inject

class AlarmScheduleService : Service() {

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val notification = AlarmNotification.getPostBootSchedulingNotification(this)
        val notificationManager = (getSystemService(NotificationManager::class.java) as NotificationManager)
        val alarmScheduler: AlarmServiceContract.AlarmScheduler by inject()

        startForeground(notification)
        notificationManager.notify(FOREGROUND_ID, notification)
        alarmScheduler.scheduleAlarms()
        stopService()

        return START_NOT_STICKY
    }

    private fun startForeground(notification: Notification) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
                startForeground(
                    FOREGROUND_ID,
                    notification,
                    ServiceInfo.FOREGROUND_SERVICE_TYPE_SPECIAL_USE,
                )
            } else {
                startForeground(
                    FOREGROUND_ID,
                    notification,
                    ServiceInfo.FOREGROUND_SERVICE_TYPE_NONE,
                )
            }
        } else {
            startForeground(
                FOREGROUND_ID,
                notification,
            )
        }
    }

    private fun stopService() {
        stopForeground(STOP_FOREGROUND_REMOVE)
        stopSelf()
    }

    override fun onBind(intent: Intent?): IBinder? = null

    companion object {
        private const val FOREGROUND_ID = 81200
    }
}
