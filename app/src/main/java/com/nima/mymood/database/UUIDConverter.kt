package com.nima.mymood.database

import androidx.room.TypeConverter
import java.util.*

class UUIDConverter {
    @TypeConverter
    fun fromUUID(uuid: UUID) = uuid.toString()

    @TypeConverter
    fun toUUID(id: String) = UUID.fromString(id)
}