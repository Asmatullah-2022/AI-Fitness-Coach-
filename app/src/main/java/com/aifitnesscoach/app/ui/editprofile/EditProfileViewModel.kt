package com.aifitnesscoach.app.ui.editprofile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aifitnesscoach.app.data.repository.FitnessRepository
import com.aifitnesscoach.app.domain.model.FitnessGoal
import com.aifitnesscoach.app.domain.model.FitnessLevel
import com.aifitnesscoach.app.domain.model.UserProfile
import com.aifitnesscoach.app.domain.model.WorkoutLocation
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate

data class EditProfileState(
    val name: String = "",
    val age: String = "",
    val heightCm: String = "",
    val weightKg: String = "",
    val goal: FitnessGoal = FitnessGoal.GENERAL_FITNESS,
    val location: WorkoutLocation = WorkoutLocation.HOME,
    val level: FitnessLevel = FitnessLevel.BEGINNER,
    val createdAt: LocalDate = LocalDate.now(),
    val isLoaded: Boolean = false,
    val isSaved: Boolean = false
) {
    val isValid: Boolean
        get() = name.isNotBlank() &&
            age.toIntOrNull()?.let { it in 10..100 } == true &&
            heightCm.toFloatOrNull()?.let { it in 80f..250f } == true &&
            weightKg.toFloatOrNull()?.let { it in 25f..300f } == true
}

class EditProfileViewModel(private val repository: FitnessRepository) : ViewModel() {

    private val _state = MutableStateFlow(EditProfileState())
    val state: StateFlow<EditProfileState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            repository.getProfile()?.let { profile ->
                _state.update {
                    it.copy(
                        name = profile.name,
                        age = profile.ageYears.toString(),
                        heightCm = profile.heightCm.toString(),
                        weightKg = profile.weightKg.toString(),
                        goal = profile.goal,
                        location = profile.location,
                        level = profile.level,
                        createdAt = profile.createdAt,
                        isLoaded = true
                    )
                }
            } ?: _state.update { it.copy(isLoaded = true) }
        }
    }

    fun onNameChange(value: String) = _state.update { it.copy(name = value) }
    fun onAgeChange(value: String) = _state.update { it.copy(age = value.filter { c -> c.isDigit() }) }
    fun onHeightChange(value: String) = _state.update { it.copy(heightCm = value.filter { c -> c.isDigit() || c == '.' }) }
    fun onWeightChange(value: String) = _state.update { it.copy(weightKg = value.filter { c -> c.isDigit() || c == '.' }) }
    fun onGoalChange(value: FitnessGoal) = _state.update { it.copy(goal = value) }
    fun onLocationChange(value: WorkoutLocation) = _state.update { it.copy(location = value) }
    fun onLevelChange(value: FitnessLevel) = _state.update { it.copy(level = value) }

    fun save() {
        val s = _state.value
        if (!s.isValid) return
        viewModelScope.launch {
            repository.saveProfile(
                UserProfile(
                    name = s.name.trim(),
                    ageYears = s.age.toInt(),
                    heightCm = s.heightCm.toFloat(),
                    weightKg = s.weightKg.toFloat(),
                    goal = s.goal,
                    location = s.location,
                    level = s.level,
                    createdAt = s.createdAt
                )
            )
            _state.update { it.copy(isSaved = true) }
        }
    }
}
