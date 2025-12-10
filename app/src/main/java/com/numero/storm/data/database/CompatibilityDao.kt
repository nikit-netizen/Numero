package com.numero.storm.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.numero.storm.data.model.CompatibilityAnalysisEntity
import com.numero.storm.data.model.CompatibilityWithProfiles
import com.numero.storm.data.model.Profile
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object for Compatibility Analysis operations.
 */
@Dao
interface CompatibilityDao {

    @Query("""
        SELECT * FROM compatibility_analyses
        WHERE (profile1Id = :profileId OR profile2Id = :profileId)
        ORDER BY calculatedAt DESC
    """)
    fun getCompatibilitiesForProfile(profileId: Long): Flow<List<CompatibilityAnalysisEntity>>

    @Query("""
        SELECT * FROM compatibility_analyses
        WHERE (profile1Id = :profile1Id AND profile2Id = :profile2Id)
        OR (profile1Id = :profile2Id AND profile2Id = :profile1Id)
        LIMIT 1
    """)
    suspend fun getCompatibilityBetweenProfiles(
        profile1Id: Long,
        profile2Id: Long
    ): CompatibilityAnalysisEntity?

    @Query("""
        SELECT * FROM compatibility_analyses
        WHERE (profile1Id = :profile1Id AND profile2Id = :profile2Id)
        OR (profile1Id = :profile2Id AND profile2Id = :profile1Id)
    """)
    fun getCompatibilityBetweenProfilesFlow(
        profile1Id: Long,
        profile2Id: Long
    ): Flow<CompatibilityAnalysisEntity?>

    @Query("SELECT * FROM compatibility_analyses ORDER BY calculatedAt DESC")
    fun getAllCompatibilities(): Flow<List<CompatibilityAnalysisEntity>>

    @Query("SELECT * FROM compatibility_analyses ORDER BY overallScore DESC LIMIT :limit")
    fun getTopCompatibilities(limit: Int): Flow<List<CompatibilityAnalysisEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCompatibility(compatibility: CompatibilityAnalysisEntity): Long

    @Query("DELETE FROM compatibility_analyses WHERE id = :id")
    suspend fun deleteCompatibility(id: Long)

    @Query("""
        DELETE FROM compatibility_analyses
        WHERE profile1Id = :profileId OR profile2Id = :profileId
    """)
    suspend fun deleteCompatibilitiesForProfile(profileId: Long)

    @Query("""
        DELETE FROM compatibility_analyses
        WHERE (profile1Id = :profile1Id AND profile2Id = :profile2Id)
        OR (profile1Id = :profile2Id AND profile2Id = :profile1Id)
    """)
    suspend fun deleteCompatibilityBetweenProfiles(profile1Id: Long, profile2Id: Long)

    @Query("SELECT COUNT(*) FROM compatibility_analyses")
    suspend fun getCompatibilityCount(): Int

    @Query("SELECT AVG(overallScore) FROM compatibility_analyses WHERE profile1Id = :profileId OR profile2Id = :profileId")
    suspend fun getAverageCompatibilityScore(profileId: Long): Double?
}
