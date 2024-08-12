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
import android.os.Bundle
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import org.a_cyb.sayitalarm.alarm_service.core.AlarmScheduler.Companion.BUNDLE_KEY_ALARM_ID
import org.a_cyb.sayitalarm.alarm_service.core.AlarmScheduler.Companion.BUNDLE_KEY_ALERT_TYPE
import org.a_cyb.sayitalarm.alarm_service.core.AlarmScheduler.Companion.BUNDLE_KEY_RINGTONE_URI
import org.a_cyb.sayitalarm.alarm_service.core.util.getNextAlarmTimeInMills
import org.a_cyb.sayitalarm.domain.repository.RepositoryContract.AlarmRepository
import org.a_cyb.sayitalarm.entity.Alarm
import org.koin.core.qualifier.named
import org.koin.java.KoinJavaComponent.inject

internal class AlarmSchedulerWorker(
    context: Context,
    workerParameters: WorkerParameters,
) : CoroutineWorker(context, workerParameters) {

    override suspend fun doWork(): Result {
        val alarmManager = applicationContext.getSystemService(AlarmManager::class.java)
        val enabledAlarms = getAllEnabledAlarms()

        enabledAlarms.forEach { alarm ->
            val receiverIntent: Intent =
                Intent(applicationContext, AlarmBroadcastReceiver::class.java)
                    .setAction(AlarmScheduler.ACTION_DELIVER_ALARM)
                    .setFlags(Intent.FLAG_RECEIVER_FOREGROUND)
                    .putExtras(getAlarmDataBundle(alarm))

            val pendingIntentToCheckDuplicate = PendingIntent
                .getBroadcast(
                    applicationContext,
                    alarm.id.toInt(),
                    receiverIntent,
                    PendingIntent.FLAG_NO_CREATE or PendingIntent.FLAG_IMMUTABLE
                )

            if (pendingIntentToCheckDuplicate == null) {
                val nextAlarmTimeInMills =
                    getNextAlarmTimeInMills(alarm.hour, alarm.minute, alarm.weeklyRepeat)

                val alarmPendingIntent = PendingIntent
                    .getBroadcast(
                        applicationContext,
                        alarm.id.toInt(),
                        receiverIntent,
                        PendingIntent.FLAG_IMMUTABLE
                    )

                alarmManager.setAlarmClock(
                    AlarmManager.AlarmClockInfo(
                        nextAlarmTimeInMills,
                        alarmPendingIntent
                    ),
                    alarmPendingIntent
                )
            }

            if (isStopped) {
                return Result.failure()
            }
        }

        return Result.success()
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

    private fun getAlarmDataBundle(alarm: Alarm): Bundle =
        Bundle().apply {
            putLong(BUNDLE_KEY_ALARM_ID, alarm.id)
            putInt(BUNDLE_KEY_ALERT_TYPE, alarm.alertType.ordinal)
            putString(BUNDLE_KEY_RINGTONE_URI, alarm.ringtone.ringtone)
        }
}
