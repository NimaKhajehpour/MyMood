package com.nima.mymood.viewmodels

import androidx.lifecycle.ViewModel
import com.nima.mymood.repository.MoodRepository
import kotlinx.coroutines.flow.distinctUntilChanged

class SavedDaysViewModel (private val repository: MoodRepository)
    :ViewModel(){

        fun gtDays() = repository.getAllDays().distinctUntilChanged()
}