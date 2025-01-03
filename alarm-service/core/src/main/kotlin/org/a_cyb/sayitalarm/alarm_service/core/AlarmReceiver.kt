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

class AlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == INTENT_ACTION_DELIVER_ALARM) {
            val wakeLock = AlarmAlertWakeLock()
            wakeLock.acquireWakeLock(context)

            val alarmServiceIntent =
                Intent(context, AlarmService::class.java)
                    .putExtras(intent.extras ?: Bundle())

            ContextCompat.startForegroundService(context, alarmServiceIntent)
        } else {
            return
        }
    }

    companion object {
        const val INTENT_ACTION_DELIVER_ALARM = "org.a_cyb.sayitalarm.DELIVER_ALARM"
        const val INTENT_EXTRA_ALARM_ID = "org.a_cyb.sayitalarm.EXTRA_ALARM_ID"
    }
}
