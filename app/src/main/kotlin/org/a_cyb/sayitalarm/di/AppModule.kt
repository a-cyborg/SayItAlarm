/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.di

import android.content.Context
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import org.a_cyb.sayitalarm.alarm_service.di.alarmServiceModule
import org.a_cyb.sayitalarm.data.di.dataModule
import org.a_cyb.sayitalarm.database.di.databaseModule
import org.a_cyb.sayitalarm.domain.interactor.di.interactorModule
import org.a_cyb.sayitalarm.presentation.formatter.di.formatterModule
import org.a_cyb.sayitalarm.system_service.ringtone_resolver.di.ringtoneResolverModule
import org.a_cyb.sayitalarm.presentation.viewmodel.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.core.qualifier.named
import org.koin.dsl.module

fun initKoin(context: Context) = startKoin {
    androidContext(context)
    modules(appModule)
}

private val appModule = module {
    includes(
        viewModelModule,
        interactorModule,
        dataModule,
        databaseModule,
        alarmServiceModule,
        formatterModule,
        ringtoneResolverModule,
        module {
            single<CoroutineDispatcher>(named("io")) { Dispatchers.IO }
        },
    )
}
