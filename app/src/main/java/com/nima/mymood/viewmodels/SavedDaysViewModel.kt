package com.nima.mymood.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nima.mymood.model.Day
import com.nima.mymood.repository.MoodRepository
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import java.util.UUID

class SavedDaysViewModel (private val repository: MoodRepository)
    :ViewModel(){

    fun gtDays() = repository.getAllDays().distinctUntilChanged()

    fun getDayRating(fk: UUID) = repository.getDayAVG(fk).distinctUntilChanged()

    fun saveDay(day: Day) = viewModelScope.launch {
        repository.addDay(day)
    }
}