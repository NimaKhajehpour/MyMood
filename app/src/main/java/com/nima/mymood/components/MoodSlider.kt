package com.nima.mymood.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun MoodSlider(
    modifier: Modifier = Modifier,
    valueRange: ClosedFloatingPointRange<Float>,
    value: Float,
    steps: Int,
    onValueChange: (Float) -> Unit,
    ) {
    Slider(
        modifier = modifier,
        valueRange = valueRange,
        onValueChange = {
            onValueChange(it)
        },
        value = value,
        steps = steps,
        colors = SliderDefaults.colors(
            activeTrackColor = MaterialTheme.colorScheme.primary,
            inactiveTrackColor = MaterialTheme.colorScheme.tertiaryContainer
        )
    )
}