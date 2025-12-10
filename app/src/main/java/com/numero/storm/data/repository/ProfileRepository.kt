package com.numero.storm.data.repository

import com.numero.storm.data.database.ProfileDao
import com.numero.storm.data.model.Profile
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate
import javax.inject.Inject

interface ProfileRepository {
    fun getAllProfiles(): Flow<List<Profile>>
    fun getProfileById(id: Long): Flow<Profile?>
    fun getPrimaryProfile(): Flow<Profile?>
    fun searchProfiles(query: String): Flow<List<Profile>>
    fun getProfileCount(): Flow<Int>
    suspend fun getProfileByIdOnce(id: Long): Profile?
    suspend fun getPrimaryProfileOnce(): Profile?
    suspend fun createProfile(
        firstName: String,
        middleName: String?,
        lastName: String,
        birthDate: LocalDate,
        isPrimary: Boolean = false
    ): Long
    suspend fun updateProfile(profile: Profile)
    suspend fun deleteProfile(profile: Profile)
    suspend fun deleteProfileById(id: Long)
    suspend fun setAsPrimary(id: Long)
}

class ProfileRepositoryImpl @Inject constructor(
    private val profileDao: ProfileDao
) : ProfileRepository {

    override fun getAllProfiles(): Flow<List<Profile>> {
        return profileDao.getAllProfiles()
    }

    override fun getProfileById(id: Long): Flow<Profile?> {
        return profileDao.getProfileByIdFlow(id)
    }

    override fun getPrimaryProfile(): Flow<Profile?> {
        return profileDao.getPrimaryProfileFlow()
    }

    override fun searchProfiles(query: String): Flow<List<Profile>> {
        return profileDao.searchProfiles(query)
    }

    override fun getProfileCount(): Flow<Int> {
        return profileDao.getProfileCountFlow()
    }

    override suspend fun getProfileByIdOnce(id: Long): Profile? {
        return profileDao.getProfileById(id)
    }

    override suspend fun getPrimaryProfileOnce(): Profile? {
        return profileDao.getPrimaryProfile()
    }

    override suspend fun createProfile(
        firstName: String,
        middleName: String?,
        lastName: String,
        birthDate: LocalDate,
        isPrimary: Boolean
    ): Long {
        val profile = Profile(
            firstName = firstName.trim(),
            middleName = middleName?.trim()?.takeIf { it.isNotBlank() },
            lastName = lastName.trim(),
            birthDate = birthDate,
            isPrimary = isPrimary
        )
        return profileDao.insertProfile(profile)
    }

    override suspend fun updateProfile(profile: Profile) {
        profileDao.updateProfile(profile.copy(updatedAt = System.currentTimeMillis()))
    }

    override suspend fun deleteProfile(profile: Profile) {
        profileDao.deleteProfile(profile)
    }

    override suspend fun deleteProfileById(id: Long) {
        profileDao.deleteProfileById(id)
    }

    override suspend fun setAsPrimary(id: Long) {
        profileDao.setAsPrimary(id)
    }
}
