package com.nima.mymood.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.nima.mymood.model.Day
import com.nima.mymood.model.Effect

@TypeConverters(UUIDConverter::class)
@Database(version = 1, exportSchema = false, entities = [Day::class, Effect::class])
abstract class MoodDatabase: RoomDatabase() {
    abstract fun dao(): MoodDao
}