/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.ringtone_manager

import android.content.Context
import android.media.RingtoneManager
import android.net.Uri
import androidx.core.net.toUri

class RingtoneManager(
    private val context: Context,
) : RingtoneManagerContract {

    override fun getRingtoneTitle(ringtone: String): String {
        val uri = if (ringtone.isEmpty()) {
            getDefaultRingtoneUri()
        } else {
            ringtone.toUri()
        }

        val title = RingtoneManager
            .getRingtone(context, uri)
            .getTitle(context)

        return title
    }

    override fun getDefaultRingtone(): String {
        return getDefaultRingtoneUri().toString()
    }

    private fun getDefaultRingtoneUri(): Uri {
        return RingtoneManager
            .getActualDefaultRingtoneUri(context, RingtoneManager.TYPE_ALARM)
    }
}
