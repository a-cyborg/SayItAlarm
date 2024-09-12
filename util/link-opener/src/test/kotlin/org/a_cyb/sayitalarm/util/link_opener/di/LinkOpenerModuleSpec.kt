/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.util.link_opener.di

import kotlin.test.assertNotNull
import android.content.Context
import io.mockk.mockk
import org.a_cyb.sayitalarm.util.link_opener.LinkOpenerContract
import org.junit.Test
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.koinApplication

class LinkOpenerModuleSpec {

    @Test
    fun `It injects LinkOpener`() {
        // Given
        val context: Context = mockk(relaxed = true)
        val koinApp = koinApplication {
            androidContext(context)
            modules(linkOpenerModule)
        }

        // When
        val opener = koinApp.koin.getOrNull<LinkOpenerContract>()

        // Then
        assertNotNull(opener)
    }
}
