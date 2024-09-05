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
        fun scheduleAlarms()
        fun scheduleSnooze(alarmId: Long, snooze: Snooze)
        fun cancelAlarm(alarmId: Long)
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
        fun scheduleNextAlarm(scope: CoroutineScope)
        fun terminate()

        sealed interface ControllerState {
            data object Initial : ControllerState
            data class Ringing(val label: Label) : ControllerState
            data class RunningSayIt(val sayItScripts: SayItScripts) : ControllerState
            data object Error : ControllerState
        }
    }

    interface SttRecognizer {
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

    interface EditDistanceCalculator {
        fun calculateEditDistance(source: String, target: String): Int
    }
}
