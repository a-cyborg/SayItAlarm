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
import android.os.IBinder
import kotlin.properties.Delegates
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import org.a_cyb.sayitalarm.alarm_service.core.AlarmServiceContract.ServiceEvent
import org.a_cyb.sayitalarm.entity.AlertType
import org.a_cyb.sayitalarm.entity.Ringtone
import org.a_cyb.sayitalarm.util.audio_vibe_player.AudioVibePlayerContract
import org.koin.android.ext.android.inject

class AlarmService : Service() {

    private val serviceScope = CoroutineScope(Dispatchers.Unconfined + SupervisorJob())
    private val serviceEvent = MutableSharedFlow<ServiceEvent>()

    private var alarmId by Delegates.notNull<Long>()

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        alarmId = intent.getLongExtra(AlarmReceiver.INTENT_EXTRA_ALARM_ID, 0L)

        val notification = getFullScreenNotificationForAlarm(this)
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
                    ServiceInfo.FOREGROUND_SERVICE_TYPE_SPECIAL_USE,
                )
            } else {
                @Suppress("DEPRECATION")
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

    override fun onBind(intent: Intent?): IBinder {
        return AlarmServiceBinder()
    }

    override fun onDestroy() {
        super.onDestroy()
        serviceScope.cancel()
    }

    inner class AlarmServiceBinder : AlarmServiceContract, Binder() {
        fun getAlarmId(): Long = this@AlarmService.alarmId

        override val serviceEvent: SharedFlow<ServiceEvent> =
            this@AlarmService.serviceEvent.asSharedFlow()

        override fun startRinging(ringtone: Ringtone, alertType: AlertType) {
            this@AlarmService.startRinging(ringtone, alertType)
        }

        override fun stopRinging() {
            this@AlarmService.stopRinging()
        }

        override fun stopService() {
            this@AlarmService.stopServiceAndActivity()
        }
    }

    private val audioVibeController: AudioVibePlayerContract by inject()

    private fun startRinging(ringtone: Ringtone, alertType: AlertType) {
        try {
            audioVibeController
                .startRinging(this, ringtone.ringtone, alertType.ordinal)
        } catch (exception: Exception) {
            serviceScope.launch {
                serviceEvent
                    .emit(ServiceEvent.AudioVibePlayerError(exception))
            }
        }
    }

    private fun stopRinging() {
        audioVibeController.stopRinging()
    }

    private fun stopServiceAndActivity() {
        stopRinging()
        stopForeground(STOP_FOREGROUND_REMOVE)
        stopSelf()
        sendBroadcast(
            Intent()
                .setPackage(packageName)
                .setAction(ACTION_EXIT_APP),
        )
    }

    companion object {
        private const val FOREGROUND_ID = 300

        const val ACTION_EXIT_APP = "org.a_cyb.sayitalarm.ACTION_EXIT_APP"
    }
}
