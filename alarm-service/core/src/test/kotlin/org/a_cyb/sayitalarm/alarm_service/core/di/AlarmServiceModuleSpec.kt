/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.alarm_service.core.di

import android.content.Context
import io.mockk.mockk
import kotlinx.coroutines.CoroutineScope
import org.a_cyb.sayitalarm.domain.alarm_service.AlarmControllerContract
import org.a_cyb.sayitalarm.domain.alarm_service.EditDistanceCalculatorContract
import org.junit.Test
import org.koin.android.ext.koin.androidContext
import org.koin.core.qualifier.named
import org.koin.dsl.koinApplication
import org.koin.dsl.module
import kotlin.test.assertNotNull

class AlarmServiceModuleSpec {

    private val context: Context = mockk(relaxed = true)

    @Test
    fun `It injects AlarmController`() {
        // Given
        val extraModules = module {
            single<CoroutineScope>(named("io")) { mockk() }
        }
        val koinApp = koinApplication {
            androidContext(context)
            modules(
                alarmServiceModule,
                extraModules,
            )
        }

        // When
        val controller = koinApp.koin.getOrNull<AlarmControllerContract>()

        // Then
        assertNotNull(controller)
    }

    @Test
    fun `It injects EditDistanceCalculator`() {
        // Given
        val koinApp = koinApplication {
            modules(
                alarmServiceModule,
            )
        }

        // When
        val calculator = koinApp.koin.getOrNull<EditDistanceCalculatorContract>()

        // Then
        assertNotNull(calculator)
    }
}
