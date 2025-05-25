package com.nima.mymood

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.single

class ThemeDataStore(private val context: Context) {

    companion object{

        private val Context.datastore: DataStore<Preferences> by preferencesDataStore("theme")

        val themeKey = booleanPreferencesKey("app_theme")
        val materialYou = booleanPreferencesKey("material_you")
        val autoRateDay = booleanPreferencesKey("auto_rate")
        val passcode = stringPreferencesKey("passcode")
        val has_passcode = booleanPreferencesKey("has_passcode")
    }

    val getPasscode: Flow<String> = context.datastore.data
        .map {
            it[passcode] ?: ""
        }

    suspend fun savePasscode(passcodeStr: String){
        context.datastore.edit {
            it[passcode] = passcodeStr
        }
    }

    val hasPasscode: Flow<Boolean> = context.datastore.data
        .map {
            it[has_passcode] ?: false
        }

    suspend fun saveHasPasscode(hasPasscode: Boolean){
        context.datastore.edit {
            it[has_passcode] = hasPasscode
        }
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