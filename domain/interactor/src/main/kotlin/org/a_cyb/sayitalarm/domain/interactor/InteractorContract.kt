/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.domain.interactor

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import org.a_cyb.sayitalarm.entity.Alarm
import org.a_cyb.sayitalarm.entity.Label
import org.a_cyb.sayitalarm.entity.SayIt
import org.a_cyb.sayitalarm.entity.Settings
import org.a_cyb.sayitalarm.entity.Snooze
import org.a_cyb.sayitalarm.entity.Theme
import org.a_cyb.sayitalarm.entity.TimeOut

interface InteractorContract {

    interface AddInteractor {
        fun save(alarm: Alarm, scope: CoroutineScope)
    }

    interface EditInteractor {
        val alarm: SharedFlow<Result<Alarm>>

        fun getAlarm(id: Long, scope: CoroutineScope)
        fun update(alarm: Alarm, scope: CoroutineScope)
    }

    interface ListInteractor {
        val alarms: Flow<Result<List<Alarm>>>

        fun setEnabled(id: Long, enabled: Boolean, scope: CoroutineScope)
        fun deleteAlarm(id: Long, scope: CoroutineScope)
    }

    interface SettingsInteractor {
        val settings: Flow<Result<Settings>>

        fun insertOrIgnore(settings: Settings, scope: CoroutineScope)
        fun setTimeOut(timeOut: TimeOut, scope: CoroutineScope)
        fun setSnooze(snooze: Snooze, scope: CoroutineScope)
        fun setTheme(theme: Theme, scope: CoroutineScope)
    }

    interface AlarmInteractor {
        val label: SharedFlow<Result<Label>>
        val event: SharedFlow<AlarmInteractorEvent>

        fun startAlarm(scope: CoroutineScope)
        fun stopAlarm()
        fun snooze()

        enum class AlarmInteractorEvent {
            ERROR_AUDIO_PLAYER,
            ERROR_SERVICE_DISCONNECTED,
            ERROR_ALARM_NOT_FOUND,
        }
    }

    interface SayItInteractor {
        val sayItState: StateFlow<SayItState>

        fun startSayIt(scope: CoroutineScope)
        fun startListening()
        fun stopSayIt()
        fun shutdown()

        sealed interface SayItState {
            data object Initial : SayItState
            data object Ready : SayItState
            data class InProgress(val status: ProgressStatus, val sayIt: SayIt, val count: Count) : SayItState
            data class Error(val error: SayItError) : SayItState
            data object Completed : SayItState
        }

        data class Count(val current: Int, val total: Int)
        enum class ProgressStatus { IN_PROGRESS, SUCCESS, FAILED }
        enum class SayItError {
            ALARM_LOAD_FAILED,
            SERVICE_DISCONNECTED_WHILE_RESOLVING_ALARM_ID,
            SPEECH_RECOGNIZER_ERROR,
        }
    }
}
