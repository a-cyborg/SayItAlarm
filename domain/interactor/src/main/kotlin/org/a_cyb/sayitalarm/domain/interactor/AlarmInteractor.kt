/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.domain.interactor

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.runBlocking
import org.a_cyb.sayitalarm.domain.repository.RepositoryContract
import org.a_cyb.sayitalarm.entity.Alarm

class AlarmInteractor(
    private val alarmRepository: RepositoryContract.AlarmRepository,
    private val coroutineContext: CoroutineDispatcher,
) : InteractorContract.AlarmInteractor {

    override fun getAlarm(id: Long, scope: CoroutineScope): Result<Alarm> =
        runBlocking(coroutineContext) {
            alarmRepository
                .getAlarm(id, scope)
                .await()
        }
}


