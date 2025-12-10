package com.numero.storm.domain.calculator

import java.time.LocalDate
import java.time.temporal.ChronoUnit

/**
 * Calculates numerology numbers derived from dates.
 * Provides calculations for Life Path, Personal Year, Personal Month, Personal Day,
 * Pinnacles, Challenges, and Life Periods.
 */
object DateCalculator {

    /**
     * Calculates the Life Path Number.
     * This is the most important number in numerology, derived from the full birth date.
     * It represents your life's purpose and the path you will walk.
     *
     * The calculation reduces month, day, and year separately before final reduction
     * to properly identify master numbers.
     *
     * @param birthDate The date of birth
     * @return The calculated life path result
     */
    fun calculateLifePathNumber(birthDate: LocalDate): LifePathResult {
        val monthReduced = NumberReducer.reduce(birthDate.monthValue)
        val dayReduced = NumberReducer.reduce(birthDate.dayOfMonth)
        val yearReduced = NumberReducer.reduce(birthDate.year)

        val sum = monthReduced + dayReduced + yearReduced
        val finalNumber = NumberReducer.reduce(sum)

        val karmicDebt = detectKarmicDebtInDate(birthDate)

        return LifePathResult(
            finalNumber = finalNumber,
            monthComponent = monthReduced,
            dayComponent = dayReduced,
            yearComponent = yearReduced,
            totalBeforeReduction = sum,
            reductionSteps = NumberReducer.getReductionSteps(sum),
            karmicDebtNumber = karmicDebt,
            isMasterNumber = NumberReducer.isMasterNumber(finalNumber)
        )
    }

    /**
     * Calculates the Birthday Number (also called Talent Number).
     * Simply the day of birth reduced. Represents natural talents.
     *
     * @param birthDate The date of birth
     * @return The birthday number (1-31 reduced to single digit or master number)
     */
    fun calculateBirthdayNumber(birthDate: LocalDate): NumerologyResult {
        val day = birthDate.dayOfMonth
        val reduced = NumberReducer.reduce(day)

        return NumerologyResult(
            finalNumber = reduced,
            originalSum = day,
            reductionSteps = NumberReducer.getReductionSteps(day),
            breakdown = emptyList(),
            karmicDebtNumber = if (NumberReducer.isKarmicDebtNumber(day)) day else null,
            isMasterNumber = NumberReducer.isMasterNumber(reduced) || NumberReducer.isMasterNumber(day)
        )
    }

    /**
     * Calculates the Personal Year Number.
     * Represents the theme and opportunities for a specific year.
     * Personal Year cycle runs from birthday to birthday.
     *
     * @param birthDate The date of birth
     * @param year The year to calculate for
     * @return The personal year number (1-9)
     */
    fun calculatePersonalYear(birthDate: LocalDate, year: Int): Int {
        val monthReduced = NumberReducer.reduceToSingleDigit(birthDate.monthValue)
        val dayReduced = NumberReducer.reduceToSingleDigit(birthDate.dayOfMonth)
        val yearReduced = NumberReducer.reduceToSingleDigit(year)

        val sum = monthReduced + dayReduced + yearReduced
        return NumberReducer.reduceToSingleDigit(sum)
    }

    /**
     * Calculates the Personal Month Number.
     * Provides monthly guidance within the personal year.
     *
     * @param personalYear The personal year number
     * @param month The calendar month (1-12)
     * @return The personal month number (1-9)
     */
    fun calculatePersonalMonth(personalYear: Int, month: Int): Int {
        val monthReduced = NumberReducer.reduceToSingleDigit(month)
        return NumberReducer.reduceToSingleDigit(personalYear + monthReduced)
    }

    /**
     * Calculates the Personal Day Number.
     * Provides daily guidance within the personal month.
     *
     * @param personalMonth The personal month number
     * @param day The calendar day (1-31)
     * @return The personal day number (1-9)
     */
    fun calculatePersonalDay(personalMonth: Int, day: Int): Int {
        val dayReduced = NumberReducer.reduceToSingleDigit(day)
        return NumberReducer.reduceToSingleDigit(personalMonth + dayReduced)
    }

    /**
     * Calculates all four Pinnacle Numbers.
     * Pinnacles represent major life phases and their challenges/opportunities.
     *
     * @param birthDate The date of birth
     * @return List of four pinnacle periods
     */
    fun calculatePinnacles(birthDate: LocalDate): List<PinnaclePeriod> {
        val lifePathResult = calculateLifePathNumber(birthDate)
        val lifePath = lifePathResult.finalNumber

        val month = NumberReducer.reduceToSingleDigit(birthDate.monthValue)
        val day = NumberReducer.reduceToSingleDigit(birthDate.dayOfMonth)
        val year = NumberReducer.reduceToSingleDigit(birthDate.year)

        // Calculate pinnacle numbers
        val firstPinnacle = NumberReducer.reduce(month + day)
        val secondPinnacle = NumberReducer.reduce(day + year)
        val thirdPinnacle = NumberReducer.reduce(firstPinnacle + secondPinnacle)
        val fourthPinnacle = NumberReducer.reduce(month + year)

        // Calculate pinnacle ages
        val firstPinnacleEndAge = NumerologyConstants.FIRST_PINNACLE_BASE_AGE - lifePath
        val secondPinnacleEndAge = firstPinnacleEndAge + NumerologyConstants.PINNACLE_CYCLE_LENGTH
        val thirdPinnacleEndAge = secondPinnacleEndAge + NumerologyConstants.PINNACLE_CYCLE_LENGTH

        return listOf(
            PinnaclePeriod(
                number = firstPinnacle,
                startAge = 0,
                endAge = firstPinnacleEndAge,
                periodIndex = 1,
                isMasterNumber = NumberReducer.isMasterNumber(firstPinnacle)
            ),
            PinnaclePeriod(
                number = secondPinnacle,
                startAge = firstPinnacleEndAge + 1,
                endAge = secondPinnacleEndAge,
                periodIndex = 2,
                isMasterNumber = NumberReducer.isMasterNumber(secondPinnacle)
            ),
            PinnaclePeriod(
                number = thirdPinnacle,
                startAge = secondPinnacleEndAge + 1,
                endAge = thirdPinnacleEndAge,
                periodIndex = 3,
                isMasterNumber = NumberReducer.isMasterNumber(thirdPinnacle)
            ),
            PinnaclePeriod(
                number = fourthPinnacle,
                startAge = thirdPinnacleEndAge + 1,
                endAge = null, // Continues for rest of life
                periodIndex = 4,
                isMasterNumber = NumberReducer.isMasterNumber(fourthPinnacle)
            )
        )
    }

    /**
     * Calculates all four Challenge Numbers.
     * Challenges represent obstacles and lessons during each life phase.
     *
     * @param birthDate The date of birth
     * @return List of four challenge periods
     */
    fun calculateChallenges(birthDate: LocalDate): List<ChallengePeriod> {
        val lifePathResult = calculateLifePathNumber(birthDate)
        val lifePath = lifePathResult.finalNumber

        val month = NumberReducer.reduceToSingleDigit(birthDate.monthValue)
        val day = NumberReducer.reduceToSingleDigit(birthDate.dayOfMonth)
        val year = NumberReducer.reduceToSingleDigit(birthDate.year)

        // Calculate challenge numbers (using absolute differences)
        val firstChallenge = kotlin.math.abs(month - day)
        val secondChallenge = kotlin.math.abs(day - year)
        val thirdChallenge = kotlin.math.abs(firstChallenge - secondChallenge)
        val fourthChallenge = kotlin.math.abs(month - year)

        // Calculate challenge ages (same timing as pinnacles)
        val firstChallengeEndAge = NumerologyConstants.FIRST_PINNACLE_BASE_AGE - lifePath
        val secondChallengeEndAge = firstChallengeEndAge + NumerologyConstants.CHALLENGE_CYCLE_LENGTH
        val thirdChallengeEndAge = secondChallengeEndAge + NumerologyConstants.CHALLENGE_CYCLE_LENGTH

        return listOf(
            ChallengePeriod(
                number = firstChallenge,
                startAge = 0,
                endAge = firstChallengeEndAge,
                periodIndex = 1
            ),
            ChallengePeriod(
                number = secondChallenge,
                startAge = firstChallengeEndAge + 1,
                endAge = secondChallengeEndAge,
                periodIndex = 2
            ),
            ChallengePeriod(
                number = thirdChallenge,
                startAge = secondChallengeEndAge + 1,
                endAge = thirdChallengeEndAge,
                periodIndex = 3
            ),
            ChallengePeriod(
                number = fourthChallenge,
                startAge = thirdChallengeEndAge + 1,
                endAge = null,
                periodIndex = 4
            )
        )
    }

    /**
     * Calculates the three Life Period (Cycle) Numbers.
     * These represent the three major phases of life.
     *
     * @param birthDate The date of birth
     * @return List of three life periods
     */
    fun calculateLifePeriods(birthDate: LocalDate): List<LifePeriod> {
        val lifePathResult = calculateLifePathNumber(birthDate)
        val lifePath = lifePathResult.finalNumber

        // First period: birth month reduced
        val firstPeriod = NumberReducer.reduce(birthDate.monthValue)
        // Second period: birth day reduced
        val secondPeriod = NumberReducer.reduce(birthDate.dayOfMonth)
        // Third period: birth year reduced
        val thirdPeriod = NumberReducer.reduce(birthDate.year)

        val firstPeriodEndAge = NumerologyConstants.FIRST_PERIOD_END_AGE_BASE + (9 - lifePath)
        val secondPeriodEndAge = firstPeriodEndAge + NumerologyConstants.PERIOD_LENGTH

        return listOf(
            LifePeriod(
                number = firstPeriod,
                startAge = 0,
                endAge = firstPeriodEndAge,
                periodIndex = 1,
                source = "Month",
                isMasterNumber = NumberReducer.isMasterNumber(firstPeriod)
            ),
            LifePeriod(
                number = secondPeriod,
                startAge = firstPeriodEndAge + 1,
                endAge = secondPeriodEndAge,
                periodIndex = 2,
                source = "Day",
                isMasterNumber = NumberReducer.isMasterNumber(secondPeriod)
            ),
            LifePeriod(
                number = thirdPeriod,
                startAge = secondPeriodEndAge + 1,
                endAge = null,
                periodIndex = 3,
                source = "Year",
                isMasterNumber = NumberReducer.isMasterNumber(thirdPeriod)
            )
        )
    }

    /**
     * Calculates the Universal Year Number.
     * Represents the collective energy and themes for a specific calendar year.
     *
     * @param year The calendar year
     * @return The universal year number (1-9)
     */
    fun calculateUniversalYear(year: Int): Int {
        return NumberReducer.reduceToSingleDigit(year)
    }

    /**
     * Calculates the Universal Month Number.
     *
     * @param year The calendar year
     * @param month The calendar month (1-12)
     * @return The universal month number (1-9)
     */
    fun calculateUniversalMonth(year: Int, month: Int): Int {
        val universalYear = calculateUniversalYear(year)
        val monthReduced = NumberReducer.reduceToSingleDigit(month)
        return NumberReducer.reduceToSingleDigit(universalYear + monthReduced)
    }

    /**
     * Calculates the Universal Day Number.
     *
     * @param date The date to calculate for
     * @return The universal day number (1-9)
     */
    fun calculateUniversalDay(date: LocalDate): Int {
        val sum = date.monthValue + date.dayOfMonth + date.year
        return NumberReducer.reduceToSingleDigit(sum)
    }

    /**
     * Calculates the current age based on birth date.
     *
     * @param birthDate The date of birth
     * @return Current age in years
     */
    fun calculateAge(birthDate: LocalDate): Int {
        return ChronoUnit.YEARS.between(birthDate, LocalDate.now()).toInt()
    }

    /**
     * Gets the current pinnacle period for a person.
     *
     * @param birthDate The date of birth
     * @return The current pinnacle period
     */
    fun getCurrentPinnacle(birthDate: LocalDate): PinnaclePeriod {
        val age = calculateAge(birthDate)
        val pinnacles = calculatePinnacles(birthDate)
        return pinnacles.first { pinnacle ->
            age >= pinnacle.startAge && (pinnacle.endAge == null || age <= pinnacle.endAge)
        }
    }

    /**
     * Gets the current challenge period for a person.
     *
     * @param birthDate The date of birth
     * @return The current challenge period
     */
    fun getCurrentChallenge(birthDate: LocalDate): ChallengePeriod {
        val age = calculateAge(birthDate)
        val challenges = calculateChallenges(birthDate)
        return challenges.first { challenge ->
            age >= challenge.startAge && (challenge.endAge == null || age <= challenge.endAge)
        }
    }

    /**
     * Gets the current life period for a person.
     *
     * @param birthDate The date of birth
     * @return The current life period
     */
    fun getCurrentLifePeriod(birthDate: LocalDate): LifePeriod {
        val age = calculateAge(birthDate)
        val periods = calculateLifePeriods(birthDate)
        return periods.first { period ->
            age >= period.startAge && (period.endAge == null || age <= period.endAge)
        }
    }

    /**
     * Detects if the birth date contains a karmic debt number.
     */
    private fun detectKarmicDebtInDate(birthDate: LocalDate): Int? {
        val day = birthDate.dayOfMonth
        if (day in NumerologyConstants.KARMIC_DEBT_NUMBERS) {
            return day
        }

        // Check if year reduces through a karmic debt number
        val yearSteps = NumberReducer.getReductionSteps(birthDate.year, preserveMasterNumbers = false)
        return yearSteps.firstOrNull { it in NumerologyConstants.KARMIC_DEBT_NUMBERS }
    }
}

/**
 * Result of Life Path Number calculation.
 */
data class LifePathResult(
    val finalNumber: Int,
    val monthComponent: Int,
    val dayComponent: Int,
    val yearComponent: Int,
    val totalBeforeReduction: Int,
    val reductionSteps: List<Int>,
    val karmicDebtNumber: Int?,
    val isMasterNumber: Boolean
)

/**
 * Represents a Pinnacle period in numerology.
 */
data class PinnaclePeriod(
    val number: Int,
    val startAge: Int,
    val endAge: Int?,
    val periodIndex: Int,
    val isMasterNumber: Boolean
)

/**
 * Represents a Challenge period in numerology.
 */
data class ChallengePeriod(
    val number: Int,
    val startAge: Int,
    val endAge: Int?,
    val periodIndex: Int
)

/**
 * Represents a Life Period (Cycle) in numerology.
 */
data class LifePeriod(
    val number: Int,
    val startAge: Int,
    val endAge: Int?,
    val periodIndex: Int,
    val source: String,
    val isMasterNumber: Boolean
)
