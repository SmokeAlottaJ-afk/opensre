package com.aiforge.chat.repository

import android.content.SharedPreferences
import androidx.core.content.edit
import javax.inject.Inject

class SettingsRepository @Inject constructor() {

    companion object {
        private const val PREFS_NAME = "AIChatSettings"
        private const val KEY_API_ENDPOINT = "api_endpoint"
        private const val KEY_API_KEY = "api_key"
        private const val KEY_MODEL = "model"
        private const val KEY_THEME = "theme"

        private var instance: SettingsRepository? = null
        private var preferences: SharedPreferences? = null

        fun init(preferences: SharedPreferences) {
            this.preferences = preferences
            instance = SettingsRepository()
        }

        fun getInstance(): SettingsRepository {
            return instance ?: throw IllegalStateException("SettingsRepository not initialized")
        }
    }

    constructor(preferences: SharedPreferences) : this() {
        Companion.preferences = preferences
    }

    private val prefs: SharedPreferences
        get() = preferences ?: throw IllegalStateException("SharedPreferences not initialized")

    fun getApiEndpoint(): String {
        return prefs.getString(KEY_API_ENDPOINT, "https://api.openai.com/") ?: "https://api.openai.com/"
    }

    fun setApiEndpoint(endpoint: String) {
        prefs.edit { putString(KEY_API_ENDPOINT, endpoint) }
    }

    fun getApiKey(): String {
        return prefs.getString(KEY_API_KEY, "") ?: ""
    }

    fun setApiKey(key: String) {
        prefs.edit { putString(KEY_API_KEY, key) }
    }

    fun getModel(): String {
        return prefs.getString(KEY_MODEL, "gpt-3.5-turbo") ?: "gpt-3.5-turbo"
    }

    fun setModel(model: String) {
        prefs.edit { putString(KEY_MODEL, model) }
    }

    fun getTheme(): Theme {
        val themeValue = prefs.getString(KEY_THEME, "SYSTEM") ?: "SYSTEM"
        return Theme.valueOf(themeValue)
    }

    fun setTheme(theme: Theme) {
        prefs.edit { putString(KEY_THEME, theme.name) }
    }
}

enum class Theme {
    SYSTEM,
    LIGHT,
    DARK
}
