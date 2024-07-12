/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.system_service.ringtone_manager.di

import kotlin.test.assertNotNull
import android.content.Context
import io.mockk.mockk
import org.a_cyb.sayitalarm.system_service.ringtone_resolver.RingtoneResolverContract
import org.a_cyb.sayitalarm.system_service.ringtone_resolver.di.ringtoneResolverModule
import org.junit.Test
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.koinApplication

class RingtoneResolverModuleSpec {
    @Test
    fun `It injects RingtoneResolver`() {
        // Given
        val context: Context = mockk()

        val koinApp = koinApplication {
            androidContext(context)
            modules(
                ringtoneResolverModule,
            )
        }

        // When
        val ringtoneResolver = koinApp.koin.getOrNull<RingtoneResolverContract>()

        // Then
        assertNotNull(ringtoneResolver)
    }
}
