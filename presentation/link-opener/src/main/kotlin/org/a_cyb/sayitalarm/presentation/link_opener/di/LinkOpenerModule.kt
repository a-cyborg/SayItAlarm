/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.presentation.link_opener.di

import org.a_cyb.sayitalarm.presentation.link_opener.LinkOpener
import org.a_cyb.sayitalarm.presentation.link_opener.LinkOpenerContract
import org.koin.dsl.module

val linkOpenerModule = module {
    single<LinkOpenerContract> {
        LinkOpener(get())
    }
}