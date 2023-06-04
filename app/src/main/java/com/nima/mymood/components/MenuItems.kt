package com.nima.mymood.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MenuItems (
    icon: ImageVector?,
    icon2: Int?,
    title: String,
    tint: Color,
    onClick: () -> Unit
){

    OutlinedButton(onClick = {
        onClick()
    },
        shape = CircleShape,
        contentPadding = PaddingValues(15.dp),
        modifier = Modifier.padding(15.dp)
    ){
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (icon != null) {
                Icon(
                    imageVector = icon, contentDescription = null,
                    modifier = Modifier
                        .size(56.dp)
                        .padding(bottom = 16.dp),
                    tint = tint
                )
            } else {
                Icon(
                    painter = painterResource(id = icon2!!), contentDescription = null,
                    modifier = Modifier
                        .size(56.dp)
                        .padding(bottom = 16.dp),
                    tint = tint
                )
            }
            Text(
                text = title,
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Light
            )
        }
    }

}