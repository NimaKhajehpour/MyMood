package com.nima.mymood.viewmodels

import androidx.lifecycle.ViewModel
import com.nima.mymood.model.Day
import com.nima.mymood.model.Effect
import com.nima.mymood.repository.MoodRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.distinctUntilChanged
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class TodayMoodViewModel @Inject constructor(private val repository: MoodRepository)
    : ViewModel() {

    suspend fun addEffect(effect: Effect) = repository.addEffect(effect)

    suspend fun updateDay(day: Day) = repository.updateDay(day)

    fun getDayById(id: UUID) = repository.getDayById(id).distinctUntilChanged()

    fun getDayEffects(fk: UUID) = repository.getEffectsByFK(fk).distinctUntilChanged()

    suspend fun deleteDay(day: Day) = repository.deleteDay(day)

    suspend fun deleteDayEffects(fk: UUID) = repository.deleteDayEffects(fk)
}