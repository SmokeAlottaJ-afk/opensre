package com.aiforge.chat.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aiforge.chat.repository.SettingsRepository
import com.aiforge.chat.repository.Theme
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val settingsRepository: SettingsRepository
) : ViewModel() {

    data class SettingsState(
        val apiEndpoint: String = "",
        val apiKey: String = "",
        val model: String = "gpt-3.5-turbo",
        val theme: Theme = Theme.SYSTEM
    )

    private val _settings = MutableStateFlow(SettingsState())
    val settings: StateFlow<SettingsState> = _settings.asStateFlow()

    init {
        loadSettings()
    }

    private fun loadSettings() {
        viewModelScope.launch {
            _settings.value = SettingsState(
                apiEndpoint = settingsRepository.getApiEndpoint(),
                apiKey = settingsRepository.getApiKey(),
                model = settingsRepository.getModel(),
                theme = settingsRepository.getTheme()
            )
        }
    }

    fun saveSettings(settings: SettingsState) {
        viewModelScope.launch {
            settingsRepository.setApiEndpoint(settings.apiEndpoint)
            settingsRepository.setApiKey(settings.apiKey)
            settingsRepository.setModel(settings.model)
            settingsRepository.setTheme(settings.theme)
            _settings.value = settings
        }
    }
}
