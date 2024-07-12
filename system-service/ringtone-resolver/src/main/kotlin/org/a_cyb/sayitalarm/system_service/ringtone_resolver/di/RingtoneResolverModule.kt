/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.system_service.ringtone_resolver.di

import org.a_cyb.sayitalarm.system_service.ringtone_resolver.RingtoneResolver
import org.a_cyb.sayitalarm.system_service.ringtone_resolver.RingtoneResolverContract
import org.koin.dsl.module

val ringtoneResolverModule = module {
    single<RingtoneResolverContract> {
        RingtoneResolver(get())
    }
}
