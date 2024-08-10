/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.alarm_service.core

import kotlin.test.assertFalse
import kotlin.test.assertTrue
import android.Manifest
import android.app.ActivityManager
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.test.core.app.ApplicationProvider
import androidx.test.filters.SmallTest
import androidx.test.rule.GrantPermissionRule
import androidx.test.rule.ServiceTestRule
import androidx.test.runner.AndroidJUnit4
import org.a_cyb.sayitalarm.domain.alarm_service.AlarmServiceContract
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@SmallTest
class AlarmServiceSpec {
    private lateinit var context: Context
    private lateinit var serviceIntent: Intent

    @get:Rule
    val serviceTestRule = ServiceTestRule()

    @get:Rule
    val mRuntimePermissionRule: GrantPermissionRule = GrantPermissionRule.grant(
        Manifest.permission.POST_NOTIFICATIONS,
        Manifest.permission.USE_FULL_SCREEN_INTENT
    )

    @Before
    fun setup() {
        context = ApplicationProvider.getApplicationContext()
        serviceIntent = Intent(context, AlarmService::class.java)
    }

    @Test
    fun alarmService_StartsForeground() {
        // When
        serviceTestRule.startService(serviceIntent)

        // Then
        assertTrue(isRunningForeground(context))
    }

    private fun isRunningForeground(context: Context): Boolean =
        context
            .getSystemService(ActivityManager::class.java)
            .getRunningServices(Int.MAX_VALUE)
            .any { it.foreground && it.service.className == AlarmService::class.qualifiedName }

    @Test
    fun alarmService_DisplaysNotification() {
        // When
        serviceTestRule.startService(serviceIntent)

        // Then
        assertTrue(notificationIsDisplayed(context))
    }

    private fun notificationIsDisplayed(context: Context) =
        context
            .getSystemService(NotificationManager::class.java)
            .activeNotifications
            .any {
                it.id == 300
                    && it.notification.channelId == "siaAlarmNotificationChannel"
                    && it.notification.category == NotificationCompat.CATEGORY_ALARM
                    && it.packageName == context.packageName
            }

    @Test
    fun alarmService_onBind_Returns_AlertServiceBinder() {
        // When
        val binder = serviceTestRule.bindService(serviceIntent)
        val service = (binder as? AlarmService.AlertServiceBinder)?.getService()

        // Then
        assertTrue { binder is AlarmService.AlertServiceBinder }
        assertTrue { service is AlarmServiceContract.AlarmService }
    }

    @Test
    fun alarmService_stopService_stopForegroundService_dismissesNotification() {
        // Given
        val binder = serviceTestRule.bindService(serviceIntent)
        val service = (binder as? AlarmService.AlertServiceBinder)?.getService()

        // When
        service?.stopService()

        // Then
        assertFalse(notificationIsDisplayed(context))
        assertFalse(isRunningForeground(context))
    }
}
