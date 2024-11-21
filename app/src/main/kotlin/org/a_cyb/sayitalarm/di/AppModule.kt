/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.di

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import org.a_cyb.sayitalarm.alarm_service.core.di.alarmServiceModule
import org.a_cyb.sayitalarm.alarm_service.scheduler.di.schedulerModule
import org.a_cyb.sayitalarm.alarm_service.stt_recognizer.di.sttRecognizerModule
import org.a_cyb.sayitalarm.data.di.dataModule
import org.a_cyb.sayitalarm.database.di.databaseModule
import org.a_cyb.sayitalarm.domain.interactor.di.interactorModule
import org.a_cyb.sayitalarm.presentation.viewmodel.di.viewModelModule
import org.a_cyb.sayitalarm.util.audio_vibe_player.di.audioVibePlayerModule
import org.a_cyb.sayitalarm.util.formatter.di.formatterModule
import org.a_cyb.sayitalarm.util.link_opener.di.linkOpenerModule
import org.a_cyb.sayitalarm.util.ringtone_resolver.di.ringtoneResolverModule
import org.a_cyb.sayitalarm.util.sound_effect_player.di.soundEffectPlayerModule
import org.a_cyb.sayitalarm.util.time_flow.di.timeFlowModule
import org.koin.core.qualifier.named
import org.koin.dsl.module

val appModule = module {
    includes(
        viewModelModule,
        interactorModule,
        dataModule,
        databaseModule,
        alarmServiceModule,
        schedulerModule,
        sttRecognizerModule,
        audioVibePlayerModule,
        formatterModule,
        linkOpenerModule,
        soundEffectPlayerModule,
        ringtoneResolverModule,
        timeFlowModule,
        module {
            single<CoroutineDispatcher>(named("io")) { Dispatchers.IO }
            single<CoroutineScope>(named("ioScope")) { CoroutineScope(SupervisorJob() + Dispatchers.IO) }
        },
    )
}
