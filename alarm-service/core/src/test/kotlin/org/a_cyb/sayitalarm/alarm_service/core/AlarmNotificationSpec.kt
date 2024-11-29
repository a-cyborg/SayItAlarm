/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.alarm_service.core

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import org.robolectric.shadow.api.Shadow
import org.robolectric.shadows.ShadowPendingIntent
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

@RunWith(AndroidJUnit4::class)
@Config(sdk = [33])
class AlarmNotificationSpec {

    private lateinit var context: Context

    @Before
    fun setup() {
        context = ApplicationProvider.getApplicationContext()
    }

    @Test
    fun `When getFullScreenNotificationForAlarm is called, it returns notification`() {
        // Given
        val actualNotification = getFullScreenNotificationForAlarm(context)

        // Then
        @Suppress("DEPRECATION")
        assertEquals(NotificationCompat.PRIORITY_MAX, actualNotification.priority)
        assertEquals(NotificationCompat.CATEGORY_ALARM, actualNotification.category)
        assertEquals(NotificationCompat.VISIBILITY_PUBLIC, actualNotification.visibility)
        assertEquals(R.drawable.ic_notif_small, actualNotification.smallIcon.resId)

        val actualPendingIntent = (Shadow.extract(actualNotification.fullScreenIntent) as ShadowPendingIntent)

        assertTrue(actualPendingIntent.isActivity)
        assertTrue(actualPendingIntent.isImmutable)
        assertEquals(818, actualPendingIntent.requestCode)
        assertEquals(
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE,
            actualPendingIntent.flags,
        )
        assertEquals(AlarmActivity::class.qualifiedName, actualPendingIntent.savedIntent.component!!.className)
        assertEquals(
            Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NO_HISTORY,
            actualPendingIntent.savedIntent.flags,
        )
    }

    @Test
    fun `When getFullScreenNotificationForAlarm is called, it creates notificationChannel`() {
        // Given
        val manager = NotificationManagerCompat.from(context)
        val actualNotification = getFullScreenNotificationForAlarm(context)

        // When
        val channel = manager.getNotificationChannel(actualNotification.channelId)

        // Then
        assertNotNull(channel)
        assertEquals(NotificationManager.IMPORTANCE_HIGH, channel.importance)
        assertEquals(context.getString(R.string.notification_alert_channel_name), channel.name)
    }
}
