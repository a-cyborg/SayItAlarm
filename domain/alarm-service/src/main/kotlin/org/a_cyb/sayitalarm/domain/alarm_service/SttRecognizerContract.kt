/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.domain.alarm_service

import kotlinx.coroutines.flow.StateFlow

interface SttRecognizerContract {
    val recognizerState: StateFlow<RecognizerState>
    val isOnDevice: StateFlow<IsOnDevice>

    fun startListening()
    fun stopRecognizer()

    sealed interface RecognizerState {
        data object Initial : RecognizerState
        data object Ready : RecognizerState
        data class Processing(val partialResults: String) : RecognizerState
        data class Done(val result: String) : RecognizerState
        data class Error(val cause: String) : RecognizerState
    }

    enum class IsOnDevice { True, False }
}

interface SttRecognizerOnDeviceHelper {
    val isOfflineAvailable: StateFlow<Boolean>

    fun downloadSttModel()
}
