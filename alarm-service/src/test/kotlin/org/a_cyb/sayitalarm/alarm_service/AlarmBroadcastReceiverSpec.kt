/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.alarm_service

import kotlin.test.Test
import kotlin.test.assertTrue
import android.Manifest
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.GrantPermissionRule
import io.mockk.mockkStatic
import io.mockk.slot
import io.mockk.verify
import org.a_cyb.sayitalarm.util.mustBe
import org.junit.Before
import org.junit.Rule
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AlarmBroadcastReceiverSpec {

    private lateinit var context: Context

    @get:Rule
    val grantPermissionRule: GrantPermissionRule = GrantPermissionRule.grant(
        Manifest.permission.FOREGROUND_SERVICE,
        Manifest.permission.FOREGROUND_SERVICE_SPECIAL_USE,
    )

    @Before
    fun setup() {
        context = ApplicationProvider.getApplicationContext()
    }

    @Test
    fun `When onReceive is invoked with ACTION_DELIVER_ALARM action it calls startForegroundService with the AlarmRingService component`() {
        // Given
        mockkStatic(ContextCompat::class)

        // When
        AlarmBroadcastReceiver().onReceive(
            context,
            Intent(AlarmScheduler.ACTION_DELIVER_ALARM).putExtras(Bundle())
        )

        val captureIntent = slot<Intent>()

        // Then
        verify(exactly = 1) {
            ContextCompat.startForegroundService(any(), capture(captureIntent))
        }

        captureIntent.captured.component!!.className mustBe AlarmRingService::class.qualifiedName
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
        val alarmBroadcastReceiverName = context.packageManager
            .getReceiverInfo(
                ComponentName(context, AlarmBroadcastReceiver::class.java),
                PackageManager.GET_RECEIVERS
            )
            .name

        resolvedInfo.forEach {
            if (it.activityInfo.name == alarmBroadcastReceiverName) {
                return true
            }
        }

        return false
    }
}