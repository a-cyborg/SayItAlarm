/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.alarm_service.core

import kotlin.test.Test
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
import org.a_cyb.sayitalarm.domain.alarm_service.AlarmServiceContract.SttRecognizer
import org.a_cyb.sayitalarm.util.fulfils
import org.a_cyb.sayitalarm.util.mustBe
import org.junit.After
import org.junit.Before
import org.junit.runner.RunWith
import org.robolectric.annotation.Config

@RunWith(AndroidJUnit4::class)
@Config(sdk = [33])
class AndroidSttRecognizerSpec {

    private lateinit var context: Context
    private lateinit var recognizer: SttRecognizer

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
        recognizer.stopSttRecognizer()

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
        intentSlot.isCaptured mustBe true
        intentSlot.captured.getStringExtra(EXTRA_CALLING_PACKAGE)!!
            .contains("org.a_cyb.sayitalarm.alarm_service.core") mustBe true
        intentSlot.captured.getBooleanExtra(EXTRA_PREFER_OFFLINE, false) mustBe true
    }

    @Test
    fun `RecognizerState is in Initial state`() {
        recognizer.recognizerState.value mustBe SttRecognizer.RecognizerState.Initial
    }

    @Test
    fun `It fulfills SttRecognizer`() {
        recognizer fulfils SttRecognizer::class
    }
}