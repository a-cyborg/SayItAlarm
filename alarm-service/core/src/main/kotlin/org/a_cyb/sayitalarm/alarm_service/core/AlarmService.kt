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
import org.a_cyb.sayitalarm.alarm_service.core.util.AudioVibeControllerContract
import org.a_cyb.sayitalarm.domain.alarm_service.AlarmServiceContract
import org.koin.android.ext.android.inject

class AlarmService : AlarmServiceContract.AlarmService, Service() {

    private val binder: Binder = AlertServiceBinder()
    private val audioVibeController: AudioVibeControllerContract by inject()

    private lateinit var alarmData: Bundle

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        alarmData = intent.extras ?: Bundle()
        val notification = AlarmNotification.getAlarmAlertNotification(this, alarmData)
        val notificationManager = (getSystemService(NotificationManager::class.java) as NotificationManager)

        startForeground(notification)
        notificationManager.notify(FOREGROUND_ID, notification)

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

    override fun onBind(intent: Intent?): IBinder {
        return binder
    }

    inner class AlertServiceBinder : Binder() {
        fun getService(): AlarmServiceContract.AlarmService = this@AlarmService
    }

    override fun ringAlarm() {
        val ringtone = alarmData.getString(AlarmScheduler.BUNDLE_KEY_RINGTONE_URI)
        val alertType = alarmData.getInt(AlarmScheduler.BUNDLE_KEY_ALERT_TYPE)

        audioVibeController.startRinging(this, ringtone, alertType)
    }

    override fun startSayIt() {
        audioVibeController.stopRinging()
    }

    override fun startSnooze() {
        audioVibeController.stopRinging()
    }

    override fun stopService() {
        stopForeground(STOP_FOREGROUND_REMOVE)
        stopSelf()
        sendBroadcast(
            Intent()
                .setPackage(packageName)
                .setAction(ACTION_EXIT_APP)
        )
    }

    companion object {
        const val FOREGROUND_ID = 300
        const val DEFAULT_ALERT_TYPE_ORDINAL = 2

        const val ACTION_EXIT_APP = "org.a_cyb.sayitalarm.ACTION_EXIT_APP"
    }
}
