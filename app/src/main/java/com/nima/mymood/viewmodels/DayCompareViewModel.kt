package com.nima.mymood.viewmodels

import androidx.lifecycle.ViewModel
import com.nima.mymood.repository.MoodRepository
import kotlinx.coroutines.flow.distinctUntilChanged
import java.util.UUID

class DayCompareViewModel (private val repository: MoodRepository): ViewModel() {

    fun getAllDays() = repository.getAllDays().distinctUntilChanged()

    fun getEffectsBuFK(fk: UUID) = repository.getEffectsByFK(fk).distinctUntilChanged()
}