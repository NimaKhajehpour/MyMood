package com.nima.mymood.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nima.mymood.model.Effect
import com.nima.mymood.repository.MoodRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import java.util.UUID

class SadEffectsViewModel (private val repository: MoodRepository)
    :ViewModel(){

    fun getSadMood() = repository.getEffectByRate(listOf(3, 4)).distinctUntilChanged()

    fun deleteEffect(effect: Effect) = viewModelScope.launch(Dispatchers.IO) {
        repository.deleteEffect(effect)

    }

    fun getDayById(id: UUID) = repository.getDayById(id).distinctUntilChanged()

    fun updateEffect(effect: Effect) = viewModelScope.launch(Dispatchers.IO) {
        repository.updateEffect(effect)
    }

}