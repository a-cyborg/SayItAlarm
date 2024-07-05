/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.domain.di

import kotlin.test.assertNotNull
import io.mockk.mockk
import org.a_cyb.sayitalarm.domain.alarm_service.AlarmServiceContract
import org.a_cyb.sayitalarm.domain.interactor.InteractorContract
import org.a_cyb.sayitalarm.domain.repository.RepositoryContract
import org.junit.Test
import org.koin.dsl.koinApplication
import org.koin.dsl.module

class DomainModuleSpec {
    @Test
    fun `It injects AddInteractor`() {
        // Given
        val externalModule = module {
            single<RepositoryContract.AlarmRepository> { mockk() }
            single<AlarmServiceContract.AlarmScheduler> { mockk() }
        }

        val koinApp = koinApplication {
            modules(
                domainModule,
                externalModule,
            )
        }

        // When
        val addInteractor = koinApp.koin.get<InteractorContract.AddInteractor>()

        // Then
        assertNotNull(addInteractor)
    }

    @Test
    fun `It injects ListInteractor`() {
        // Given
        val externalModule = module {
            single<RepositoryContract.AlarmRepository> { mockk() }
            single<AlarmServiceContract.AlarmScheduler> { mockk() }
        }

        val koinApp = koinApplication {
            modules(
                domainModule,
                externalModule,
            )
        }

        // When
        val listInteractor = koinApp.koin.get<InteractorContract.ListInteractor>()

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
                domainModule,
                externalModule,
            )
        }

        // When
        val settingsInteractor = koinApp.koin.get<InteractorContract.SettingsInteractor>()

        // Then
        assertNotNull(settingsInteractor)
    }
}
