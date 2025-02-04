package com.nima.mymood.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun MoodNavigationDrawerItem(
    modifier: Modifier = Modifier,
    label: String,
    selected: Boolean = false,
    onClick: () -> Unit
) {
    NavigationDrawerItem(
        label = {
            Text(label, color = MaterialTheme.colorScheme.onSurfaceVariant)
        },
        onClick = onClick,
        selected = selected
    )
}