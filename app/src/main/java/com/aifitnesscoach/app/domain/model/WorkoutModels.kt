package com.aifitnesscoach.app.domain.model

data class Exercise(
    val name: String,
    val sets: Int,
    val reps: String,
    val restSeconds: Int,
    val muscleGroup: String,
    val equipment: String
)

data class DayPlan(
    val dayIndex: Int,
    val dayName: String,
    val focus: String,
    val isRestDay: Boolean,
    val exercises: List<Exercise>,
    val estimatedDurationMin: Int
)

data class WeeklyPlan(
    val days: List<DayPlan>
)
