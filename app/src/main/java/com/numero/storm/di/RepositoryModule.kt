package com.numero.storm.di

import com.numero.storm.data.repository.CompatibilityRepository
import com.numero.storm.data.repository.CompatibilityRepositoryImpl
import com.numero.storm.data.repository.NumerologyRepository
import com.numero.storm.data.repository.NumerologyRepositoryImpl
import com.numero.storm.data.repository.ProfileRepository
import com.numero.storm.data.repository.ProfileRepositoryImpl
import com.numero.storm.data.repository.SettingsRepository
import com.numero.storm.data.repository.SettingsRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindProfileRepository(
        impl: ProfileRepositoryImpl
    ): ProfileRepository

    @Binds
    @Singleton
    abstract fun bindNumerologyRepository(
        impl: NumerologyRepositoryImpl
    ): NumerologyRepository

    @Binds
    @Singleton
    abstract fun bindCompatibilityRepository(
        impl: CompatibilityRepositoryImpl
    ): CompatibilityRepository

    @Binds
    @Singleton
    abstract fun bindSettingsRepository(
        impl: SettingsRepositoryImpl
    ): SettingsRepository
}
