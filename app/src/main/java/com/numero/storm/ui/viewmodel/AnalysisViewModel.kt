package com.numero.storm.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.numero.storm.data.model.ChallengeEntity
import com.numero.storm.data.model.LifePeriodEntity
import com.numero.storm.data.model.NumerologyAnalysis
import com.numero.storm.data.model.PinnacleEntity
import com.numero.storm.data.model.Profile
import com.numero.storm.data.model.getKarmicLessonsList
import com.numero.storm.data.repository.NumerologyRepository
import com.numero.storm.data.repository.ProfileRepository
import com.numero.storm.data.repository.SettingsRepository
import com.numero.storm.domain.calculator.DateCalculator
import com.numero.storm.domain.calculator.NumberInterpretation
import com.numero.storm.domain.calculator.NumerologyInterpretations
import com.numero.storm.domain.calculator.NumerologySystem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

data class AnalysisUiState(
    val isLoading: Boolean = true,
    val profile: Profile? = null,
    val analysis: NumerologyAnalysis? = null,
    val pinnacles: List<PinnacleEntity> = emptyList(),
    val challenges: List<ChallengeEntity> = emptyList(),
    val lifePeriods: List<LifePeriodEntity> = emptyList(),
    val currentAge: Int = 0,
    val currentPinnacleIndex: Int = 0,
    val currentChallengeIndex: Int = 0,
    val currentLifePeriodIndex: Int = 0,
    val numerologySystem: NumerologySystem = NumerologySystem.PYTHAGOREAN,
    val showMasterNumbers: Boolean = true,
    val showKarmicDebt: Boolean = true,
    val error: String? = null
)

data class AnalysisDetailUiState(
    val isLoading: Boolean = true,
    val profile: Profile? = null,
    val numberType: String = "",
    val numberValue: Int = 0,
    val isMasterNumber: Boolean = false,
    val karmicDebt: Int? = null,
    val interpretation: NumberInterpretation? = null,
    val interpretationText: String = "",
    val additionalInfo: Map<String, Any> = emptyMap(),
    val error: String? = null
)

@HiltViewModel
class AnalysisViewModel @Inject constructor(
    private val profileRepository: ProfileRepository,
    private val numerologyRepository: NumerologyRepository,
    private val settingsRepository: SettingsRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(AnalysisUiState())
    val uiState: StateFlow<AnalysisUiState> = _uiState.asStateFlow()

    fun loadAnalysis(profileId: Long) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)

            try {
                val settings = settingsRepository.getSettings().first()
                val profile = profileRepository.getProfileByIdOnce(profileId)

                if (profile == null) {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = "Profile not found"
                    )
                    return@launch
                }

                var analysis = numerologyRepository.getAnalysis(profileId, settings.numerologySystem)

                if (analysis == null) {
                    val analysisId = numerologyRepository.calculateAndSaveAnalysis(
                        profile,
                        settings.numerologySystem
                    )
                    analysis = numerologyRepository.getAnalysis(profileId, settings.numerologySystem)
                }

                val pinnacles = analysis?.let { numerologyRepository.getPinnacles(it.id) } ?: emptyList()
                val challenges = analysis?.let { numerologyRepository.getChallenges(it.id) } ?: emptyList()
                val lifePeriods = analysis?.let { numerologyRepository.getLifePeriods(it.id) } ?: emptyList()

                val currentAge = DateCalculator.calculateAge(profile.birthDate)

                val currentPinnacleIndex = pinnacles.indexOfFirst { pinnacle ->
                    currentAge >= pinnacle.startAge && (pinnacle.endAge == null || currentAge <= pinnacle.endAge)
                }.takeIf { it >= 0 } ?: 0

                val currentChallengeIndex = challenges.indexOfFirst { challenge ->
                    currentAge >= challenge.startAge && (challenge.endAge == null || currentAge <= challenge.endAge)
                }.takeIf { it >= 0 } ?: 0

                val currentLifePeriodIndex = lifePeriods.indexOfFirst { period ->
                    currentAge >= period.startAge && (period.endAge == null || currentAge <= period.endAge)
                }.takeIf { it >= 0 } ?: 0

                _uiState.value = AnalysisUiState(
                    isLoading = false,
                    profile = profile,
                    analysis = analysis,
                    pinnacles = pinnacles,
                    challenges = challenges,
                    lifePeriods = lifePeriods,
                    currentAge = currentAge,
                    currentPinnacleIndex = currentPinnacleIndex,
                    currentChallengeIndex = currentChallengeIndex,
                    currentLifePeriodIndex = currentLifePeriodIndex,
                    numerologySystem = settings.numerologySystem,
                    showMasterNumbers = settings.showMasterNumbers,
                    showKarmicDebt = settings.showKarmicDebt
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message ?: "Failed to load analysis"
                )
            }
        }
    }

    fun recalculateAnalysis() {
        val profile = _uiState.value.profile ?: return
        val system = _uiState.value.numerologySystem

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)

            try {
                numerologyRepository.deleteAnalysesForProfile(profile.id)
                loadAnalysis(profile.id)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message ?: "Failed to recalculate"
                )
            }
        }
    }
}

@HiltViewModel
class AnalysisDetailViewModel @Inject constructor(
    private val profileRepository: ProfileRepository,
    private val numerologyRepository: NumerologyRepository,
    private val settingsRepository: SettingsRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(AnalysisDetailUiState())
    val uiState: StateFlow<AnalysisDetailUiState> = _uiState.asStateFlow()

    fun loadDetail(profileId: Long, numberType: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)

            try {
                val settings = settingsRepository.getSettings().first()
                val profile = profileRepository.getProfileByIdOnce(profileId)

                if (profile == null) {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = "Profile not found"
                    )
                    return@launch
                }

                val analysis = numerologyRepository.getAnalysis(profileId, settings.numerologySystem)

                if (analysis == null) {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = "Analysis not found"
                    )
                    return@launch
                }

                val (numberValue, isMaster, karmicDebt, interpretation, interpretationText, additionalInfo) =
                    getNumberDetails(numberType, analysis, profile)

                _uiState.value = AnalysisDetailUiState(
                    isLoading = false,
                    profile = profile,
                    numberType = numberType,
                    numberValue = numberValue,
                    isMasterNumber = isMaster,
                    karmicDebt = karmicDebt,
                    interpretation = interpretation,
                    interpretationText = interpretationText,
                    additionalInfo = additionalInfo
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message ?: "Failed to load details"
                )
            }
        }
    }

    private fun getNumberDetails(
        numberType: String,
        analysis: NumerologyAnalysis,
        profile: Profile
    ): NumberDetails {
        return when (numberType.uppercase()) {
            "LIFE_PATH" -> NumberDetails(
                numberValue = analysis.lifePathNumber,
                isMaster = analysis.lifePathMasterNumber,
                karmicDebt = analysis.lifePathKarmicDebt,
                interpretation = NumerologyInterpretations.getLifePathInterpretation(analysis.lifePathNumber),
                interpretationText = "",
                additionalInfo = mapOf(
                    "birthDate" to profile.birthDate.toString()
                )
            )
            "EXPRESSION" -> NumberDetails(
                numberValue = analysis.expressionNumber,
                isMaster = analysis.expressionMasterNumber,
                karmicDebt = analysis.expressionKarmicDebt,
                interpretation = null,
                interpretationText = NumerologyInterpretations.getExpressionInterpretation(analysis.expressionNumber),
                additionalInfo = mapOf(
                    "fullName" to profile.fullName
                )
            )
            "SOUL_URGE" -> NumberDetails(
                numberValue = analysis.soulUrgeNumber,
                isMaster = analysis.soulUrgeMasterNumber,
                karmicDebt = analysis.soulUrgeKarmicDebt,
                interpretation = null,
                interpretationText = NumerologyInterpretations.getSoulUrgeInterpretation(analysis.soulUrgeNumber),
                additionalInfo = emptyMap()
            )
            "PERSONALITY" -> NumberDetails(
                numberValue = analysis.personalityNumber,
                isMaster = analysis.personalityMasterNumber,
                karmicDebt = analysis.personalityKarmicDebt,
                interpretation = null,
                interpretationText = NumerologyInterpretations.getPersonalityInterpretation(analysis.personalityNumber),
                additionalInfo = emptyMap()
            )
            "BIRTHDAY" -> NumberDetails(
                numberValue = analysis.birthdayNumber,
                isMaster = analysis.birthdayMasterNumber,
                karmicDebt = null,
                interpretation = null,
                interpretationText = NumerologyInterpretations.getBirthdayInterpretation(profile.birthDate.dayOfMonth),
                additionalInfo = mapOf(
                    "birthDay" to profile.birthDate.dayOfMonth
                )
            )
            "MATURITY" -> NumberDetails(
                numberValue = analysis.maturityNumber,
                isMaster = analysis.maturityMasterNumber,
                karmicDebt = null,
                interpretation = null,
                interpretationText = "Your Maturity Number ${analysis.maturityNumber} represents the true self that emerges in the second half of life. It is the sum of your Life Path (${analysis.lifePathNumber}) and Expression (${analysis.expressionNumber}) numbers, revealing the person you are becoming.",
                additionalInfo = mapOf(
                    "lifePath" to analysis.lifePathNumber,
                    "expression" to analysis.expressionNumber
                )
            )
            "BALANCE" -> NumberDetails(
                numberValue = analysis.balanceNumber,
                isMaster = false,
                karmicDebt = null,
                interpretation = null,
                interpretationText = "Your Balance Number ${analysis.balanceNumber} reveals how you handle difficult situations and find equilibrium during challenging times. It's derived from the initials of your name.",
                additionalInfo = emptyMap()
            )
            "HIDDEN_PASSION" -> NumberDetails(
                numberValue = analysis.hiddenPassionNumber ?: 0,
                isMaster = false,
                karmicDebt = null,
                interpretation = null,
                interpretationText = if (analysis.hiddenPassionNumber != null) {
                    "Your Hidden Passion Number ${analysis.hiddenPassionNumber} indicates your strongest talents and abilities - the areas where you naturally excel and feel most passionate."
                } else {
                    "No clear Hidden Passion number was found in your name. This suggests a balanced distribution of energies across all numbers."
                },
                additionalInfo = emptyMap()
            )
            "KARMIC_LESSONS" -> {
                val lessons = analysis.getKarmicLessonsList()
                NumberDetails(
                    numberValue = lessons.size,
                    isMaster = false,
                    karmicDebt = null,
                    interpretation = null,
                    interpretationText = if (lessons.isEmpty()) {
                        "You have no karmic lessons - all numbers are represented in your name. This indicates a well-rounded nature with access to all fundamental energies."
                    } else {
                        "Your Karmic Lessons are: ${lessons.joinToString(", ")}. These numbers are missing from your name and represent areas of growth and learning in this lifetime."
                    },
                    additionalInfo = mapOf(
                        "lessons" to lessons.toList()
                    )
                )
            }
            else -> NumberDetails(
                numberValue = 0,
                isMaster = false,
                karmicDebt = null,
                interpretation = null,
                interpretationText = "Unknown number type",
                additionalInfo = emptyMap()
            )
        }
    }

    private data class NumberDetails(
        val numberValue: Int,
        val isMaster: Boolean,
        val karmicDebt: Int?,
        val interpretation: NumberInterpretation?,
        val interpretationText: String,
        val additionalInfo: Map<String, Any>
    )
}
