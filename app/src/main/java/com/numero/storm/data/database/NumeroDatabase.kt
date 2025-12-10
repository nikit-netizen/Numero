package com.numero.storm.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.numero.storm.data.model.ChallengeEntity
import com.numero.storm.data.model.CompatibilityAnalysisEntity
import com.numero.storm.data.model.LifePeriodEntity
import com.numero.storm.data.model.NumerologyAnalysis
import com.numero.storm.data.model.PinnacleEntity
import com.numero.storm.data.model.Profile

/**
 * Main Room Database for the Numero application.
 */
@Database(
    entities = [
        Profile::class,
        NumerologyAnalysis::class,
        PinnacleEntity::class,
        ChallengeEntity::class,
        LifePeriodEntity::class,
        CompatibilityAnalysisEntity::class
    ],
    version = 1,
    exportSchema = true
)
@TypeConverters(Converters::class)
abstract class NumeroDatabase : RoomDatabase() {

    abstract fun profileDao(): ProfileDao
    abstract fun numerologyDao(): NumerologyDao
    abstract fun compatibilityDao(): CompatibilityDao

    companion object {
        private const val DATABASE_NAME = "numero_database"

        @Volatile
        private var INSTANCE: NumeroDatabase? = null

        fun getInstance(context: Context): NumeroDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    NumeroDatabase::class.java,
                    DATABASE_NAME
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
