package com.nima.mymood.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.nima.mymood.R
import com.nima.mymood.utils.Calculate

@Composable
fun SavedDayItem(
    day: Int,
    month: Int,
    year: Int,
    id: String,
    rate: Int,
    red: Int,
    green: Int,
    blue: Int,
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
            if (rate != -1){
                Icon(
                    painter = painterResource(
                        id =
                        when (rate) {
                            0 -> R.drawable.ic_outline_sentiment_very_satisfied_24
                            1 -> R.drawable.ic_outline_sentiment_satisfied_alt_24
                            2 -> R.drawable.ic_outline_sentiment_neutral_24
                            3 -> R.drawable.ic_outline_sentiment_dissatisfied_24
                            else -> R.drawable.ic_outline_sentiment_very_dissatisfied_24
                        }
                    ), contentDescription = null,
                    tint = Color(red = red, green = green, blue = blue),
                    modifier = Modifier.padding(end = 5.dp)
                )
            }

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