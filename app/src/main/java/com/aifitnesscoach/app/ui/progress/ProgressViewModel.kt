package com.aifitnesscoach.app.ui.progress

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aifitnesscoach.app.data.repository.FitnessRepository
import com.aifitnesscoach.app.domain.model.UserProfile
import com.aifitnesscoach.app.domain.model.WeightEntry
import com.aifitnesscoach.app.domain.util.BmiCalculator
import com.aifitnesscoach.app.domain.util.StreakInfo
import com.aifitnesscoach.app.domain.util.StreakCalculator
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

data class ProgressUiState(
    val profile: UserProfile? = null,
    val weightHistory: List<WeightEntry> = emptyList(),
    val startingWeight: Float = 0f,
    val currentWeight: Float = 0f,
    val currentBmi: Float = 0f,
    val streak: StreakInfo = StreakInfo(0, 0),
    val totalWorkoutsCompleted: Int = 0,
    val isLoading: Boolean = true
)

class ProgressViewModel(private val repository: FitnessRepository) : ViewModel() {

    private val _state = MutableStateFlow(ProgressUiState())
    val state: StateFlow<ProgressUiState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            combine(
                repository.observeProfile(),
                repository.observeWeightHistory(),
                repository.observeHistory()
            ) { profile, weights, history ->
                val completedDates = history.filter { it.completed }.map { it.date }
                val streak = StreakCalculator.compute(completedDates)
                val current = weights.lastOrNull()?.weightKg ?: profile?.weightKg ?: 0f
                val starting = weights.firstOrNull()?.weightKg ?: profile?.weightKg ?: 0f
                ProgressUiState(
                    profile = profile,
                    weightHistory = weights,
                    startingWeight = starting,
                    currentWeight = current,
                    currentBmi = profile?.let { BmiCalculator.rounded(BmiCalculator.calculate(current, it.heightCm)) } ?: 0f,
                    streak = streak,
                    totalWorkoutsCompleted = completedDates.distinct().size,
                    isLoading = false
                )
            }.collect { _state.value = it }
        }
    }

    fun logWeight(weightKg: Float) {
        viewModelScope.launch {
            repository.addWeightEntry(weightKg)
        }
    }
}
