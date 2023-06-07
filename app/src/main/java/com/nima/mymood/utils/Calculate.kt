package com.nima.mymood.utils

import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.nima.mymood.R
import com.nima.mymood.ui.theme.*

object Calculate {

    fun calculateDayName(day: Int): String =
        when(day){
            1 -> "Sun"
            2 -> "Mon"
            3 -> "Tue"
            4 -> "Wed"
            5 -> "Thu"
            6 -> "Fri"
            7 -> "Sat"
            else -> ""
        }

    fun calculateMonthName(month: Int): String =
        when(month){

            1 -> "Jan"
            2 -> "Feb"
            3 -> "Mar"
            4 -> "Apr"
            5 -> "May"
            6 -> "Jun"
            7 -> "Jul"
            8 -> "Aug"
            9 -> "Sep"
            10 -> "Oct"
            11 -> "Nov"
            12 -> "Dec"
            else -> ""
        }
    @Composable
    fun calculateIconWithRate(rate: Int, size: Dp = 24.dp)=
        when(rate){
            0 -> Icon(painter = painterResource(id = R.drawable.ic_outline_sentiment_very_satisfied_24),
                contentDescription = null,
                tint = very_satisfied,
                modifier = Modifier.size(size)
            )
            1 -> Icon(painter = painterResource(id = R.drawable.ic_outline_sentiment_satisfied_alt_24),
                contentDescription = null,
                tint = satisfied,
                modifier = Modifier.size(size)
            )
            2 -> Icon(painter = painterResource(id = R.drawable.ic_outline_sentiment_neutral_24),
                contentDescription = null,
                tint = neutral,
                modifier = Modifier.size(size)

            )
            3 -> Icon(painter = painterResource(id = R.drawable.ic_outline_sentiment_dissatisfied_24),
                contentDescription = null,
                tint = dissatisfied,
                modifier = Modifier.size(size)

            )
            else -> Icon(painter = painterResource(id = R.drawable.ic_outline_sentiment_very_dissatisfied_24),
                contentDescription = null,
                tint = very_dissatisfied,
                modifier = Modifier.size(size)

            )
        }
}