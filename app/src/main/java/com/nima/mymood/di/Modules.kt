package com.nima.mymood.di

import androidx.room.Room
import com.nima.mymood.database.MoodDatabase
import com.nima.mymood.database.migration1to2
import com.nima.mymood.repository.MoodRepository
import com.nima.mymood.viewmodels.CalendarOverViewViewModel
import com.nima.mymood.viewmodels.DayCompareViewModel
import com.nima.mymood.viewmodels.DayGraphViewModel
import com.nima.mymood.viewmodels.DayViewModel
import com.nima.mymood.viewmodels.DaysOverviewViewModel
import com.nima.mymood.viewmodels.EditViewModel
import com.nima.mymood.viewmodels.HappyEffectsViewModel
import com.nima.mymood.viewmodels.HomeViewModel
import com.nima.mymood.viewmodels.MenuViewModel
import com.nima.mymood.viewmodels.NeutralEffectsViewModel
import com.nima.mymood.viewmodels.SadEffectsViewModel
import com.nima.mymood.viewmodels.SavedDaysViewModel
import com.nima.mymood.viewmodels.TodayMoodViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module


val appModule = module {
    single {
        Room.databaseBuilder(
            androidApplication(),
            MoodDatabase::class.java,
            "MoodDatabase"
        ).fallbackToDestructiveMigration()
            .addMigrations(migration1to2).build()
    }
    single {
        val database = get<MoodDatabase>()
        database.dao()
    }
    single{
        MoodRepository(get())
    }
    viewModel { DayGraphViewModel(get()) }
    viewModel { DayViewModel(get()) }
    viewModel { EditViewModel(get()) }
    viewModel { HappyEffectsViewModel(get()) }
    viewModel { HomeViewModel(get()) }
    viewModel { MenuViewModel(get()) }
    viewModel { NeutralEffectsViewModel(get()) }
    viewModel { SadEffectsViewModel(get()) }
    viewModel { SavedDaysViewModel(get()) }
    viewModel { TodayMoodViewModel(get()) }
    viewModel { DayCompareViewModel(get()) }
    viewModel { DaysOverviewViewModel(get()) }
    viewModel { CalendarOverViewViewModel(get()) }
}