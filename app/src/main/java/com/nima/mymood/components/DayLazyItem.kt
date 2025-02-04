package com.nima.mymood.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.Checkbox
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.nima.mymood.utils.Calculate

@Composable
fun DayLazyItem(
    modifier: Modifier = Modifier,
    selected: Boolean,
    month: Int,
    day: Int,
    year: Int,
    onClick: () -> Unit
    ) {
    Column (
        modifier = modifier.padding(bottom = 8.dp)
            .fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Row(
            modifier = modifier
                .selectable(
                    selected = selected,
                    onClick = {
                        onClick()
                    }
                )
                .padding(bottom = 5.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            Checkbox(
                checked = selected,
                onCheckedChange = {
                    onClick()
                },
                modifier = Modifier.padding(end = 10.dp)
            )
            Text(
                "${Calculate.calculateMonthName(month)} $day $year"
            )
        }
        HorizontalDivider()
    }
}