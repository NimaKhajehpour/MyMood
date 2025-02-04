package com.nima.mymood.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Switch
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun MoodSwitch(
    modifier: Modifier = Modifier,
    checked: Boolean,
    onCheckChange: () -> Unit
) {
    Switch(
        checked = checked,
        modifier = modifier,
        onCheckedChange = {
            onCheckChange()
        }
    )
}