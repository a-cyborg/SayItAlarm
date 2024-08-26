/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.presentation.viewmodel.di

import org.a_cyb.sayitalarm.presentation.AddContract
import org.a_cyb.sayitalarm.presentation.AlarmContract
import org.a_cyb.sayitalarm.presentation.EditContract
import org.a_cyb.sayitalarm.presentation.ListContract
import org.a_cyb.sayitalarm.presentation.SayItContract
import org.a_cyb.sayitalarm.presentation.SettingsContract
import org.a_cyb.sayitalarm.presentation.viewmodel.AddViewModel
import org.a_cyb.sayitalarm.presentation.viewmodel.AlarmViewModel
import org.a_cyb.sayitalarm.presentation.viewmodel.EditViewModel
import org.a_cyb.sayitalarm.presentation.viewmodel.ListViewModel
import org.a_cyb.sayitalarm.presentation.viewmodel.SayItViewModel
import org.a_cyb.sayitalarm.presentation.viewmodel.SettingsViewModel
import org.a_cyb.sayitalarm.presentation.viewmodel.converter.AlarmUIConverterContract
import org.a_cyb.sayitalarm.presentation.viewmodel.converter.AlarmUiConverter
import org.a_cyb.sayitalarm.presentation.viewmodel.mapper.AlarmMapper
import org.a_cyb.sayitalarm.presentation.viewmodel.mapper.AlarmMapperContract
import org.a_cyb.sayitalarm.presentation.viewmodel.time_flow.TimeFlow
import org.a_cyb.sayitalarm.presentation.viewmodel.time_flow.TimeFlowContract
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.module.Module
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
        ListViewModel(get(), get(), get())
    } bind ListContract.ListViewModel::class

    viewModel {
        SettingsViewModel(get(), get())
    } bind SettingsContract.SettingsViewModel::class

    viewModel {
        AlarmViewModel(get(), get(), get())
    } bind AlarmContract.AlarmViewModel::class

    viewModel {
        SayItViewModel(get(), get(), get(), get())
    } bind SayItContract.SayItViewModel::class

    single<AlarmMapperContract> {
        AlarmMapper(get(), get(), get(), get())
    }

    factory<AlarmUIConverterContract> {
        AlarmUiConverter(get(), get())
    }

    factory<TimeFlowContract> {
        TimeFlow
    }
}
