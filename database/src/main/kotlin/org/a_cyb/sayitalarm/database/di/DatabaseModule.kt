/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.database.di

import org.a_cyb.sayitalarm.database.DatabaseFactory
import org.a_cyb.sayitalarm.database.SayItDB
import org.acyb.sayitalarm.database.AlarmQueries
import org.koin.dsl.module

val databaseModule = module {
    single<SayItDB> {
        DatabaseFactory.getInstance(get())
    }

    single<AlarmQueries> {
        get<SayItDB>().alarmQueries
    }
}
