package com.nima.mymood.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nima.mymood.model.Day
import com.nima.mymood.model.Effect
import com.nima.mymood.repository.MoodRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class DayViewModel @Inject constructor(private val repository: MoodRepository)
    :ViewModel(){

    fun getDayById(id: UUID) = repository.getDayById(id).distinctUntilChanged()
    fun getDayEffects(fk: UUID) = repository.getEffectsByFK(fk).distinctUntilChanged()

    fun deleteEffect(effect: Effect) = viewModelScope.launch(Dispatchers.IO) {
        repository.deleteEffect(effect)

    }

    fun deleteDay(day: Day) =
        viewModelScope.launch {
            repository.deleteDay(day)
        }

    fun updateEffect(effect: Effect) = viewModelScope.launch(Dispatchers.IO) {
        repository.updateEffect(effect)
    }
}