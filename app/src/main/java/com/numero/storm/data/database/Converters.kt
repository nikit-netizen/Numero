package com.numero.storm.data.database

import androidx.room.TypeConverter
import com.numero.storm.domain.calculator.CompatibilityLevel
import com.numero.storm.domain.calculator.NumerologySystem
import java.time.LocalDate

/**
 * Room type converters for custom data types.
 */
class Converters {

    @TypeConverter
    fun fromLocalDate(date: LocalDate?): Long? {
        return date?.toEpochDay()
    }

    @TypeConverter
    fun toLocalDate(epochDay: Long?): LocalDate? {
        return epochDay?.let { LocalDate.ofEpochDay(it) }
    }

    @TypeConverter
    fun fromNumerologySystem(system: NumerologySystem): String {
        return system.name
    }

    @TypeConverter
    fun toNumerologySystem(name: String): NumerologySystem {
        return NumerologySystem.valueOf(name)
    }

    @TypeConverter
    fun fromCompatibilityLevel(level: CompatibilityLevel): String {
        return level.name
    }

    @TypeConverter
    fun toCompatibilityLevel(name: String): CompatibilityLevel {
        return CompatibilityLevel.valueOf(name)
    }
}
