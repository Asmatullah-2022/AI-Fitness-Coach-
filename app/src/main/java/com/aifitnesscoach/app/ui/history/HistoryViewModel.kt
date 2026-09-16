package com.aifitnesscoach.app.ui.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aifitnesscoach.app.data.repository.FitnessRepository
import com.aifitnesscoach.app.domain.model.WorkoutHistoryEntry
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class HistoryUiState(
    val entries: List<WorkoutHistoryEntry> = emptyList(),
    val isLoading: Boolean = true
)

class HistoryViewModel(private val repository: FitnessRepository) : ViewModel() {

    private val _state = MutableStateFlow(HistoryUiState())
    val state: StateFlow<HistoryUiState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            repository.observeHistory().collect { list ->
                _state.value = HistoryUiState(entries = list, isLoading = false)
            }
        }
    }
}
