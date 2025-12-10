package com.numero.storm.domain.calculator

import java.time.LocalDate

/**
 * Calculates numerology compatibility between two people.
 * Provides detailed analysis of relationship potential across multiple dimensions.
 */
object CompatibilityCalculator {

    /**
     * Compatibility level thresholds.
     */
    private const val EXCELLENT_THRESHOLD = 85
    private const val GOOD_THRESHOLD = 70
    private const val MODERATE_THRESHOLD = 50

    /**
     * Natural compatibility matrix based on numerological principles.
     * Higher values indicate better natural compatibility.
     */
    private val COMPATIBILITY_MATRIX: Map<Pair<Int, Int>, Int> = buildMap {
        // Number 1 compatibilities
        put(Pair(1, 1), 75); put(Pair(1, 2), 60); put(Pair(1, 3), 90)
        put(Pair(1, 4), 55); put(Pair(1, 5), 85); put(Pair(1, 6), 70)
        put(Pair(1, 7), 65); put(Pair(1, 8), 80); put(Pair(1, 9), 85)

        // Number 2 compatibilities
        put(Pair(2, 2), 70); put(Pair(2, 3), 75); put(Pair(2, 4), 85)
        put(Pair(2, 5), 55); put(Pair(2, 6), 90); put(Pair(2, 7), 80)
        put(Pair(2, 8), 75); put(Pair(2, 9), 85)

        // Number 3 compatibilities
        put(Pair(3, 3), 80); put(Pair(3, 4), 50); put(Pair(3, 5), 90)
        put(Pair(3, 6), 95); put(Pair(3, 7), 60); put(Pair(3, 8), 65)
        put(Pair(3, 9), 90)

        // Number 4 compatibilities
        put(Pair(4, 4), 75); put(Pair(4, 5), 45); put(Pair(4, 6), 80)
        put(Pair(4, 7), 85); put(Pair(4, 8), 90); put(Pair(4, 9), 55)

        // Number 5 compatibilities
        put(Pair(5, 5), 85); put(Pair(5, 6), 50); put(Pair(5, 7), 75)
        put(Pair(5, 8), 60); put(Pair(5, 9), 80)

        // Number 6 compatibilities
        put(Pair(6, 6), 85); put(Pair(6, 7), 55); put(Pair(6, 8), 70)
        put(Pair(6, 9), 95)

        // Number 7 compatibilities
        put(Pair(7, 7), 80); put(Pair(7, 8), 50); put(Pair(7, 9), 65)

        // Number 8 compatibilities
        put(Pair(8, 8), 75); put(Pair(8, 9), 70)

        // Number 9 compatibilities
        put(Pair(9, 9), 80)
    }

    /**
     * Master number compatibility bonuses.
     */
    private val MASTER_NUMBER_BONUSES: Map<Pair<Int, Int>, Int> = mapOf(
        Pair(11, 11) to 10,
        Pair(22, 22) to 10,
        Pair(33, 33) to 10,
        Pair(11, 22) to 8,
        Pair(11, 33) to 8,
        Pair(22, 33) to 8
    )

    /**
     * Calculates comprehensive compatibility between two people.
     *
     * @param person1Name First person's full name
     * @param person1BirthDate First person's birth date
     * @param person2Name Second person's full name
     * @param person2BirthDate Second person's birth date
     * @return Detailed compatibility analysis
     */
    fun calculateCompatibility(
        person1Name: String,
        person1BirthDate: LocalDate,
        person2Name: String,
        person2BirthDate: LocalDate
    ): CompatibilityResult {
        // Calculate all core numbers for both people
        val person1Numbers = calculateCoreNumbers(person1Name, person1BirthDate)
        val person2Numbers = calculateCoreNumbers(person2Name, person2BirthDate)

        // Calculate individual aspect compatibilities
        val lifePathCompatibility = calculateAspectCompatibility(
            person1Numbers.lifePath,
            person2Numbers.lifePath,
            person1Numbers.lifePathMaster,
            person2Numbers.lifePathMaster
        )

        val expressionCompatibility = calculateAspectCompatibility(
            person1Numbers.expression,
            person2Numbers.expression,
            person1Numbers.expressionMaster,
            person2Numbers.expressionMaster
        )

        val soulUrgeCompatibility = calculateAspectCompatibility(
            person1Numbers.soulUrge,
            person2Numbers.soulUrge,
            person1Numbers.soulUrgeMaster,
            person2Numbers.soulUrgeMaster
        )

        val personalityCompatibility = calculateAspectCompatibility(
            person1Numbers.personality,
            person2Numbers.personality,
            person1Numbers.personalityMaster,
            person2Numbers.personalityMaster
        )

        val birthdayCompatibility = calculateAspectCompatibility(
            person1Numbers.birthday,
            person2Numbers.birthday,
            false, false
        )

        // Calculate weighted overall score
        val overallScore = calculateWeightedScore(
            lifePathCompatibility to 30,
            expressionCompatibility to 25,
            soulUrgeCompatibility to 20,
            personalityCompatibility to 15,
            birthdayCompatibility to 10
        )

        // Determine compatibility level
        val level = when {
            overallScore >= EXCELLENT_THRESHOLD -> CompatibilityLevel.EXCELLENT
            overallScore >= GOOD_THRESHOLD -> CompatibilityLevel.GOOD
            overallScore >= MODERATE_THRESHOLD -> CompatibilityLevel.MODERATE
            else -> CompatibilityLevel.CHALLENGING
        }

        // Find shared numbers (strengths)
        val sharedNumbers = findSharedNumbers(person1Numbers, person2Numbers)

        // Find complementary aspects
        val complementaryAspects = findComplementaryAspects(person1Numbers, person2Numbers)

        // Find potential challenges
        val challenges = findChallenges(person1Numbers, person2Numbers)

        return CompatibilityResult(
            overallScore = overallScore,
            level = level,
            lifePathCompatibility = AspectCompatibility("Life Path", lifePathCompatibility, person1Numbers.lifePath, person2Numbers.lifePath),
            expressionCompatibility = AspectCompatibility("Expression", expressionCompatibility, person1Numbers.expression, person2Numbers.expression),
            soulUrgeCompatibility = AspectCompatibility("Soul Urge", soulUrgeCompatibility, person1Numbers.soulUrge, person2Numbers.soulUrge),
            personalityCompatibility = AspectCompatibility("Personality", personalityCompatibility, person1Numbers.personality, person2Numbers.personality),
            birthdayCompatibility = AspectCompatibility("Birthday", birthdayCompatibility, person1Numbers.birthday, person2Numbers.birthday),
            sharedNumbers = sharedNumbers,
            complementaryAspects = complementaryAspects,
            challenges = challenges,
            person1CoreNumbers = person1Numbers,
            person2CoreNumbers = person2Numbers
        )
    }

    /**
     * Calculates compatibility for a specific aspect (e.g., life path).
     */
    private fun calculateAspectCompatibility(
        number1: Int,
        number2: Int,
        isMaster1: Boolean,
        isMaster2: Boolean
    ): Int {
        val reducedNum1 = if (number1 > 9) NumberReducer.reduceToSingleDigit(number1) else number1
        val reducedNum2 = if (number2 > 9) NumberReducer.reduceToSingleDigit(number2) else number2

        val baseCompatibility = getBaseCompatibility(reducedNum1, reducedNum2)

        // Apply master number bonuses
        var bonus = 0
        if (isMaster1 && isMaster2) {
            val masterPair = if (number1 <= number2) Pair(number1, number2) else Pair(number2, number1)
            bonus = MASTER_NUMBER_BONUSES[masterPair] ?: 5
        } else if (isMaster1 || isMaster2) {
            bonus = 3
        }

        return minOf(100, baseCompatibility + bonus)
    }

    /**
     * Gets base compatibility from the matrix.
     */
    private fun getBaseCompatibility(num1: Int, num2: Int): Int {
        val pair = if (num1 <= num2) Pair(num1, num2) else Pair(num2, num1)
        return COMPATIBILITY_MATRIX[pair] ?: 50
    }

    /**
     * Calculates weighted score from multiple aspects.
     */
    private fun calculateWeightedScore(vararg aspects: Pair<Int, Int>): Int {
        val totalWeight = aspects.sumOf { it.second }
        val weightedSum = aspects.sumOf { it.first * it.second }
        return weightedSum / totalWeight
    }

    /**
     * Finds numbers that both people share.
     */
    private fun findSharedNumbers(person1: CoreNumbers, person2: CoreNumbers): List<Int> {
        val numbers1 = setOf(person1.lifePath, person1.expression, person1.soulUrge, person1.personality, person1.birthday)
        val numbers2 = setOf(person2.lifePath, person2.expression, person2.soulUrge, person2.personality, person2.birthday)
        return (numbers1 intersect numbers2).toList().sorted()
    }

    /**
     * Finds complementary aspects between two people.
     */
    private fun findComplementaryAspects(person1: CoreNumbers, person2: CoreNumbers): List<String> {
        val aspects = mutableListOf<String>()

        // Check for complementary life paths
        val lpSum = person1.lifePath + person2.lifePath
        if (NumberReducer.reduceToSingleDigit(lpSum) == 9) {
            aspects.add("Life Paths combine to 9 - Universal completion")
        }

        // Check for 1-2 leadership/support dynamic
        if ((person1.lifePath == 1 && person2.lifePath == 2) ||
            (person1.lifePath == 2 && person2.lifePath == 1)
        ) {
            aspects.add("Natural leader-supporter dynamic")
        }

        // Check for 3-6 creative/nurturing dynamic
        if ((person1.expression == 3 && person2.expression == 6) ||
            (person1.expression == 6 && person2.expression == 3)
        ) {
            aspects.add("Creative expression meets nurturing support")
        }

        // Check for soul urge alignment
        if (person1.soulUrge == person2.soulUrge) {
            aspects.add("Shared inner desires and motivations")
        }

        // Check for complementary master numbers
        if (person1.lifePathMaster && person2.lifePathMaster) {
            aspects.add("Both carry master number energy")
        }

        return aspects
    }

    /**
     * Finds potential challenges in the relationship.
     */
    private fun findChallenges(person1: CoreNumbers, person2: CoreNumbers): List<String> {
        val challenges = mutableListOf<String>()

        // Check for conflicting life paths
        val conflictingPairs = setOf(Pair(4, 5), Pair(5, 4), Pair(7, 8), Pair(8, 7))
        if (Pair(person1.lifePath, person2.lifePath) in conflictingPairs) {
            challenges.add("Different approaches to life structure and freedom")
        }

        // Check for personality clashes
        if (person1.personality == 1 && person2.personality == 1) {
            challenges.add("Both desire to lead - may compete for control")
        }

        // Check for communication differences (3 vs 7)
        if ((person1.expression == 3 && person2.expression == 7) ||
            (person1.expression == 7 && person2.expression == 3)
        ) {
            challenges.add("Different communication styles - social vs introspective")
        }

        // Check for value differences (soul urge 1 vs 2)
        if ((person1.soulUrge == 1 && person2.soulUrge == 2) ||
            (person1.soulUrge == 2 && person2.soulUrge == 1)
        ) {
            challenges.add("Different core needs - independence vs partnership")
        }

        return challenges
    }

    /**
     * Calculates core numbers for a person.
     */
    private fun calculateCoreNumbers(name: String, birthDate: LocalDate): CoreNumbers {
        val lifePath = DateCalculator.calculateLifePathNumber(birthDate)
        val expression = NameCalculator.calculateExpressionNumber(name)
        val soulUrge = NameCalculator.calculateSoulUrgeNumber(name)
        val personality = NameCalculator.calculatePersonalityNumber(name)
        val birthday = DateCalculator.calculateBirthdayNumber(birthDate)

        return CoreNumbers(
            lifePath = lifePath.finalNumber,
            lifePathMaster = lifePath.isMasterNumber,
            expression = expression.finalNumber,
            expressionMaster = expression.isMasterNumber,
            soulUrge = soulUrge.finalNumber,
            soulUrgeMaster = soulUrge.isMasterNumber,
            personality = personality.finalNumber,
            personalityMaster = personality.isMasterNumber,
            birthday = birthday.finalNumber
        )
    }

    /**
     * Calculates relationship number from two birth dates.
     * This represents the combined energy of the relationship itself.
     *
     * @param birthDate1 First person's birth date
     * @param birthDate2 Second person's birth date
     * @return The relationship number
     */
    fun calculateRelationshipNumber(birthDate1: LocalDate, birthDate2: LocalDate): Int {
        val lifePath1 = DateCalculator.calculateLifePathNumber(birthDate1).finalNumber
        val lifePath2 = DateCalculator.calculateLifePathNumber(birthDate2).finalNumber
        return NumberReducer.reduce(lifePath1 + lifePath2)
    }

    /**
     * Calculates the best dates for important relationship events.
     *
     * @param birthDate1 First person's birth date
     * @param birthDate2 Second person's birth date
     * @param year The year to analyze
     * @return List of auspicious dates with their scores
     */
    fun calculateAuspiciousDates(
        birthDate1: LocalDate,
        birthDate2: LocalDate,
        year: Int
    ): List<AuspiciousDate> {
        val relationshipNumber = calculateRelationshipNumber(birthDate1, birthDate2)
        val auspiciousDates = mutableListOf<AuspiciousDate>()

        val startDate = LocalDate.of(year, 1, 1)
        val endDate = LocalDate.of(year, 12, 31)

        var currentDate = startDate
        while (!currentDate.isAfter(endDate)) {
            val universalDay = DateCalculator.calculateUniversalDay(currentDate)
            val personalYear1 = DateCalculator.calculatePersonalYear(birthDate1, year)
            val personalYear2 = DateCalculator.calculatePersonalYear(birthDate2, year)

            val score = calculateDateScore(
                universalDay,
                relationshipNumber,
                personalYear1,
                personalYear2,
                currentDate.dayOfMonth
            )

            if (score >= 80) {
                auspiciousDates.add(AuspiciousDate(currentDate, score))
            }

            currentDate = currentDate.plusDays(1)
        }

        return auspiciousDates.sortedByDescending { it.score }.take(30)
    }

    /**
     * Calculates score for a specific date.
     */
    private fun calculateDateScore(
        universalDay: Int,
        relationshipNumber: Int,
        personalYear1: Int,
        personalYear2: Int,
        dayOfMonth: Int
    ): Int {
        var score = 50

        // Universal day matches relationship number
        if (universalDay == relationshipNumber) score += 20

        // Day of month reduces to relationship number
        if (NumberReducer.reduceToSingleDigit(dayOfMonth) == relationshipNumber) score += 15

        // Harmony with personal years
        if (universalDay == personalYear1 || universalDay == personalYear2) score += 10

        // Auspicious numbers (6, 9 for relationships)
        if (universalDay == 6 || universalDay == 9) score += 10

        // Master number days
        if (dayOfMonth == 11 || dayOfMonth == 22) score += 5

        return minOf(100, score)
    }
}

/**
 * Core numerology numbers for a person.
 */
data class CoreNumbers(
    val lifePath: Int,
    val lifePathMaster: Boolean,
    val expression: Int,
    val expressionMaster: Boolean,
    val soulUrge: Int,
    val soulUrgeMaster: Boolean,
    val personality: Int,
    val personalityMaster: Boolean,
    val birthday: Int
)

/**
 * Compatibility for a specific aspect.
 */
data class AspectCompatibility(
    val aspectName: String,
    val score: Int,
    val number1: Int,
    val number2: Int
)

/**
 * Overall compatibility result.
 */
data class CompatibilityResult(
    val overallScore: Int,
    val level: CompatibilityLevel,
    val lifePathCompatibility: AspectCompatibility,
    val expressionCompatibility: AspectCompatibility,
    val soulUrgeCompatibility: AspectCompatibility,
    val personalityCompatibility: AspectCompatibility,
    val birthdayCompatibility: AspectCompatibility,
    val sharedNumbers: List<Int>,
    val complementaryAspects: List<String>,
    val challenges: List<String>,
    val person1CoreNumbers: CoreNumbers,
    val person2CoreNumbers: CoreNumbers
)

/**
 * Compatibility level classification.
 */
enum class CompatibilityLevel {
    EXCELLENT,
    GOOD,
    MODERATE,
    CHALLENGING
}

/**
 * Auspicious date with score.
 */
data class AuspiciousDate(
    val date: LocalDate,
    val score: Int
)
