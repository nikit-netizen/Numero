package com.numero.storm.data.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.numero.storm.domain.calculator.CompatibilityLevel

/**
 * Stores compatibility analysis between two profiles.
 */
@Entity(
    tableName = "compatibility_analyses",
    foreignKeys = [
        ForeignKey(
            entity = Profile::class,
            parentColumns = ["id"],
            childColumns = ["profile1Id"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Profile::class,
            parentColumns = ["id"],
            childColumns = ["profile2Id"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["profile1Id"]),
        Index(value = ["profile2Id"]),
        Index(value = ["profile1Id", "profile2Id"], unique = true)
    ]
)
data class CompatibilityAnalysisEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val profile1Id: Long,
    val profile2Id: Long,

    // Overall Compatibility
    val overallScore: Int,
    val level: CompatibilityLevel,

    // Aspect Scores
    val lifePathScore: Int,
    val lifePathNumber1: Int,
    val lifePathNumber2: Int,

    val expressionScore: Int,
    val expressionNumber1: Int,
    val expressionNumber2: Int,

    val soulUrgeScore: Int,
    val soulUrgeNumber1: Int,
    val soulUrgeNumber2: Int,

    val personalityScore: Int,
    val personalityNumber1: Int,
    val personalityNumber2: Int,

    val birthdayScore: Int,
    val birthdayNumber1: Int,
    val birthdayNumber2: Int,

    // Additional Insights (stored as JSON strings)
    val sharedNumbers: String, // comma-separated
    val complementaryAspects: String, // JSON array
    val challenges: String, // JSON array

    // Relationship Number
    val relationshipNumber: Int,

    // Timestamps
    val calculatedAt: Long = System.currentTimeMillis()
)

/**
 * Data class for displaying compatibility with profile information.
 */
data class CompatibilityWithProfiles(
    val compatibility: CompatibilityAnalysisEntity,
    val profile1: Profile,
    val profile2: Profile
)

/**
 * Extension to parse shared numbers.
 */
fun CompatibilityAnalysisEntity.getSharedNumbersList(): List<Int> {
    if (sharedNumbers.isBlank()) return emptyList()
    return sharedNumbers.split(",").mapNotNull { it.trim().toIntOrNull() }
}

/**
 * Extension to create shared numbers string.
 */
fun List<Int>.toSharedNumbersString(): String {
    return this.joinToString(",")
}
