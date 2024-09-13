/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.util.time_flow.di

import org.a_cyb.sayitalarm.util.time_flow.TimeFlow
import org.a_cyb.sayitalarm.util.time_flow.TimeFlowContract
import org.koin.dsl.module

val timeFlowModule = module {
    single<TimeFlowContract> { TimeFlow }
}