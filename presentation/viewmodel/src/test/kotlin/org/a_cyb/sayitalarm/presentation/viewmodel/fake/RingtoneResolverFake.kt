/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.presentation.viewmodel.fake

import org.a_cyb.sayitalarm.util.ringtone_resolver.RingtoneResolverContract

class RingtoneResolverFake : RingtoneResolverContract {

    override fun getRingtoneTitle(ringtone: String): Result<String> =
        Result.success(
            ringtone.split("//")
                .last()
                .split(".")
                .first(),
        )

    override fun getDefaultRingtone(): Result<String> = Result.success("file://Radial.mp3")
}
