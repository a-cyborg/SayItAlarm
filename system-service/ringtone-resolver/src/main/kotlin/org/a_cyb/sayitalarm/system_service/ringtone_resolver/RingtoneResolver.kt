/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.system_service.ringtone_resolver

import android.content.Context
import android.content.res.Resources.NotFoundException
import android.media.RingtoneManager
import android.net.Uri
import android.provider.Settings
import androidx.core.net.toUri

class RingtoneResolver(private val context: Context) : RingtoneResolverContract {

    override fun getRingtoneTitle(ringtone: String): Result<String> {
        val resolvedRingtone = RingtoneManager.getRingtone(context, ringtone.toUri())

        return if (resolvedRingtone != null) {
            Result.success(resolvedRingtone.getTitle(context))
        } else {
            Result.failure(NotFoundException())
        }
    }

    override fun getDefaultRingtone(): Result<String> {
        val uri = getSystemDefaultRingtone()

        return if (uri != null) {
            Result.success(uri.toString())
        } else {
            Result.failure(NotFoundException())
        }
    }

    private fun getSystemDefaultRingtone(): Uri? {
        return Settings.System.DEFAULT_ALARM_ALERT_URI
    }
}
