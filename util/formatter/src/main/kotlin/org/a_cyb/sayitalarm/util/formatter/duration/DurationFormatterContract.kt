/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.util.formatter.duration

import kotlin.time.Duration

interface DurationFormatterContract {
    data class FormattedDuration(val short: String, val long: String)

    fun format(duration: Duration): FormattedDuration
}
