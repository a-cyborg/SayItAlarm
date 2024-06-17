/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.presentation.viewmodel.fake

import org.a_cyb.sayitalarm.ringtone_manager.RingtoneManagerContract

class RingtoneManagerFake : RingtoneManagerContract {

    override fun getRingtoneTitle(ringtone: String): String {
        return ringtone
    }

    override fun getDefaultRingtoneTitle(): String {
        return "Radial"
    }
}