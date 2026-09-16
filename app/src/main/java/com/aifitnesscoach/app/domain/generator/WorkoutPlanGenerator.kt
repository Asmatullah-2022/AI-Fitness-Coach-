package com.aifitnesscoach.app.domain.generator

import com.aifitnesscoach.app.domain.model.DayPlan
import com.aifitnesscoach.app.domain.model.Exercise
import com.aifitnesscoach.app.domain.model.FitnessGoal
import com.aifitnesscoach.app.domain.model.FitnessLevel
import com.aifitnesscoach.app.domain.model.WeeklyPlan
import com.aifitnesscoach.app.domain.model.WorkoutLocation

private val DAY_NAMES = listOf("Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday")

/**
 * Rule-based personalized plan generator. Deterministic given the same
 * profile inputs, so the same user always gets the same weekly structure
 * (only exercise/rep prescriptions change if they update their profile).
 */
object WorkoutPlanGenerator {

    fun generate(
        goal: FitnessGoal,
        location: WorkoutLocation,
        level: FitnessLevel
    ): WeeklyPlan {
        val split = trainingSplit(location, level)
        val library = ExerciseLibrary.forLocation(location)
        val setsRepsRest = prescriptionFor(goal, level)

        val days = DAY_NAMES.mapIndexed { index, dayName ->
            val focus = split.getOrNull(index)
            if (focus == null) {
                DayPlan(
                    dayIndex = index,
                    dayName = dayName,
                    focus = "Rest & Recovery",
                    isRestDay = true,
                    exercises = emptyList(),
                    estimatedDurationMin = 0
                )
            } else {
                val pool = library[focus].orEmpty()
                val count = exerciseCountFor(level, goal)
                val chosen = pool.take(count).map { base ->
                    applyPrescription(base, goal, setsRepsRest)
                }
                DayPlan(
                    dayIndex = index,
                    dayName = dayName,
                    focus = focus,
                    isRestDay = false,
                    exercises = chosen,
                    estimatedDurationMin = estimateDuration(chosen)
                )
            }
        }

        return WeeklyPlan(days)
    }

    /** Returns 7 entries (Mon..Sun), null meaning a rest day. */
    private fun trainingSplit(location: WorkoutLocation, level: FitnessLevel): List<String?> {
        val focuses = ExerciseLibrary.forLocation(location).keys.toList()
        val daysPerWeek = when (level) {
            FitnessLevel.BEGINNER -> 3
            FitnessLevel.INTERMEDIATE -> 4
            FitnessLevel.ADVANCED -> 5
        }
        val trainingDayPositions = spreadPositions(daysPerWeek)
        val result = MutableList<String?>(7) { null }
        var focusIndex = 0
        for (pos in trainingDayPositions) {
            result[pos] = focuses[focusIndex % focuses.size]
            focusIndex++
        }
        return result
    }

    private fun spreadPositions(count: Int): Set<Int> = when (count) {
        3 -> setOf(0, 2, 4)
        4 -> setOf(0, 1, 3, 4)
        5 -> setOf(0, 1, 2, 3, 4)
        else -> setOf(0, 2, 4)
    }

    private data class Prescription(val sets: Int, val reps: String, val restSeconds: Int)

    private fun prescriptionFor(goal: FitnessGoal, level: FitnessLevel): Prescription {
        val levelBoost = when (level) {
            FitnessLevel.BEGINNER -> 0
            FitnessLevel.INTERMEDIATE -> 1
            FitnessLevel.ADVANCED -> 2
        }
        return when (goal) {
            FitnessGoal.LOSE_WEIGHT -> Prescription(sets = 3 + levelBoost / 2, reps = "15-20", restSeconds = 30)
            FitnessGoal.BUILD_MUSCLE -> Prescription(sets = 3 + levelBoost, reps = "8-12", restSeconds = 75)
            FitnessGoal.IMPROVE_ENDURANCE -> Prescription(sets = 2 + levelBoost / 2, reps = "20-30", restSeconds = 20)
            FitnessGoal.GENERAL_FITNESS -> Prescription(sets = 3, reps = "10-15", restSeconds = 45)
        }
    }

    private fun exerciseCountFor(level: FitnessLevel, goal: FitnessGoal): Int {
        val base = when (level) {
            FitnessLevel.BEGINNER -> 4
            FitnessLevel.INTERMEDIATE -> 5
            FitnessLevel.ADVANCED -> 6
        }
        return if (goal == FitnessGoal.IMPROVE_ENDURANCE) base - 1 else base
    }

    private fun applyPrescription(base: Exercise, goal: FitnessGoal, p: Prescription): Exercise {
        val isCardio = base.muscleGroup.equals("Cardio", ignoreCase = true)
        return if (isCardio) {
            base.copy(sets = 1, reps = if (goal == FitnessGoal.IMPROVE_ENDURANCE) "15-20 min" else "8-12 min", restSeconds = 60)
        } else {
            base.copy(sets = p.sets, reps = p.reps, restSeconds = p.restSeconds)
        }
    }

    private fun estimateDuration(exercises: List<Exercise>): Int {
        if (exercises.isEmpty()) return 0
        val perExercise = exercises.sumOf { (it.sets * 45 + it.sets * it.restSeconds) / 60 + 2 }
        return (perExercise + 10).coerceAtLeast(15) // + warm-up/cool-down
    }
}
