package com.aifitnesscoach.app.domain.generator

import com.aifitnesscoach.app.domain.model.FitnessGoal
import com.aifitnesscoach.app.domain.model.FitnessLevel
import com.aifitnesscoach.app.domain.model.WorkoutLocation
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class WorkoutPlanGeneratorTest {

    @Test
    fun `generate always returns exactly seven days`() {
        val plan = WorkoutPlanGenerator.generate(FitnessGoal.GENERAL_FITNESS, WorkoutLocation.HOME, FitnessLevel.BEGINNER)
        assertEquals(7, plan.days.size)
    }

    @Test
    fun `training day count matches fitness level`() {
        val beginner = WorkoutPlanGenerator.generate(FitnessGoal.GENERAL_FITNESS, WorkoutLocation.HOME, FitnessLevel.BEGINNER)
        val intermediate = WorkoutPlanGenerator.generate(FitnessGoal.GENERAL_FITNESS, WorkoutLocation.HOME, FitnessLevel.INTERMEDIATE)
        val advanced = WorkoutPlanGenerator.generate(FitnessGoal.GENERAL_FITNESS, WorkoutLocation.HOME, FitnessLevel.ADVANCED)

        assertEquals(3, beginner.days.count { !it.isRestDay })
        assertEquals(4, intermediate.days.count { !it.isRestDay })
        assertEquals(5, advanced.days.count { !it.isRestDay })
    }

    @Test
    fun `rest days have no exercises and zero duration`() {
        val plan = WorkoutPlanGenerator.generate(FitnessGoal.GENERAL_FITNESS, WorkoutLocation.HOME, FitnessLevel.BEGINNER)
        plan.days.filter { it.isRestDay }.forEach { day ->
            assertTrue(day.exercises.isEmpty())
            assertEquals(0, day.estimatedDurationMin)
        }
    }

    @Test
    fun `training days have exercises and positive estimated duration`() {
        val plan = WorkoutPlanGenerator.generate(FitnessGoal.BUILD_MUSCLE, WorkoutLocation.GYM, FitnessLevel.INTERMEDIATE)
        plan.days.filterNot { it.isRestDay }.forEach { day ->
            assertTrue("expected exercises for ${day.dayName}", day.exercises.isNotEmpty())
            assertTrue("expected positive duration for ${day.dayName}", day.estimatedDurationMin > 0)
        }
    }

    @Test
    fun `cardio exercises always get a single set regardless of goal`() {
        val plan = WorkoutPlanGenerator.generate(FitnessGoal.BUILD_MUSCLE, WorkoutLocation.HOME, FitnessLevel.ADVANCED)
        plan.days.flatMap { it.exercises }
            .filter { it.muscleGroup.equals("Cardio", ignoreCase = true) }
            .forEach { exercise -> assertEquals(1, exercise.sets) }
    }

    @Test
    fun `generate is deterministic for the same profile`() {
        val first = WorkoutPlanGenerator.generate(FitnessGoal.LOSE_WEIGHT, WorkoutLocation.OUTDOOR, FitnessLevel.BEGINNER)
        val second = WorkoutPlanGenerator.generate(FitnessGoal.LOSE_WEIGHT, WorkoutLocation.OUTDOOR, FitnessLevel.BEGINNER)
        assertEquals(first, second)
    }

    @Test
    fun `every location produces a usable plan`() {
        WorkoutLocation.entries.forEach { location ->
            val plan = WorkoutPlanGenerator.generate(FitnessGoal.GENERAL_FITNESS, location, FitnessLevel.INTERMEDIATE)
            assertEquals(7, plan.days.size)
            assertTrue(plan.days.any { !it.isRestDay })
        }
    }
}
