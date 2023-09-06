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

class HomeViewModel (
    private val repository: MoodRepository
)
    : ViewModel() {

    suspend fun getDayByDate(year: Int, month: Int, day: Int) =
        repository.getDayByDate(year, month, day)

    suspend fun addDay(day: Day) = repository.addDay(day)

    fun getDayEffect(fk: UUID) = repository.getEffectsByFK(fk).distinctUntilChanged()

    fun deleteEffect(effect: Effect) = viewModelScope.launch(Dispatchers.IO) {
        repository.deleteEffect(effect)
    }
    fun updateEffect(effect: Effect) = viewModelScope.launch(Dispatchers.IO) {
        repository.updateEffect(effect)
    }
}