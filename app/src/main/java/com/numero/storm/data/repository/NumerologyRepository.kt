package com.numero.storm.data.repository

import com.numero.storm.data.database.NumerologyDao
import com.numero.storm.data.model.ChallengeEntity
import com.numero.storm.data.model.LifePeriodEntity
import com.numero.storm.data.model.NumerologyAnalysis
import com.numero.storm.data.model.PinnacleEntity
import com.numero.storm.data.model.Profile
import com.numero.storm.data.model.toKarmicLessonsString
import com.numero.storm.domain.calculator.DateCalculator
import com.numero.storm.domain.calculator.NameCalculator
import com.numero.storm.domain.calculator.NumerologySystem
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface NumerologyRepository {
    fun getAnalysisFlow(profileId: Long, system: NumerologySystem): Flow<NumerologyAnalysis?>
    fun getPinnaclesFlow(analysisId: Long): Flow<List<PinnacleEntity>>
    fun getChallengesFlow(analysisId: Long): Flow<List<ChallengeEntity>>
    fun getLifePeriodsFlow(analysisId: Long): Flow<List<LifePeriodEntity>>
    suspend fun getAnalysis(profileId: Long, system: NumerologySystem): NumerologyAnalysis?
    suspend fun calculateAndSaveAnalysis(profile: Profile, system: NumerologySystem): Long
    suspend fun getPinnacles(analysisId: Long): List<PinnacleEntity>
    suspend fun getChallenges(analysisId: Long): List<ChallengeEntity>
    suspend fun getLifePeriods(analysisId: Long): List<LifePeriodEntity>
    suspend fun deleteAnalysesForProfile(profileId: Long)
}

class NumerologyRepositoryImpl @Inject constructor(
    private val numerologyDao: NumerologyDao
) : NumerologyRepository {

    override fun getAnalysisFlow(profileId: Long, system: NumerologySystem): Flow<NumerologyAnalysis?> {
        return numerologyDao.getAnalysisByProfileAndSystemFlow(profileId, system)
    }

    override fun getPinnaclesFlow(analysisId: Long): Flow<List<PinnacleEntity>> {
        return numerologyDao.getPinnaclesForAnalysisFlow(analysisId)
    }

    override fun getChallengesFlow(analysisId: Long): Flow<List<ChallengeEntity>> {
        return numerologyDao.getChallengesForAnalysisFlow(analysisId)
    }

    override fun getLifePeriodsFlow(analysisId: Long): Flow<List<LifePeriodEntity>> {
        return numerologyDao.getLifePeriodsForAnalysisFlow(analysisId)
    }

    override suspend fun getAnalysis(profileId: Long, system: NumerologySystem): NumerologyAnalysis? {
        return numerologyDao.getAnalysisByProfileAndSystem(profileId, system)
    }

    override suspend fun calculateAndSaveAnalysis(profile: Profile, system: NumerologySystem): Long {
        val birthDate = profile.birthDate
        val fullName = profile.fullName

        // Calculate date-based numbers
        val lifePathResult = DateCalculator.calculateLifePathNumber(birthDate)
        val birthdayResult = DateCalculator.calculateBirthdayNumber(birthDate)
        val pinnacles = DateCalculator.calculatePinnacles(birthDate)
        val challenges = DateCalculator.calculateChallenges(birthDate)
        val lifePeriods = DateCalculator.calculateLifePeriods(birthDate)

        // Calculate name-based numbers
        val expressionResult = NameCalculator.calculateExpressionNumber(fullName, system)
        val soulUrgeResult = NameCalculator.calculateSoulUrgeNumber(fullName, system)
        val personalityResult = NameCalculator.calculatePersonalityNumber(fullName, system)
        val maturityResult = NameCalculator.calculateMaturityNumber(
            lifePathResult.finalNumber,
            expressionResult.finalNumber
        )
        val balanceResult = NameCalculator.calculateBalanceNumber(fullName, system)
        val karmicLessons = NameCalculator.calculateKarmicLessons(fullName, system)
        val hiddenPassion = NameCalculator.calculateHiddenPassion(fullName, system)
        val subconsciousSelf = NameCalculator.calculateSubconsciousSelf(fullName, system)
        val (cornerstone, capstone, firstVowel) = NameCalculator.calculateNameSpecialNumbers(
            profile.firstName,
            system
        )

        // Create analysis entity
        val analysis = NumerologyAnalysis(
            profileId = profile.id,
            system = system,
            lifePathNumber = lifePathResult.finalNumber,
            lifePathMasterNumber = lifePathResult.isMasterNumber,
            lifePathKarmicDebt = lifePathResult.karmicDebtNumber,
            expressionNumber = expressionResult.finalNumber,
            expressionMasterNumber = expressionResult.isMasterNumber,
            expressionKarmicDebt = expressionResult.karmicDebtNumber,
            soulUrgeNumber = soulUrgeResult.finalNumber,
            soulUrgeMasterNumber = soulUrgeResult.isMasterNumber,
            soulUrgeKarmicDebt = soulUrgeResult.karmicDebtNumber,
            personalityNumber = personalityResult.finalNumber,
            personalityMasterNumber = personalityResult.isMasterNumber,
            personalityKarmicDebt = personalityResult.karmicDebtNumber,
            birthdayNumber = birthdayResult.finalNumber,
            birthdayMasterNumber = birthdayResult.isMasterNumber,
            maturityNumber = maturityResult.finalNumber,
            maturityMasterNumber = maturityResult.isMasterNumber,
            balanceNumber = balanceResult.finalNumber,
            hiddenPassionNumber = hiddenPassion,
            subconsciousSelfNumber = subconsciousSelf,
            cornerstoneNumber = cornerstone,
            capstoneNumber = capstone,
            firstVowelNumber = firstVowel,
            karmicLessons = karmicLessons.toKarmicLessonsString()
        )

        // Create pinnacle entities
        val pinnacleEntities = pinnacles.map { pinnacle ->
            PinnacleEntity(
                analysisId = 0,
                periodIndex = pinnacle.periodIndex,
                number = pinnacle.number,
                startAge = pinnacle.startAge,
                endAge = pinnacle.endAge,
                isMasterNumber = pinnacle.isMasterNumber
            )
        }

        // Create challenge entities
        val challengeEntities = challenges.map { challenge ->
            ChallengeEntity(
                analysisId = 0,
                periodIndex = challenge.periodIndex,
                number = challenge.number,
                startAge = challenge.startAge,
                endAge = challenge.endAge
            )
        }

        // Create life period entities
        val lifePeriodEntities = lifePeriods.map { period ->
            LifePeriodEntity(
                analysisId = 0,
                periodIndex = period.periodIndex,
                number = period.number,
                startAge = period.startAge,
                endAge = period.endAge,
                source = period.source,
                isMasterNumber = period.isMasterNumber
            )
        }

        // Save complete analysis
        return numerologyDao.saveCompleteAnalysis(
            analysis,
            pinnacleEntities,
            challengeEntities,
            lifePeriodEntities
        )
    }

    override suspend fun getPinnacles(analysisId: Long): List<PinnacleEntity> {
        return numerologyDao.getPinnaclesForAnalysis(analysisId)
    }

    override suspend fun getChallenges(analysisId: Long): List<ChallengeEntity> {
        return numerologyDao.getChallengesForAnalysis(analysisId)
    }

    override suspend fun getLifePeriods(analysisId: Long): List<LifePeriodEntity> {
        return numerologyDao.getLifePeriodsForAnalysis(analysisId)
    }

    override suspend fun deleteAnalysesForProfile(profileId: Long) {
        numerologyDao.deleteAnalysesForProfile(profileId)
    }
}
