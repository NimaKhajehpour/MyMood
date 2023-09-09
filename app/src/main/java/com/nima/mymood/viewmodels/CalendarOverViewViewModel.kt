package com.nima.mymood.viewmodels

import androidx.lifecycle.ViewModel
import com.nima.mymood.model.Day
import com.nima.mymood.repository.MoodRepository
import kotlinx.coroutines.flow.MutableStateFlow
import java.util.UUID

class CalendarOverViewViewModel (private val repository: MoodRepository): ViewModel() {
    suspend fun getDay(year: Int, day: Int, month: Int) = repository.getDayByDate(year = year, month = month, day = day)

    fun getEffects(fk: UUID) = repository.getEffectsByFK(fk)
}