/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.util.time_flow

import java.time.LocalTime
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.a_cyb.sayitalarm.entity.Hour
import org.a_cyb.sayitalarm.entity.Minute

object TimeFlow : TimeFlowContract {
    override val currentTimeFlow: Flow<Pair<Hour, Minute>> = flow {
        while (true) {
            emit(getCurrentTime())

            delay(FIVE_SEC_IN_MILLIS)
        }
    }

    private fun getCurrentTime(): Pair<Hour, Minute> {
        val now = LocalTime.now()

        return Pair(Hour(now.hour), Minute(now.minute))
    }

    private const val FIVE_SEC_IN_MILLIS = 5000L
}
