/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.alarm_service.core.di

import org.a_cyb.sayitalarm.alarm_service.core.AlarmScheduler
import org.a_cyb.sayitalarm.domain.alarm_service.AlarmServiceContract
import org.koin.dsl.module

val alarmServiceModule = module {
    single<AlarmServiceContract.AlarmScheduler> {
        AlarmScheduler(get())
    }
}