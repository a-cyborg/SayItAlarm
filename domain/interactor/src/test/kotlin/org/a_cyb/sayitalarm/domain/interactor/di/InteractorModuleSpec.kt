/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.domain.interactor.di

import io.mockk.mockk
import kotlin.test.assertNotNull
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import org.a_cyb.sayitalarm.domain.alarm_service.AlarmControllerContract
import org.a_cyb.sayitalarm.domain.alarm_service.AlarmSchedulerContract
import org.a_cyb.sayitalarm.domain.interactor.InteractorContract
import org.a_cyb.sayitalarm.domain.repository.RepositoryContract
import org.junit.Test
import org.koin.core.qualifier.named
import org.koin.dsl.koinApplication
import org.koin.dsl.module

class InteractorModuleSpec {
    @Test
    fun `It injects AddInteractor`() {
        // Given
        val externalModule = module {
            single<RepositoryContract.AlarmRepository> { mockk() }
            factory<AlarmSchedulerContract> { mockk() }
        }

        val koinApp = koinApplication {
            modules(
                interactorModule,
                externalModule,
            )
        }

        // When
        val addInteractor = koinApp.koin.getOrNull<InteractorContract.AddInteractor>()

        // Then
        assertNotNull(addInteractor)
    }

    @Test
    fun `It injects EditInteractor`() {
        // Given
        val externalModule = module {
            single<RepositoryContract.AlarmRepository> { mockk() }
            factory<AlarmSchedulerContract> { mockk() }
        }

        val koinApp = koinApplication {
            modules(
                interactorModule,
                externalModule,
            )
        }

        // When
        val editInteractor = koinApp.koin.getOrNull<InteractorContract.EditInteractor>()

        // Then
        assertNotNull(editInteractor)
    }

    @Test
    fun `It injects ListInteractor`() {
        // Given
        val externalModule = module {
            single<RepositoryContract.AlarmRepository> { mockk() }
            factory<AlarmSchedulerContract> { mockk() }
        }

        val koinApp = koinApplication {
            modules(
                interactorModule,
                externalModule,
            )
        }

        // When
        val listInteractor = koinApp.koin.getOrNull<InteractorContract.ListInteractor>()

        // Then
        assertNotNull(listInteractor)
    }

    @Test
    fun `It injects SettingsInteractor`() {
        // Given
        val externalModule = module {
            single<RepositoryContract.SettingsRepository> { mockk() }
        }

        val koinApp = koinApplication {
            modules(
                interactorModule,
                externalModule,
            )
        }

        // When
        val settingsInteractor = koinApp.koin.getOrNull<InteractorContract.SettingsInteractor>()

        // Then
        assertNotNull(settingsInteractor)
    }

    @Test
    fun `It injects AlarmInteractor`() {
        // Given
        val externalModule = module {
            factory<AlarmControllerContract> { mockk() }
            single<RepositoryContract.AlarmRepository> { mockk() }
            single<RepositoryContract.SettingsRepository> { mockk() }
            single<AlarmSchedulerContract> { mockk() }
            single<CoroutineDispatcher>(named("io")) { mockk() }
            single<CoroutineScope>(named("ioScope")) { mockk() }
        }

        val koinApp = koinApplication {
            modules(
                interactorModule,
                externalModule,
            )
        }

        // When
        val alarmInteractor = koinApp.koin.getOrNull<InteractorContract.AlarmInteractor>()

        // Then
        assertNotNull(alarmInteractor)
    }
}
