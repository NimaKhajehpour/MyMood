package com.nima.mymood.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun MoodFilterChip(
    modifier: Modifier = Modifier,
    selected: Boolean,
    label: String,
    onclick: () -> Unit
    ) {
    FilterChip(
        selected = selected,
        onClick = {
            onclick()
        },
        label = {
            Text(label)
        },
        leadingIcon = {
            if (selected){
                Icon(imageVector = Icons.Default.Done, contentDescription = null, modifier = Modifier.size(18.dp))
            }else{
                null
            }
        },
        colors = FilterChipDefaults.filterChipColors(
            containerColor = Color.Transparent,
            labelColor = MaterialTheme.colorScheme.onSurfaceVariant,
            selectedContainerColor = MaterialTheme.colorScheme.tertiaryContainer,
            selectedLabelColor = MaterialTheme.colorScheme.onTertiaryContainer,
            selectedLeadingIconColor = MaterialTheme.colorScheme.onTertiaryContainer
        ),
        border = BorderStroke(1.dp, color = MaterialTheme.colorScheme.tertiaryContainer)
    )
}