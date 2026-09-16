package com.aifitnesscoach.app.domain.model

enum class FitnessGoal(val label: String) {
    LOSE_WEIGHT("Lose Weight"),
    BUILD_MUSCLE("Build Muscle"),
    IMPROVE_ENDURANCE("Improve Endurance"),
    GENERAL_FITNESS("General Fitness")
}

enum class WorkoutLocation(val label: String) {
    HOME("Home"),
    GYM("Gym"),
    OUTDOOR("Outdoor")
}

enum class FitnessLevel(val label: String) {
    BEGINNER("Beginner"),
    INTERMEDIATE("Intermediate"),
    ADVANCED("Advanced")
}

enum class Gender(val label: String) {
    MALE("Male"),
    FEMALE("Female"),
    OTHER("Other")
}
