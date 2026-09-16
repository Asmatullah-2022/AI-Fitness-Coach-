# AI Fitness Coach (Android)

A native Android app, built with Kotlin and Jetpack Compose, that generates a
personalized workout plan from a short profile (goal, workout location,
fitness level, age, height, weight) and tracks progress over time.

## Features

- **Onboarding**: collects goal (lose weight / build muscle / improve
  endurance / general fitness), workout location (home / gym / outdoor),
  fitness level (beginner / intermediate / advanced), age, height, and weight.
- **AI-style personalized plan generator**: a deterministic, rule-based
  engine (`domain/generator/WorkoutPlanGenerator.kt`) builds a 7-day training
  split — number of training days, exercise selection, sets/reps/rest — from
  the profile. It's structured so a real ML-ranked exercise source could
  later be swapped in behind the same API.
- **Home dashboard**: today's workout, current BMI + category, and streak.
- **Workout session**: check off exercises as you complete them; progress is
  saved automatically.
- **Workout history**: every session (date, focus, exercises completed,
  duration) is logged and browsable.
- **Progress tracking**: weight log with a trend chart, BMI over time, total
  workouts completed, and current/longest streak.
- **Edit profile**: update your stats or goals at any time; the plan
  regenerates automatically.

All data is stored locally on-device with Room (SQLite) — no backend or
network access required.

## Architecture

- **UI**: Jetpack Compose, Material 3, single-activity + Navigation Compose.
- **State management**: MVVM — one `ViewModel` per screen, exposing a
  `StateFlow<UiState>`, backed by a manual `ViewModelFactory` (no DI
  framework, to keep the project easy to open and read).
- **Data**: Room database (`UserProfile`, `WorkoutSession`, `WeightEntry`
  entities) behind a single `FitnessRepository`.
- **Domain**: pure Kotlin, framework-free — `BmiCalculator`,
  `StreakCalculator`, `WorkoutPlanGenerator`, `ExerciseLibrary`.

```
app/src/main/java/com/aifitnesscoach/app/
├── data/            # Room entities, DAOs, database, repository
├── domain/          # models, BMI/streak calculators, plan generator
└── ui/              # one package per screen + navigation + theme
```

## Requirements

- Android Studio (Ladybug/2024.2+ or newer)
- JDK 17
- Android SDK: compileSdk 35, minSdk 26 (Android 8.0+)

## Build & run

```bash
./gradlew assembleDebug      # build a debug APK
./gradlew installDebug       # install on a connected device/emulator
```

Or open the project root in Android Studio and press Run.

## Notes

- The workout plan is regenerated deterministically from the current
  profile each time it's needed, so it always reflects the latest goal,
  location, and level without needing separate "regenerate" logic.
- Streaks count consecutive calendar days containing at least one fully
  completed workout, anchored at today (or yesterday, so the streak isn't
  lost before today's workout is done).
