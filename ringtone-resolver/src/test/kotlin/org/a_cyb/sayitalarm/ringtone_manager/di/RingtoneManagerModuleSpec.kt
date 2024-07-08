/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.ringtone_manager.di

import kotlin.test.assertNotNull
import android.content.Context
import io.mockk.mockk
import org.a_cyb.sayitalarm.ringtone_resolver.RingtoneResolverContract
import org.a_cyb.sayitalarm.ringtone_resolver.di.ringtoneResolverModule
import org.junit.Test
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.koinApplication

class RingtoneManagerModuleSpec {
    @Test
    fun `It injects RingtoneManager`() {
        // Given
        val context: Context = mockk()

        val koinApp = koinApplication {
            androidContext(context)
            modules(
                ringtoneResolverModule,
            )
        }

        // When
        val ringtoneManager = koinApp.koin.get<RingtoneResolverContract>()

        // Then
        assertNotNull(ringtoneManager)
    }
}