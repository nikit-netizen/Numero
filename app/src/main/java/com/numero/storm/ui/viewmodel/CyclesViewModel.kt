package com.numero.storm.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.numero.storm.data.model.Profile
import com.numero.storm.data.repository.ProfileRepository
import com.numero.storm.data.repository.SettingsRepository
import com.numero.storm.domain.calculator.DateCalculator
import com.numero.storm.domain.calculator.NumerologyInterpretations
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

data class PersonalCycleData(
    val number: Int,
    val interpretation: String
)

data class PersonalCyclesUiState(
    val isLoading: Boolean = true,
    val profile: Profile? = null,
    val currentAge: Int = 0,
    val personalYear: PersonalCycleData? = null,
    val personalMonth: PersonalCycleData? = null,
    val personalDay: PersonalCycleData? = null,
    val universalYear: PersonalCycleData? = null,
    val universalMonth: PersonalCycleData? = null,
    val universalDay: PersonalCycleData? = null,
    val yearlyForecast: List<PersonalCycleData> = emptyList(),
    val error: String? = null
)

data class DailyNumbersUiState(
    val isLoading: Boolean = false,
    val selectedDate: LocalDate = LocalDate.now(),
    val universalDay: PersonalCycleData? = null,
    val universalMonth: PersonalCycleData? = null,
    val universalYear: PersonalCycleData? = null,
    val primaryProfile: Profile? = null,
    val personalDay: PersonalCycleData? = null,
    val personalMonth: PersonalCycleData? = null,
    val personalYear: PersonalCycleData? = null,
    val error: String? = null
)

@HiltViewModel
class PersonalCyclesViewModel @Inject constructor(
    private val profileRepository: ProfileRepository,
    private val settingsRepository: SettingsRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(PersonalCyclesUiState())
    val uiState: StateFlow<PersonalCyclesUiState> = _uiState.asStateFlow()

    fun loadCycles(profileId: Long) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)

            try {
                val profile = profileRepository.getProfileByIdOnce(profileId)

                if (profile == null) {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = "Profile not found"
                    )
                    return@launch
                }

                val today = LocalDate.now()
                val currentAge = DateCalculator.calculateAge(profile.birthDate)

                // Personal cycles
                val personalYear = DateCalculator.calculatePersonalYear(profile.birthDate, today.year)
                val personalMonth = DateCalculator.calculatePersonalMonth(personalYear, today.monthValue)
                val personalDay = DateCalculator.calculatePersonalDay(personalMonth, today.dayOfMonth)

                // Universal cycles
                val universalYear = DateCalculator.calculateUniversalYear(today.year)
                val universalMonth = DateCalculator.calculateUniversalMonth(today.year, today.monthValue)
                val universalDay = DateCalculator.calculateUniversalDay(today)

                // Yearly forecast for next 9 years
                val yearlyForecast = (0..8).map { offset ->
                    val year = today.year + offset
                    val py = DateCalculator.calculatePersonalYear(profile.birthDate, year)
                    PersonalCycleData(
                        number = py,
                        interpretation = "$year: ${NumerologyInterpretations.getPersonalYearInterpretation(py).take(100)}..."
                    )
                }

                _uiState.value = PersonalCyclesUiState(
                    isLoading = false,
                    profile = profile,
                    currentAge = currentAge,
                    personalYear = PersonalCycleData(
                        number = personalYear,
                        interpretation = NumerologyInterpretations.getPersonalYearInterpretation(personalYear)
                    ),
                    personalMonth = PersonalCycleData(
                        number = personalMonth,
                        interpretation = getPersonalMonthInterpretation(personalMonth)
                    ),
                    personalDay = PersonalCycleData(
                        number = personalDay,
                        interpretation = getPersonalDayInterpretation(personalDay)
                    ),
                    universalYear = PersonalCycleData(
                        number = universalYear,
                        interpretation = "Universal Year $universalYear influences collective energy and global trends."
                    ),
                    universalMonth = PersonalCycleData(
                        number = universalMonth,
                        interpretation = "Universal Month $universalMonth colors this month's collective opportunities."
                    ),
                    universalDay = PersonalCycleData(
                        number = universalDay,
                        interpretation = "Universal Day $universalDay sets today's collective tone."
                    ),
                    yearlyForecast = yearlyForecast
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message ?: "Failed to load cycles"
                )
            }
        }
    }

    private fun getPersonalMonthInterpretation(number: Int): String {
        return when (number) {
            1 -> "A month for new beginnings, initiative, and asserting yourself."
            2 -> "A month for patience, cooperation, and nurturing relationships."
            3 -> "A month for creativity, self-expression, and social activities."
            4 -> "A month for hard work, organization, and building foundations."
            5 -> "A month for change, adventure, and embracing new experiences."
            6 -> "A month for home, family, and taking on responsibilities."
            7 -> "A month for reflection, study, and inner development."
            8 -> "A month for business matters, financial decisions, and achievement."
            9 -> "A month for completion, release, and humanitarian activities."
            else -> "A month of transition and growth."
        }
    }

    private fun getPersonalDayInterpretation(number: Int): String {
        return when (number) {
            1 -> "Today favors new starts, leadership, and independent action."
            2 -> "Today favors diplomacy, partnerships, and patient waiting."
            3 -> "Today favors creative expression, communication, and joy."
            4 -> "Today favors practical work, planning, and attention to detail."
            5 -> "Today favors flexibility, adventure, and unexpected opportunities."
            6 -> "Today favors family matters, service, and creating harmony."
            7 -> "Today favors quiet reflection, research, and spiritual pursuits."
            8 -> "Today favors business dealings, financial matters, and authority."
            9 -> "Today favors letting go, compassion, and global thinking."
            else -> "Today offers opportunities for growth."
        }
    }
}

@HiltViewModel
class DailyNumbersViewModel @Inject constructor(
    private val profileRepository: ProfileRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(DailyNumbersUiState())
    val uiState: StateFlow<DailyNumbersUiState> = _uiState.asStateFlow()

    init {
        loadNumbers(LocalDate.now())
    }

    fun loadNumbers(date: LocalDate) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, selectedDate = date)

            try {
                val primaryProfile = profileRepository.getPrimaryProfileOnce()

                val universalDay = DateCalculator.calculateUniversalDay(date)
                val universalMonth = DateCalculator.calculateUniversalMonth(date.year, date.monthValue)
                val universalYear = DateCalculator.calculateUniversalYear(date.year)

                var personalDay: PersonalCycleData? = null
                var personalMonth: PersonalCycleData? = null
                var personalYear: PersonalCycleData? = null

                if (primaryProfile != null) {
                    val py = DateCalculator.calculatePersonalYear(primaryProfile.birthDate, date.year)
                    val pm = DateCalculator.calculatePersonalMonth(py, date.monthValue)
                    val pd = DateCalculator.calculatePersonalDay(pm, date.dayOfMonth)

                    personalYear = PersonalCycleData(
                        number = py,
                        interpretation = NumerologyInterpretations.getPersonalYearInterpretation(py)
                    )
                    personalMonth = PersonalCycleData(
                        number = pm,
                        interpretation = getMonthInterpretation(pm)
                    )
                    personalDay = PersonalCycleData(
                        number = pd,
                        interpretation = getDayInterpretation(pd)
                    )
                }

                _uiState.value = DailyNumbersUiState(
                    isLoading = false,
                    selectedDate = date,
                    universalDay = PersonalCycleData(
                        number = universalDay,
                        interpretation = "Universal Day $universalDay - ${getDayInterpretation(universalDay)}"
                    ),
                    universalMonth = PersonalCycleData(
                        number = universalMonth,
                        interpretation = "Universal Month $universalMonth"
                    ),
                    universalYear = PersonalCycleData(
                        number = universalYear,
                        interpretation = "Universal Year $universalYear"
                    ),
                    primaryProfile = primaryProfile,
                    personalDay = personalDay,
                    personalMonth = personalMonth,
                    personalYear = personalYear
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message ?: "Failed to load numbers"
                )
            }
        }
    }

    fun selectDate(date: LocalDate) {
        loadNumbers(date)
    }

    private fun getMonthInterpretation(number: Int): String {
        return when (number) {
            1 -> "New beginnings, initiative, independence"
            2 -> "Patience, cooperation, partnerships"
            3 -> "Creativity, self-expression, joy"
            4 -> "Hard work, organization, foundations"
            5 -> "Change, adventure, freedom"
            6 -> "Home, family, responsibility"
            7 -> "Reflection, study, spirituality"
            8 -> "Achievement, business, authority"
            9 -> "Completion, release, compassion"
            else -> "Transition and growth"
        }
    }

    private fun getDayInterpretation(number: Int): String {
        return when (number) {
            1 -> "Start new projects, take initiative"
            2 -> "Cooperate, be patient, nurture relationships"
            3 -> "Express creativity, socialize, communicate"
            4 -> "Focus on practical matters, organize"
            5 -> "Embrace change, seek adventure"
            6 -> "Attend to home and family matters"
            7 -> "Reflect, meditate, seek knowledge"
            8 -> "Handle business and financial affairs"
            9 -> "Let go, show compassion, serve others"
            else -> "Balance and adapt"
        }
    }
}
