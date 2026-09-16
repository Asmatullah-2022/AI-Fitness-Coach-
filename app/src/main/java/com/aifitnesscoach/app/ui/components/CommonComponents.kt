package com.aifitnesscoach.app.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun SectionTitle(text: String, modifier: Modifier = Modifier) {
    Text(
        text = text,
        style = MaterialTheme.typography.titleMedium,
        modifier = modifier.padding(bottom = 8.dp)
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T> ChoiceChip(
    value: T,
    label: String,
    selected: Boolean,
    onSelected: (T) -> Unit
) {
    FilterChip(
        selected = selected,
        onClick = { onSelected(value) },
        label = { Text(label) }
    )
}

@Composable
fun InfoCard(content: @Composable () -> Unit) {
    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
        modifier = Modifier.padding(vertical = 6.dp)
    ) {
        androidx.compose.foundation.layout.Column(Modifier.padding(16.dp)) { content() }
    }
}
