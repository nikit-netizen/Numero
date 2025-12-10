package com.numero.storm.data.model

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.numero.storm.domain.calculator.NumerologySystem

/**
 * Stores a complete numerology analysis for a profile.
 * This allows for quick retrieval without recalculation.
 */
@Entity(
    tableName = "numerology_analyses",
    foreignKeys = [
        ForeignKey(
            entity = Profile::class,
            parentColumns = ["id"],
            childColumns = ["profileId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["profileId"])]
)
data class NumerologyAnalysis(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val profileId: Long,
    val system: NumerologySystem,

    // Core Numbers
    val lifePathNumber: Int,
    val lifePathMasterNumber: Boolean,
    val lifePathKarmicDebt: Int?,

    val expressionNumber: Int,
    val expressionMasterNumber: Boolean,
    val expressionKarmicDebt: Int?,

    val soulUrgeNumber: Int,
    val soulUrgeMasterNumber: Boolean,
    val soulUrgeKarmicDebt: Int?,

    val personalityNumber: Int,
    val personalityMasterNumber: Boolean,
    val personalityKarmicDebt: Int?,

    val birthdayNumber: Int,
    val birthdayMasterNumber: Boolean,

    val maturityNumber: Int,
    val maturityMasterNumber: Boolean,

    val balanceNumber: Int,

    // Special Numbers
    val hiddenPassionNumber: Int?,
    val subconsciousSelfNumber: Int,
    val cornerstoneNumber: Int?,
    val capstoneNumber: Int?,
    val firstVowelNumber: Int?,

    // Karmic Lessons (stored as comma-separated string)
    val karmicLessons: String,

    // Timestamps
    val calculatedAt: Long = System.currentTimeMillis()
)

/**
 * Represents the pinnacles data for a profile.
 */
@Entity(
    tableName = "pinnacles",
    foreignKeys = [
        ForeignKey(
            entity = NumerologyAnalysis::class,
            parentColumns = ["id"],
            childColumns = ["analysisId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["analysisId"])]
)
data class PinnacleEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val analysisId: Long,
    val periodIndex: Int,
    val number: Int,
    val startAge: Int,
    val endAge: Int?,
    val isMasterNumber: Boolean
)

/**
 * Represents the challenges data for a profile.
 */
@Entity(
    tableName = "challenges",
    foreignKeys = [
        ForeignKey(
            entity = NumerologyAnalysis::class,
            parentColumns = ["id"],
            childColumns = ["analysisId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["analysisId"])]
)
data class ChallengeEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val analysisId: Long,
    val periodIndex: Int,
    val number: Int,
    val startAge: Int,
    val endAge: Int?
)

/**
 * Represents the life periods data for a profile.
 */
@Entity(
    tableName = "life_periods",
    foreignKeys = [
        ForeignKey(
            entity = NumerologyAnalysis::class,
            parentColumns = ["id"],
            childColumns = ["analysisId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["analysisId"])]
)
data class LifePeriodEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val analysisId: Long,
    val periodIndex: Int,
    val number: Int,
    val startAge: Int,
    val endAge: Int?,
    val source: String,
    val isMasterNumber: Boolean
)

/**
 * Complete analysis with all related data.
 */
data class CompleteAnalysis(
    @Embedded
    val analysis: NumerologyAnalysis,
    val pinnacles: List<PinnacleEntity>,
    val challenges: List<ChallengeEntity>,
    val lifePeriods: List<LifePeriodEntity>
)

/**
 * Extension to parse karmic lessons from stored string.
 */
fun NumerologyAnalysis.getKarmicLessonsList(): Set<Int> {
    if (karmicLessons.isBlank()) return emptySet()
    return karmicLessons.split(",").mapNotNull { it.trim().toIntOrNull() }.toSet()
}

/**
 * Extension to create karmic lessons string from set.
 */
fun Set<Int>.toKarmicLessonsString(): String {
    return this.sorted().joinToString(",")
}
