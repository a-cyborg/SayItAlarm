package org.a_cyb.sayitalarm.core.alarm.util

import android.content.Context
import android.os.PowerManager
import android.os.PowerManager.WakeLock

object AlarmAlertWakeLock {
    private const val WAKELOCK_TAG = "sayit:alert_wake_lock"

    private var wakeLock: WakeLock? = null

    fun getPartialWakeLock(context: Context): WakeLock =
        (context.getSystemService(Context.POWER_SERVICE) as PowerManager)
            .newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, WAKELOCK_TAG)

    fun wakePersistentlyUntilRelease(context: Context) {
        if (wakeLock == null) {
            wakeLock = getPartialWakeLock(context)
            wakeLock!!.acquire()
        }
    }

    fun releaseWakeLock() {
        wakeLock?.release()
        wakeLock = null
    }
}