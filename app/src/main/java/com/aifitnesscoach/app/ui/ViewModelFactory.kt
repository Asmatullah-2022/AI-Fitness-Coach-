package com.aifitnesscoach.app.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.aifitnesscoach.app.data.repository.FitnessRepository
import com.aifitnesscoach.app.ui.editprofile.EditProfileViewModel
import com.aifitnesscoach.app.ui.history.HistoryViewModel
import com.aifitnesscoach.app.ui.home.HomeViewModel
import com.aifitnesscoach.app.ui.onboarding.OnboardingViewModel
import com.aifitnesscoach.app.ui.progress.ProgressViewModel
import com.aifitnesscoach.app.ui.workout.WorkoutViewModel

class ViewModelFactory(private val repository: FitnessRepository) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T = when (modelClass) {
        OnboardingViewModel::class.java -> OnboardingViewModel(repository) as T
        HomeViewModel::class.java -> HomeViewModel(repository) as T
        WorkoutViewModel::class.java -> WorkoutViewModel(repository) as T
        HistoryViewModel::class.java -> HistoryViewModel(repository) as T
        ProgressViewModel::class.java -> ProgressViewModel(repository) as T
        EditProfileViewModel::class.java -> EditProfileViewModel(repository) as T
        else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}
