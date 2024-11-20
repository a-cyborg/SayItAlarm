/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.alarm_service.stt_recognizer

import android.content.Context
import android.content.Intent
import android.speech.RecognizerIntent.EXTRA_CALLING_PACKAGE
import android.speech.RecognizerIntent.EXTRA_PREFER_OFFLINE
import android.speech.SpeechRecognizer
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.slot
import io.mockk.verify
import org.a_cyb.sayitalarm.domain.alarm_service.SttRecognizerContract
import org.junit.After
import org.junit.Before
import org.junit.runner.RunWith
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertTrue

@RunWith(AndroidJUnit4::class)
class AndroidSttRecognizerSpec {

    private lateinit var context: Context
    private lateinit var recognizer: SttRecognizerContract

    @Before
    fun setup() {
        mockkStatic(SpeechRecognizer::class)

        context = ApplicationProvider.getApplicationContext()
        recognizer = AndroidSttRecognizer(context)
    }

    @After
    fun tearDown() {
        clearAllMocks()
    }

    @Test
    fun `When onDeviceRecognizer is not available it uses online SpeechRecognizer`() {
        // Given
        every { SpeechRecognizer.isOnDeviceRecognitionAvailable(any()) } returns false

        // When
        recognizer.startListening()

        // Then
        verify { SpeechRecognizer.createSpeechRecognizer(any()) }
    }

    @Test
    fun `When onDeviceRecognizer is available it uses onDevice SpeechRecognizer`() {
        // Given
        every { SpeechRecognizer.isOnDeviceRecognitionAvailable(any()) } returns true

        // When
        recognizer.startListening()

        // Then
        verify { SpeechRecognizer.createOnDeviceSpeechRecognizer(any()) }
    }

    @Test
    fun `When startListening is called it sets listener and invoke startListening `() {
        // Given
        val speechRecognizerMockk: SpeechRecognizer = mockk(relaxed = true)
        every { SpeechRecognizer.isOnDeviceRecognitionAvailable(any()) } returns true
        every { SpeechRecognizer.createOnDeviceSpeechRecognizer(any()) } returns speechRecognizerMockk

        // When
        recognizer.startListening()

        // Then
        verify { speechRecognizerMockk.setRecognitionListener(any()) }
        verify { speechRecognizerMockk.startListening(any()) }
    }

    @Test
    fun `When stopRecognizer is called it invokes stopListening and destroy`() {
        // Given
        val speechRecognizerMockk: SpeechRecognizer = mockk(relaxed = true)
        every { SpeechRecognizer.isOnDeviceRecognitionAvailable(any()) } returns true
        every { SpeechRecognizer.createOnDeviceSpeechRecognizer(any()) } returns speechRecognizerMockk

        // When
        recognizer.stopRecognizer()

        // Then
        verify { speechRecognizerMockk.stopListening() }
        verify { speechRecognizerMockk.destroy() }
    }

    @Test
    fun `When startListening It sets proper intent`() {
        // Given
        val intentSlot = slot<Intent>()
        val speechRecognizerMockk: SpeechRecognizer = mockk(relaxed = true)

        every { SpeechRecognizer.isOnDeviceRecognitionAvailable(any()) } returns true
        every { SpeechRecognizer.createOnDeviceSpeechRecognizer(any()) } returns speechRecognizerMockk
        every { speechRecognizerMockk.startListening(capture(intentSlot)) } answers {}

        // When
        recognizer.startListening()

        // Then
        assertTrue(intentSlot.isCaptured)
        assertTrue(intentSlot.captured.getBooleanExtra(EXTRA_PREFER_OFFLINE, false))
        assertTrue(
            intentSlot.captured
                .getStringExtra(EXTRA_CALLING_PACKAGE)!!
                .contains("org.a_cyb.sayitalarm.alarm_service.stt_recognizer"),
        )
    }

    @Test
    fun `RecognizerState is in Initial state`() {
        assertEquals(
            SttRecognizerContract.RecognizerState.Initial,
            recognizer.recognizerState.value,
        )
    }

    @Test
    fun `It fulfills SttRecognizerContract`() {
        assertIs<SttRecognizerContract>(recognizer)
    }
}
