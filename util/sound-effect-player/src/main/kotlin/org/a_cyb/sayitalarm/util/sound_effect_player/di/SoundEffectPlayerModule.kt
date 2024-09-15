/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.util.sound_effect_player.di

import org.a_cyb.sayitalarm.util.sound_effect_player.SoundEffectPlayer
import org.a_cyb.sayitalarm.util.sound_effect_player.SoundEffectPlayerContract
import org.koin.dsl.module

val soundEffectPlayerModule = module {
    single<SoundEffectPlayerContract> {
        SoundEffectPlayer(get())
    }
}
