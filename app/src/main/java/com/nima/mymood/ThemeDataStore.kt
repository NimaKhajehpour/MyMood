package com.nima.mymood

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ThemeDataStore(private val context: Context) {

    companion object{

        private val Context.datastore: DataStore<Preferences> by preferencesDataStore("theme")

        val themeKey = booleanPreferencesKey("app_theme")
        val materialYou = booleanPreferencesKey("material_you")
        val autoRateDay = booleanPreferencesKey("auto_rate")
    }

    val getTheme: Flow<Boolean?> = context.datastore.data
        .map {
            it[themeKey] ?: false
        }

    suspend fun saveTheme(isDark: Boolean){
        context.datastore.edit {
            it[themeKey] = isDark
        }
    }

    val getAutoRate: Flow<Boolean?> = context.datastore.data
        .map {
            it[autoRateDay] ?: false
        }

    suspend fun saveAutoRate(autoRate: Boolean){
        context.datastore.edit {
            it[autoRateDay] = autoRate
        }
    }

    val getMaterialYou: Flow<Boolean?> = context.datastore.data
        .map {
            it[materialYou] ?: false
        }

    suspend fun saveMaterialYou(isYou: Boolean){
        context.datastore.edit {
            it[materialYou] = isYou
        }
    }
}