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
import android.os.VibrationEffect
import android.os.Vibrator
import android.provider.Settings
import androidx.annotation.RequiresApi
import org.a_cyb.sayitalarm.alarm_service.core.AlarmService.Companion.DEFAULT_ALERT_TYPE_ORDINAL
import org.a_cyb.sayitalarm.entity.AlertType

interface AudioVibeControllerContract {
    fun startRinging(context: Context, ringtone: String?, alertTypeOrdinal: Int?)
    fun stopRinging()
}

object AudioVibeController : AudioVibeControllerContract {
    private var audioManager: AudioManager? = null
    private var vibrator: Vibrator? = null
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

    private fun getAudioAttribute(): AudioAttributes =
        AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_ALARM)
            .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
            .build()

    override fun startRinging(context: Context, ringtone: String?, alertTypeOrdinal: Int?) {
        val audioUri = resolveRingtoneUri(ringtone)
        val alertType = resolveAlertType(alertTypeOrdinal)

        when (alertType) {
            AlertType.VIBRATE_ONLY -> playVibration(context)
            AlertType.SOUND_ONLY -> playRingtone(context, audioUri)
            AlertType.SOUND_AND_VIBRATE -> {
                playRingtone(context, audioUri)
                playVibration(context)
            }
        }
    }

    private fun resolveRingtoneUri(ringtone: String?): Uri =
        ringtone
            ?.let { Uri.parse(ringtone) }
            ?: Settings.System.DEFAULT_ALARM_ALERT_URI

    private fun resolveAlertType(alertTypeOrdinal: Int?): AlertType =
        AlertType.entries
            .getOrNull(alertTypeOrdinal ?: DEFAULT_ALERT_TYPE_ORDINAL)
            ?: AlertType.SOUND_AND_VIBRATE

    private fun playRingtone(context: Context, audioUri: Uri) {
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

        mediaPlayer
            .run {
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

    private fun playVibration(context: Context) {
        val vibrationPatter = longArrayOf(0, 200, 400, 200, 400, 600)

        vibrator = (context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator)
            .apply {
                if (hasVibrator()) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        vibrate(VibrationEffect.createWaveform(vibrationPatter, 0))
                    } else {
                        vibrate(vibrationPatter, 0)
                    }
                }
            }
    }

    override fun stopRinging() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            audioManager?.abandonAudioFocusRequest(audioFocusRequest)
            audioManager = null
        }
        mediaPlayer.stop()
        mediaPlayer.release()
        vibrator?.cancel()
        vibrator = null
    }
}
