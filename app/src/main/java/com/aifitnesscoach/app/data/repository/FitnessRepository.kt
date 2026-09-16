package com.aifitnesscoach.app.data.repository

import com.aifitnesscoach.app.data.local.dao.UserProfileDao
import com.aifitnesscoach.app.data.local.dao.WeightEntryDao
import com.aifitnesscoach.app.data.local.dao.WorkoutSessionDao
import com.aifitnesscoach.app.data.local.entity.UserProfileEntity
import com.aifitnesscoach.app.data.local.entity.WeightEntryEntity
import com.aifitnesscoach.app.data.local.entity.WorkoutSessionEntity
import com.aifitnesscoach.app.domain.model.FitnessGoal
import com.aifitnesscoach.app.domain.model.FitnessLevel
import com.aifitnesscoach.app.domain.model.UserProfile
import com.aifitnesscoach.app.domain.model.WeightEntry
import com.aifitnesscoach.app.domain.model.WorkoutHistoryEntry
import com.aifitnesscoach.app.domain.model.WorkoutLocation
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDate

class FitnessRepository(
    private val profileDao: UserProfileDao,
    private val sessionDao: WorkoutSessionDao,
    private val weightDao: WeightEntryDao
) {

    fun observeProfile(): Flow<UserProfile?> = profileDao.observeProfile().map { it?.toDomain() }

    suspend fun getProfile(): UserProfile? = profileDao.getProfile()?.toDomain()

    suspend fun saveProfile(profile: UserProfile) {
        profileDao.upsert(profile.toEntity())
        weightDao.insert(WeightEntryEntity(epochDay = LocalDate.now().toEpochDay(), weightKg = profile.weightKg))
    }

    fun observeHistory(): Flow<List<WorkoutHistoryEntry>> =
        sessionDao.observeAll().map { list -> list.map { it.toDomain() } }

    suspend fun getSessionForToday(): WorkoutHistoryEntry? =
        sessionDao.getForDay(LocalDate.now().toEpochDay())?.toDomain()

    suspend fun recordSession(
        date: LocalDate,
        dayFocus: String,
        totalExercises: Int,
        completedExercises: Int,
        durationMinutes: Int
    ) {
        val existing = sessionDao.getForDay(date.toEpochDay())
        val entity = WorkoutSessionEntity(
            id = existing?.id ?: 0,
            epochDay = date.toEpochDay(),
            dayFocus = dayFocus,
            totalExercises = totalExercises,
            completedExercises = completedExercises,
            durationMinutes = durationMinutes,
            completed = completedExercises >= totalExercises && totalExercises > 0
        )
        sessionDao.upsert(entity)
    }

    suspend fun getCompletedWorkoutDates(): List<LocalDate> =
        sessionDao.getAllCompleted().map { LocalDate.ofEpochDay(it.epochDay) }

    fun observeWeightHistory(): Flow<List<WeightEntry>> =
        weightDao.observeAll().map { list -> list.map { it.toDomain() } }

    suspend fun addWeightEntry(weightKg: Float, date: LocalDate = LocalDate.now()) {
        weightDao.insert(WeightEntryEntity(epochDay = date.toEpochDay(), weightKg = weightKg))
    }
}

private fun UserProfileEntity.toDomain() = UserProfile(
    name = name,
    ageYears = ageYears,
    heightCm = heightCm,
    weightKg = weightKg,
    goal = FitnessGoal.valueOf(goal),
    location = WorkoutLocation.valueOf(location),
    level = FitnessLevel.valueOf(level),
    createdAt = LocalDate.ofEpochDay(createdAtEpochDay)
)

private fun UserProfile.toEntity() = UserProfileEntity(
    name = name,
    ageYears = ageYears,
    heightCm = heightCm,
    weightKg = weightKg,
    goal = goal.name,
    location = location.name,
    level = level.name,
    createdAtEpochDay = createdAt.toEpochDay()
)

private fun WorkoutSessionEntity.toDomain() = WorkoutHistoryEntry(
    id = id,
    date = LocalDate.ofEpochDay(epochDay),
    dayFocus = dayFocus,
    totalExercises = totalExercises,
    completedExercises = completedExercises,
    durationMinutes = durationMinutes,
    completed = completed
)

private fun WeightEntryEntity.toDomain() = WeightEntry(
    id = id,
    date = LocalDate.ofEpochDay(epochDay),
    weightKg = weightKg
)
