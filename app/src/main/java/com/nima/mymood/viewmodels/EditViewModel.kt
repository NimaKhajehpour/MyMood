package com.nima.mymood.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nima.mymood.model.Effect
import com.nima.mymood.repository.MoodRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class EditViewModel @Inject constructor(private val repository: MoodRepository): ViewModel() {

    fun getEffect(id: UUID) = repository.getEffectById(id).distinctUntilChanged()

    fun updateEffect(effect: Effect, onComplete: () -> Unit) = viewModelScope.launch {
        repository.updateEffect(effect)
    }.invokeOnCompletion {
        onComplete()
    }

}