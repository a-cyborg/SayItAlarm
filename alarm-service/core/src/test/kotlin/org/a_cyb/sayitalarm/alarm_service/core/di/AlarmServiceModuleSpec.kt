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
import org.a_cyb.sayitalarm.domain.alarm_service.AlarmServiceContract
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.koinApplication

@RunWith(AndroidJUnit4::class)
class AlarmServiceModuleSpec {

    private val context: Context = ApplicationProvider.getApplicationContext()

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
}