/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.alarm_service

import android.content.Context
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import kotlinx.coroutines.CoroutineScope
import org.a_cyb.sayitalarm.domain.alarm_service.AlarmServiceContract

class AlarmScheduler(
    private val context: Context,
) : AlarmServiceContract.AlarmScheduler {

    override suspend fun setAlarm(scope: CoroutineScope) {
        // TODO: Check permission
        val schedulerRequest = OneTimeWorkRequest
            .from(AlarmSchedulerWorker::class.java)

        WorkManager
            .getInstance(context)
            .enqueue(schedulerRequest)
    }

    override suspend fun cancelAlarm(id: Long, scope: CoroutineScope) {
    }

    companion object {
        const val ACTION_DELIVER_ALARM = "org.a_cyb.sayitalarm.DELIVER_ALARM"

        const val BUNDLE_KEY_ALARM_ID = "org.a_cyb.sayitalarm.BUNDLE_ALARM_ID"
        const val BUNDLE_KEY_SAYIT_SCRIPTS = "org.a_cyb.sayitalarm.BUNDLE_SAYIT_SCRIPTS"
        const val BUNDLE_KEY_IS_REPEAT = "org.a_cyb.sayitalarm.BUNDLE_IS_REPEAT"
        const val BUNDLE_KEY_LABEL = "org.a_cyb.sayitalarm.BUNDLE_LABEL"
        const val BUNDLE_KEY_ALERT_TYPE = "org.a_cyb.sayitalarm.BUNDLE_ALERT_TYPE"
        const val BUNDLE_KEY_RINGTONE_URI = "org.a_cyb.sayitalarm.BUNDLE_RINGTONE_URI"
        const val BUNDLE_KEY_TIME_OUT = "org.a_cyb.sayitalarm.BUNDLE_TIME_OUT"
        const val BUNDLE_KEY_SNOOZE = "org.a_cyb.sayitalarm.BUNDLE_SNOOZE"
        const val BUNDLE_KEY_THEME = "org.a_cyb.sayitalarm.BUNDLE_THEME"

        // This should be removed after implementing default settings
        const val SETTINGS_DEFAULT_TIME_OUT = 180
        const val SETTINGS_DEFAULT_SNOOZE = 15
        const val SETTINGS_DEFAULT_THEME = 0
    }
}
