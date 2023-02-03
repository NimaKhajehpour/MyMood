package com.nima.mymood.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.nima.mymood.model.Day
import com.nima.mymood.model.Effect
import kotlinx.coroutines.flow.Flow
import java.util.*

@Dao
interface MoodDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addDay(day: Day)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addEffect(effect: Effect)

    @Query("select * from effect where foreignKey = :fk")
    fun getEffectsByFK(fk: UUID): Flow<List<Effect>>

    @Query("select * from effect where rate = :rate")
    fun getEffectByRate(rate: Int): Flow<List<Effect>>

    @Query("select * from day where year = :year and month = :month and day = :day")
    suspend fun getDayByDate(year: Int, month: Int, day: Int): Day

    @Query("select * from day where id = :id")
    suspend fun getDayById(id: UUID): Day

    @Query("select * from day where year = :year")
    fun getDayByYear(year: Int): Flow<List<Day>>

    @Query("select * from day where year = :year and month = :month")
    fun getDayByMonth(year: Int, month: Int): Flow<List<Day>>

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateDay(day: Day)

    @Delete
    suspend fun deleteDay(day: Day)

    @Query("delete from effect where foreignKey = :fk")
    suspend fun deleteDayEffects(fk: UUID)
}