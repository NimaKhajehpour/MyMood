package com.nima.mymood.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.nima.mymood.R
import com.nima.mymood.utils.Calculate

@Composable
fun SavedDayItemWAVG(
    day: Int,
    month: Int,
    year: Int,
    id: String,
    icon: @Composable() () -> Unit,
    onClick: (String) -> Unit
) {

    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        shape = RoundedCornerShape(5.dp),
        elevation = CardDefaults.elevatedCardElevation(15.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start,
        ) {
            icon()

            Text(text = "${Calculate.calculateMonthName(month)} " +
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