package com.nima.mymood.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nima.mymood.model.Day
import com.nima.mymood.repository.MoodRepository
import kotlinx.coroutines.launch
import java.util.UUID

class DaySettingsViewModel (private val repository: MoodRepository): ViewModel() {

    fun getDay(id: UUID) = repository.getDayById(id)

    fun deleteDay(day: Day) = viewModelScope.launch{ repository.deleteDay(day )}

    fun updateDay(day: Day) = viewModelScope.launch { repository.updateDay(day) }

    fun deleteEffects(id: UUID) = viewModelScope.launch { repository.deleteDayEffects(id) }

}