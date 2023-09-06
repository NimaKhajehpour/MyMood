package com.nima.mymood.viewmodels

import androidx.lifecycle.ViewModel
import com.nima.mymood.repository.MoodRepository
import kotlinx.coroutines.flow.distinctUntilChanged
import java.util.UUID

class DayGraphViewModel (private val repository: MoodRepository): ViewModel() {

    fun getDayEffects(fk: UUID) = repository.getEffectsByFK(fk).distinctUntilChanged()

    fun getDay(id: UUID) = repository.getDayById(id).distinctUntilChanged()
}