package com.nima.mymood.viewmodels

import androidx.lifecycle.ViewModel
import com.nima.mymood.repository.MoodRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.distinctUntilChanged
import javax.inject.Inject

@HiltViewModel
class HappyEffectsViewModel @Inject constructor(private val repository: MoodRepository)
    :ViewModel(){

    fun getHappyMood() = repository.getEffectByRate(listOf(0, 1)).distinctUntilChanged()
}