package com.nima.mymood.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nima.mymood.model.Day
import com.nima.mymood.model.Effect
import com.nima.mymood.repository.MoodRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import java.util.UUID

class TodayMoodViewModel (private val repository: MoodRepository)
    : ViewModel() {

    suspend fun addEffect(effect: Effect) = repository.addEffect(effect)

    fun updateDay(day: Day) = viewModelScope.launch(Dispatchers.IO) {
        repository.updateDay(day)
    }

    fun updateEffect(effect: Effect) = viewModelScope.launch(Dispatchers.IO) {
        repository.updateEffect(effect)
    }

    fun getDayById(id: UUID) = repository.getDayById(id).distinctUntilChanged()

    fun deleteEffect(effect: Effect) = viewModelScope.launch(Dispatchers.IO) {
        repository.deleteEffect(effect)

    }
}