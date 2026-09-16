package com.aifitnesscoach.app

import android.app.Application
import com.aifitnesscoach.app.data.local.AppDatabase
import com.aifitnesscoach.app.data.repository.FitnessRepository

class AiFitnessApp : Application() {

    lateinit var repository: FitnessRepository
        private set

    override fun onCreate() {
        super.onCreate()
        val database = AppDatabase.getInstance(this)
        repository = FitnessRepository(
            profileDao = database.userProfileDao(),
            sessionDao = database.workoutSessionDao(),
            weightDao = database.weightEntryDao()
        )
    }
}
