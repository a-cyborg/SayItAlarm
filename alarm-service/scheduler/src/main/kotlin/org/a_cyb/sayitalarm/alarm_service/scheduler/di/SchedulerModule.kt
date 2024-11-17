/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.alarm_service.scheduler.di

import org.a_cyb.sayitalarm.alarm_service.scheduler.AlarmScheduler
import org.a_cyb.sayitalarm.domain.alarm_service.AlarmSchedulerContract
import org.koin.dsl.module

val schedulerModule = module {
    single<AlarmSchedulerContract> {
        AlarmScheduler(get())
    }
}
