package com.numero.storm.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable
import java.time.LocalDate

/**
 * Represents a user profile with their personal information
 * used for numerology calculations.
 */
@Entity(tableName = "profiles")
data class Profile(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val firstName: String,
    val middleName: String?,
    val lastName: String,
    val birthDate: LocalDate,
    val isPrimary: Boolean = false,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
) {
    val fullName: String
        get() = listOfNotNull(firstName, middleName, lastName)
            .filter { it.isNotBlank() }
            .joinToString(" ")

    val displayName: String
        get() = if (middleName.isNullOrBlank()) {
            "$firstName $lastName"
        } else {
            "$firstName ${middleName.first()}. $lastName"
        }
}

/**
 * Serializable version of Profile for data export/import.
 */
@Serializable
data class ProfileExport(
    val firstName: String,
    val middleName: String?,
    val lastName: String,
    val birthYear: Int,
    val birthMonth: Int,
    val birthDay: Int,
    val isPrimary: Boolean
)

/**
 * Extension function to convert Profile to ProfileExport.
 */
fun Profile.toExport(): ProfileExport {
    return ProfileExport(
        firstName = firstName,
        middleName = middleName,
        lastName = lastName,
        birthYear = birthDate.year,
        birthMonth = birthDate.monthValue,
        birthDay = birthDate.dayOfMonth,
        isPrimary = isPrimary
    )
}

/**
 * Extension function to convert ProfileExport to Profile.
 */
fun ProfileExport.toProfile(): Profile {
    return Profile(
        firstName = firstName,
        middleName = middleName,
        lastName = lastName,
        birthDate = LocalDate.of(birthYear, birthMonth, birthDay),
        isPrimary = isPrimary
    )
}
