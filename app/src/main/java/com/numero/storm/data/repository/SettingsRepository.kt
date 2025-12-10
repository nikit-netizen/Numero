package com.numero.storm.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.numero.storm.data.model.AppLanguage
import com.numero.storm.data.model.AppSettings
import com.numero.storm.data.model.ThemeMode
import com.numero.storm.domain.calculator.NumerologySystem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

interface SettingsRepository {
    fun getSettings(): Flow<AppSettings>
    suspend fun setLanguage(language: AppLanguage)
    suspend fun setThemeMode(themeMode: ThemeMode)
    suspend fun setNumerologySystem(system: NumerologySystem)
    suspend fun setShowMasterNumbers(show: Boolean)
    suspend fun setShowKarmicDebt(show: Boolean)
    suspend fun setDefaultProfileId(profileId: Long?)
    suspend fun setOnboardingCompleted(completed: Boolean)
    suspend fun updateLastSyncTimestamp()
}

class SettingsRepositoryImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>
) : SettingsRepository {

    private object PreferencesKeys {
        val LANGUAGE = stringPreferencesKey("language")
        val THEME_MODE = stringPreferencesKey("theme_mode")
        val NUMEROLOGY_SYSTEM = stringPreferencesKey("numerology_system")
        val SHOW_MASTER_NUMBERS = booleanPreferencesKey("show_master_numbers")
        val SHOW_KARMIC_DEBT = booleanPreferencesKey("show_karmic_debt")
        val DEFAULT_PROFILE_ID = longPreferencesKey("default_profile_id")
        val HAS_COMPLETED_ONBOARDING = booleanPreferencesKey("has_completed_onboarding")
        val LAST_SYNC_TIMESTAMP = longPreferencesKey("last_sync_timestamp")
    }

    override fun getSettings(): Flow<AppSettings> {
        return dataStore.data.map { preferences ->
            AppSettings(
                language = preferences[PreferencesKeys.LANGUAGE]?.let { code ->
                    AppLanguage.entries.find { it.code == code }
                } ?: AppLanguage.ENGLISH,
                themeMode = preferences[PreferencesKeys.THEME_MODE]?.let { mode ->
                    try {
                        ThemeMode.valueOf(mode)
                    } catch (e: IllegalArgumentException) {
                        ThemeMode.SYSTEM
                    }
                } ?: ThemeMode.SYSTEM,
                numerologySystem = preferences[PreferencesKeys.NUMEROLOGY_SYSTEM]?.let { system ->
                    try {
                        NumerologySystem.valueOf(system)
                    } catch (e: IllegalArgumentException) {
                        NumerologySystem.PYTHAGOREAN
                    }
                } ?: NumerologySystem.PYTHAGOREAN,
                showMasterNumbers = preferences[PreferencesKeys.SHOW_MASTER_NUMBERS] ?: true,
                showKarmicDebt = preferences[PreferencesKeys.SHOW_KARMIC_DEBT] ?: true,
                defaultProfileId = preferences[PreferencesKeys.DEFAULT_PROFILE_ID],
                hasCompletedOnboarding = preferences[PreferencesKeys.HAS_COMPLETED_ONBOARDING] ?: false,
                lastSyncTimestamp = preferences[PreferencesKeys.LAST_SYNC_TIMESTAMP] ?: 0
            )
        }
    }

    override suspend fun setLanguage(language: AppLanguage) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.LANGUAGE] = language.code
        }
    }

    override suspend fun setThemeMode(themeMode: ThemeMode) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.THEME_MODE] = themeMode.name
        }
    }

    override suspend fun setNumerologySystem(system: NumerologySystem) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.NUMEROLOGY_SYSTEM] = system.name
        }
    }

    override suspend fun setShowMasterNumbers(show: Boolean) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.SHOW_MASTER_NUMBERS] = show
        }
    }

    override suspend fun setShowKarmicDebt(show: Boolean) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.SHOW_KARMIC_DEBT] = show
        }
    }

    override suspend fun setDefaultProfileId(profileId: Long?) {
        dataStore.edit { preferences ->
            if (profileId != null) {
                preferences[PreferencesKeys.DEFAULT_PROFILE_ID] = profileId
            } else {
                preferences.remove(PreferencesKeys.DEFAULT_PROFILE_ID)
            }
        }
    }

    override suspend fun setOnboardingCompleted(completed: Boolean) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.HAS_COMPLETED_ONBOARDING] = completed
        }
    }

    override suspend fun updateLastSyncTimestamp() {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.LAST_SYNC_TIMESTAMP] = System.currentTimeMillis()
        }
    }
}
