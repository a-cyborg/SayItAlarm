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
import org.a_cyb.sayitalarm.alarm_service.core.util.AudioVibeController
import org.a_cyb.sayitalarm.domain.alarm_service.AlarmServiceContract

class AlarmService : AlarmServiceContract.AlarmService, Service() {

    private val binder: Binder = AlertServiceBinder()

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        val alarmData = intent.extras ?: Bundle()
        val notification = AlarmNotification.getAlarmAlertNotification(this, alarmData)

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

    override fun startSayIt() {
        AudioVibeController.stopRinging()
    }

    override fun startSnooze() {}

    override fun onBind(intent: Intent?): IBinder {
        ringAlarm(intent)

        return binder
    }

    private fun ringAlarm(intent: Intent?) {
        val ringtone = intent?.getStringExtra(SERVICE_BIND_EXTRA_RINGTONE_URI)
        val alertTypeOrdinal = intent
            ?.getIntExtra(SERVICE_BIND_EXTRA_ALERT_TYPE, DEFAULT_ALERT_TYPE_ORDINAL)

        AudioVibeController.startRinging(this, ringtone, alertTypeOrdinal)
    }

    inner class AlertServiceBinder : Binder() {
        fun getService(): AlarmServiceContract.AlarmService =
            this@AlarmService
    }

    companion object {
        const val FOREGROUND_ID = 300

        const val SERVICE_BIND_EXTRA_RINGTONE_URI = "org.a_cyb.sayitalarm.SERVICE_BIND_EXTRA_RINGTONE_URI"
        const val SERVICE_BIND_EXTRA_ALERT_TYPE = "org.a_cyb.sayitalarm.SERVICE_BIND_EXTRA_ALERT_TYPE"

        const val DEFAULT_ALERT_TYPE_ORDINAL = 1
    }
}

