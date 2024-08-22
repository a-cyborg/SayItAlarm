/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.alarm_service.core.di

import kotlin.test.assertNotNull
import android.content.Context
import io.mockk.mockk
import kotlinx.coroutines.CoroutineScope
import org.a_cyb.sayitalarm.domain.alarm_service.AlarmServiceContract
import org.a_cyb.sayitalarm.domain.repository.RepositoryContract
import org.junit.Test
import org.koin.android.ext.koin.androidContext
import org.koin.core.qualifier.named
import org.koin.dsl.koinApplication
import org.koin.dsl.module

class AlarmServiceModuleSpec {

    private val context: Context = mockk(relaxed = true)

    @Test
    fun `It injects AlarmScheduler`() {
        // Given
        val koinApp = koinApplication {
            androidContext(context)
            modules(
                alarmServiceModule
            )
        }

        // When
        val alarmScheduler = koinApp.koin.getOrNull<AlarmServiceContract.AlarmScheduler>()

        // Then
        assertNotNull(alarmScheduler)
    }

    @Test
    fun `It injects AlarmServiceController`() {
        // Given
        val extraModules = module {
            single<RepositoryContract.AlarmRepository> { mockk() }
            single<RepositoryContract.SettingsRepository> { mockk() }
            single<CoroutineScope>(named("ioScope")) { mockk() }
        }
        val koinApp = koinApplication {
            androidContext(context)
            modules(
                alarmServiceModule,
                extraModules,
            )
        }

        // When
        val controller = koinApp.koin.getOrNull<AlarmServiceContract.AlarmServiceController>()

        // Then
        assertNotNull(controller)
    }

    @Test
    fun `It injects SttRecognizer`() {
        // Given
        val koinApp = koinApplication {
            androidContext(context)
            modules(
                alarmServiceModule,
            )
        }

        // When
        val recognizer = koinApp.koin.getOrNull<AlarmServiceContract.SttRecognizer>()

        // Then
        assertNotNull(recognizer)
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
        val calculator = koinApp.koin.getOrNull<AlarmServiceContract.EditDistanceCalculator>()

        // Then
        assertNotNull(calculator)
    }
}
