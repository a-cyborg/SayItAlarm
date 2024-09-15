/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.util.sound_effect_player

interface SoundEffectPlayerContract {
    fun playSuccessSoundEffect()
    fun playFailureSoundEffect()
    fun stopPlayer()
}
