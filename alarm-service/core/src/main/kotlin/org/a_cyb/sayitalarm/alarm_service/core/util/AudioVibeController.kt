/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.alarm_service.core.util

import android.content.Context
import android.media.AudioAttributes
import android.media.AudioFocusRequest
import android.media.AudioManager
import android.media.MediaPlayer
import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import org.a_cyb.sayitalarm.entity.AlertType

interface AudioVibeControllerContract {
    fun startRinging(context: Context, audioUri: Uri, alertType: AlertType)
    fun stopRinging()
}

object AudioVibeController : AudioVibeControllerContract {
    private var audioManager: AudioManager? = null
    private val mediaPlayer = MediaPlayer()

    private val audioFocusChangeListener = AudioManager
        .OnAudioFocusChangeListener { focusChange ->
            when (focusChange) {
                AudioManager.AUDIOFOCUS_GAIN -> mediaPlayer.start()
                AudioManager.AUDIOFOCUS_LOSS -> mediaPlayer.stop()
            }
        }

    @RequiresApi(Build.VERSION_CODES.O)
    private val audioFocusRequest: AudioFocusRequest =
        AudioFocusRequest
            .Builder(AudioManager.AUDIOFOCUS_GAIN)
            .setAudioAttributes(getAudioAttribute())
            .setAcceptsDelayedFocusGain(true)
            .setOnAudioFocusChangeListener(audioFocusChangeListener)
            .build()

    override fun startRinging(context: Context, audioUri: Uri, alertType: AlertType) {
        audioManager = (context.getSystemService(Context.AUDIO_SERVICE) as AudioManager)
            .apply {
                setStreamVolume(
                    AudioManager.STREAM_ALARM,
                    getStreamMaxVolume(AudioManager.STREAM_ALARM),
                    0
                )

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    requestAudioFocus(audioFocusRequest)
                }
            }

        mediaPlayer.run {
            try {
                setDataSource(context, audioUri)
                setAudioAttributes(getAudioAttribute())
                isLooping = true
                prepare()
            } catch (_: Exception) {
            }
            setOnPreparedListener { mediaPlayer.start() }
        }
    }

    override fun stopRinging() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            audioManager?.abandonAudioFocusRequest(audioFocusRequest)
        }
        mediaPlayer.stop()
    }

    private fun getAudioAttribute(): AudioAttributes =
        AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_ALARM)
            .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
            .build()
}
