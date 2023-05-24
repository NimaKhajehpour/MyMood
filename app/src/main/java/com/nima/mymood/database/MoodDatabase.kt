package com.nima.mymood.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.nima.mymood.model.Day
import com.nima.mymood.model.Effect

@TypeConverters(UUIDConverter::class)
@Database(version = 2, exportSchema = false, entities = [Day::class, Effect::class])
abstract class MoodDatabase: RoomDatabase() {
    abstract fun dao(): MoodDao
}

val migration1to2 = object:Migration(1,2){
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("alter table effect add column hour text not null default ''")
        database.execSQL("alter table effect add column minute text not null default ''")
    }
}