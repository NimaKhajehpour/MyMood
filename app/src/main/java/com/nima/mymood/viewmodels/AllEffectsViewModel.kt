package com.nima.mymood.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nima.mymood.repository.MoodRepository
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import java.util.UUID

class AllEffectsViewModel(private val repository: MoodRepository): ViewModel() {

    fun getAllEffects() = repository.getAllEffectsWithDate().distinctUntilChanged()
    fun getAllEffects(rate: List<Int>) = repository.getAllEffectsWithDate(rate = rate).distinctUntilChanged()
    fun deleteEffect(id: UUID) = viewModelScope.launch {
        repository.deleteEffect(id)
    }
}