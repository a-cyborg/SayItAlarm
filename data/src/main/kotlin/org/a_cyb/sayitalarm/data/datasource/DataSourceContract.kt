/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.data.datasource

import kotlinx.coroutines.flow.Flow
import org.a_cyb.sayitalarm.data.model.AlarmEntity

interface DataSourceContract {

    interface AlarmDataSource {
        fun getAllByTimeAsc(): Flow<Result<List<AlarmEntity>>>
        suspend fun getById(id: Long): Result<AlarmEntity>
        suspend fun insert(alarm: AlarmEntity)
        suspend fun update(alarm: AlarmEntity)
        suspend fun updateEnabled(id: Long, enabled: Boolean)
        suspend fun delete(id: Long)
    }
}