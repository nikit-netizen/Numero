package com.numero.storm.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.numero.storm.data.model.Profile
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object for Profile operations.
 */
@Dao
interface ProfileDao {

    @Query("SELECT * FROM profiles ORDER BY isPrimary DESC, updatedAt DESC")
    fun getAllProfiles(): Flow<List<Profile>>

    @Query("SELECT * FROM profiles WHERE id = :id")
    suspend fun getProfileById(id: Long): Profile?

    @Query("SELECT * FROM profiles WHERE id = :id")
    fun getProfileByIdFlow(id: Long): Flow<Profile?>

    @Query("SELECT * FROM profiles WHERE isPrimary = 1 LIMIT 1")
    suspend fun getPrimaryProfile(): Profile?

    @Query("SELECT * FROM profiles WHERE isPrimary = 1 LIMIT 1")
    fun getPrimaryProfileFlow(): Flow<Profile?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProfile(profile: Profile): Long

    @Update
    suspend fun updateProfile(profile: Profile)

    @Delete
    suspend fun deleteProfile(profile: Profile)

    @Query("DELETE FROM profiles WHERE id = :id")
    suspend fun deleteProfileById(id: Long)

    @Query("UPDATE profiles SET isPrimary = 0 WHERE isPrimary = 1")
    suspend fun clearPrimaryProfile()

    @Query("UPDATE profiles SET isPrimary = 1 WHERE id = :id")
    suspend fun setPrimaryProfile(id: Long)

    @Transaction
    suspend fun setAsPrimary(id: Long) {
        clearPrimaryProfile()
        setPrimaryProfile(id)
    }

    @Query("SELECT COUNT(*) FROM profiles")
    suspend fun getProfileCount(): Int

    @Query("SELECT COUNT(*) FROM profiles")
    fun getProfileCountFlow(): Flow<Int>

    @Query("""
        SELECT * FROM profiles
        WHERE firstName LIKE '%' || :query || '%'
        OR lastName LIKE '%' || :query || '%'
        OR middleName LIKE '%' || :query || '%'
        ORDER BY updatedAt DESC
    """)
    fun searchProfiles(query: String): Flow<List<Profile>>
}
