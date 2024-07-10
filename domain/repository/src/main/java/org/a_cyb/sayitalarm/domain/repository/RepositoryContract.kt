package org.a_cyb.sayitalarm.domain.repository

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.flow.Flow
import org.a_cyb.sayitalarm.entity.Alarm
import org.a_cyb.sayitalarm.entity.Settings
import org.a_cyb.sayitalarm.entity.Snooze
import org.a_cyb.sayitalarm.entity.Theme
import org.a_cyb.sayitalarm.entity.TimeOut

interface RepositoryContract {

    interface AlarmRepository {
        fun getAllAlarms(): Flow<Result<List<Alarm>>>
        fun getAlarm(id: Long, scope: CoroutineScope): Deferred<Result<Alarm>>
        fun save(alarm: Alarm, scope: CoroutineScope)
        fun update(alarm: Alarm, scope: CoroutineScope)
        fun updateEnabled(id: Long, enabled: Boolean, scope: CoroutineScope)
        fun delete(id: Long, scope: CoroutineScope)
    }

    interface SettingsRepository {
        fun getSettings(): Flow<Result<Settings>>
        fun insertOrIgnore(settings: Settings, scope: CoroutineScope)
        fun setTimeOut(timeOut: TimeOut, scope: CoroutineScope)
        fun setSnooze(snooze: Snooze, scope: CoroutineScope)
        fun setTheme(theme: Theme, scope: CoroutineScope)
    }
}
