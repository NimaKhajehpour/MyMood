package com.nima.mymood.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nima.mymood.model.Day
import com.nima.mymood.model.Effect
import com.nima.mymood.repository.MoodRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch

class MenuViewModel (private val repository: MoodRepository)
    :ViewModel(){

    fun getAllDays() = repository.getAllDays().distinctUntilChanged()

    fun getAllEffects() = repository.getAllEffects().distinctUntilChanged()

    fun addDay(day: Day) = viewModelScope.launch {
        repository.addDay(day)
    }

    fun updateEffect(effect: Effect) = viewModelScope.launch(Dispatchers.IO) {
        repository.updateEffect(effect)
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