package com.numero.storm.domain.calculator

/**
 * Calculates numerology numbers derived from names.
 * Supports both Pythagorean and Chaldean systems, as well as English and Nepali names.
 */
object NameCalculator {

    /**
     * Calculates the Expression Number (also called Destiny Number).
     * This number represents the full potential and abilities you were born with.
     *
     * @param fullName The full name at birth
     * @param system The numerology system to use
     * @return The calculated expression number
     */
    fun calculateExpressionNumber(
        fullName: String,
        system: NumerologySystem = NumerologySystem.PYTHAGOREAN
    ): NumerologyResult {
        val normalizedName = fullName.uppercase().filter { it.isLetter() || it in NumerologyConstants.NEPALI_LETTER_VALUES }
        val letterValues = getLetterValues(system)
        val nepaliValues = NumerologyConstants.NEPALI_LETTER_VALUES

        var totalSum = 0
        val breakdown = mutableListOf<LetterBreakdown>()

        for (char in normalizedName) {
            val value = when {
                char in letterValues -> letterValues[char]!!
                char in nepaliValues -> nepaliValues[char]!!
                else -> continue
            }
            totalSum += value
            breakdown.add(LetterBreakdown(char, value))
        }

        val reducedValue = NumberReducer.reduce(totalSum)
        val karmicDebt = NumberReducer.getKarmicDebt(totalSum)

        return NumerologyResult(
            finalNumber = reducedValue,
            originalSum = totalSum,
            reductionSteps = NumberReducer.getReductionSteps(totalSum),
            breakdown = breakdown,
            karmicDebtNumber = karmicDebt,
            isMasterNumber = NumberReducer.isMasterNumber(reducedValue)
        )
    }

    /**
     * Calculates the Soul Urge Number (also called Heart's Desire Number).
     * This number represents your inner self, motivations, and what you truly desire.
     * Calculated from the vowels in the name.
     *
     * @param fullName The full name at birth
     * @param system The numerology system to use
     * @return The calculated soul urge number
     */
    fun calculateSoulUrgeNumber(
        fullName: String,
        system: NumerologySystem = NumerologySystem.PYTHAGOREAN
    ): NumerologyResult {
        val normalizedName = fullName.uppercase()
        val letterValues = getLetterValues(system)
        val nepaliValues = NumerologyConstants.NEPALI_LETTER_VALUES

        var totalSum = 0
        val breakdown = mutableListOf<LetterBreakdown>()

        for (char in normalizedName) {
            val isVowel = char in NumerologyConstants.VOWELS || char in NumerologyConstants.NEPALI_VOWELS
            if (!isVowel) continue

            val value = when {
                char in letterValues -> letterValues[char]!!
                char in nepaliValues -> nepaliValues[char]!!
                else -> continue
            }
            totalSum += value
            breakdown.add(LetterBreakdown(char, value))
        }

        val reducedValue = NumberReducer.reduce(totalSum)
        val karmicDebt = NumberReducer.getKarmicDebt(totalSum)

        return NumerologyResult(
            finalNumber = reducedValue,
            originalSum = totalSum,
            reductionSteps = NumberReducer.getReductionSteps(totalSum),
            breakdown = breakdown,
            karmicDebtNumber = karmicDebt,
            isMasterNumber = NumberReducer.isMasterNumber(reducedValue)
        )
    }

    /**
     * Calculates the Personality Number (also called Outer Personality Number).
     * This number represents how others perceive you based on your external personality.
     * Calculated from the consonants in the name.
     *
     * @param fullName The full name at birth
     * @param system The numerology system to use
     * @return The calculated personality number
     */
    fun calculatePersonalityNumber(
        fullName: String,
        system: NumerologySystem = NumerologySystem.PYTHAGOREAN
    ): NumerologyResult {
        val normalizedName = fullName.uppercase()
        val letterValues = getLetterValues(system)
        val nepaliValues = NumerologyConstants.NEPALI_LETTER_VALUES

        var totalSum = 0
        val breakdown = mutableListOf<LetterBreakdown>()

        for (char in normalizedName) {
            val isConsonant = char in NumerologyConstants.CONSONANTS ||
                    (char in NumerologyConstants.NEPALI_CONSONANTS && char !in NumerologyConstants.NEPALI_VOWELS)
            if (!isConsonant) continue

            val value = when {
                char in letterValues -> letterValues[char]!!
                char in nepaliValues -> nepaliValues[char]!!
                else -> continue
            }
            totalSum += value
            breakdown.add(LetterBreakdown(char, value))
        }

        val reducedValue = NumberReducer.reduce(totalSum)
        val karmicDebt = NumberReducer.getKarmicDebt(totalSum)

        return NumerologyResult(
            finalNumber = reducedValue,
            originalSum = totalSum,
            reductionSteps = NumberReducer.getReductionSteps(totalSum),
            breakdown = breakdown,
            karmicDebtNumber = karmicDebt,
            isMasterNumber = NumberReducer.isMasterNumber(reducedValue)
        )
    }

    /**
     * Calculates the Maturity Number.
     * This number represents your true self and potential that emerges in mid-life (around 35-45).
     * It's the sum of Life Path and Expression numbers.
     *
     * @param lifePathNumber The life path number
     * @param expressionNumber The expression number
     * @return The calculated maturity number
     */
    fun calculateMaturityNumber(lifePathNumber: Int, expressionNumber: Int): NumerologyResult {
        val sum = lifePathNumber + expressionNumber
        val reducedValue = NumberReducer.reduce(sum)

        return NumerologyResult(
            finalNumber = reducedValue,
            originalSum = sum,
            reductionSteps = NumberReducer.getReductionSteps(sum),
            breakdown = listOf(
                LetterBreakdown('L', lifePathNumber),
                LetterBreakdown('E', expressionNumber)
            ),
            karmicDebtNumber = NumberReducer.getKarmicDebt(sum),
            isMasterNumber = NumberReducer.isMasterNumber(reducedValue)
        )
    }

    /**
     * Calculates the Balance Number.
     * This number provides guidance on how to handle difficult situations.
     * Calculated from the first initials of each name.
     *
     * @param fullName The full name
     * @param system The numerology system to use
     * @return The calculated balance number
     */
    fun calculateBalanceNumber(
        fullName: String,
        system: NumerologySystem = NumerologySystem.PYTHAGOREAN
    ): NumerologyResult {
        val letterValues = getLetterValues(system)
        val parts = fullName.trim().split(Regex("\\s+"))

        var totalSum = 0
        val breakdown = mutableListOf<LetterBreakdown>()

        for (part in parts) {
            if (part.isEmpty()) continue
            val initial = part.first().uppercaseChar()
            val value = letterValues[initial] ?: continue
            totalSum += value
            breakdown.add(LetterBreakdown(initial, value))
        }

        val reducedValue = NumberReducer.reduceToSingleDigit(totalSum)

        return NumerologyResult(
            finalNumber = reducedValue,
            originalSum = totalSum,
            reductionSteps = NumberReducer.getReductionSteps(totalSum, preserveMasterNumbers = false),
            breakdown = breakdown,
            karmicDebtNumber = null,
            isMasterNumber = false
        )
    }

    /**
     * Calculates Karmic Lessons.
     * These are numbers missing from the name, indicating lessons to be learned.
     *
     * @param fullName The full name
     * @param system The numerology system to use
     * @return Set of missing numbers (1-9)
     */
    fun calculateKarmicLessons(
        fullName: String,
        system: NumerologySystem = NumerologySystem.PYTHAGOREAN
    ): Set<Int> {
        val normalizedName = fullName.uppercase().filter { it.isLetter() }
        val letterValues = getLetterValues(system)
        val presentNumbers = mutableSetOf<Int>()

        for (char in normalizedName) {
            letterValues[char]?.let { presentNumbers.add(it) }
        }

        return NumerologyConstants.ALL_SINGLE_DIGITS - presentNumbers
    }

    /**
     * Calculates the Hidden Passion Number.
     * The number that appears most frequently in the name.
     *
     * @param fullName The full name
     * @param system The numerology system to use
     * @return The hidden passion number, or null if no clear passion
     */
    fun calculateHiddenPassion(
        fullName: String,
        system: NumerologySystem = NumerologySystem.PYTHAGOREAN
    ): Int? {
        val normalizedName = fullName.uppercase().filter { it.isLetter() }
        val letterValues = getLetterValues(system)
        val frequencyMap = mutableMapOf<Int, Int>()

        for (char in normalizedName) {
            val value = letterValues[char] ?: continue
            frequencyMap[value] = frequencyMap.getOrDefault(value, 0) + 1
        }

        if (frequencyMap.isEmpty()) return null

        val maxFrequency = frequencyMap.values.maxOrNull() ?: return null
        if (maxFrequency < 2) return null

        return frequencyMap.entries
            .filter { it.value == maxFrequency }
            .maxByOrNull { it.key }?.key
    }

    /**
     * Calculates the Subconscious Self Number.
     * Represents how many different numbers are present in the name (out of 9).
     * Indicates your ability to handle unexpected situations.
     *
     * @param fullName The full name
     * @param system The numerology system to use
     * @return Number from 1-9 representing unique numbers present
     */
    fun calculateSubconsciousSelf(
        fullName: String,
        system: NumerologySystem = NumerologySystem.PYTHAGOREAN
    ): Int {
        val karmicLessons = calculateKarmicLessons(fullName, system)
        return 9 - karmicLessons.size
    }

    /**
     * Calculates all Cornerstone, Capstone, and First Vowel numbers.
     * - Cornerstone: First letter of first name (how you approach new situations)
     * - Capstone: Last letter of first name (how you complete tasks)
     * - First Vowel: First vowel in name (inner self reaction)
     *
     * @param firstName The first name
     * @param system The numerology system to use
     * @return Triple of (Cornerstone, Capstone, FirstVowel) numbers
     */
    fun calculateNameSpecialNumbers(
        firstName: String,
        system: NumerologySystem = NumerologySystem.PYTHAGOREAN
    ): Triple<Int?, Int?, Int?> {
        val normalizedName = firstName.uppercase().filter { it.isLetter() }
        if (normalizedName.isEmpty()) return Triple(null, null, null)

        val letterValues = getLetterValues(system)

        val cornerstone = letterValues[normalizedName.first()]
        val capstone = letterValues[normalizedName.last()]
        val firstVowel = normalizedName.firstOrNull { it in NumerologyConstants.VOWELS }
            ?.let { letterValues[it] }

        return Triple(cornerstone, capstone, firstVowel)
    }

    private fun getLetterValues(system: NumerologySystem): Map<Char, Int> {
        return when (system) {
            NumerologySystem.PYTHAGOREAN -> NumerologyConstants.PYTHAGOREAN_VALUES
            NumerologySystem.CHALDEAN -> NumerologyConstants.CHALDEAN_VALUES
        }
    }
}

/**
 * Represents the numerology system used for calculations.
 */
enum class NumerologySystem {
    PYTHAGOREAN,
    CHALDEAN
}

/**
 * Represents the breakdown of a single letter's contribution to a number.
 */
data class LetterBreakdown(
    val letter: Char,
    val value: Int
)

/**
 * Represents the result of a numerology calculation.
 */
data class NumerologyResult(
    val finalNumber: Int,
    val originalSum: Int,
    val reductionSteps: List<Int>,
    val breakdown: List<LetterBreakdown>,
    val karmicDebtNumber: Int?,
    val isMasterNumber: Boolean
)
