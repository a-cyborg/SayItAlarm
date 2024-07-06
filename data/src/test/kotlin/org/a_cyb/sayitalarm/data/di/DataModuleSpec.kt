/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.data.di

import kotlin.test.Test
import kotlin.test.assertNotNull
import io.mockk.mockk
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.test.StandardTestDispatcher
import org.a_cyb.sayitalarm.data.datasource.DataSourceContract
import org.a_cyb.sayitalarm.domain.repository.RepositoryContract
import org.acyb.sayitalarm.database.AlarmQueries
import org.acyb.sayitalarm.database.SettingsQueries
import org.koin.dsl.koinApplication
import org.koin.dsl.module
import org.koin.core.qualifier.named

class DataModuleSpec {
    @Test
    fun `It injects AlarmDataSource`() {
        // Given
        val databaseModule = module {
            single<AlarmQueries> { AlarmQueries(mockk()) }
        }

        val koinApp = koinApplication {
            modules(
                dataModule,
                databaseModule,
            )
        }

        // When
        val alarmDataSource = koinApp.koin.get<DataSourceContract.AlarmDataSource>()

        // Then
        assertNotNull(alarmDataSource)
    }

    @Test
    fun `It injects AlarmRepository`() {
        // Given
        val dispatcherModule = module {
            single<AlarmQueries> { AlarmQueries(mockk()) }
            single<CoroutineDispatcher>(named("io")) { StandardTestDispatcher() }
        }

        val koinApp = koinApplication {
            modules(
                dataModule,
                dispatcherModule,
            )
        }

        // When
        val alarmRepository = koinApp.koin.get<RepositoryContract.AlarmRepository>()

        // Then
        assertNotNull(alarmRepository)
    }

    @Test
    fun `It injects SettingsDataSource`() {
        // Given
        val databaseModule = module {
            single<SettingsQueries> { SettingsQueries(mockk()) }
        }

        val koinApp = koinApplication {
            modules(
                dataModule,
                databaseModule,
            )
        }

        // When
        val settingsDataSource = koinApp.koin.get<DataSourceContract.SettingsDataSource>()

        // Then
        assertNotNull(settingsDataSource)
    }

    @Test
    fun `It injects SettingsRepository`() {
        // Given
        val dispatcherModule = module {
            single<SettingsQueries> { SettingsQueries(mockk()) }
            single<CoroutineDispatcher>(named("io")) { StandardTestDispatcher() }
        }

        val koinApp = koinApplication {
            modules(
                dataModule,
                dispatcherModule,
            )
        }

        // When
        val settingsRepository = koinApp.koin.get<RepositoryContract.SettingsRepository>()

        // Then
        assertNotNull(settingsRepository)
    }
}
