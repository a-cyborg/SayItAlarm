/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.implementations.permission_manager

import android.Manifest
import android.app.AlarmManager
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager.PERMISSION_DENIED
import android.os.Build
import android.os.PowerManager

class PermissionChecker(
    private val context: Context,
) : PermissionManagerContract.PermissionCheckerContract {

    override fun getMissingEssentialPermission(): List<String> {
        val missingPermissions = mutableListOf<String>()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val alarmManager = context.getSystemService(AlarmManager::class.java)

            if (!alarmManager.canScheduleExactAlarms()) {
                missingPermissions.add(Manifest.permission.SCHEDULE_EXACT_ALARM)
            }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val postNotificationPermission = Manifest.permission.POST_NOTIFICATIONS

            if (context.checkSelfPermission(postNotificationPermission) == PERMISSION_DENIED) {
                missingPermissions.add(postNotificationPermission)
            }
        }

        if (context.checkSelfPermission(Manifest.permission.RECORD_AUDIO) == PERMISSION_DENIED) {
            missingPermissions.add(Manifest.permission.RECORD_AUDIO)
        }

        return missingPermissions
    }

    override fun getMissingPermission(): List<String> {
        val missingPermissions = mutableListOf<String>()

        val powerManager = context.getSystemService(PowerManager::class.java)

        if (!powerManager.isIgnoringBatteryOptimizations(context.packageName)) {
            missingPermissions.add(Manifest.permission.REQUEST_IGNORE_BATTERY_OPTIMIZATIONS)
        }

        val notificationManager = context.getSystemService(NotificationManager::class.java)

        if (!notificationManager.isNotificationPolicyAccessGranted) {
            missingPermissions.add(Manifest.permission.ACCESS_NOTIFICATION_POLICY)
        }

        val audioFilePermission =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                Manifest.permission.READ_MEDIA_AUDIO
            } else {
                Manifest.permission.READ_EXTERNAL_STORAGE
            }

        if (context.checkSelfPermission(audioFilePermission) == PERMISSION_DENIED) {
            missingPermissions.add(audioFilePermission)
        }

        return missingPermissions
    }
}
