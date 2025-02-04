package com.nima.mymood.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.nima.mymood.utils.Calculate

@Composable
fun MoodAssistChip(
    modifier: Modifier = Modifier,
    month: Int,
    day: Int,
    year: Int
    ) {
    AssistChip(
        onClick = {},
        label = {
            Text(
                text = "${Calculate.calculateMonthName(month)} " +
                        "$day $year",
                style = MaterialTheme.typography.labelLarge,
            )
        },
        colors = AssistChipDefaults.assistChipColors(
            containerColor = Color.Transparent,
            labelColor = MaterialTheme.colorScheme.onSurface
        ),
        border = BorderStroke(
            width = 1.dp,
            color = MaterialTheme.colorScheme.tertiaryContainer
        ),
        modifier = modifier.padding(bottom = 16.dp)
    )
}