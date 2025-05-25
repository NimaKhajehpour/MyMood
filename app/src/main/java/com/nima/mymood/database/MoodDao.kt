package com.nima.mymood.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.nima.mymood.model.Day
import com.nima.mymood.model.Effect
import com.nima.mymood.model.EffectWithDate
import kotlinx.coroutines.flow.Flow
import java.util.*

@Dao
interface MoodDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addDay(day: Day)

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun addEffect(effect: Effect)

    @Query("select * from effect where foreignKey = :fk order by (hour * 60 + minute) desc")
    fun getEffectsByFK(fk: UUID): Flow<List<Effect>>

    @Query("select * from day where year = :year and month = :month and day = :day")
    suspend fun getDayByDate(year: Int, month: Int, day: Int): Day

    @Query("select * from day where id = :id")
    fun getDayById(id: UUID): Flow<Day>

    @Query("select case when count(*) is not 0 then avg(rate) else 0.0 end as avg from effect where foreignKey = :fk")
    fun getDayAVG(fk: UUID): Flow<Double>

    @Query("select * from day order by (year * 365 + month * 30 + day) desc")
    fun getAllDays(): Flow<List<Day>>

    @Query("select * from effect")
    fun getAllEffects(): Flow<List<Effect>>

    @Query("select effect.id, effect.description, effect.foreignKey, effect.hour, effect.minute, effect.rate, day.day, day.month, day.year  from effect " +
            "inner join day on effect.foreignKey = day.id " +
            "order by (year * 365 + month * 30 + day) desc, (hour * 60 + minute) desc")
    fun getAllEffectsWithDate(): Flow<List<EffectWithDate>>

    @Query("select effect.id, effect.description, effect.foreignKey, effect.hour, effect.minute, effect.rate, day.day, day.month, day.year  from effect " +
            "inner join day on effect.foreignKey = day.id " +
            "where effect.rate in (:rate) " +
            "order by (year * 365 + month * 30 + day) desc, (hour * 60 + minute) desc")
    fun getAllEffectsWithDate(rate: List<Int>): Flow<List<EffectWithDate>>

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateDay(day: Day)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateEffect(effect: Effect)

    @Query("select * from effect where id = :id")
    fun getEffectById(id: UUID): Flow<Effect>

    @Delete
    suspend fun deleteDay(day: Day)

    @Delete
    suspend fun deleteEffect(effect: Effect)

    @Query("delete from effect where foreignKey = :fk")
    suspend fun deleteDayEffects(fk: UUID)

    @Query("delete from effect")
    suspend fun deleteAllEffects()

    @Query("delete from effect where effect.id = :id")
    suspend fun deleteEffect(id: UUID)

    @Query("delete from day")
    suspend fun deleteAllDays()
}