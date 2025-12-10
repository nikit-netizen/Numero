package com.numero.storm.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.numero.storm.data.model.CompatibilityAnalysisEntity
import com.numero.storm.data.model.Profile
import com.numero.storm.data.model.getSharedNumbersList
import com.numero.storm.data.repository.CompatibilityRepository
import com.numero.storm.data.repository.ProfileRepository
import com.numero.storm.domain.calculator.AuspiciousDate
import com.numero.storm.domain.calculator.CompatibilityCalculator
import com.numero.storm.domain.calculator.CompatibilityLevel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import java.time.LocalDate
import javax.inject.Inject

data class CompatibilitySelectUiState(
    val isLoading: Boolean = true,
    val profiles: List<Profile> = emptyList(),
    val selectedProfile1: Profile? = null,
    val selectedProfile2: Profile? = null,
    val error: String? = null
)

data class CompatibilityResultUiState(
    val isLoading: Boolean = true,
    val profile1: Profile? = null,
    val profile2: Profile? = null,
    val compatibility: CompatibilityAnalysisEntity? = null,
    val sharedNumbers: List<Int> = emptyList(),
    val complementaryAspects: List<String> = emptyList(),
    val challenges: List<String> = emptyList(),
    val auspiciousDates: List<AuspiciousDate> = emptyList(),
    val error: String? = null
)

@HiltViewModel
class CompatibilitySelectViewModel @Inject constructor(
    private val profileRepository: ProfileRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(CompatibilitySelectUiState())
    val uiState: StateFlow<CompatibilitySelectUiState> = _uiState.asStateFlow()

    init {
        loadProfiles()
    }

    private fun loadProfiles() {
        viewModelScope.launch {
            profileRepository.getAllProfiles().collect { profiles ->
                _uiState.value = CompatibilitySelectUiState(
                    isLoading = false,
                    profiles = profiles,
                    selectedProfile1 = _uiState.value.selectedProfile1,
                    selectedProfile2 = _uiState.value.selectedProfile2
                )
            }
        }
    }

    fun selectProfile1(profile: Profile) {
        _uiState.value = _uiState.value.copy(
            selectedProfile1 = profile,
            selectedProfile2 = if (_uiState.value.selectedProfile2?.id == profile.id) null
            else _uiState.value.selectedProfile2
        )
    }

    fun selectProfile2(profile: Profile) {
        _uiState.value = _uiState.value.copy(
            selectedProfile2 = profile,
            selectedProfile1 = if (_uiState.value.selectedProfile1?.id == profile.id) null
            else _uiState.value.selectedProfile1
        )
    }

    fun clearSelection() {
        _uiState.value = _uiState.value.copy(
            selectedProfile1 = null,
            selectedProfile2 = null
        )
    }

    fun canCompare(): Boolean {
        val state = _uiState.value
        return state.selectedProfile1 != null &&
                state.selectedProfile2 != null &&
                state.selectedProfile1.id != state.selectedProfile2.id
    }
}

@HiltViewModel
class CompatibilityResultViewModel @Inject constructor(
    private val profileRepository: ProfileRepository,
    private val compatibilityRepository: CompatibilityRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(CompatibilityResultUiState())
    val uiState: StateFlow<CompatibilityResultUiState> = _uiState.asStateFlow()

    private val json = Json { ignoreUnknownKeys = true }

    fun loadCompatibility(profile1Id: Long, profile2Id: Long) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)

            try {
                val profile1 = profileRepository.getProfileByIdOnce(profile1Id)
                val profile2 = profileRepository.getProfileByIdOnce(profile2Id)

                if (profile1 == null || profile2 == null) {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = "One or both profiles not found"
                    )
                    return@launch
                }

                var compatibility = compatibilityRepository.getCompatibilityBetweenProfiles(
                    profile1Id,
                    profile2Id
                )

                if (compatibility == null) {
                    compatibilityRepository.calculateAndSaveCompatibility(profile1, profile2)
                    compatibility = compatibilityRepository.getCompatibilityBetweenProfiles(
                        profile1Id,
                        profile2Id
                    )
                }

                val sharedNumbers = compatibility?.getSharedNumbersList() ?: emptyList()
                val complementaryAspects = compatibility?.let {
                    try {
                        json.decodeFromString<List<String>>(it.complementaryAspects)
                    } catch (e: Exception) {
                        emptyList()
                    }
                } ?: emptyList()
                val challenges = compatibility?.let {
                    try {
                        json.decodeFromString<List<String>>(it.challenges)
                    } catch (e: Exception) {
                        emptyList()
                    }
                } ?: emptyList()

                val auspiciousDates = CompatibilityCalculator.calculateAuspiciousDates(
                    profile1.birthDate,
                    profile2.birthDate,
                    LocalDate.now().year
                )

                _uiState.value = CompatibilityResultUiState(
                    isLoading = false,
                    profile1 = profile1,
                    profile2 = profile2,
                    compatibility = compatibility,
                    sharedNumbers = sharedNumbers,
                    complementaryAspects = complementaryAspects,
                    challenges = challenges,
                    auspiciousDates = auspiciousDates
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message ?: "Failed to calculate compatibility"
                )
            }
        }
    }

    fun recalculate() {
        val profile1 = _uiState.value.profile1 ?: return
        val profile2 = _uiState.value.profile2 ?: return

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)

            try {
                compatibilityRepository.deleteCompatibilityBetweenProfiles(profile1.id, profile2.id)
                loadCompatibility(profile1.id, profile2.id)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message ?: "Failed to recalculate"
                )
            }
        }
    }
}
