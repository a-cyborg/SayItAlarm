/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.alarm_service.core

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.core.content.ContextCompat
import org.a_cyb.sayitalarm.alarm_service.core.util.AlarmAlertWakeLock

class AlarmBroadcastReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        when (intent.action) {
            AlarmScheduler.ACTION_DELIVER_ALARM -> {
                AlarmAlertWakeLock.acquireWakeLock(context)

                val alertServiceIntent =
                    Intent(context, AlertService::class.java)
                        .putExtras(intent.extras ?: Bundle())

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