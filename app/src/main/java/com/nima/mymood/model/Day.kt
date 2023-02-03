package com.nima.mymood.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity
data class Day(
    @PrimaryKey
    val id: UUID = UUID.randomUUID(),
    @ColumnInfo
    var day: Int,
    @ColumnInfo
    var month: Int,
    @ColumnInfo
    var year: Int,
)
