package com.aifitnesscoach.app.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_profile")
data class UserProfileEntity(
    @PrimaryKey val id: Int = 1,
    val name: String,
    val ageYears: Int,
    val heightCm: Float,
    val weightKg: Float,
    val goal: String,
    val location: String,
    val level: String,
    val createdAtEpochDay: Long
)
