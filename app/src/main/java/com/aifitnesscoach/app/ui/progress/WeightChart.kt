package com.aifitnesscoach.app.ui.progress

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.dp
import com.aifitnesscoach.app.domain.model.WeightEntry

@Composable
fun WeightChart(entries: List<WeightEntry>, modifier: Modifier = Modifier) {
    val lineColor = MaterialTheme.colorScheme.primary
    val pointColor = MaterialTheme.colorScheme.secondary

    Canvas(modifier = modifier.fillMaxWidth().height(180.dp)) {
        if (entries.size < 2) return@Canvas

        val minWeight = entries.minOf { it.weightKg }
        val maxWeight = entries.maxOf { it.weightKg }
        val range = (maxWeight - minWeight).coerceAtLeast(1f)
        val stepX = size.width / (entries.size - 1)

        fun pointFor(index: Int): Offset {
            val entry = entries[index]
            val normalized = (entry.weightKg - minWeight) / range
            val y = size.height - (normalized * size.height)
            return Offset(index * stepX, y)
        }

        for (i in 0 until entries.size - 1) {
            drawLine(
                color = lineColor,
                start = pointFor(i),
                end = pointFor(i + 1),
                strokeWidth = 6f,
                cap = androidx.compose.ui.graphics.StrokeCap.Round
            )
        }
        entries.indices.forEach { i ->
            drawCircle(color = pointColor, radius = 8f, center = pointFor(i))
        }
    }
}
