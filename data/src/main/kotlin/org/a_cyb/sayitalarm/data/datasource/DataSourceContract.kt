/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.data.datasource

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import org.acyb.sayitalarm.database.Alarm as AlarmDTO
import org.acyb.sayitalarm.database.Get as SettingsDTO

interface DataSourceContract {

    interface AlarmDataSource {
        fun getAllByTimeAsc(dispatcher: CoroutineDispatcher): Flow<Result<List<AlarmDTO>>>
        suspend fun getById(id: Long): Result<AlarmDTO>
        suspend fun insert(alarmDto: AlarmDTO)
        suspend fun update(alarmDto: AlarmDTO)
        suspend fun updateEnabled(id: Long, enabled: Boolean)
        suspend fun delete(id: Long)
    }

    interface SettingsDataSource {
        fun getSettings(dispatcher: CoroutineDispatcher): Flow<Result<SettingsDTO>>
        suspend fun insert(settings: SettingsDTO)
        suspend fun setTimeOut(timeOut: Long)
        suspend fun setSnooze(snooze: Long)
        suspend fun setTheme(theme: Long)
    }
}
