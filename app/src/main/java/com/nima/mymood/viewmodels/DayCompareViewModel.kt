package com.nima.mymood.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nima.mymood.model.Effect
import com.nima.mymood.repository.MoodRepository
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import java.util.UUID

class DayCompareViewModel (private val repository: MoodRepository): ViewModel() {

    fun getAllDays() = repository.getAllDays().distinctUntilChanged()

    fun getEffectsBuFK(fk: UUID) = repository.getEffectsByFK(fk).distinctUntilChanged()
}