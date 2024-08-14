/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.alarm_service.core

import kotlin.properties.Delegates
import android.app.Notification
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.content.pm.ServiceInfo
import android.os.Binder
import android.os.Build
import android.os.IBinder
import org.a_cyb.sayitalarm.alarm_service.core.util.AudioVibeControllerContract
import org.a_cyb.sayitalarm.domain.alarm_service.AlarmServiceContract
import org.a_cyb.sayitalarm.entity.AlertType
import org.a_cyb.sayitalarm.entity.Ringtone
import org.a_cyb.sayitalarm.entity.Snooze
import org.koin.android.ext.android.inject

class AlarmService : AlarmServiceContract.AlarmService, Service() {

    private val binder: Binder = AlertServiceBinder()
    private val audioVibeController: AudioVibeControllerContract by inject()

    private var alarmId by Delegates.notNull<Long>()

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        alarmId = intent.getLongExtra(AlarmScheduler.EXTRA_ALARM_ID, 0L)

        val notification = AlarmNotification.getAlarmAlertNotification(this)
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

    override fun ringAlarm(ringtone: Ringtone, alertType: AlertType) {
        audioVibeController
            .startRinging(this, ringtone.ringtone, alertType.ordinal)
    }

    override fun startSayIt() {
        audioVibeController.stopRinging()
    }

    override fun startSnooze(snooze: Snooze) {
        audioVibeController.stopRinging()
        TODO()
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

    inner class AlertServiceBinder : Binder() {
        fun getAlarmId(): Long = alarmId
        fun getService(): AlarmServiceContract.AlarmService = this@AlarmService
    }

    companion object {
        const val FOREGROUND_ID = 300
        const val DEFAULT_ALERT_TYPE_ORDINAL = 2

        const val ACTION_EXIT_APP = "org.a_cyb.sayitalarm.ACTION_EXIT_APP"
    }
}
