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
        val alarmState: StateFlow<AlarmServiceState>

        fun onServiceBind(service: AlarmService, alarmId: Long)
        fun onServiceDisconnected()
        fun startSayIt()
        fun startSnooze()
        fun terminate()

        sealed interface AlarmServiceState {
            data object Initial : AlarmServiceState
            data class Ringing(val label: Label) : AlarmServiceState
            data object RunningSayIt : AlarmServiceState
            data object Completed : AlarmServiceState
            data object Error : AlarmServiceState
        }
    }

    interface SayItController {
        val sayItState: StateFlow<SayItState>

        fun startSayIt(scripts: SayItScripts)
        fun stopSayIt()

        sealed interface SayItState {
            data object Initial : SayItState
            data object Ready : SayItState
            data class Processing(val progressState: ProgressCounter, val currentScript: String) : SayItState
            data class Processed(val processResult: ProcessResult) : SayItState
            data class Done(val sayItResult: SayItResult) : SayItState
        }

        data class ProgressCounter(val current: Int, val total: Int)
        data class ProcessResult(val isSuccess: Boolean)
        data class SayItResult(val isSuccess: Boolean, val message: String)
    }

    interface SttRecognizer {
        val recognizerState: StateFlow<RecognizerState>
        val rmsDbState: StateFlow<RecognizerRmsDb>

        fun startListening()
        fun stopSttRecognizer()

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
