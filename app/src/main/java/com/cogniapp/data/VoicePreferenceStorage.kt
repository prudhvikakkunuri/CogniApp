package com.cogniapp.data

import android.content.Context
import android.content.SharedPreferences

/**
 * Storage interface for persisting selected TTS voice preference.
 */
interface VoicePreferenceStorage {
    fun getSelectedVoice(): String?
    fun setSelectedVoice(voiceName: String?)
}

/**
 * In-memory storage for unit testing without Android dependencies.
 */
class InMemoryVoiceStorage(private var storedVoice: String? = null) : VoicePreferenceStorage {
    override fun getSelectedVoice(): String? = storedVoice
    override fun setSelectedVoice(voiceName: String?) {
        storedVoice = voiceName
    }
}

/**
 * SharedPreferences storage for persisting the selected offline voice across app launches.
 */
class SharedPreferencesVoiceStorage(context: Context) : VoicePreferenceStorage {
    private val prefs: SharedPreferences =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    override fun getSelectedVoice(): String? {
        return prefs.getString(KEY_SELECTED_VOICE, null)
    }

    override fun setSelectedVoice(voiceName: String?) {
        prefs.edit().apply {
            if (voiceName != null) {
                putString(KEY_SELECTED_VOICE, voiceName)
            } else {
                remove(KEY_SELECTED_VOICE)
            }
            apply()
        }
    }

    companion object {
        private const val PREFS_NAME = "cogniapp_preferences"
        private const val KEY_SELECTED_VOICE = "selected_tts_voice_name"
    }
}
