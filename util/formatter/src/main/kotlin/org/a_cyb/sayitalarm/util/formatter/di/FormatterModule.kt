/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.util.formatter.di

import org.a_cyb.sayitalarm.util.formatter.duration.DurationFormatter
import org.a_cyb.sayitalarm.util.formatter.duration.DurationFormatterContract
import org.a_cyb.sayitalarm.util.formatter.enum.AlarmTypeFormatter
import org.a_cyb.sayitalarm.util.formatter.enum.AlertTypeFormatter
import org.a_cyb.sayitalarm.util.formatter.enum.EnumFormatterContract
import org.a_cyb.sayitalarm.util.formatter.time.TimeFormatter
import org.a_cyb.sayitalarm.util.formatter.time.TimeFormatterContract
import org.a_cyb.sayitalarm.util.formatter.weekday.WeekdayFormatter
import org.a_cyb.sayitalarm.util.formatter.weekday.WeekdayFormatterContract
import org.koin.dsl.module

val formatterModule = module {
    single<DurationFormatterContract> {
        DurationFormatter()
    }

    single<EnumFormatterContract.AlertTypeFormatter> {
        AlertTypeFormatter(get())
    }

    single<EnumFormatterContract.AlarmTypeFormatter> {
        AlarmTypeFormatter(get())
    }

    single<TimeFormatterContract> {
        TimeFormatter(get())
    }

    single<WeekdayFormatterContract> {
        WeekdayFormatter(get())
    }
}
