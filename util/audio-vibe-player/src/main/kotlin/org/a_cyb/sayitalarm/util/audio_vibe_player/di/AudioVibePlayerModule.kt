/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.util.audio_vibe_player.di

import org.a_cyb.sayitalarm.util.audio_vibe_player.AudioVibePlayer
import org.a_cyb.sayitalarm.util.audio_vibe_player.AudioVibePlayerContract
import org.koin.dsl.module

val audioVibePlayerModule = module {
    factory<AudioVibePlayerContract> {
        AudioVibePlayer()
    }
}
