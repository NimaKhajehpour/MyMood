package com.nima.mymood.viewmodels

import androidx.lifecycle.ViewModel
import com.nima.mymood.repository.MoodRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.distinctUntilChanged
import javax.inject.Inject

@HiltViewModel
class SadEffectsViewModel @Inject constructor(private val repository: MoodRepository)
    :ViewModel(){

    fun getSadMood() = repository.getEffectByRate(listOf(3, 4)).distinctUntilChanged()

}