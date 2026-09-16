package com.aifitnesscoach.app.domain.generator

import com.aifitnesscoach.app.domain.model.Exercise
import com.aifitnesscoach.app.domain.model.WorkoutLocation

/**
 * A small curated exercise database, grouped by location and muscle focus.
 * This is a rule-based stand-in for a real recommendation model: it is
 * deterministic and offline, but structured so a future ML-ranked source
 * could be swapped in behind the same [WorkoutPlanGenerator] API.
 */
object ExerciseLibrary {

    private fun ex(name: String, muscle: String, equipment: String) = Exercise(
        name = name, sets = 0, reps = "", restSeconds = 0, muscleGroup = muscle, equipment = equipment
    )

    val homeExercises = mapOf(
        "Full Body" to listOf(
            ex("Bodyweight Squats", "Legs", "None"),
            ex("Push-ups", "Chest", "None"),
            ex("Glute Bridges", "Glutes", "None"),
            ex("Plank", "Core", "None"),
            ex("Mountain Climbers", "Core", "None"),
            ex("Lunges", "Legs", "None"),
            ex("Superman Hold", "Back", "None"),
            ex("Bicycle Crunches", "Core", "None")
        ),
        "Upper Body" to listOf(
            ex("Push-ups", "Chest", "None"),
            ex("Pike Push-ups", "Shoulders", "None"),
            ex("Tricep Dips (chair)", "Triceps", "Chair"),
            ex("Diamond Push-ups", "Triceps", "None"),
            ex("Superman Hold", "Back", "None"),
            ex("Plank Shoulder Taps", "Shoulders", "None")
        ),
        "Lower Body" to listOf(
            ex("Bodyweight Squats", "Legs", "None"),
            ex("Lunges", "Legs", "None"),
            ex("Glute Bridges", "Glutes", "None"),
            ex("Calf Raises", "Calves", "None"),
            ex("Wall Sit", "Legs", "None"),
            ex("Step-ups (chair)", "Legs", "Chair")
        ),
        "Core" to listOf(
            ex("Plank", "Core", "None"),
            ex("Bicycle Crunches", "Core", "None"),
            ex("Leg Raises", "Core", "None"),
            ex("Russian Twists", "Core", "None"),
            ex("Mountain Climbers", "Core", "None"),
            ex("Side Plank", "Core", "None")
        ),
        "Cardio" to listOf(
            ex("Jumping Jacks", "Cardio", "None"),
            ex("High Knees", "Cardio", "None"),
            ex("Burpees", "Cardio", "None"),
            ex("Mountain Climbers", "Cardio", "None"),
            ex("Jump Squats", "Cardio", "None")
        )
    )

    val gymExercises = mapOf(
        "Push (Chest/Shoulders/Triceps)" to listOf(
            ex("Barbell Bench Press", "Chest", "Barbell"),
            ex("Incline Dumbbell Press", "Chest", "Dumbbell"),
            ex("Overhead Shoulder Press", "Shoulders", "Dumbbell"),
            ex("Cable Tricep Pushdown", "Triceps", "Cable Machine"),
            ex("Lateral Raises", "Shoulders", "Dumbbell"),
            ex("Chest Fly", "Chest", "Cable Machine")
        ),
        "Pull (Back/Biceps)" to listOf(
            ex("Lat Pulldown", "Back", "Cable Machine"),
            ex("Seated Cable Row", "Back", "Cable Machine"),
            ex("Barbell Deadlift", "Back", "Barbell"),
            ex("Barbell Curl", "Biceps", "Barbell"),
            ex("Face Pulls", "Rear Delts", "Cable Machine"),
            ex("Dumbbell Hammer Curl", "Biceps", "Dumbbell")
        ),
        "Legs" to listOf(
            ex("Barbell Back Squat", "Legs", "Barbell"),
            ex("Leg Press", "Legs", "Machine"),
            ex("Romanian Deadlift", "Hamstrings", "Barbell"),
            ex("Leg Extension", "Quads", "Machine"),
            ex("Leg Curl", "Hamstrings", "Machine"),
            ex("Standing Calf Raise", "Calves", "Machine")
        ),
        "Full Body" to listOf(
            ex("Barbell Deadlift", "Full Body", "Barbell"),
            ex("Barbell Back Squat", "Legs", "Barbell"),
            ex("Barbell Bench Press", "Chest", "Barbell"),
            ex("Pull-ups", "Back", "Pull-up Bar"),
            ex("Dumbbell Shoulder Press", "Shoulders", "Dumbbell"),
            ex("Plank", "Core", "None")
        ),
        "Cardio" to listOf(
            ex("Treadmill Run", "Cardio", "Treadmill"),
            ex("Rowing Machine", "Cardio", "Rowing Machine"),
            ex("Stationary Bike", "Cardio", "Bike"),
            ex("Stair Climber", "Cardio", "Machine")
        )
    )

    val outdoorExercises = mapOf(
        "Run/Walk" to listOf(
            ex("Brisk Walk Warm-up", "Cardio", "None"),
            ex("Steady State Jog", "Cardio", "None"),
            ex("Interval Sprints", "Cardio", "None"),
            ex("Cool-down Walk", "Cardio", "None")
        ),
        "Bodyweight Circuit" to listOf(
            ex("Bodyweight Squats", "Legs", "None"),
            ex("Push-ups", "Chest", "None"),
            ex("Walking Lunges", "Legs", "None"),
            ex("Park Bench Step-ups", "Legs", "Bench"),
            ex("Incline Push-ups (bench)", "Chest", "Bench"),
            ex("Plank", "Core", "None")
        ),
        "Hill/Stairs" to listOf(
            ex("Hill/Stair Sprints", "Cardio", "None"),
            ex("Stair Step-ups", "Legs", "None"),
            ex("Bodyweight Squats", "Legs", "None"),
            ex("Jumping Jacks", "Cardio", "None")
        ),
        "Core & Mobility" to listOf(
            ex("Plank", "Core", "None"),
            ex("Bicycle Crunches", "Core", "None"),
            ex("Bird Dog", "Core", "None"),
            ex("Standing Side Bends", "Core", "None"),
            ex("Dynamic Stretching", "Mobility", "None")
        )
    )

    fun forLocation(location: WorkoutLocation): Map<String, List<Exercise>> = when (location) {
        WorkoutLocation.HOME -> homeExercises
        WorkoutLocation.GYM -> gymExercises
        WorkoutLocation.OUTDOOR -> outdoorExercises
    }
}
