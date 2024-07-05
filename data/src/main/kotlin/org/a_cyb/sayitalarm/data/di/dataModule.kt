/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.data.di

import org.a_cyb.sayitalarm.data.AlarmRepository
import org.a_cyb.sayitalarm.data.datasource.AlarmDataSource
import org.a_cyb.sayitalarm.data.datasource.DataSourceContract
import org.a_cyb.sayitalarm.domain.repository.RepositoryContract
import org.koin.core.qualifier.named
import org.koin.dsl.module

val dataModule = module {
    single<DataSourceContract.AlarmDataSource> {
        AlarmDataSource(get())
    }

    single<RepositoryContract.AlarmRepository> {
        AlarmRepository(get(), get(named("io")))
    }
}
