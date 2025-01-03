/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.presentation.viewmodel.di

import org.a_cyb.sayitalarm.presentation.contracts.AddContract
import org.a_cyb.sayitalarm.presentation.contracts.AlarmContract
import org.a_cyb.sayitalarm.presentation.contracts.EditContract
import org.a_cyb.sayitalarm.presentation.contracts.ListContract
import org.a_cyb.sayitalarm.presentation.contracts.SayItContract
import org.a_cyb.sayitalarm.presentation.contracts.SettingsContract
import org.a_cyb.sayitalarm.presentation.viewmodel.AddViewModel
import org.a_cyb.sayitalarm.presentation.viewmodel.AlarmViewModel
import org.a_cyb.sayitalarm.presentation.viewmodel.EditViewModel
import org.a_cyb.sayitalarm.presentation.viewmodel.ListViewModel
import org.a_cyb.sayitalarm.presentation.viewmodel.SayItViewModel
import org.a_cyb.sayitalarm.presentation.viewmodel.SettingsViewModel
import org.a_cyb.sayitalarm.presentation.viewmodel.converter.AlarmUiConverter
import org.a_cyb.sayitalarm.presentation.viewmodel.converter.AlarmUiConverterContract
import org.a_cyb.sayitalarm.presentation.viewmodel.mapper.AlarmMapper
import org.a_cyb.sayitalarm.presentation.viewmodel.mapper.AlarmMapperContract
import org.koin.core.module.Module
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.bind
import org.koin.dsl.module

val viewModelModule: Module = module {
    viewModel {
        AddViewModel(get(), get(), get())
    } bind AddContract.AddViewModel::class

    viewModel {
        EditViewModel(get(), get(), get(), get())
    } bind EditContract.EditViewModel::class

    viewModel {
        ListViewModel(get(), get(), get(), get())
    } bind ListContract.ListViewModel::class

    viewModel {
        SettingsViewModel(get(), get(), get())
    } bind SettingsContract.SettingsViewModel::class

    viewModel {
        AlarmViewModel(get(), get(), get())
    } bind AlarmContract.AlarmViewModel::class

    viewModel {
        SayItViewModel(get(), get())
    } bind SayItContract.SayItViewModel::class

    single<AlarmMapperContract> {
        AlarmMapper(get(), get(), get(), get())
    }

    factory<AlarmUiConverterContract> {
        AlarmUiConverter(get(), get())
    }
}
