/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.ringtone_resolver

import android.content.Context
import android.media.RingtoneManager
import android.net.Uri
import android.provider.Settings
import androidx.core.net.toUri

class RingtoneResolver(
    private val context: Context,
) : RingtoneResolverContract {

    override fun getRingtoneTitle(ringtone: String): String {
        val uri = if (ringtone.isEmpty()) {
            getSystemDefaultRingtone()
        } else {
            ringtone.toUri()
        }

        val title = RingtoneManager
            .getRingtone(context, uri)
            .getTitle(context)

        return title ?: "NO NAME"
    }

    override fun getDefaultRingtone(): String {
        return getSystemDefaultRingtone().toString()
    }

    private fun getSystemDefaultRingtone(): Uri {
        return Settings.System.DEFAULT_RINGTONE_URI
    }
}
