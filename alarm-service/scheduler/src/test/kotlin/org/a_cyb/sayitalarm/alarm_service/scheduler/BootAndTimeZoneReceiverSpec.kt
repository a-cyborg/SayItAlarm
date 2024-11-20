/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.alarm_service.scheduler

import android.content.Context
import android.content.Intent
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import io.mockk.clearAllMocks
import io.mockk.mockk
import io.mockk.verify
import org.a_cyb.sayitalarm.domain.alarm_service.AlarmSchedulerContract
import org.junit.After
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module

@RunWith(AndroidJUnit4::class)
class BootAndTimeZoneReceiverSpec {

    private lateinit var context: Context
    private lateinit var alarmSchedulerMockk: AlarmSchedulerContract

    @Before
    fun setup() {
        context = ApplicationProvider.getApplicationContext()
        alarmSchedulerMockk = mockk(relaxed = true)

        startKoin {
            modules(
                module {
                    factory<AlarmSchedulerContract> {
                        alarmSchedulerMockk
                    }
                },
            )
        }
    }

    @After
    fun teatDown() {
        clearAllMocks()
        stopKoin()
    }

    @Test
    fun `When onReceive is called with LOCKED_BOOT_COMPLETED, it triggers scheduleAlarms`() {
        // Given
        val intent = Intent(Intent.ACTION_LOCKED_BOOT_COMPLETED)

        // When
        BootAndTimeZoneReceiver().onReceive(context, intent)

        // Then
        verify { alarmSchedulerMockk.scheduleAlarms() }
    }

    @Test
    fun `When onReceive is called with BOOT_COMPLETED action, it triggers scheduleAlarms`() {
        // Given
        val intent = Intent(Intent.ACTION_BOOT_COMPLETED)

        // When
        BootAndTimeZoneReceiver().onReceive(context, intent)

        // Then
        verify { alarmSchedulerMockk.scheduleAlarms() }
    }

    @Test
    fun `It is declared to handle the intent action BOOT_COMPLETED`() {
        // Given
        val resolvedInfo = context.packageManager
            .queryBroadcastReceivers(
                Intent("android.intent.action.BOOT_COMPLETED"),
                0,
            )
            .find { it.activityInfo.name == BootAndTimeZoneReceiver::class.qualifiedName }

        // Then
        assertNotNull(resolvedInfo)
    }

    @Test
    fun `It is declared to handle the intent action LOCKED_BOOT_COMPLETED`() {
        // Given
        val resolvedInfo = context.packageManager
            .queryBroadcastReceivers(
                Intent("android.intent.action.LOCKED_BOOT_COMPLETED"),
                0,
            )
            .find { it.activityInfo.name == BootAndTimeZoneReceiver::class.qualifiedName }

        // Then
        assertNotNull(resolvedInfo)
    }
}
