package com.nima.mymood.di

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import com.nima.mymood.database.MoodDao
import com.nima.mymood.database.MoodDatabase
import com.nima.mymood.database.migration1to2
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object Modules {

    @Provides
    @Singleton
    fun provideDao(database: MoodDatabase): MoodDao =  database.dao()

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): MoodDatabase =
        Room.databaseBuilder(
            context,
            MoodDatabase::class.java,
            "MoodDatabase"
        ).fallbackToDestructiveMigration()
            .addMigrations(migration1to2).build()
}