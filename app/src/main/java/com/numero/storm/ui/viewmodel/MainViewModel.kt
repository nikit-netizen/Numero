package com.numero.storm.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.numero.storm.data.model.AppLanguage
import com.numero.storm.data.model.AppSettings
import com.numero.storm.data.model.ThemeMode
import com.numero.storm.data.repository.SettingsRepository
import com.numero.storm.domain.calculator.NumerologySystem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val settingsRepository: SettingsRepository
) : ViewModel() {

    val settings: StateFlow<AppSettings> = settingsRepository.getSettings()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = AppSettings()
        )

    fun setLanguage(language: AppLanguage) {
        viewModelScope.launch {
            settingsRepository.setLanguage(language)
        }
    }

    fun setThemeMode(themeMode: ThemeMode) {
        viewModelScope.launch {
            settingsRepository.setThemeMode(themeMode)
        }
    }

    fun setNumerologySystem(system: NumerologySystem) {
        viewModelScope.launch {
            settingsRepository.setNumerologySystem(system)
        }
    }

    fun setShowMasterNumbers(show: Boolean) {
        viewModelScope.launch {
            settingsRepository.setShowMasterNumbers(show)
        }
    }

    fun setShowKarmicDebt(show: Boolean) {
        viewModelScope.launch {
            settingsRepository.setShowKarmicDebt(show)
        }
    }

    fun completeOnboarding() {
        viewModelScope.launch {
            settingsRepository.setOnboardingCompleted(true)
        }
    }
}
