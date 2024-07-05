/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.domain.di

import org.a_cyb.sayitalarm.domain.interactor.AddInteractor
import org.a_cyb.sayitalarm.domain.interactor.InteractorContract
import org.a_cyb.sayitalarm.domain.interactor.ListInteractor
import org.a_cyb.sayitalarm.domain.interactor.SettingsInteractor
import org.koin.dsl.module

val domainModule = module {
    single<InteractorContract.AddInteractor> {
        AddInteractor(get(), get())
    }

    single<InteractorContract.ListInteractor> {
        ListInteractor(get(), get())
    }

    single<InteractorContract.SettingsInteractor> {
        SettingsInteractor(get())
    }
}
