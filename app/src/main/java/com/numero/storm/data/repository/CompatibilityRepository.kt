package com.numero.storm.data.repository

import com.numero.storm.data.database.CompatibilityDao
import com.numero.storm.data.model.CompatibilityAnalysisEntity
import com.numero.storm.data.model.Profile
import com.numero.storm.data.model.toSharedNumbersString
import com.numero.storm.domain.calculator.CompatibilityCalculator
import kotlinx.coroutines.flow.Flow
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import javax.inject.Inject

interface CompatibilityRepository {
    fun getCompatibilitiesForProfile(profileId: Long): Flow<List<CompatibilityAnalysisEntity>>
    fun getCompatibilityBetweenProfilesFlow(profile1Id: Long, profile2Id: Long): Flow<CompatibilityAnalysisEntity?>
    fun getAllCompatibilities(): Flow<List<CompatibilityAnalysisEntity>>
    fun getTopCompatibilities(limit: Int = 10): Flow<List<CompatibilityAnalysisEntity>>
    suspend fun getCompatibilityBetweenProfiles(profile1Id: Long, profile2Id: Long): CompatibilityAnalysisEntity?
    suspend fun calculateAndSaveCompatibility(profile1: Profile, profile2: Profile): Long
    suspend fun deleteCompatibility(id: Long)
    suspend fun deleteCompatibilityBetweenProfiles(profile1Id: Long, profile2Id: Long)
    suspend fun getAverageScore(profileId: Long): Double?
}

class CompatibilityRepositoryImpl @Inject constructor(
    private val compatibilityDao: CompatibilityDao
) : CompatibilityRepository {

    private val json = Json { ignoreUnknownKeys = true }

    override fun getCompatibilitiesForProfile(profileId: Long): Flow<List<CompatibilityAnalysisEntity>> {
        return compatibilityDao.getCompatibilitiesForProfile(profileId)
    }

    override fun getCompatibilityBetweenProfilesFlow(
        profile1Id: Long,
        profile2Id: Long
    ): Flow<CompatibilityAnalysisEntity?> {
        return compatibilityDao.getCompatibilityBetweenProfilesFlow(profile1Id, profile2Id)
    }

    override fun getAllCompatibilities(): Flow<List<CompatibilityAnalysisEntity>> {
        return compatibilityDao.getAllCompatibilities()
    }

    override fun getTopCompatibilities(limit: Int): Flow<List<CompatibilityAnalysisEntity>> {
        return compatibilityDao.getTopCompatibilities(limit)
    }

    override suspend fun getCompatibilityBetweenProfiles(
        profile1Id: Long,
        profile2Id: Long
    ): CompatibilityAnalysisEntity? {
        return compatibilityDao.getCompatibilityBetweenProfiles(profile1Id, profile2Id)
    }

    override suspend fun calculateAndSaveCompatibility(profile1: Profile, profile2: Profile): Long {
        val result = CompatibilityCalculator.calculateCompatibility(
            person1Name = profile1.fullName,
            person1BirthDate = profile1.birthDate,
            person2Name = profile2.fullName,
            person2BirthDate = profile2.birthDate
        )

        val relationshipNumber = CompatibilityCalculator.calculateRelationshipNumber(
            profile1.birthDate,
            profile2.birthDate
        )

        val entity = CompatibilityAnalysisEntity(
            profile1Id = profile1.id,
            profile2Id = profile2.id,
            overallScore = result.overallScore,
            level = result.level,
            lifePathScore = result.lifePathCompatibility.score,
            lifePathNumber1 = result.lifePathCompatibility.number1,
            lifePathNumber2 = result.lifePathCompatibility.number2,
            expressionScore = result.expressionCompatibility.score,
            expressionNumber1 = result.expressionCompatibility.number1,
            expressionNumber2 = result.expressionCompatibility.number2,
            soulUrgeScore = result.soulUrgeCompatibility.score,
            soulUrgeNumber1 = result.soulUrgeCompatibility.number1,
            soulUrgeNumber2 = result.soulUrgeCompatibility.number2,
            personalityScore = result.personalityCompatibility.score,
            personalityNumber1 = result.personalityCompatibility.number1,
            personalityNumber2 = result.personalityCompatibility.number2,
            birthdayScore = result.birthdayCompatibility.score,
            birthdayNumber1 = result.birthdayCompatibility.number1,
            birthdayNumber2 = result.birthdayCompatibility.number2,
            sharedNumbers = result.sharedNumbers.toSharedNumbersString(),
            complementaryAspects = json.encodeToString(result.complementaryAspects),
            challenges = json.encodeToString(result.challenges),
            relationshipNumber = relationshipNumber
        )

        // Delete any existing compatibility between these profiles first
        compatibilityDao.deleteCompatibilityBetweenProfiles(profile1.id, profile2.id)

        return compatibilityDao.insertCompatibility(entity)
    }

    override suspend fun deleteCompatibility(id: Long) {
        compatibilityDao.deleteCompatibility(id)
    }

    override suspend fun deleteCompatibilityBetweenProfiles(profile1Id: Long, profile2Id: Long) {
        compatibilityDao.deleteCompatibilityBetweenProfiles(profile1Id, profile2Id)
    }

    override suspend fun getAverageScore(profileId: Long): Double? {
        return compatibilityDao.getAverageCompatibilityScore(profileId)
    }
}
