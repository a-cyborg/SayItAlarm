/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.presentation.sound_effect_player

import android.content.Context
import android.media.AudioAttributes
import android.media.SoundPool

class SoundEffectPlayer(private val context: Context) : SoundEffectPlayerContract {
    private var soundPool: SoundPool? = null
    private val successSoundsId by lazy { loadSuccessSound() }
    private val failSoundsId by lazy { loadFailSound() }

    init {
        soundPool = SoundPool.Builder()
            .setAudioAttributes(getAudioAttributes())
            .build()

        soundPool?.setOnLoadCompleteListener { soundPool, sampleId, _ ->
            soundPool.play(sampleId, 1f, 1f, 1, 0, 1f)
        }
    }

    private fun getAudioAttributes() = AudioAttributes.Builder()
        .setUsage(AudioAttributes.USAGE_ASSISTANCE_SONIFICATION)
        .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
        .build()

    // If the soundPool is null, there will be no sound.
    private fun loadSuccessSound(): Int = soundPool?.load(context, R.raw.success_sound, 1) ?: 0
    private fun loadFailSound(): Int = soundPool?.load(context, R.raw.fail_sound, 1) ?: 0

    override fun playSuccessSoundEffect() {
        soundPool?.play(successSoundsId, 1f, 1f, 1, 0, 1f)
    }

    override fun playFailureSoundEffect() {
        soundPool?.play(failSoundsId, 1f, 1f, 1, 0, 1f)
    }

    override fun stopPlayer() {
        soundPool?.release()
        soundPool = null
    }
}