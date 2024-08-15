/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.alarm_service.core.di

import org.a_cyb.sayitalarm.alarm_service.core.AlarmScheduler
import org.a_cyb.sayitalarm.alarm_service.core.AlarmServiceController
import org.a_cyb.sayitalarm.alarm_service.core.SayItRecognizer
import org.a_cyb.sayitalarm.alarm_service.core.util.AudioVibeController
import org.a_cyb.sayitalarm.alarm_service.core.util.AudioVibeControllerContract
import org.a_cyb.sayitalarm.domain.alarm_service.AlarmServiceContract
import org.koin.core.qualifier.named
import org.koin.dsl.module

val alarmServiceModule = module {
    single<AlarmServiceContract.AlarmScheduler> {
        AlarmScheduler(get())
    }

    single<AlarmServiceContract.AlarmServiceController> {
        AlarmServiceController(get(), get(), get(), get(named("io")))
    }

    single<AlarmServiceContract.SayItRecognizer> {
        SayItRecognizer(get())
    }

    single<AudioVibeControllerContract> {
        AudioVibeController
    }
}