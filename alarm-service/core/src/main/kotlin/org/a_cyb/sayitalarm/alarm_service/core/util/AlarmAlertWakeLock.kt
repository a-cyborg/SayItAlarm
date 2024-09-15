/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.alarm_service.core.util

import android.content.Context
import android.os.PowerManager

object AlarmAlertWakeLock {

    private const val WAKELOCK_TAG = "org.a_cyb.sayitalarm:AlarmAlertServiceWakeLockTag"
    private const val WAKELOCK_TIMEOUT = 60 * 1000L // 1 minute

    private var wakeLock: PowerManager.WakeLock? = null

    fun acquireWakeLock(context: Context) {
        if (wakeLock == null) {
            wakeLock = getPartialWakeLock(context)
        }

        wakeLock!!.acquire(WAKELOCK_TIMEOUT)
    }

    private fun getPartialWakeLock(context: Context): PowerManager.WakeLock =
        (context.getSystemService(Context.POWER_SERVICE) as PowerManager)
            .newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, WAKELOCK_TAG)

    fun releaseWakeLock() {
        wakeLock?.release()
        wakeLock = null
    }
}
