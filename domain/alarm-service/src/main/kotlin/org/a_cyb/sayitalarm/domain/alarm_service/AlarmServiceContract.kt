/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.domain.alarm_service

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.StateFlow
import org.a_cyb.sayitalarm.entity.AlertType
import org.a_cyb.sayitalarm.entity.Label
import org.a_cyb.sayitalarm.entity.Ringtone
import org.a_cyb.sayitalarm.entity.SayItScripts
import org.a_cyb.sayitalarm.entity.Snooze

sealed interface AlarmServiceContract {

    interface AlarmScheduler {
        suspend fun scheduleAlarms(scope: CoroutineScope)
        fun scheduleSnooze(alarmId: Long, snooze: Snooze)
        suspend fun cancelAlarm(alarmId: Long, scope: CoroutineScope)
    }

    interface AlarmService {
        fun ringAlarm(ringtone: Ringtone, alertType: AlertType)
        fun startSayIt()
        fun startSnooze()
        fun stopService()
    }

    interface AlarmServiceController {
        val controllerState: StateFlow<ControllerState>

        fun onServiceBind(service: AlarmService, alarmId: Long)
        fun onServiceDisconnected()
        fun startSayIt()
        fun startSnooze()
        fun terminate()

        sealed interface ControllerState {
            data object Initial : ControllerState
            data class Ringing(val label: Label) : ControllerState
            data class RunningSayIt(val scripts: SayItScripts) : ControllerState
            data object Error : ControllerState
        }
    }

    interface SayItProcessor {
        val processorState: StateFlow<SayItProcessorState>

        fun startSayIt(script: String)
        fun stopProcessor()

        sealed interface SayItProcessorState {
            data object Initial : SayItProcessorState
            data class Processing(val script: Script, val sttResult: SttResult) : SayItProcessorState
            data class Processed(val processResult: ProcessResult, val sttResult: SttResult) : SayItProcessorState
            data class Error(val cause: String) : SayItProcessorState
        }

        data class Script(val script: String)
        data class SttResult(val text: String)
        enum class ProcessResult { SUCCESS, FAILED }
    }

    interface SttRecognizer {
        val recognizerState: StateFlow<RecognizerState>
        val rmsDbState: StateFlow<RecognizerRmsDb>

        fun startListening()
        fun stopRecognizer()

        data class RecognizerRmsDb(val rmsDb: Float)

        sealed interface RecognizerState {
            data object Initial : RecognizerState
            data object Ready : RecognizerState
            data class Processing(val partialResults: String) : RecognizerState
            data class Done(val result: String) : RecognizerState
            data class Error(val cause: String) : RecognizerState
        }
    }
}
