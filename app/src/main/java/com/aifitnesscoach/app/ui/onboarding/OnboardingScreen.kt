package com.aifitnesscoach.app.ui.onboarding

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.aifitnesscoach.app.domain.model.FitnessGoal
import com.aifitnesscoach.app.domain.model.FitnessLevel
import com.aifitnesscoach.app.domain.model.WorkoutLocation
import com.aifitnesscoach.app.ui.components.ChoiceChip
import com.aifitnesscoach.app.ui.components.SectionTitle

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OnboardingScreen(
    viewModel: OnboardingViewModel,
    onFinished: () -> Unit
) {
    val state by viewModel.state.collectAsState()

    if (state.isSaved) {
        onFinished()
        return
    }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Tell us about you") }) }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp),
            contentPadding = PaddingValues(vertical = 16.dp)
        ) {
            item {
                Text(
                    "Build your AI-personalized workout plan",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
            }
            item {
                OutlinedTextField(
                    value = state.name,
                    onValueChange = viewModel::onNameChange,
                    label = { Text("Your name") },
                    modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp)
                )
            }
            item {
                OutlinedTextField(
                    value = state.age,
                    onValueChange = viewModel::onAgeChange,
                    label = { Text("Age (years)") },
                    keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp)
                )
            }
            item {
                OutlinedTextField(
                    value = state.heightCm,
                    onValueChange = viewModel::onHeightChange,
                    label = { Text("Height (cm)") },
                    keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp)
                )
            }
            item {
                OutlinedTextField(
                    value = state.weightKg,
                    onValueChange = viewModel::onWeightChange,
                    label = { Text("Weight (kg)") },
                    keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    modifier = Modifier.fillMaxWidth().padding(bottom = 20.dp)
                )
            }
            item {
                SectionTitle("Fitness goal")
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(FitnessGoal.entries) { goal ->
                        ChoiceChip(goal, goal.label, state.goal == goal, viewModel::onGoalChange)
                    }
                }
            }
            item {
                SectionTitle("Workout location", Modifier.padding(top = 20.dp))
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(WorkoutLocation.entries) { location ->
                        ChoiceChip(location, location.label, state.location == location, viewModel::onLocationChange)
                    }
                }
            }
            item {
                SectionTitle("Fitness level", Modifier.padding(top = 20.dp))
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(FitnessLevel.entries) { level ->
                        ChoiceChip(level, level.label, state.level == level, viewModel::onLevelChange)
                    }
                }
            }
            item {
                Button(
                    onClick = viewModel::saveProfile,
                    enabled = state.isValid,
                    modifier = Modifier.fillMaxWidth().padding(top = 28.dp)
                ) {
                    Text("Generate my plan")
                }
            }
        }
    }
}
