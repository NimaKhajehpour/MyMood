package com.nima.mymood.viewmodels

import androidx.lifecycle.ViewModel
import com.nima.mymood.repository.MoodRepository
import kotlinx.coroutines.flow.distinctUntilChanged
import java.util.UUID

class SavedDaysViewModel (private val repository: MoodRepository)
    :ViewModel(){

    fun gtDays() = repository.getAllDays().distinctUntilChanged()

    fun getDayRating(fk: UUID) = repository.getDayAVG(fk).distinctUntilChanged()
}