/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.presentation.interactor

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharedFlow
import org.a_cyb.sayitalarm.entity.Alarm

interface ListInteractorContract {

    val alarms: SharedFlow<Result<List<Alarm>>>

    suspend fun setEnabled(id: Long, enabled: Boolean, scope: CoroutineScope)
    suspend fun deleteAlarm(id: Long, scope: CoroutineScope)
}
