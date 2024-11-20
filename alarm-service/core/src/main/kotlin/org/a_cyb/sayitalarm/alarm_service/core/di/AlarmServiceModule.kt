/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.alarm_service.core.di

import org.a_cyb.sayitalarm.alarm_service.core.AlarmServiceController
import org.a_cyb.sayitalarm.alarm_service.core.AndroidSttRecognizer
import org.a_cyb.sayitalarm.alarm_service.core.TemptRecognizerOfflineHelper
import org.a_cyb.sayitalarm.alarm_service.core.util.AudioVibeController
import org.a_cyb.sayitalarm.alarm_service.core.util.AudioVibeControllerContract
import org.a_cyb.sayitalarm.alarm_service.core.util.EditDistanceCalculator
import org.a_cyb.sayitalarm.domain.alarm_service.AlarmServiceControllerContract
import org.a_cyb.sayitalarm.domain.alarm_service.EditDistanceCalculatorContract
import org.a_cyb.sayitalarm.domain.alarm_service.SttRecognizerContract
import org.a_cyb.sayitalarm.domain.alarm_service.SttRecognizerOnDeviceHelper
import org.koin.core.qualifier.named
import org.koin.dsl.module

val alarmServiceModule = module {
    single<AlarmServiceControllerContract> {
        AlarmServiceController(get(), get(), get(), get(named("ioScope")))
    }

    single<SttRecognizerContract> {
        AndroidSttRecognizer(get())
    }

    single<EditDistanceCalculatorContract> {
        EditDistanceCalculator
    }

    single<AudioVibeControllerContract> {
        AudioVibeController
    }

    single<SttRecognizerOnDeviceHelper> {
        TemptRecognizerOfflineHelper(get())
    }
}
