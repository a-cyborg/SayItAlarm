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
import android.os.Binder
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import org.a_cyb.sayitalarm.domain.alarm_service.AlarmServiceContract

class AlarmService : AlarmServiceContract.AlarmService, Service() {

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        val notification = AlarmNotification.getAlarmAlertNotification(
            this,
            intent.extras ?: Bundle()
        )

        startForeground(notification)
        notifyNotification(notification)

        return START_NOT_STICKY
    }

    private fun startForeground(notification: Notification) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
                startForeground(
                    FOREGROUND_ID,
                    notification,
                    ServiceInfo.FOREGROUND_SERVICE_TYPE_SPECIAL_USE
                )
            } else {
                startForeground(
                    FOREGROUND_ID,
                    notification,
                    ServiceInfo.FOREGROUND_SERVICE_TYPE_NONE
                )
            }
        } else {
            startForeground(
                FOREGROUND_ID,
                notification
            )
        }
    }

    private fun notifyNotification(notification: Notification) {
        (getSystemService(NotificationManager::class.java) as NotificationManager)
            .notify(FOREGROUND_ID, notification)
    }

    private val binder: Binder = AlertServiceBinder()

    override fun onBind(intent: Intent?): IBinder {
        return binder
    }

    inner class AlertServiceBinder : Binder() {
        fun getService(): AlarmServiceContract.AlarmService =
            this@AlarmService
    }

    override fun startSayIt() {
        println("[***] ${this::class.java.simpleName}: [startSayIt] is called")
    }

    override fun startSnooze() {
        println("[***] ${this::class.java.simpleName}: [startSnooze] is called")
    }

    companion object {
        const val FOREGROUND_ID = 300
    }
}