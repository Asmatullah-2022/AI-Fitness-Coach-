package com.aifitnesscoach.app.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.aifitnesscoach.app.data.local.dao.UserProfileDao
import com.aifitnesscoach.app.data.local.dao.WeightEntryDao
import com.aifitnesscoach.app.data.local.dao.WorkoutSessionDao
import com.aifitnesscoach.app.data.local.entity.UserProfileEntity
import com.aifitnesscoach.app.data.local.entity.WeightEntryEntity
import com.aifitnesscoach.app.data.local.entity.WorkoutSessionEntity

@Database(
    entities = [UserProfileEntity::class, WorkoutSessionEntity::class, WeightEntryEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun userProfileDao(): UserProfileDao
    abstract fun workoutSessionDao(): WorkoutSessionDao
    abstract fun weightEntryDao(): WeightEntryDao

    companion object {
        @Volatile
        private var instance: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase =
            instance ?: synchronized(this) {
                instance ?: Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "ai_fitness_coach.db"
                ).build().also { instance = it }
            }
    }
}
