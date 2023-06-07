package com.nima.mymood.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nima.mymood.repository.MoodRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class DayGraphViewModel @Inject constructor(val repository: MoodRepository): ViewModel() {

    fun getDayEffects(fk: UUID) = repository.getEffectsByFK(fk).distinctUntilChanged()

    fun getDay(id: UUID) = repository.getDayById(id).distinctUntilChanged()
}