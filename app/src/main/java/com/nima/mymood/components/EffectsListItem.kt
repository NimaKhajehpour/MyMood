package com.nima.mymood.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.nima.mymood.R
import com.nima.mymood.ui.theme.*

@Composable
fun EffectsListItem(
    effectRate: Int = 0,
    effectDescription: String = ""
) {
    ElevatedCard(
        shape = RoundedCornerShape(5.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 5.dp),
        elevation = CardDefaults.elevatedCardElevation(15.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.Start
        ) {
            Icon(painter =
                painterResource(id =
                    when(effectRate){
                        0 -> R.drawable.ic_outline_sentiment_very_satisfied_24
                        1 -> R.drawable.ic_outline_sentiment_satisfied_alt_24
                        2 -> R.drawable.ic_outline_sentiment_neutral_24
                        3 -> R.drawable.ic_outline_sentiment_dissatisfied_24
                        else -> R.drawable.ic_outline_sentiment_very_dissatisfied_24
                    }
                ),
                contentDescription = null,
                tint = when(effectRate){
                    0 -> very_satisfied
                    1 -> satisfied
                    2 -> neutral
                    3 -> dissatisfied
                    else -> very_dissatisfied
                }
            )

            Text(text = effectDescription,
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Light,
                modifier = Modifier.padding(start = 16.dp)

            )
        }
    }
}