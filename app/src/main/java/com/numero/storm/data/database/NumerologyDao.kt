package com.numero.storm.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.numero.storm.data.model.ChallengeEntity
import com.numero.storm.data.model.LifePeriodEntity
import com.numero.storm.data.model.NumerologyAnalysis
import com.numero.storm.data.model.PinnacleEntity
import com.numero.storm.domain.calculator.NumerologySystem
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object for Numerology Analysis operations.
 */
@Dao
interface NumerologyDao {

    // Analysis Operations
    @Query("SELECT * FROM numerology_analyses WHERE profileId = :profileId AND system = :system")
    suspend fun getAnalysisByProfileAndSystem(profileId: Long, system: NumerologySystem): NumerologyAnalysis?

    @Query("SELECT * FROM numerology_analyses WHERE profileId = :profileId AND system = :system")
    fun getAnalysisByProfileAndSystemFlow(profileId: Long, system: NumerologySystem): Flow<NumerologyAnalysis?>

    @Query("SELECT * FROM numerology_analyses WHERE profileId = :profileId")
    suspend fun getAllAnalysesForProfile(profileId: Long): List<NumerologyAnalysis>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAnalysis(analysis: NumerologyAnalysis): Long

    @Query("DELETE FROM numerology_analyses WHERE profileId = :profileId")
    suspend fun deleteAnalysesForProfile(profileId: Long)

    @Query("DELETE FROM numerology_analyses WHERE profileId = :profileId AND system = :system")
    suspend fun deleteAnalysisByProfileAndSystem(profileId: Long, system: NumerologySystem)

    // Pinnacle Operations
    @Query("SELECT * FROM pinnacles WHERE analysisId = :analysisId ORDER BY periodIndex")
    suspend fun getPinnaclesForAnalysis(analysisId: Long): List<PinnacleEntity>

    @Query("SELECT * FROM pinnacles WHERE analysisId = :analysisId ORDER BY periodIndex")
    fun getPinnaclesForAnalysisFlow(analysisId: Long): Flow<List<PinnacleEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPinnacles(pinnacles: List<PinnacleEntity>)

    @Query("DELETE FROM pinnacles WHERE analysisId = :analysisId")
    suspend fun deletePinnaclesForAnalysis(analysisId: Long)

    // Challenge Operations
    @Query("SELECT * FROM challenges WHERE analysisId = :analysisId ORDER BY periodIndex")
    suspend fun getChallengesForAnalysis(analysisId: Long): List<ChallengeEntity>

    @Query("SELECT * FROM challenges WHERE analysisId = :analysisId ORDER BY periodIndex")
    fun getChallengesForAnalysisFlow(analysisId: Long): Flow<List<ChallengeEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertChallenges(challenges: List<ChallengeEntity>)

    @Query("DELETE FROM challenges WHERE analysisId = :analysisId")
    suspend fun deleteChallengesForAnalysis(analysisId: Long)

    // Life Period Operations
    @Query("SELECT * FROM life_periods WHERE analysisId = :analysisId ORDER BY periodIndex")
    suspend fun getLifePeriodsForAnalysis(analysisId: Long): List<LifePeriodEntity>

    @Query("SELECT * FROM life_periods WHERE analysisId = :analysisId ORDER BY periodIndex")
    fun getLifePeriodsForAnalysisFlow(analysisId: Long): Flow<List<LifePeriodEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLifePeriods(periods: List<LifePeriodEntity>)

    @Query("DELETE FROM life_periods WHERE analysisId = :analysisId")
    suspend fun deleteLifePeriodsForAnalysis(analysisId: Long)

    // Transaction for complete analysis save
    @Transaction
    suspend fun saveCompleteAnalysis(
        analysis: NumerologyAnalysis,
        pinnacles: List<PinnacleEntity>,
        challenges: List<ChallengeEntity>,
        lifePeriods: List<LifePeriodEntity>
    ): Long {
        // Delete existing analysis for this profile and system
        val existingAnalysis = getAnalysisByProfileAndSystem(analysis.profileId, analysis.system)
        existingAnalysis?.let {
            deletePinnaclesForAnalysis(it.id)
            deleteChallengesForAnalysis(it.id)
            deleteLifePeriodsForAnalysis(it.id)
            deleteAnalysisByProfileAndSystem(analysis.profileId, analysis.system)
        }

        // Insert new analysis
        val analysisId = insertAnalysis(analysis)

        // Update entity IDs and insert related data
        val pinnaclesWithId = pinnacles.map { it.copy(analysisId = analysisId) }
        val challengesWithId = challenges.map { it.copy(analysisId = analysisId) }
        val periodsWithId = lifePeriods.map { it.copy(analysisId = analysisId) }

        insertPinnacles(pinnaclesWithId)
        insertChallenges(challengesWithId)
        insertLifePeriods(periodsWithId)

        return analysisId
    }
}
