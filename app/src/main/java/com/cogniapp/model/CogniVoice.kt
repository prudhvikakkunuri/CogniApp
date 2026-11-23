package com.cogniapp.model

/**
 * Representation of an offline Text-to-Speech voice option for CogniApp.
 *
 * @property name Internal voice identifier used by TextToSpeech engine.
 * @property displayName User-friendly human-readable label (e.g. "English (US) - Voice 1").
 * @property locale Language/locale tag (e.g. "en-US").
 * @property isDefault Whether this voice is marked as the default voice option.
 */
data class CogniVoice(
    val name: String,
    val displayName: String,
    val locale: String = "en-US",
    val isDefault: Boolean = false
)
