/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.alarm_service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat
import org.a_cyb.sayitalarm.alarm_service.util.AlarmAlertWakeLock

class AlarmBroadcastReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        when (intent.action) {
            AlarmScheduler.ACTION_DELIVER_ALARM -> {
                AlarmAlertWakeLock.acquireWakeLock(context)

                val alertServiceIntent =
                    Intent(context, AlertService::class.java)
                        .putExtras(intent.extras!!)

                ContextCompat.startForegroundService(
                    context,
                    alertServiceIntent
                )

                AlarmAlertWakeLock.releaseWakeLock()
            }

            Intent.ACTION_BOOT_COMPLETED, Intent.ACTION_TIMEZONE_CHANGED -> {
            }
        }
    }
}