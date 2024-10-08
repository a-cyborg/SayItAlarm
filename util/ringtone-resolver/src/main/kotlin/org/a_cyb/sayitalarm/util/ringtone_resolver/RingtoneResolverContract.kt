/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.util.ringtone_resolver

interface RingtoneResolverContract {
    fun getRingtoneTitle(ringtone: String): Result<String>
    fun getDefaultRingtone(): Result<String>
}
