package com.nima.mymood.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity
data class Effect(
    @PrimaryKey
    val id: UUID = UUID.randomUUID(),
    @ColumnInfo
    val foreignKey: UUID,
    @ColumnInfo
    val description: String,
    @ColumnInfo
    val rate: Int
)
