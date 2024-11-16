/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.alarm_service.scheduler.di

import android.content.Context
import io.mockk.mockk
import kotlin.test.assertNotNull
import org.a_cyb.sayitalarm.domain.alarm_service.AlarmSchedulerContract
import org.junit.Test
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.koinApplication

class SchedulerModuleSpec {

    @Test
    fun `It injects AlarmScheduler`() {
        // Given
        val koinApp = koinApplication {
            androidContext(mockk<Context>())
            modules(schedulerModule)
        }

        // When
        val alarmScheduler = koinApp.koin.getOrNull<AlarmSchedulerContract>()

        // Then
        assertNotNull(alarmScheduler)
    }

}