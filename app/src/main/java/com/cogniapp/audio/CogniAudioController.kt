package com.cogniapp.audio

import android.content.Context
import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioTrack
import android.media.MediaPlayer
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import com.cogniapp.R
import kotlin.concurrent.thread

/**
 * Controller providing calm, soothing background ambient music
 * and gentle tactile/sound feedback for CogniApp.
 *
 * Implements:
 * - Soothing, adult harmonic ambient pad loop (`R.raw.ambient_calm_loop`)
 *   at comfortable volume (~0.30f), seamlessly looping without clicks.
 * - Automatic pause during active Speech-to-Text listening and auto-resume.
 * - Lifecycle synchronization (`onPause`, `onResume`, `release`).
 * - Gentle match chime and tactile haptics.
 */
class CogniAudioController(private val context: Context) {

    @Volatile
    private var isMusicRequested: Boolean = false

    @Volatile
    private var isPausedForSpeech: Boolean = false

    private var mediaPlayer: MediaPlayer? = null

    private val vibrator: Vibrator? by lazy {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val vibratorManager = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as? VibratorManager
            vibratorManager?.defaultVibrator
        } else {
            @Suppress("DEPRECATION")
            context.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
        }
    }

    /**
     * Starts or stops soothing background music based on [enabled] flag.
     */
    @Synchronized
    fun setMusicEnabled(enabled: Boolean) {
        isMusicRequested = enabled
        if (enabled) {
            isPausedForSpeech = false
            startAmbientMusic()
        } else {
            stopAmbientMusic()
        }
    }

    /**
     * Temporarily pauses background ambient music when Speech-to-Text starts listening,
     * ensuring microphone captures the user's voice clearly.
     */
    @Synchronized
    fun pauseMusicForSpeech() {
        if (isMusicRequested && mediaPlayer?.isPlaying == true) {
            isPausedForSpeech = true
            try {
                mediaPlayer?.pause()
            } catch (_: Exception) {}
        }
    }

    /**
     * Automatically resumes background ambient music when Speech-to-Text finishes listening.
     */
    @Synchronized
    fun resumeMusicAfterSpeech() {
        if (isMusicRequested && isPausedForSpeech) {
            isPausedForSpeech = false
            try {
                mediaPlayer?.start()
            } catch (_: Exception) {
                startAmbientMusic()
            }
        }
    }

    /**
     * Starts looping playback of the ambient music loop asset at comfortable volume (~0.30f).
     */
    @Synchronized
    fun startAmbientMusic() {
        if (!isMusicRequested) return
        try {
            if (mediaPlayer == null) {
                mediaPlayer = MediaPlayer.create(context, R.raw.ambient_calm_loop)?.apply {
                    isLooping = true
                    setVolume(0.30f, 0.30f)
                }
            }
            mediaPlayer?.let { player ->
                if (!player.isPlaying) {
                    player.start()
                }
            }
        } catch (_: Exception) {
            // Fallback gracefully if hardware audio focus fails
        }
    }

    /**
     * Pauses or stops ambient music playback.
     */
    @Synchronized
    fun stopAmbientMusic() {
        try {
            mediaPlayer?.pause()
            mediaPlayer?.seekTo(0)
        } catch (_: Exception) {}
    }

    /**
     * Activity onPause lifecycle hook.
     */
    @Synchronized
    fun onPause() {
        try {
            if (mediaPlayer?.isPlaying == true) {
                mediaPlayer?.pause()
            }
        } catch (_: Exception) {}
    }

    /**
     * Activity onResume lifecycle hook.
     */
    @Synchronized
    fun onResume(musicEnabled: Boolean) {
        isMusicRequested = musicEnabled
        if (musicEnabled && !isPausedForSpeech) {
            startAmbientMusic()
        }
    }

    /**
     * Releases audio player resources cleanly on activity destroy.
     */
    @Synchronized
    fun release() {
        try {
            mediaPlayer?.stop()
            mediaPlayer?.release()
        } catch (_: Exception) {}
        mediaPlayer = null
    }

    /**
     * Plays a gentle, pleasant confirmation chime when an answer matches.
     */
    fun playMatchedChime(hapticsAndSoundEnabled: Boolean) {
        if (!hapticsAndSoundEnabled) return

        triggerGentleHaptic()

        thread(name = "CogniChimeThread", isDaemon = true) {
            try {
                val sampleRate = 22050
                val durationMs = 260
                val numSamples = (sampleRate * (durationMs / 1000.0)).toInt()
                val buffer = ShortArray(numSamples)

                // Gentle two-tone chord (C5 523Hz + G5 784Hz) with soft exponential decay
                val f1 = 523.25
                val f2 = 783.99
                for (i in 0 until numSamples) {
                    val t = i.toDouble() / sampleRate
                    val decay = kotlin.math.exp(-10.0 * t)
                    val wave = (0.6 * kotlin.math.sin(2.0 * Math.PI * f1 * t) +
                            0.4 * kotlin.math.sin(2.0 * Math.PI * f2 * t)) * decay
                    val sample = (wave * 0.20 * Short.MAX_VALUE).toInt().coerceIn(Short.MIN_VALUE.toInt(), Short.MAX_VALUE.toInt())
                    buffer[i] = sample.toShort()
                }

                val track = AudioTrack.Builder()
                    .setAudioAttributes(
                        AudioAttributes.Builder()
                            .setUsage(AudioAttributes.USAGE_ASSISTANCE_SONIFICATION)
                            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                            .build()
                    )
                    .setAudioFormat(
                        AudioFormat.Builder()
                            .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
                            .setSampleRate(sampleRate)
                            .setChannelMask(AudioFormat.CHANNEL_OUT_MONO)
                            .build()
                    )
                    .setBufferSizeInBytes(buffer.size * 2)
                    .setTransferMode(AudioTrack.MODE_STATIC)
                    .build()

                track.write(buffer, 0, buffer.size)
                track.play()
                Thread.sleep(durationMs.toLong() + 50L)
                track.stop()
                track.release()
            } catch (_: Exception) {}
        }
    }

    /**
     * Triggers a subtle tactile haptic tap on devices supporting vibration.
     */
    private fun triggerGentleHaptic() {
        try {
            vibrator?.let { vib ->
                if (vib.hasVibrator()) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        vib.vibrate(VibrationEffect.createPredefined(VibrationEffect.EFFECT_CLICK))
                    } else {
                        @Suppress("DEPRECATION")
                        vib.vibrate(35L)
                    }
                }
            }
        } catch (_: Exception) {}
    }
}
