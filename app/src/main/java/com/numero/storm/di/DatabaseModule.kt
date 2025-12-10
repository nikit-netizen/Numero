package com.numero.storm.di

import android.content.Context
import androidx.room.Room
import com.numero.storm.data.database.CompatibilityDao
import com.numero.storm.data.database.NumeroDatabase
import com.numero.storm.data.database.NumerologyDao
import com.numero.storm.data.database.ProfileDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideNumeroDatabase(
        @ApplicationContext context: Context
    ): NumeroDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            NumeroDatabase::class.java,
            "numero_database"
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @Singleton
    fun provideProfileDao(database: NumeroDatabase): ProfileDao {
        return database.profileDao()
    }

    @Provides
    @Singleton
    fun provideNumerologyDao(database: NumeroDatabase): NumerologyDao {
        return database.numerologyDao()
    }

    @Provides
    @Singleton
    fun provideCompatibilityDao(database: NumeroDatabase): CompatibilityDao {
        return database.compatibilityDao()
    }
}
