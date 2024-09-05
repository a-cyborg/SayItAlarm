/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.alarm_service.core

import java.util.concurrent.Executors
import android.content.Context
import android.content.Intent
import android.os.Build
import android.speech.ModelDownloadListener
import android.speech.RecognitionSupport
import android.speech.RecognitionSupportCallback
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import androidx.annotation.RequiresApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import org.a_cyb.sayitalarm.domain.alarm_service.AlarmServiceContract

class TemptRecognizerOfflineHelper(private val context: Context) : AlarmServiceContract.SttRecognizerOnDeviceHelper {
    private val _isOfflineAvailable: MutableStateFlow<Boolean> = MutableStateFlow(false)
    override val isOfflineAvailable: StateFlow<Boolean> = _isOfflineAvailable.asStateFlow()

    init {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            updateIsOfflineAvailable()
        }
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun updateIsOfflineAvailable() {
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        val executor = Executors.newSingleThreadExecutor()
        val recognizer = SpeechRecognizer.createSpeechRecognizer(context)
        val onDeviceAvailable = SpeechRecognizer.isOnDeviceRecognitionAvailable(context)
        val supportListener = object : RecognitionSupportCallback {
            override fun onSupportResult(recognitionSupport: RecognitionSupport) {
                val supported = recognitionSupport.supportedOnDeviceLanguages
                val installed = recognitionSupport.installedOnDeviceLanguages

                if (supported.isNotEmpty() && installed.isEmpty() && onDeviceAvailable) {
                    _isOfflineAvailable.update { true }
                }
            }

            override fun onError(error: Int) {}
        }

        recognizer.checkRecognitionSupport(intent, executor, supportListener)
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun downloadSttModel() {
        val recognizer = SpeechRecognizer.createSpeechRecognizer(context)
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            downloadSttModelWithListener(recognizer, intent)
        } else {
            recognizer.triggerModelDownload(intent)
            _isOfflineAvailable.update { false }
        }
    }

    @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
    private fun downloadSttModelWithListener(recognizer: SpeechRecognizer, intent: Intent) {
        val executor = Executors.newSingleThreadExecutor()
        val listener = object : ModelDownloadListener {
            override fun onSuccess() {
                _isOfflineAvailable.update { false }
            }

            override fun onProgress(completedPercent: Int) {}
            override fun onScheduled() {}
            override fun onError(error: Int) {}
        }

        recognizer.triggerModelDownload(intent, executor, listener)
    }
}