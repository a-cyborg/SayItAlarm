/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.alarm_service.core

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.RecognizerIntent.EXTRA_CALLING_PACKAGE
import android.speech.RecognizerIntent.EXTRA_PREFER_OFFLINE
import android.speech.SpeechRecognizer
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import org.a_cyb.sayitalarm.domain.alarm_service.AlarmServiceContract.SttRecognizer
import org.a_cyb.sayitalarm.domain.alarm_service.AlarmServiceContract.SttRecognizer.IsOnDevice
import org.a_cyb.sayitalarm.domain.alarm_service.AlarmServiceContract.SttRecognizer.RecognizerState

class AndroidSttRecognizer(private val context: Context) : SttRecognizer, RecognitionListener {

    private val recognizer: SpeechRecognizer by lazy { resolveSpeechRecognizer() }

    private val _recognizerState: MutableStateFlow<RecognizerState> = MutableStateFlow(RecognizerState.Initial)
    override val recognizerState: StateFlow<RecognizerState> = _recognizerState.asStateFlow()

    private val _isOnDevice: MutableStateFlow<IsOnDevice> = MutableStateFlow(IsOnDevice.True)
    override val isOnDevice: StateFlow<IsOnDevice> = _isOnDevice.asStateFlow()

    override fun startListening() {
        recognizer.setRecognitionListener(this)
        recognizer.startListening(getRecognizerIntent())
    }

    override fun stopRecognizer() {
        recognizer.stopListening()
        recognizer.destroy()
    }

    private fun resolveSpeechRecognizer(): SpeechRecognizer =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S &&
            SpeechRecognizer.isOnDeviceRecognitionAvailable(context)
        ) {
            SpeechRecognizer.createOnDeviceSpeechRecognizer(context)
        } else {
            _isOnDevice.update { IsOnDevice.False }
            SpeechRecognizer.createSpeechRecognizer(context)
        }

    private fun getRecognizerIntent(): Intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        .putExtra(EXTRA_CALLING_PACKAGE, context.packageName)
        .putExtra(EXTRA_PREFER_OFFLINE, true)

    override fun onReadyForSpeech(params: Bundle?) {
        RecognizerState.Ready.update()
    }

    override fun onPartialResults(partialResults: Bundle?) {
        val result = partialResults?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
        RecognizerState.Processing(result?.lastOrNull() ?: "").update()
    }

    override fun onResults(results: Bundle?) {
        val result = results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
        RecognizerState.Done(result = result?.firstOrNull() ?: "").update()
    }

    override fun onError(error: Int) {
        if (error == SpeechRecognizer.ERROR_NO_MATCH) {
            // This error frequently occurs. It can be resolved by listening again.
            startListening()
        } else {
            RecognizerState.Error(resolveError(error)).update()
        }
    }

    private fun RecognizerState.update() {
        _recognizerState.update { this }
    }

    override fun onBeginningOfSpeech() {}
    override fun onBufferReceived(buffer: ByteArray?) {}
    override fun onRmsChanged(rmsdB: Float) {}
    override fun onEndOfSpeech() {}
    override fun onEvent(eventType: Int, params: Bundle?) {}

    private fun resolveError(error: Int): String =
        when (error) {
            SpeechRecognizer.ERROR_AUDIO -> "Error_Audio"
            SpeechRecognizer.ERROR_CLIENT -> "Error_Client"
            SpeechRecognizer.ERROR_SERVER, SpeechRecognizer.ERROR_SERVER_DISCONNECTED -> "Error_Server"
            SpeechRecognizer.ERROR_NETWORK, SpeechRecognizer.ERROR_NETWORK_TIMEOUT -> "Error_Network"
            SpeechRecognizer.ERROR_CANNOT_CHECK_SUPPORT -> "Error_Cannot_Check_Support"
            SpeechRecognizer.ERROR_LANGUAGE_NOT_SUPPORTED -> "Error_Language_Not_Supported"
            SpeechRecognizer.ERROR_LANGUAGE_UNAVAILABLE -> "Error_Language_Unavailable"
            SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS -> "Error_Insufficient_Permission"
            SpeechRecognizer.ERROR_TOO_MANY_REQUESTS -> "Error_Too_Many_Requests"
            SpeechRecognizer.ERROR_SPEECH_TIMEOUT -> "Error_Speech_Timeout"
            SpeechRecognizer.ERROR_RECOGNIZER_BUSY -> "Error_Recognizer_Busy"
            SpeechRecognizer.ERROR_CANNOT_LISTEN_TO_DOWNLOAD_EVENTS -> "Error_Cannot_Listen_To_Download_Events"
            else -> "Unknown error = $error"
        }
}
