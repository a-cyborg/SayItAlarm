/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.presentation

import kotlinx.coroutines.flow.StateFlow
import org.a_cyb.sayitalarm.presentation.command.AlarmCommandContract
import org.a_cyb.sayitalarm.presentation.command.CommandContract

interface AlarmContract {
    interface AlarmViewModel : AlarmCommandContract.StartSayIt, CommandContract.CommandExecutor {
        val state: StateFlow<AlarmUiState>
        val currentTime: StateFlow<String>
    }

    sealed interface AlarmUiState {
        data object Initial : AlarmUiState
        data object Ringing : AlarmUiState
        data object VoiceInputProcessing : AlarmUiState
        data object Completed : AlarmUiState
        data object Error : AlarmUiState
    }
}