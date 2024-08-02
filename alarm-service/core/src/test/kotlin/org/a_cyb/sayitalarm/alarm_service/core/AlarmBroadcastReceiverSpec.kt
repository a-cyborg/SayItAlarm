/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.alarm_service.core

import kotlin.test.Test
import kotlin.test.assertTrue
import android.content.Context
import android.content.Intent
import android.content.pm.ResolveInfo
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import io.mockk.mockkStatic
import io.mockk.slot
import io.mockk.verify
import org.a_cyb.sayitalarm.util.mustBe
import org.junit.Before
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AlarmBroadcastReceiverSpec {

    private lateinit var context: Context

    @Before
    fun setup() {
        context = ApplicationProvider.getApplicationContext()
    }

    @Test
    fun `When onReceive is invoked with ACTION_DELIVER_ALARM action it calls startForegroundService with the AlarmRingService component`() {
        // Given
        mockkStatic(ContextCompat::class)

        val intent = Intent(AlarmScheduler.ACTION_DELIVER_ALARM)
            .putExtras(Bundle())

        // When
        AlarmBroadcastReceiver()
            .onReceive(context, intent)

        val captureIntent = slot<Intent>()

        verify(exactly = 1) {
            ContextCompat.startForegroundService(
                any(),
                capture(captureIntent)
            )
        }

        // Then
        captureIntent.captured.component!!.className mustBe
            AlarmService::class.qualifiedName
    }

    @Test
    fun `It is declared to handle the intent action BOOT_COMPLETED`() {
        val resolvedInfo = context.packageManager
            .queryBroadcastReceivers(
                Intent("android.intent.action.BOOT_COMPLETED"),
                0
            )

        assertTrue(isContainsAlarmBroadcastReceiver(resolvedInfo))
    }

    @Test
    fun `It is declared to handle the intent action TIMEZONE_CHANGED`() {
        val resolvedInfo = context.packageManager
            .queryBroadcastReceivers(
                Intent("android.intent.action.TIMEZONE_CHANGED"),
                0
            )

        assertTrue(isContainsAlarmBroadcastReceiver(resolvedInfo))
    }

    @Test
    fun `It is declared to handle the intent action LOCKED_BOOT_COMPLETED`() {
        val resolvedInfo = context.packageManager
            .queryBroadcastReceivers(
                Intent("android.intent.action.LOCKED_BOOT_COMPLETED"),
                0
            )

        assertTrue(isContainsAlarmBroadcastReceiver(resolvedInfo))
    }

    @Test
    fun `It is declared to handle the intent action DELIVER_ALARM`() {
        val resolvedInfo = context.packageManager
            .queryBroadcastReceivers(
                Intent("org.a_cyb.sayitalarm.DELIVER_ALARM"),
                0
            )

        assertTrue(isContainsAlarmBroadcastReceiver(resolvedInfo))
    }

    private fun isContainsAlarmBroadcastReceiver(resolvedInfo: List<ResolveInfo>): Boolean {
        resolvedInfo.forEach {
            if (it.activityInfo.name == AlarmBroadcastReceiver::class.qualifiedName) {
                return true
            }
        }
        return false
    }
}