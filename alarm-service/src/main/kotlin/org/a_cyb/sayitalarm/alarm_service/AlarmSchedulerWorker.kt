/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.alarm_service

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.withContext
import org.a_cyb.sayitalarm.alarm_service.AlarmScheduler.Companion.BUNDLE_KEY_ALARM_ID
import org.a_cyb.sayitalarm.alarm_service.AlarmScheduler.Companion.BUNDLE_KEY_ALERT_TYPE
import org.a_cyb.sayitalarm.alarm_service.AlarmScheduler.Companion.BUNDLE_KEY_IS_REPEAT
import org.a_cyb.sayitalarm.alarm_service.AlarmScheduler.Companion.BUNDLE_KEY_LABEL
import org.a_cyb.sayitalarm.alarm_service.AlarmScheduler.Companion.BUNDLE_KEY_RINGTONE_URI
import org.a_cyb.sayitalarm.alarm_service.AlarmScheduler.Companion.BUNDLE_KEY_SAYIT_SCRIPTS
import org.a_cyb.sayitalarm.alarm_service.AlarmScheduler.Companion.BUNDLE_KEY_SNOOZE
import org.a_cyb.sayitalarm.alarm_service.AlarmScheduler.Companion.BUNDLE_KEY_THEME
import org.a_cyb.sayitalarm.alarm_service.AlarmScheduler.Companion.BUNDLE_KEY_TIME_OUT
import org.a_cyb.sayitalarm.alarm_service.AlarmScheduler.Companion.SETTINGS_DEFAULT_SNOOZE
import org.a_cyb.sayitalarm.alarm_service.AlarmScheduler.Companion.SETTINGS_DEFAULT_THEME
import org.a_cyb.sayitalarm.alarm_service.AlarmScheduler.Companion.SETTINGS_DEFAULT_TIME_OUT
import org.a_cyb.sayitalarm.alarm_service.util.getNextAlarmTimeInMills
import org.a_cyb.sayitalarm.domain.repository.RepositoryContract.AlarmRepository
import org.a_cyb.sayitalarm.domain.repository.RepositoryContract.SettingsRepository
import org.a_cyb.sayitalarm.entity.Alarm
import org.a_cyb.sayitalarm.entity.Settings
import org.a_cyb.sayitalarm.entity.Snooze
import org.a_cyb.sayitalarm.entity.Theme
import org.a_cyb.sayitalarm.entity.TimeOut
import org.koin.core.qualifier.named
import org.koin.java.KoinJavaComponent.inject

internal class AlarmSchedulerWorker(
    context: Context,
    workerParameters: WorkerParameters,
) : CoroutineWorker(context, workerParameters) {

    override suspend fun doWork(): Result {
        val alarmManager = applicationContext
            .getSystemService(AlarmManager::class.java)

        val enabledAlarms = getAllEnabledAlarms()
        val settings = getSettingsOrDefault()

        enabledAlarms.forEach { alarm ->
            val receiverIntent: Intent =
                Intent(applicationContext, AlarmBroadcastReceiver::class.java)
                    .setAction(AlarmScheduler.ACTION_DELIVER_ALARM)
                    .setFlags(Intent.FLAG_RECEIVER_FOREGROUND)
                    .putExtras(getAlarmDataBundle(alarm, settings))

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

    private suspend fun getSettingsOrDefault(): Settings {
        val settingsRepository: SettingsRepository by inject(SettingsRepository::class.java)

        val settings: Settings = settingsRepository.getSettings()
            .firstOrNull()
            ?.getOrNull()
            ?: getDefaultSettings()

        return settings
    }

    private fun getDefaultSettings(): Settings =
        Settings(
            TimeOut(SETTINGS_DEFAULT_TIME_OUT),
            Snooze(SETTINGS_DEFAULT_SNOOZE),
            Theme.entries[SETTINGS_DEFAULT_THEME],
        )

    private fun getAlarmDataBundle(alarm: Alarm, settings: Settings): Bundle =
        Bundle().apply {
            putLong(BUNDLE_KEY_ALARM_ID, alarm.id)
            putBoolean(BUNDLE_KEY_IS_REPEAT, alarm.weeklyRepeat.weekdays.isNotEmpty())
            putString(BUNDLE_KEY_LABEL, alarm.label.label)
            putInt(BUNDLE_KEY_ALERT_TYPE, alarm.alertType.ordinal)
            putString(BUNDLE_KEY_RINGTONE_URI, alarm.ringtone.ringtone)
            putStringArray(BUNDLE_KEY_SAYIT_SCRIPTS, alarm.sayItScripts.scripts.toTypedArray())
            putInt(BUNDLE_KEY_TIME_OUT, settings.timeOut.timeOut)
            putInt(BUNDLE_KEY_SNOOZE, settings.snooze.snooze)
            putInt(BUNDLE_KEY_THEME, settings.theme.ordinal)
        }
}
