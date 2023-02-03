package com.nima.mymood.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.nima.mymood.utils.Calculate

@Composable
fun SavedDayItem(
    day: Int,
    month: Int,
    year: Int,
    id: String,
    onClick: (String) -> Unit
) {

    ElevatedCard(
        modifier = Modifier.fillMaxWidth()
            .padding(8.dp),
        shape = RoundedCornerShape(5.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            Text(text = "${Calculate.calculateDayName(day)} " +
                    "${Calculate.calculateMonthName(month)} " +
                    "$day $year",
                modifier = Modifier.padding(start = 5.dp)
            )

            Spacer(modifier = Modifier.weight(1f))

            TextButton(onClick = {
                onClick(id)
            }) {
                Text(text = "See Effects")
            }
        }
    }
}