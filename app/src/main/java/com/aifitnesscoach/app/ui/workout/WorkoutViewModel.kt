package com.aifitnesscoach.app.ui.workout

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aifitnesscoach.app.data.repository.FitnessRepository
import com.aifitnesscoach.app.domain.generator.WorkoutPlanGenerator
import com.aifitnesscoach.app.domain.model.DayPlan
import com.aifitnesscoach.app.domain.model.Exercise
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import java.time.LocalDate

data class WorkoutUiState(
    val dayPlan: DayPlan? = null,
    val completed: Set<Int> = emptySet(),
    val isFinished: Boolean = false,
    val isLoading: Boolean = true
) {
    val progress: Float
        get() {
            val total = dayPlan?.exercises?.size ?: 0
            return if (total == 0) 0f else completed.size.toFloat() / total
        }
}

class WorkoutViewModel(private val repository: FitnessRepository) : ViewModel() {

    private val _state = MutableStateFlow(WorkoutUiState())
    val state: StateFlow<WorkoutUiState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            val profile = repository.getProfile() ?: return@launch
            val weekly = WorkoutPlanGenerator.generate(profile.goal, profile.location, profile.level)
            val todayIndex = LocalDate.now().dayOfWeek.let { it.value - DayOfWeek.MONDAY.value }
            val plan = weekly.days.getOrNull(todayIndex)
            val existing = repository.getSessionForToday()
            _state.update {
                it.copy(
                    dayPlan = plan,
                    // Only the completed *count* is persisted, so a resumed session
                    // assumes the first N exercises were the ones done; it can't
                    // recover exactly which ones if they weren't checked off in order.
                    completed = if (existing != null && plan != null) {
                        (0 until existing.completedExercises).toSet()
                    } else emptySet(),
                    isLoading = false
                )
            }
        }
    }

    fun toggleExercise(index: Int) {
        _state.update { current ->
            val newCompleted = if (current.completed.contains(index)) {
                current.completed - index
            } else {
                current.completed + index
            }
            current.copy(completed = newCompleted)
        }
        persistProgress()
    }

    fun finishWorkout() {
        persistProgress()
        _state.update { it.copy(isFinished = true) }
    }

    private fun persistProgress() {
        val s = _state.value
        val plan = s.dayPlan ?: return
        viewModelScope.launch {
            repository.recordSession(
                date = LocalDate.now(),
                dayFocus = plan.focus,
                totalExercises = plan.exercises.size,
                completedExercises = s.completed.size,
                durationMinutes = plan.estimatedDurationMin
            )
        }
    }
}

fun Exercise.summary(): String = "$sets x $reps · rest ${restSeconds}s"
