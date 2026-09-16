package com.aifitnesscoach.app.domain.model

import java.time.LocalDate

data class UserProfile(
    val name: String,
    val ageYears: Int,
    val heightCm: Float,
    val weightKg: Float,
    val goal: FitnessGoal,
    val location: WorkoutLocation,
    val level: FitnessLevel,
    val createdAt: LocalDate
)

data class WorkoutHistoryEntry(
    val id: Long,
    val date: LocalDate,
    val dayFocus: String,
    val totalExercises: Int,
    val completedExercises: Int,
    val durationMinutes: Int,
    val completed: Boolean
)

data class WeightEntry(
    val id: Long,
    val date: LocalDate,
    val weightKg: Float
)
