/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.alarm_service.core

import android.content.Context
import androidx.work.Data
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import kotlinx.coroutines.CoroutineScope
import org.a_cyb.sayitalarm.domain.alarm_service.AlarmServiceContract
import org.a_cyb.sayitalarm.entity.Snooze

class AlarmScheduler(
    private val context: Context,
) : AlarmServiceContract.AlarmScheduler {

    override suspend fun scheduleAlarms(scope: CoroutineScope) {
        val data = Data.Builder()
            .putInt(SCHEDULER_WORKER_WORK_TYPE, SCHEDULER_WORKER_WORK_SET_ALARM)
            .build()

        val request = OneTimeWorkRequestBuilder<AlarmSchedulerWorker>()
            .setInputData(data)
            .build()

        WorkManager
            .getInstance(context)
            .enqueue(request)
    }

    override fun scheduleSnooze(alarmId: Long, snooze: Snooze) {
        val data = Data.Builder()
            .putInt(SCHEDULER_WORKER_WORK_TYPE, SCHEDULER_WORKER_WORK_SET_SNOOZE)
            .putLong(SCHEDULER_WORKER_INPUT_DATA_ALARM_ID, alarmId)
            .putInt(SCHEDULER_WORKER_INPUT_DATA_SNOOZE_MIN, snooze.snooze)
            .build()

        val request = OneTimeWorkRequestBuilder<AlarmSchedulerWorker>()
            .setInputData(data)
            .build()

        WorkManager
            .getInstance(context)
            .enqueue(request)
    }

    override suspend fun cancelAlarm(id: Long, scope: CoroutineScope) {
    }

    companion object {
        const val SCHEDULER_WORKER_WORK_TYPE = "org.a_cyb.sayitalarm.SCHEDULER_WORKER_WORK_TYPE"
        const val SCHEDULER_WORKER_INPUT_DATA_SNOOZE_MIN = "org.a_cyb.sayitalarm.SCHEDULER_WORKER_INPUT_DATA_SNOOZE_MIN"
        const val SCHEDULER_WORKER_INPUT_DATA_ALARM_ID = "org.a_cyb.sayitalarm.SCHEDULER_WORKER_INPUT_DATA_ALARM_ID"
        const val SCHEDULER_WORKER_WORK_SET_ALARM = 30145
        const val SCHEDULER_WORKER_WORK_SET_SNOOZE = 13946

        const val ACTION_DELIVER_ALARM = "org.a_cyb.sayitalarm.DELIVER_ALARM"
        const val EXTRA_ALARM_ID = "org.a_cyb.sayitalarm.EXTRA_ALARM_ID"
    }
}
