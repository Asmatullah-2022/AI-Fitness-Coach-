package com.aifitnesscoach.app.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "workout_sessions")
data class WorkoutSessionEntity(
    @PrimaryKey val epochDay: Long,
    val dayFocus: String,
    val totalExercises: Int,
    val completedExercises: Int,
    val durationMinutes: Int,
    val completed: Boolean
)
