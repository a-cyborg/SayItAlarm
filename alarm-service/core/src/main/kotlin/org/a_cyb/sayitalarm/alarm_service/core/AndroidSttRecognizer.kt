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
import org.a_cyb.sayitalarm.domain.alarm_service.AlarmServiceContract.SttRecognizer.RecognizerRmsDb
import org.a_cyb.sayitalarm.domain.alarm_service.AlarmServiceContract.SttRecognizer.RecognizerState

class AndroidSttRecognizer(private val context: Context) : SttRecognizer {

    private val recognizer: SpeechRecognizer by lazy { resolveSpeechRecognizer() }
    private val recognizerListener: RecognitionListener by lazy { resolveListener() }

    private val _recognizerState: MutableStateFlow<RecognizerState> = MutableStateFlow(RecognizerState.Initial)
    override val recognizerState: StateFlow<RecognizerState> = _recognizerState.asStateFlow()

    private val _rmsDbState: MutableStateFlow<RecognizerRmsDb> = MutableStateFlow(RecognizerRmsDb(0f))
    override val rmsDbState: StateFlow<RecognizerRmsDb> = _rmsDbState.asStateFlow()

    override fun startListening() {
        recognizer.setRecognitionListener(recognizerListener)
        recognizer.startListening(getRecognizerIntent())
    }

    override fun stopSttRecognizer() {
        recognizer.stopListening()
        recognizer.destroy()
    }

    private fun resolveSpeechRecognizer(): SpeechRecognizer {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (SpeechRecognizer.isOnDeviceRecognitionAvailable(context))
                return SpeechRecognizer.createOnDeviceSpeechRecognizer(context)
        }
        return SpeechRecognizer.createSpeechRecognizer(context)
    }

    private fun getRecognizerIntent(): Intent =
        Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
            .putExtra(EXTRA_CALLING_PACKAGE, context.packageName)
            .putExtra(EXTRA_PREFER_OFFLINE, true)

    private fun resolveListener(): RecognitionListener = object : RecognitionListener {
        override fun onReadyForSpeech(params: Bundle?) {
            _recognizerState.update {
                RecognizerState.Ready
            }
        }

        override fun onRmsChanged(rmsdB: Float) {
            _rmsDbState.update {
                RecognizerRmsDb(rmsdB)
            }
        }

        override fun onError(error: Int) {
            _recognizerState.update {
                RecognizerState.Error(resolveError(error))
            }
        }

        override fun onResults(results: Bundle?) {
            val result = results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)

            _recognizerState.update {
                RecognizerState.Done(result = result?.first() ?: "")
            }
        }

        override fun onPartialResults(partialResults: Bundle?) {
            val result = partialResults?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)

            _recognizerState.update {
                RecognizerState.Processing(result?.first() ?: "")
            }
        }

        override fun onBeginningOfSpeech() {}
        override fun onBufferReceived(buffer: ByteArray?) {}
        override fun onEndOfSpeech() {}
        override fun onEvent(eventType: Int, params: Bundle?) {}
    }

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
            else -> "Error_Unknown"
        }
}
