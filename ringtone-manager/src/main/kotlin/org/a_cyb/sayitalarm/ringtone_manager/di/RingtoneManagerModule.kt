/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.ringtone_manager.di

import org.a_cyb.sayitalarm.ringtone_manager.RingtoneManager
import org.a_cyb.sayitalarm.ringtone_manager.RingtoneManagerContract
import org.koin.dsl.module

val ringtoneManagerModule = module {
    single<RingtoneManagerContract> {
        RingtoneManager(get())
    }
}
