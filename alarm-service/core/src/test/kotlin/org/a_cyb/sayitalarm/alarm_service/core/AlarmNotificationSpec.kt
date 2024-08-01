/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.alarm_service.core

import kotlin.test.assertNotNull
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.a_cyb.sayitalarm.util.mustBe
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import org.robolectric.shadow.api.Shadow
import org.robolectric.shadows.ShadowPendingIntent

@RunWith(AndroidJUnit4::class)
@Config(sdk = [33])
class AlarmNotificationSpec {

    private lateinit var context: Context

    @Before
    fun setup() {
        context = ApplicationProvider.getApplicationContext()
    }

    @Test
    fun `When getAlarmAlertNotification is called it returns notification`() {
        // Given
        val alarmBundle = Bundle().apply {
            putLong(AlarmScheduler.BUNDLE_KEY_ALARM_ID, 3)
        }

        // When
        val actualNotification = AlarmNotification
            .getAlarmAlertNotification(context, alarmBundle)

        // Then
        with(actualNotification) {
            priority mustBe NotificationCompat.PRIORITY_MAX
            category mustBe NotificationCompat.CATEGORY_ALARM
            visibility mustBe NotificationCompat.VISIBILITY_PUBLIC
            smallIcon.resId mustBe R.drawable.ic_notif_small
        }

        val pendingIntent = (Shadow.extract(actualNotification.fullScreenIntent) as ShadowPendingIntent)

        with(pendingIntent) {
            isActivity mustBe true
            isImmutable mustBe true
            requestCode mustBe 818
            flags mustBe (PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT)
        }

        with(pendingIntent.savedIntent) {
            component!!.className mustBe AlarmActivity::class.qualifiedName
            extras!!.getLong(AlarmScheduler.BUNDLE_KEY_ALARM_ID) mustBe 3
            flags mustBe (
                Intent.FLAG_ACTIVITY_NEW_TASK
                    or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    or Intent.FLAG_ACTIVITY_NO_HISTORY
                )
        }
    }

    @Test
    fun `When getAlarmAlertNotification is called it creates notificationChannel`() {
        // Given
        val manager = NotificationManagerCompat.from(context)

        // When
        val actualNotification = AlarmNotification
            .getAlarmAlertNotification(context, Bundle())

        // Then
        with(manager.getNotificationChannel(actualNotification.channelId)) {
            assertNotNull(this)
            this.importance mustBe NotificationManager.IMPORTANCE_HIGH
            this.name mustBe context.getString(R.string.notification_alert_channel_name)
        }
    }
}
