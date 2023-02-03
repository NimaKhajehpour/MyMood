package com.nima.mymood.viewmodels

import androidx.lifecycle.ViewModel
import com.nima.mymood.model.Day
import com.nima.mymood.repository.MoodRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: MoodRepository
)
    : ViewModel() {

    suspend fun getDayByDate(year: Int, month: Int, day: Int) =
        repository.getDayByDate(year, month, day)

    suspend fun addDay(day: Day) = repository.addDay(day)
}