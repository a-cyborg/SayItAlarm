/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.alarm_service.core

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
class AlertServiceSpec {
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
        serviceIntent = Intent(context, AlertService::class.java)
    }

    @Test
    fun alertService_StartsForeground() {
        // When
        serviceTestRule.startService(serviceIntent)

        // Then
        assertTrue(isRunningForeground(context))
    }

    private fun isRunningForeground(context: Context): Boolean =
        context
            .getSystemService(ActivityManager::class.java)
            .getRunningServices(Int.MAX_VALUE)
            .any { it.foreground && it.service.className == AlertService::class.qualifiedName }

    @Test
    fun alertService_DisplaysNotification() {
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
    fun alertService_onBind_Returns_AlertServiceBinder() {
        // When
        val binder = serviceTestRule.bindService(serviceIntent)
        val service = (binder as? AlertService.AlertServiceBinder)?.getService()

        // Then
        assertTrue { binder is AlertService.AlertServiceBinder }
        assertTrue { service is AlarmServiceContract.AlertServiceContract }
    }
}
