package com.nima.mymood.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.nima.mymood.R

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun NumberKeyBoard(
    onClick: (String) -> Unit
) {
    val keys = listOf(
        "1", "2", "3", "4", "5", "6", "7", "8", "9", "del", "0", "Ok"
    )
    FlowRow (
        modifier = Modifier.padding(32.dp),
        horizontalArrangement = Arrangement.Center,
        verticalArrangement = Arrangement.Center,
        maxLines = 4,
        maxItemsInEachRow = 3
    ){
        keys.forEachIndexed { index, item ->
            Box(
                modifier = Modifier
                    .padding(8.dp)
                    .size(78.dp)
                    .aspectRatio(1f) // Makes width = height
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.surfaceVariant.copy(.5f))
                    .clickable {
                        onClick(item)
                    },
                contentAlignment = Alignment.Center
            ) {
                if (item == "del"){
                    Icon(painter = painterResource(R.drawable.backspace), contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.size(18.dp))
                }
                else{
                    Text(
                        text = item,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.ExtraBold
                    )
                }
            }
        }
    }
}