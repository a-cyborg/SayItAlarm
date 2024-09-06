/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.alarm_service.core

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import org.a_cyb.sayitalarm.alarm_service.core.AlarmScheduler.Companion.SCHEDULER_WORKER_INPUT_DATA_ALARM_ID
import org.a_cyb.sayitalarm.alarm_service.core.AlarmScheduler.Companion.SCHEDULER_WORKER_INPUT_DATA_SNOOZE_MIN
import org.a_cyb.sayitalarm.alarm_service.core.util.getNextAlarmTimeInMills
import org.a_cyb.sayitalarm.alarm_service.core.util.getSnoozeTimeInMills
import org.a_cyb.sayitalarm.domain.repository.RepositoryContract.AlarmRepository
import org.a_cyb.sayitalarm.entity.Alarm
import org.koin.core.qualifier.named
import org.koin.java.KoinJavaComponent.inject

internal class AlarmSchedulerWorker(
    context: Context,
    workerParameters: WorkerParameters,
) : CoroutineWorker(context, workerParameters) {

    override suspend fun doWork(): Result {
        val workType = inputData.getInt(AlarmScheduler.SCHEDULER_WORKER_WORK_TYPE, 0)

        return when (workType) {
            AlarmScheduler.SCHEDULER_WORKER_WORK_SET_ALARM -> scheduleEnabledAlarms()
            AlarmScheduler.SCHEDULER_WORKER_WORK_SET_SNOOZE -> scheduleSnooze()
            AlarmScheduler.SCHEDULER_WORKER_WORK_CANCEL_ALARM -> cancelAlarm()
            else -> return Result.failure()
        }
    }

    private suspend fun scheduleEnabledAlarms(): Result {
        getAllEnabledAlarms().forEach { alarm ->
            val nextAlarmTimeInMills = getNextAlarmTimeInMills(alarm.hour, alarm.minute, alarm.weeklyRepeat)

            setAlarmClock(alarm.id, nextAlarmTimeInMills)
        }

        return Result.success()
    }

    private fun scheduleSnooze(): Result {
        val alarmId = inputData.getLong(SCHEDULER_WORKER_INPUT_DATA_ALARM_ID, 0)
        val snoozeMin = inputData.getInt(SCHEDULER_WORKER_INPUT_DATA_SNOOZE_MIN, 5)
        val snoozeAlarmTime = getSnoozeTimeInMills(snoozeMin)

        setAlarmClock(alarmId, snoozeAlarmTime)

        return Result.success()
    }

    private fun cancelAlarm(): Result {
        val alarmId = inputData.getLong(SCHEDULER_WORKER_INPUT_DATA_ALARM_ID, 0)
        val deliverIntent = getDeliverAlarmIntent(alarmId)
        val pendingIntent = getPendingIntent(false, alarmId.toInt(), deliverIntent)
        val alarmManager = applicationContext.getSystemService(AlarmManager::class.java)

        alarmManager.cancel(pendingIntent!!)
        pendingIntent.cancel()

        return Result.success()
    }

    private fun setAlarmClock(alarmId: Long, alarmTimeInMills: Long) {
        val alarmManager = applicationContext.getSystemService(AlarmManager::class.java)
        val deliverIntent = getDeliverAlarmIntent(alarmId)
        val pendingIntentToCheckDuplicate = getPendingIntent(isForDupeCheck = true, alarmId.toInt(), deliverIntent)

        if (pendingIntentToCheckDuplicate == null) {
            val alarmPendingIntent = getPendingIntent(false, alarmId.toInt(), deliverIntent)

            alarmManager.setAlarmClock(
                AlarmManager.AlarmClockInfo(alarmTimeInMills, alarmPendingIntent),
                alarmPendingIntent!!
            )
        }
    }

    private fun getDeliverAlarmIntent(alarmId: Long): Intent =
        Intent(applicationContext, AlarmBroadcastReceiver::class.java)
            .setAction(AlarmScheduler.INTENT_ACTION_DELIVER_ALARM)
            .setFlags(Intent.FLAG_RECEIVER_FOREGROUND)
            .putExtra(AlarmScheduler.INTENT_EXTRA_ALARM_ID, alarmId)

    private fun getPendingIntent(isForDupeCheck: Boolean, alarmId: Int, intent: Intent): PendingIntent? {
        val flags = when (isForDupeCheck) {
            true -> PendingIntent.FLAG_NO_CREATE or PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_ONE_SHOT
            false -> PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_ONE_SHOT
        }

        return PendingIntent.getBroadcast(applicationContext, alarmId, intent, flags)
    }

    private suspend fun getAllEnabledAlarms(): List<Alarm> {
        val alarmRepository: AlarmRepository by inject(AlarmRepository::class.java)
        val dispatcher: CoroutineDispatcher by inject(CoroutineDispatcher::class.java, named("io"))

        return withContext(dispatcher) {
            alarmRepository
                .getAllEnabledAlarm(this)
                .await()
        }
    }
}
