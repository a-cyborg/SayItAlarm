/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.alarm_service.scheduler

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import org.a_cyb.sayitalarm.domain.alarm_service.AlarmSchedulerContract
import org.koin.java.KoinJavaComponent.get

class BootAndTimeZoneReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        when (intent.action) {
            Intent.ACTION_BOOT_COMPLETED, Intent.ACTION_LOCKED_BOOT_COMPLETED -> {
                get<AlarmSchedulerContract>(AlarmSchedulerContract::class.java)
                    .scheduleAlarms()
            }

            else -> return
        }
    }
}
