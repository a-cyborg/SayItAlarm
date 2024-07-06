/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.domain.interactor

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharedFlow
import org.a_cyb.sayitalarm.entity.Alarm
import org.a_cyb.sayitalarm.entity.Settings
import org.a_cyb.sayitalarm.entity.Snooze
import org.a_cyb.sayitalarm.entity.Theme
import org.a_cyb.sayitalarm.entity.TimeOut

interface InteractorContract {

    interface AddInteractor {
        fun save(alarm: Alarm, scope: CoroutineScope)
    }

    interface EditInteractor {
        fun getAlarm(id: Long, scope: CoroutineScope): Result<Alarm>
        fun update(alarm: Alarm, scope: CoroutineScope)
    }

    interface ListInteractor {
        fun getAllAlarms(): Flow<Result<List<Alarm>>>
        fun setEnabled(id: Long, enabled: Boolean, scope: CoroutineScope)
        fun deleteAlarm(id: Long, scope: CoroutineScope)
    }

    interface SettingsInteractor {
        fun getSettings(): Flow<Result<Settings>>
        fun setTimeOut(timeOut: TimeOut, scope: CoroutineScope)
        fun setSnooze(snooze: Snooze, scope: CoroutineScope)
        fun setTheme(theme: Theme, scope: CoroutineScope)
    }
}
