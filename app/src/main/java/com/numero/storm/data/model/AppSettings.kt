package com.numero.storm.data.model

import com.numero.storm.domain.calculator.NumerologySystem

/**
 * Application settings stored in DataStore.
 */
data class AppSettings(
    val language: AppLanguage = AppLanguage.ENGLISH,
    val themeMode: ThemeMode = ThemeMode.SYSTEM,
    val numerologySystem: NumerologySystem = NumerologySystem.PYTHAGOREAN,
    val showMasterNumbers: Boolean = true,
    val showKarmicDebt: Boolean = true,
    val defaultProfileId: Long? = null,
    val hasCompletedOnboarding: Boolean = false,
    val lastSyncTimestamp: Long = 0
)

/**
 * Supported application languages.
 */
enum class AppLanguage(val code: String, val displayName: String, val nativeName: String) {
    ENGLISH("en", "English", "English"),
    NEPALI("ne", "Nepali", "नेपाली")
}

/**
 * Theme mode options.
 */
enum class ThemeMode {
    LIGHT,
    DARK,
    SYSTEM
}
