/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.alarm_service.core

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
        const val EXTRA_ALARM_ID = "org.a_cyb.sayitalarm.EXTRA_ALARM_ID"
    }
}
