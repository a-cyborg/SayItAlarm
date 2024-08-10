/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.domain.interactor.di

import org.a_cyb.sayitalarm.domain.interactor.AddInteractor
import org.a_cyb.sayitalarm.domain.interactor.AlarmInteractor
import org.a_cyb.sayitalarm.domain.interactor.EditInteractor
import org.a_cyb.sayitalarm.domain.interactor.InteractorContract
import org.a_cyb.sayitalarm.domain.interactor.ListInteractor
import org.a_cyb.sayitalarm.domain.interactor.SettingsInteractor
import org.koin.core.qualifier.named
import org.koin.dsl.module

val interactorModule = module {
    single<InteractorContract.AddInteractor> {
        AddInteractor(get(), get())
    }

    single<InteractorContract.AlarmInteractor> {
        AlarmInteractor(get(), get(named("io")))
    }

    single<InteractorContract.EditInteractor> {
        EditInteractor(get(), get())
    }

    single<InteractorContract.ListInteractor> {
        ListInteractor(get(), get())
    }

    single<InteractorContract.SettingsInteractor> {
        SettingsInteractor(get())
    }
}
