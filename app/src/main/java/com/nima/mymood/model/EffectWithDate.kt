package com.nima.mymood.model

import java.util.UUID

data class EffectWithDate(
    val id: UUID,
    val description: String,
    val foreignKey: UUID,
    val hour: String,
    val minute: String,
    val rate: Int,
    val day: Int,
    val month: Int,
    val year: Int
)
