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
import org.a_cyb.sayitalarm.domain.alarm_service.AlarmServiceContract
import org.a_cyb.sayitalarm.entity.Snooze

class AlarmScheduler(
    private val context: Context,
) : AlarmServiceContract.AlarmScheduler {

    override fun scheduleAlarms() {
        val data = Data.Builder()
            .putInt(SCHEDULER_WORKER_WORK_TYPE, SCHEDULER_WORKER_WORK_SET_ALARM)
            .build()

        enqueueWork(data)
    }

    override fun scheduleSnooze(alarmId: Long, snooze: Snooze) {
        val data = Data.Builder()
            .putInt(SCHEDULER_WORKER_WORK_TYPE, SCHEDULER_WORKER_WORK_SET_SNOOZE)
            .putLong(SCHEDULER_WORKER_INPUT_DATA_ALARM_ID, alarmId)
            .putInt(SCHEDULER_WORKER_INPUT_DATA_SNOOZE_MIN, snooze.snooze)
            .build()

        enqueueWork(data)
    }

    override fun cancelAlarm(alarmId: Long) {
        val data = Data.Builder()
            .putInt(SCHEDULER_WORKER_WORK_TYPE, SCHEDULER_WORKER_WORK_CANCEL_ALARM)
            .putLong(SCHEDULER_WORKER_INPUT_DATA_ALARM_ID, alarmId)
            .build()

        enqueueWork(data)
    }

    private fun enqueueWork(data: Data) {
        val request = OneTimeWorkRequestBuilder<AlarmSchedulerWorker>()
            .setInputData(data)
            .build()

        WorkManager
            .getInstance(context)
            .enqueue(request)
    }

    companion object {
        const val SCHEDULER_WORKER_WORK_TYPE = "org.a_cyb.sayitalarm.SCHEDULER_WORKER_WORK_TYPE"
        const val SCHEDULER_WORKER_INPUT_DATA_SNOOZE_MIN = "org.a_cyb.sayitalarm.SCHEDULER_WORKER_INPUT_DATA_SNOOZE_MIN"
        const val SCHEDULER_WORKER_INPUT_DATA_ALARM_ID = "org.a_cyb.sayitalarm.SCHEDULER_WORKER_INPUT_DATA_ALARM_ID"
        const val SCHEDULER_WORKER_WORK_SET_ALARM = 30145
        const val SCHEDULER_WORKER_WORK_SET_SNOOZE = 13946
        const val SCHEDULER_WORKER_WORK_CANCEL_ALARM = 13049

        const val INTENT_ACTION_DELIVER_ALARM = "org.a_cyb.sayitalarm.DELIVER_ALARM"
        const val INTENT_EXTRA_ALARM_ID = "org.a_cyb.sayitalarm.EXTRA_ALARM_ID"
    }
}
