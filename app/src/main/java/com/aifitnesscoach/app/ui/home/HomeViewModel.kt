package com.aifitnesscoach.app.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aifitnesscoach.app.data.repository.FitnessRepository
import com.aifitnesscoach.app.domain.generator.WorkoutPlanGenerator
import com.aifitnesscoach.app.domain.model.DayPlan
import com.aifitnesscoach.app.domain.model.UserProfile
import com.aifitnesscoach.app.domain.util.BmiCalculator
import com.aifitnesscoach.app.domain.util.BmiCategory
import com.aifitnesscoach.app.domain.util.StreakCalculator
import com.aifitnesscoach.app.domain.util.StreakInfo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import java.time.LocalDate

data class HomeUiState(
    val profile: UserProfile? = null,
    val todayPlan: DayPlan? = null,
    val bmi: Float = 0f,
    val bmiCategory: BmiCategory = BmiCategory.NORMAL,
    val streak: StreakInfo = StreakInfo(0, 0),
    val todayCompletedExercises: Int = 0,
    val isLoading: Boolean = true
)

class HomeViewModel(private val repository: FitnessRepository) : ViewModel() {

    private val _state = MutableStateFlow(HomeUiState())
    val state: StateFlow<HomeUiState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            combine(
                repository.observeProfile(),
                repository.observeHistory()
            ) { profile, history ->
                if (profile == null) {
                    HomeUiState(profile = null, isLoading = false)
                } else {
                    val weekly = WorkoutPlanGenerator.generate(profile.goal, profile.location, profile.level)
                    val todayIndex = LocalDate.now().dayOfWeek.let { it.value - DayOfWeek.MONDAY.value }
                    val todayPlan = weekly.days.getOrNull(todayIndex)
                    val bmi = BmiCalculator.calculate(profile.weightKg, profile.heightCm)
                    val completedDates = history.filter { it.completed }.map { it.date }
                    val streak = StreakCalculator.compute(completedDates)
                    val todaySession = history.firstOrNull { it.date == LocalDate.now() }
                    HomeUiState(
                        profile = profile,
                        todayPlan = todayPlan,
                        bmi = BmiCalculator.rounded(bmi),
                        bmiCategory = BmiCalculator.categoryFor(bmi),
                        streak = streak,
                        todayCompletedExercises = todaySession?.completedExercises ?: 0,
                        isLoading = false
                    )
                }
            }.collect { _state.value = it }
        }
    }
}
