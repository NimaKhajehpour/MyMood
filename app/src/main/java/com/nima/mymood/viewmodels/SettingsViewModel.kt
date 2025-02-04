package com.nima.mymood.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nima.mymood.model.Day
import com.nima.mymood.model.Effect
import com.nima.mymood.repository.MoodRepository
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch

class SettingsViewModel(private val repository: MoodRepository): ViewModel() {

    fun deleteAllDays() = viewModelScope.launch { repository.deleteAllDays() }
    fun deleteAllEffects() = viewModelScope.launch { repository.deleteAllEffects() }
    fun getAllEffects() = repository.getAllEffects().distinctUntilChanged()
    fun getAllDays() = repository.getAllDays().distinctUntilChanged()
    fun addDay(day: Day) = viewModelScope.launch { repository.addDay(day) }
    fun addEffect(effect: Effect) = viewModelScope.launch { repository.addEffect(effect) }
}