/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.util.audio_vibe_player

import android.content.Context
import android.media.AudioAttributes
import android.media.AudioFocusRequest
import android.media.AudioManager
import android.media.MediaPlayer
import android.net.Uri
import android.os.VibrationEffect
import android.os.Vibrator
import android.provider.Settings
import org.a_cyb.sayitalarm.entity.AlertType

class AudioVibePlayer : AudioVibePlayerContract {
    private var audioManager: AudioManager? = null
    private var vibrator: Vibrator? = null
    private var mediaPlayer: MediaPlayer? = null

    private val audioFocusChangeListener = AudioManager.OnAudioFocusChangeListener { focusChange ->
        when (focusChange) {
            AudioManager.AUDIOFOCUS_GAIN -> mediaPlayer?.start()
            AudioManager.AUDIOFOCUS_LOSS -> mediaPlayer?.stop()
        }
    }

    private val audioFocusRequest: AudioFocusRequest = AudioFocusRequest.Builder(AudioManager.AUDIOFOCUS_GAIN)
        .setAudioAttributes(getAudioAttribute())
        .setAcceptsDelayedFocusGain(true)
        .setOnAudioFocusChangeListener(audioFocusChangeListener)
        .build()

    private fun getAudioAttribute(): AudioAttributes = AudioAttributes.Builder()
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

    private fun resolveAlertType(alertTypeOrdinal: Int?): AlertType = AlertType.entries
        .getOrNull(alertTypeOrdinal ?: AlertType.SOUND_AND_VIBRATE.ordinal)
        ?: AlertType.SOUND_AND_VIBRATE

    @Throws
    private fun playRingtone(context: Context, ringtoneUri: Uri) {
        audioManager = (context.getSystemService(Context.AUDIO_SERVICE) as AudioManager).apply {
            setStreamVolume(AudioManager.STREAM_ALARM, getStreamMaxVolume(AudioManager.STREAM_ALARM), 0)
            requestAudioFocus(audioFocusRequest)
        }

        mediaPlayer = MediaPlayer().apply {
            try {
                setDataSource(context, ringtoneUri)
                setAudioAttributes(getAudioAttribute())
                isLooping = true
                prepare()
            } catch (exception: Exception) {
                throw exception
            }

            setOnPreparedListener { it.start() }
        }
    }

    private fun playVibration(context: Context) {
        val vibrationPatter = longArrayOf(0, 200, 400, 200, 400, 600)

        vibrator = (context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator).apply {
            if (hasVibrator()) {
                vibrate(VibrationEffect.createWaveform(vibrationPatter, 0))
            }
        }
    }

    override fun stopRinging() {
        audioManager?.abandonAudioFocusRequest(audioFocusRequest)
        if (mediaPlayer?.isPlaying == true) mediaPlayer?.stop()
        mediaPlayer?.release()
        vibrator?.cancel()

        audioManager = null
        mediaPlayer = null
        vibrator = null
    }
}
