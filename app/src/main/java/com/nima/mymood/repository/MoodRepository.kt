package com.nima.mymood.repository

import com.nima.mymood.database.MoodDao
import com.nima.mymood.model.Day
import com.nima.mymood.model.Effect
import com.nima.mymood.model.EffectWithDate
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.flowOn
import java.util.UUID

class MoodRepository (private val dao: MoodDao) {

    suspend fun addDay(day: Day) =
        dao.addDay(day)

    suspend fun addEffect(effect: Effect) =
        dao.addEffect(effect)

    fun getEffectsByFK(fk: UUID): Flow<List<Effect>> =
        dao.getEffectsByFK(fk).flowOn(Dispatchers.IO).conflate()

    suspend fun getDayByDate(year: Int, month: Int, day: Int): Day =
        dao.getDayByDate(year, month, day)

    fun getDayById(id: UUID): Flow<Day> =
        dao.getDayById(id)

    fun getAllDays(): Flow<List<Day>> =
        dao.getAllDays()

    fun getAllEffects(): Flow<List<Effect>> =
        dao.getAllEffects()

    fun getAllEffectsWithDate(): Flow<List<EffectWithDate>> =
        dao.getAllEffectsWithDate()

    fun getAllEffectsWithDate(rate: List<Int>): Flow<List<EffectWithDate>> =
        dao.getAllEffectsWithDate(rate = rate)

    suspend fun updateDay(day: Day) =
        dao.updateDay(day)

    suspend fun updateEffect(effect: Effect) =
        dao.updateEffect(effect)

    suspend fun deleteDay(day: Day) =
        dao.deleteDay(day)

    suspend fun deleteDayEffects(fk: UUID) =
        dao.deleteDayEffects(fk)

    suspend fun deleteAllEffects() =
        dao.deleteAllEffects()

    suspend fun deleteAllDays() =
        dao.deleteAllDays()

    suspend fun deleteEffect(effect: Effect) =
        dao.deleteEffect(effect)

    suspend fun deleteEffect(id: UUID) =
        dao.deleteEffect(id = id)

    fun getEffectById(id: UUID): Flow<Effect> =
        dao.getEffectById(id).flowOn(Dispatchers.IO).conflate()

    fun getDayAVG(fk: UUID): Flow<Double> =
        dao.getDayAVG(fk).flowOn(Dispatchers.IO).conflate()
}