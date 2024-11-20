/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.alarm_service.stt_recognizer.di

import io.mockk.mockk
import org.a_cyb.sayitalarm.domain.alarm_service.SttRecognizerContract
import org.junit.Test
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.koinApplication
import kotlin.test.assertNotNull

class SttRecognizerModuleSpec {

    @Test
    fun `It injects SttRecognizer`() {
        // Given
        val koinApp = koinApplication {
            androidContext(mockk())
            modules(
                sttRecognizerModule,
            )
        }

        // When
        val recognizer = koinApp.koin.getOrNull<SttRecognizerContract>()

        // Then
        assertNotNull(recognizer)
    }
}
