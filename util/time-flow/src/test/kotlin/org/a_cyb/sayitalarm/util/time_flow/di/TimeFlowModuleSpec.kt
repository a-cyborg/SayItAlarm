/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.util.time_flow.di

import org.a_cyb.sayitalarm.util.time_flow.TimeFlowContract
import org.junit.Assert.assertNotNull
import org.junit.Test
import org.koin.dsl.koinApplication

class TimeFlowModuleSpec {
    @Test
    fun `It injects TimeFlow`() {
        // Given
        val koinApp = koinApplication {
            modules(
                timeFlowModule
            )
        }

        // When
        val timeFlow = koinApp.koin.getOrNull<TimeFlowContract>()

        // Then
        assertNotNull(timeFlow)
    }
}