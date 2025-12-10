package com.numero.storm.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.numero.storm.data.model.Profile
import com.numero.storm.data.repository.NumerologyRepository
import com.numero.storm.data.repository.ProfileRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

data class ProfileListUiState(
    val isLoading: Boolean = true,
    val profiles: List<Profile> = emptyList(),
    val error: String? = null
)

data class ProfileFormUiState(
    val isLoading: Boolean = false,
    val firstName: String = "",
    val middleName: String = "",
    val lastName: String = "",
    val birthDate: LocalDate? = null,
    val isPrimary: Boolean = false,
    val firstNameError: String? = null,
    val lastNameError: String? = null,
    val birthDateError: String? = null,
    val isSaved: Boolean = false,
    val savedProfileId: Long? = null,
    val error: String? = null
)

@HiltViewModel
class ProfileListViewModel @Inject constructor(
    private val profileRepository: ProfileRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileListUiState())
    val uiState: StateFlow<ProfileListUiState> = _uiState.asStateFlow()

    init {
        loadProfiles()
    }

    private fun loadProfiles() {
        viewModelScope.launch {
            profileRepository.getAllProfiles().collect { profiles ->
                _uiState.value = ProfileListUiState(
                    isLoading = false,
                    profiles = profiles
                )
            }
        }
    }

    fun deleteProfile(profile: Profile) {
        viewModelScope.launch {
            profileRepository.deleteProfile(profile)
        }
    }

    fun setAsPrimary(profileId: Long) {
        viewModelScope.launch {
            profileRepository.setAsPrimary(profileId)
        }
    }
}

@HiltViewModel
class CreateProfileViewModel @Inject constructor(
    private val profileRepository: ProfileRepository,
    private val numerologyRepository: NumerologyRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileFormUiState())
    val uiState: StateFlow<ProfileFormUiState> = _uiState.asStateFlow()

    fun updateFirstName(name: String) {
        _uiState.value = _uiState.value.copy(
            firstName = name,
            firstNameError = null
        )
    }

    fun updateMiddleName(name: String) {
        _uiState.value = _uiState.value.copy(middleName = name)
    }

    fun updateLastName(name: String) {
        _uiState.value = _uiState.value.copy(
            lastName = name,
            lastNameError = null
        )
    }

    fun updateBirthDate(date: LocalDate?) {
        _uiState.value = _uiState.value.copy(
            birthDate = date,
            birthDateError = null
        )
    }

    fun updateIsPrimary(isPrimary: Boolean) {
        _uiState.value = _uiState.value.copy(isPrimary = isPrimary)
    }

    fun saveProfile() {
        val state = _uiState.value

        // Validate
        var hasError = false
        var newState = state

        if (state.firstName.isBlank()) {
            newState = newState.copy(firstNameError = "First name is required")
            hasError = true
        }

        if (state.lastName.isBlank()) {
            newState = newState.copy(lastNameError = "Last name is required")
            hasError = true
        }

        if (state.birthDate == null) {
            newState = newState.copy(birthDateError = "Birth date is required")
            hasError = true
        } else if (state.birthDate.isAfter(LocalDate.now())) {
            newState = newState.copy(birthDateError = "Birth date cannot be in the future")
            hasError = true
        }

        if (hasError) {
            _uiState.value = newState
            return
        }

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)

            try {
                val profileId = profileRepository.createProfile(
                    firstName = state.firstName,
                    middleName = state.middleName.takeIf { it.isNotBlank() },
                    lastName = state.lastName,
                    birthDate = state.birthDate!!,
                    isPrimary = state.isPrimary
                )

                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    isSaved = true,
                    savedProfileId = profileId
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message ?: "Failed to save profile"
                )
            }
        }
    }
}

@HiltViewModel
class EditProfileViewModel @Inject constructor(
    private val profileRepository: ProfileRepository,
    private val numerologyRepository: NumerologyRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileFormUiState(isLoading = true))
    val uiState: StateFlow<ProfileFormUiState> = _uiState.asStateFlow()

    private var originalProfile: Profile? = null

    fun loadProfile(profileId: Long) {
        viewModelScope.launch {
            try {
                val profile = profileRepository.getProfileByIdOnce(profileId)
                if (profile != null) {
                    originalProfile = profile
                    _uiState.value = ProfileFormUiState(
                        isLoading = false,
                        firstName = profile.firstName,
                        middleName = profile.middleName ?: "",
                        lastName = profile.lastName,
                        birthDate = profile.birthDate,
                        isPrimary = profile.isPrimary
                    )
                } else {
                    _uiState.value = ProfileFormUiState(
                        isLoading = false,
                        error = "Profile not found"
                    )
                }
            } catch (e: Exception) {
                _uiState.value = ProfileFormUiState(
                    isLoading = false,
                    error = e.message ?: "Failed to load profile"
                )
            }
        }
    }

    fun updateFirstName(name: String) {
        _uiState.value = _uiState.value.copy(
            firstName = name,
            firstNameError = null
        )
    }

    fun updateMiddleName(name: String) {
        _uiState.value = _uiState.value.copy(middleName = name)
    }

    fun updateLastName(name: String) {
        _uiState.value = _uiState.value.copy(
            lastName = name,
            lastNameError = null
        )
    }

    fun updateBirthDate(date: LocalDate?) {
        _uiState.value = _uiState.value.copy(
            birthDate = date,
            birthDateError = null
        )
    }

    fun updateIsPrimary(isPrimary: Boolean) {
        _uiState.value = _uiState.value.copy(isPrimary = isPrimary)
    }

    fun saveProfile() {
        val state = _uiState.value
        val original = originalProfile ?: return

        // Validate
        var hasError = false
        var newState = state

        if (state.firstName.isBlank()) {
            newState = newState.copy(firstNameError = "First name is required")
            hasError = true
        }

        if (state.lastName.isBlank()) {
            newState = newState.copy(lastNameError = "Last name is required")
            hasError = true
        }

        if (state.birthDate == null) {
            newState = newState.copy(birthDateError = "Birth date is required")
            hasError = true
        } else if (state.birthDate.isAfter(LocalDate.now())) {
            newState = newState.copy(birthDateError = "Birth date cannot be in the future")
            hasError = true
        }

        if (hasError) {
            _uiState.value = newState
            return
        }

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)

            try {
                val hasNameChanged = original.firstName != state.firstName ||
                        original.middleName != state.middleName.takeIf { it.isNotBlank() } ||
                        original.lastName != state.lastName
                val hasBirthDateChanged = original.birthDate != state.birthDate

                val updatedProfile = original.copy(
                    firstName = state.firstName.trim(),
                    middleName = state.middleName.trim().takeIf { it.isNotBlank() },
                    lastName = state.lastName.trim(),
                    birthDate = state.birthDate!!,
                    isPrimary = state.isPrimary
                )

                profileRepository.updateProfile(updatedProfile)

                // If name or birth date changed, delete cached analyses
                if (hasNameChanged || hasBirthDateChanged) {
                    numerologyRepository.deleteAnalysesForProfile(original.id)
                }

                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    isSaved = true,
                    savedProfileId = original.id
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message ?: "Failed to update profile"
                )
            }
        }
    }

    fun deleteProfile() {
        val original = originalProfile ?: return

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)

            try {
                profileRepository.deleteProfile(original)
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    isSaved = true
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message ?: "Failed to delete profile"
                )
            }
        }
    }
}
