package com.nima.mymood.viewmodels

import androidx.lifecycle.ViewModel
import com.nima.mymood.repository.MoodRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.distinctUntilChanged
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class DayViewModel @Inject constructor(private val repository: MoodRepository)
    :ViewModel(){

    fun getDayById(id: UUID) = repository.getDayById(id).distinctUntilChanged()
    fun getDayEffects(fk: UUID) = repository.getEffectsByFK(fk).distinctUntilChanged()
}