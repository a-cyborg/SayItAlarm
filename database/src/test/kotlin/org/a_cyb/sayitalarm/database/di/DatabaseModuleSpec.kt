/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.database.di

import android.content.Context
import io.mockk.mockk
import org.a_cyb.sayitalarm.database.SayItDB
import org.acyb.sayitalarm.database.AlarmQueries
import org.acyb.sayitalarm.database.SettingsQueries
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.koinApplication
import kotlin.test.Test
import kotlin.test.assertNotNull

class DatabaseModuleSpec {
    @Test
    fun `It injects SayItDB`() {
        // Given
        val context: Context = mockk(relaxed = true)

        val koinApp = koinApplication {
            androidContext(context)
            modules(databaseModule)
        }

        // When
        val sayItDB = koinApp.koin.getOrNull<SayItDB>()

        // Then
        assertNotNull(sayItDB)
    }

    @Test
    fun `It inject AlamQueries`() {
        // Given
        val context: Context = mockk(relaxed = true)

        val koinApp = koinApplication {
            androidContext(context)
            modules(databaseModule)
        }

        // When
        val alarmQueries = koinApp.koin.getOrNull<AlarmQueries>()

        // Then
        assertNotNull(alarmQueries)
    }

    @Test
    fun `It inject SettingsQueries`() {
        // Given
        val context: Context = mockk(relaxed = true)

        val koinApp = koinApplication {
            androidContext(context)
            modules(databaseModule)
        }

        // When
        val settingsQueries = koinApp.koin.getOrNull<SettingsQueries>()

        // Then
        assertNotNull(settingsQueries)
    }
}
