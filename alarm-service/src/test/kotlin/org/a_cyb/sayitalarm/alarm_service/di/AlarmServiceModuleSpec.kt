/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.alarm_service.di

import kotlin.test.assertNotNull
import org.a_cyb.sayitalarm.domain.alarm_service.AlarmServiceContract
import org.junit.Test
import org.koin.dsl.koinApplication

class AlarmServiceModuleSpec {
    @Test
    fun `It injects AlarmScheduler`() {
        // Given
        val koinApp = koinApplication {
            modules(
                alarmServiceModule
            )
        }

        // When
        val alarmScheduler = koinApp.koin.getOrNull<AlarmServiceContract.AlarmScheduler>()

        // Then
        assertNotNull(alarmScheduler)
    }
}