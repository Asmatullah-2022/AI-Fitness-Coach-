package com.aifitnesscoach.app.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.aifitnesscoach.app.data.local.entity.WorkoutSessionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface WorkoutSessionDao {

    @Upsert
    suspend fun upsert(session: WorkoutSessionEntity)

    @Query("SELECT * FROM workout_sessions ORDER BY epochDay DESC")
    fun observeAll(): Flow<List<WorkoutSessionEntity>>

    @Query("SELECT * FROM workout_sessions WHERE epochDay = :epochDay LIMIT 1")
    suspend fun getForDay(epochDay: Long): WorkoutSessionEntity?

    @Query("SELECT * FROM workout_sessions WHERE completed = 1")
    suspend fun getAllCompleted(): List<WorkoutSessionEntity>

    @Query("DELETE FROM workout_sessions")
    suspend fun clear()
}
