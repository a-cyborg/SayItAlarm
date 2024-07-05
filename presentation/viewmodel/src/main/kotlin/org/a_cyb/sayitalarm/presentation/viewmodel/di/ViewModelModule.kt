/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.presentation.viewmodel.di

import org.a_cyb.sayitalarm.presentation.AddContract
import org.a_cyb.sayitalarm.presentation.EditContract
import org.a_cyb.sayitalarm.presentation.ListContract
import org.a_cyb.sayitalarm.presentation.SettingsContract
import org.a_cyb.sayitalarm.presentation.viewmodel.AddViewModel
import org.a_cyb.sayitalarm.presentation.viewmodel.EditViewModel
import org.a_cyb.sayitalarm.presentation.viewmodel.ListViewModel
import org.a_cyb.sayitalarm.presentation.viewmodel.SettingsViewModel
import org.koin.core.module.Module
import org.koin.dsl.module

val viewModelModule: Module = module {
    single<AddContract.AddViewModel> {
        AddViewModel(get(), get(), get(), get())
    }

    single<EditContract.EditViewModel> {
        EditViewModel(get(), get(), get(), get(), get())
    }

    single<ListContract.ListViewModel> {
        ListViewModel(get(), get(), get())
    }

    single<SettingsContract.SettingsViewModel> {
        SettingsViewModel(get(), get())
    }
}
