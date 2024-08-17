/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.alarm_service.core.di

import kotlin.test.assertNotNull
import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import io.mockk.mockk
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.test.StandardTestDispatcher
import org.a_cyb.sayitalarm.domain.alarm_service.AlarmServiceContract
import org.a_cyb.sayitalarm.domain.repository.RepositoryContract
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.android.ext.koin.androidContext
import org.koin.core.qualifier.named
import org.koin.dsl.koinApplication
import org.koin.dsl.module

@RunWith(AndroidJUnit4::class)
class AlarmServiceModuleSpec {

    private lateinit var context: Context

    @Before
    fun setup() {
        context = ApplicationProvider.getApplicationContext()
    }

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
            single<CoroutineDispatcher>(named("io")) { mockk() }
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
    fun `It injects SayItProcessor`() {
        // Given
        val extraModules = module {
            single<AlarmServiceContract.SttRecognizer> { mockk(relaxed = true) }
            single<CoroutineScope>(named("ioScope")) { CoroutineScope(StandardTestDispatcher()) }
        }
        val koinApp = koinApplication {
            modules(
                alarmServiceModule,
                extraModules,
            )
        }

        // When
        val processor = koinApp.koin.getOrNull<AlarmServiceContract.SayItProcessor>()

        // Then
        assertNotNull(processor)
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
}
