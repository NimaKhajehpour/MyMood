package com.nima.mymood.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nima.mymood.model.Day
import com.nima.mymood.model.Effect
import com.nima.mymood.repository.MoodRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class MenuViewModel @Inject constructor(private val repository: MoodRepository)
    :ViewModel(){

    fun getAllDays() = repository.getAllDays().distinctUntilChanged()

    fun getAllEffects() = repository.getAllEffects().distinctUntilChanged()

    fun addDay(day: Day) = viewModelScope.launch {
        repository.addDay(day)
    }

    fun addEffect(effect: Effect) = viewModelScope.launch {
        repository.addEffect(effect)
    }

    fun deleteAllEffects() = viewModelScope.launch {
        repository.deleteAllEffects()
    }

    fun deleteAllDays() = viewModelScope.launch {
        repository.deleteAllDays()
    }
}