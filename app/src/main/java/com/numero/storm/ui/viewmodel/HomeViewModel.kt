package com.numero.storm.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.numero.storm.data.model.NumerologyAnalysis
import com.numero.storm.data.model.Profile
import com.numero.storm.data.repository.NumerologyRepository
import com.numero.storm.data.repository.ProfileRepository
import com.numero.storm.data.repository.SettingsRepository
import com.numero.storm.domain.calculator.DateCalculator
import com.numero.storm.domain.calculator.NumerologyInterpretations
import com.numero.storm.domain.calculator.NumerologySystem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

data class HomeUiState(
    val isLoading: Boolean = true,
    val primaryProfile: Profile? = null,
    val primaryAnalysis: NumerologyAnalysis? = null,
    val profiles: List<Profile> = emptyList(),
    val personalYear: Int? = null,
    val personalMonth: Int? = null,
    val personalDay: Int? = null,
    val universalDay: Int = DateCalculator.calculateUniversalDay(LocalDate.now()),
    val dailyInsight: String = "",
    val error: String? = null
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val profileRepository: ProfileRepository,
    private val numerologyRepository: NumerologyRepository,
    private val settingsRepository: SettingsRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        loadData()
    }

    private fun loadData() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)

            try {
                combine(
                    profileRepository.getAllProfiles(),
                    profileRepository.getPrimaryProfile(),
                    settingsRepository.getSettings()
                ) { profiles, primaryProfile, settings ->
                    Triple(profiles, primaryProfile, settings)
                }.collect { (profiles, primaryProfile, settings) ->
                    val today = LocalDate.now()
                    val universalDay = DateCalculator.calculateUniversalDay(today)

                    var analysis: NumerologyAnalysis? = null
                    var personalYear: Int? = null
                    var personalMonth: Int? = null
                    var personalDay: Int? = null

                    if (primaryProfile != null) {
                        analysis = numerologyRepository.getAnalysis(
                            primaryProfile.id,
                            settings.numerologySystem
                        )

                        if (analysis == null) {
                            numerologyRepository.calculateAndSaveAnalysis(
                                primaryProfile,
                                settings.numerologySystem
                            )
                            analysis = numerologyRepository.getAnalysis(
                                primaryProfile.id,
                                settings.numerologySystem
                            )
                        }

                        personalYear = DateCalculator.calculatePersonalYear(
                            primaryProfile.birthDate,
                            today.year
                        )
                        personalMonth = DateCalculator.calculatePersonalMonth(
                            personalYear,
                            today.monthValue
                        )
                        personalDay = DateCalculator.calculatePersonalDay(
                            personalMonth,
                            today.dayOfMonth
                        )
                    }

                    val dailyInsight = NumerologyInterpretations.getPersonalYearInterpretation(
                        personalDay ?: universalDay
                    )

                    _uiState.value = HomeUiState(
                        isLoading = false,
                        primaryProfile = primaryProfile,
                        primaryAnalysis = analysis,
                        profiles = profiles,
                        personalYear = personalYear,
                        personalMonth = personalMonth,
                        personalDay = personalDay,
                        universalDay = universalDay,
                        dailyInsight = dailyInsight,
                        error = null
                    )
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message ?: "An error occurred"
                )
            }
        }
    }

    fun setAsPrimary(profileId: Long) {
        viewModelScope.launch {
            profileRepository.setAsPrimary(profileId)
        }
    }

    fun deleteProfile(profile: Profile) {
        viewModelScope.launch {
            profileRepository.deleteProfile(profile)
        }
    }

    fun refresh() {
        loadData()
    }
}
