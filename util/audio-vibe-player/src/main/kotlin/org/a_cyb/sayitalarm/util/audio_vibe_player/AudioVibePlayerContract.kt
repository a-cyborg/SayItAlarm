/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.util.audio_vibe_player

import android.content.Context

interface AudioVibePlayerContract {
    fun startRinging(context: Context, ringtone: String?, alertTypeOrdinal: Int?)
    fun stopRinging()
}
