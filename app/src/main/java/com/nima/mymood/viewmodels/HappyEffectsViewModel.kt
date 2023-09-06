package com.nima.mymood.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nima.mymood.model.Effect
import com.nima.mymood.repository.MoodRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import java.util.UUID

class HappyEffectsViewModel (private val repository: MoodRepository)
    :ViewModel(){

    fun getHappyMood() = repository.getEffectByRate(listOf(0, 1)).distinctUntilChanged()

    fun deleteEffect(effect: Effect) = viewModelScope.launch(Dispatchers.IO) {
        repository.deleteEffect(effect)
    }

    fun getDayById(id: UUID) = repository.getDayById(id).distinctUntilChanged()
    fun updateEffect(effect: Effect) = viewModelScope.launch(Dispatchers.IO) {
        repository.updateEffect(effect)
    }
}