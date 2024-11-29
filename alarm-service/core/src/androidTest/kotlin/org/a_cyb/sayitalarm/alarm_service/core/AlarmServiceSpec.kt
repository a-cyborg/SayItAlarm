/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.alarm_service.core

import android.Manifest
import android.app.ActivityManager
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import androidx.test.rule.GrantPermissionRule
import androidx.test.rule.ServiceTestRule
import org.a_cyb.sayitalarm.util.audio_vibe_player.di.audioVibePlayerModule
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.core.context.startKoin

@RunWith(AndroidJUnit4::class)
@SmallTest
class AlarmServiceSpec {
    private lateinit var context: Context
    private lateinit var serviceIntent: Intent

    @get:Rule(order = 0)
    val mRuntimePermissionRule: GrantPermissionRule = GrantPermissionRule.grant(
        Manifest.permission.POST_NOTIFICATIONS,
        Manifest.permission.USE_FULL_SCREEN_INTENT,
    )

    @get:Rule(order = 1)
    val serviceTestRule = ServiceTestRule()

    @Before
    fun setup() {
        context = ApplicationProvider.getApplicationContext()
        serviceIntent = Intent(context, AlarmService::class.java)
    }

    @Test
    fun alarmService_onStart_startForegroundService() {
        // When
        serviceTestRule.startService(serviceIntent)

        // Then
        assertTrue(isForegroundServiceRunning(context))
    }

    @Test
    fun alarmService_onStart_displaysNotification() {
        // When
        serviceTestRule.startService(serviceIntent)

        // Then
        assertTrue(isNotificationDisplayed(context))
    }

    @Test
    fun alarmService_onBind_returnsAlarmService() {
        // When
        val service = serviceTestRule.bindService(serviceIntent)

        // Then
        assertTrue(service is AlarmServiceContract)
    }

    @Test
    fun alarmService_stopServiceIsCalled_stopForegroundServiceAndDismissesNotification() {
        // Given
        startKoin {
            modules(audioVibePlayerModule)
        }

        serviceTestRule.startService(serviceIntent)

        val service = serviceTestRule.bindService(serviceIntent) as AlarmServiceContract

        // When
        service.stopService()

        // Then
        assertFalse(isNotificationDisplayed(context))
        assertFalse(isForegroundServiceRunning(context))
    }

    @Suppress("DEPRECATION")
    private fun isForegroundServiceRunning(context: Context): Boolean =
        context
            .getSystemService(ActivityManager::class.java)
            .getRunningServices(Int.MAX_VALUE)
            .any { it.foreground && it.service.className == AlarmService::class.qualifiedName }

    private fun isNotificationDisplayed(context: Context): Boolean =
        context
            .getSystemService(NotificationManager::class.java)
            .activeNotifications
            .any {
                it.id == 300 &&
                    it.notification.channelId == "siaAlarmNotificationChannel" &&
                    it.notification.category == NotificationCompat.CATEGORY_ALARM &&
                    it.packageName == context.packageName
            }
}
