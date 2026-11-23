package com.cogniapp.audio

import android.content.Context
import android.speech.tts.TextToSpeech
import android.speech.tts.Voice
import com.cogniapp.model.CogniVoice
import java.util.Locale

/**
 * Controller providing calm, offline Text-to-Speech (Read Aloud) capabilities for CogniApp.
 *
 * Implements:
 * - Offline English voice filtering from [TextToSpeech.getVoices] (!isNetworkConnectionRequired).
 * - Human-readable label formatting by region and voice index.
 * - Calm speech pacing (rate = 0.88f) and gentle pitch (pitch = 0.98f).
 * - Voice preview speaking a short calming sample phrase.
 * - Safe initialization and lifecycle cleanup.
 */
class CogniTtsController(
    private val context: Context,
    private val onInitComplete: ((List<CogniVoice>) -> Unit)? = null
) : TextToSpeech.OnInitListener {

    private var tts: TextToSpeech? = null
    private var isInitialized = false
    private var pendingVoiceName: String? = null

    companion object {
        const val CALM_SPEECH_RATE = 0.88f
        const val CALM_PITCH = 0.98f
        const val PREVIEW_PHRASE = "Hello, this is CogniApp. Practice memory with calm focus."
        const val UTTERANCE_ID = "CogniTtsUtterance"
    }

    init {
        try {
            tts = TextToSpeech(context.applicationContext, this)
        } catch (_: Exception) {
            tts = null
        }
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            isInitialized = true
            try {
                tts?.setSpeechRate(CALM_SPEECH_RATE)
                tts?.setPitch(CALM_PITCH)
            } catch (_: Exception) {}

            val voices = queryAvailableVoices()
            pendingVoiceName?.let { setVoice(it) }
            onInitComplete?.invoke(voices)
        }
    }

    /**
     * Queries offline English voices from TextToSpeech engine and formats them with clean labels.
     */
    fun queryAvailableVoices(): List<CogniVoice> {
        val rawVoices = try {
            tts?.voices
        } catch (_: Exception) {
            null
        }

        val offlineEnglishVoices = rawVoices?.filter { voice ->
            voice.locale.language.equals("en", ignoreCase = true) &&
                    !voice.isNetworkConnectionRequired
        }?.sortedBy { it.name } ?: emptyList()

        if (offlineEnglishVoices.isEmpty()) {
            return listOf(
                CogniVoice(
                    name = "default",
                    displayName = "System Default",
                    locale = "en-US",
                    isDefault = true
                )
            )
        }

        return offlineEnglishVoices.mapIndexed { index, voice ->
            val region = when (voice.locale.country.uppercase()) {
                "US" -> "US"
                "GB", "UK" -> "UK"
                "AU" -> "Australia"
                "CA" -> "Canada"
                "IN" -> "India"
                "IE" -> "Ireland"
                "NZ" -> "New Zealand"
                "ZA" -> "South Africa"
                else -> voice.locale.displayCountry.ifBlank { "English" }
            }
            val label = "English ($region) - Voice ${index + 1}"
            CogniVoice(
                name = voice.name,
                displayName = label,
                locale = voice.locale.toLanguageTag(),
                isDefault = index == 0
            )
        }
    }

    /**
     * Applies the specified voice to the TTS engine.
     */
    fun setVoice(voiceName: String) {
        if (!isInitialized || tts == null) {
            pendingVoiceName = voiceName
            return
        }
        if (voiceName == "default") return

        try {
            val targetVoice = tts?.voices?.find { it.name == voiceName }
            if (targetVoice != null) {
                tts?.voice = targetVoice
            }
        } catch (_: Exception) {}
    }

    /**
     * Speaks the given text using the configured voice and speech rate.
     */
    fun speak(text: String) {
        if (!isInitialized || tts == null || text.isBlank()) return
        try {
            tts?.speak(text, TextToSpeech.QUEUE_FLUSH, null, UTTERANCE_ID)
        } catch (_: Exception) {}
    }

    /**
     * Speaks a short calm preview phrase to test the selected voice.
     */
    fun previewVoice(voiceName: String? = null) {
        if (voiceName != null) {
            setVoice(voiceName)
        }
        speak(PREVIEW_PHRASE)
    }

    /**
     * Stops current speech playback immediately.
     */
    fun stop() {
        try {
            tts?.stop()
        } catch (_: Exception) {}
    }

    /**
     * Releases TTS resources on component destruction.
     */
    fun shutdown() {
        try {
            tts?.stop()
            tts?.shutdown()
        } catch (_: Exception) {}
        tts = null
        isInitialized = false
    }
}
