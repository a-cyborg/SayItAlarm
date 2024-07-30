/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.presentation.viewmodel.time_flow

import kotlinx.coroutines.flow.Flow
import org.a_cyb.sayitalarm.entity.Hour
import org.a_cyb.sayitalarm.entity.Minute

interface TimeFlowContract {
    val currentTimeFlow: Flow<Pair<Hour, Minute>>
}