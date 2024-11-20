/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.alarm_service.stt_recognizer.di

import org.a_cyb.sayitalarm.alarm_service.stt_recognizer.AndroidSttRecognizer
import org.a_cyb.sayitalarm.alarm_service.stt_recognizer.TemptRecognizerOfflineHelper
import org.a_cyb.sayitalarm.domain.alarm_service.SttRecognizerContract
import org.a_cyb.sayitalarm.domain.alarm_service.SttRecognizerOnDeviceHelper
import org.koin.dsl.module

val sttRecognizerModule = module {
    factory<SttRecognizerContract> {
        AndroidSttRecognizer(get())
    }
    factory<SttRecognizerOnDeviceHelper> {
        TemptRecognizerOfflineHelper(get())
    }
}
