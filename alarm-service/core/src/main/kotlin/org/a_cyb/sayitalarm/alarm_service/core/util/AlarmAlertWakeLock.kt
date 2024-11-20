/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.alarm_service.core.util

import android.content.Context
import android.os.PowerManager

class AlarmAlertWakeLock {
    private var wakeLock: PowerManager.WakeLock? = null

    fun acquireWakeLock(context: Context) {
        if (wakeLock == null) {
            wakeLock =
                (context.getSystemService(Context.POWER_SERVICE) as PowerManager)
                    .newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, WAKELOCK_TAG)
        }

        wakeLock!!.acquire(WAKELOCK_TIMEOUT)
    }

    fun releaseWakeLock() {
        wakeLock?.release()
        wakeLock = null
    }

    companion object {
        private const val WAKELOCK_TAG = "org.a_cyb.sayitalarm:AlarmAlertServiceWakeLockTag"
        private const val WAKELOCK_TIMEOUT = 60 * 1000L // 1 minute
    }
}
