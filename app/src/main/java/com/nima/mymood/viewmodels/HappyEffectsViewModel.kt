package com.nima.mymood.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nima.mymood.model.Effect
import com.nima.mymood.repository.MoodRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class HappyEffectsViewModel @Inject constructor(private val repository: MoodRepository)
    :ViewModel(){

    fun getHappyMood() = repository.getEffectByRate(listOf(0, 1)).distinctUntilChanged()

    fun deleteEffect(effect: Effect) = viewModelScope.launch(Dispatchers.IO) {
        repository.deleteEffect(effect)
    }

    fun getDayById(id: UUID) = repository.getDayById(id).distinctUntilChanged()
}