package com.nima.mymood.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.nima.mymood.components.MenuItems
import com.nima.mymood.R
import com.nima.mymood.navigation.Screens
import com.nima.mymood.ui.theme.neutral
import com.nima.mymood.ui.theme.very_dissatisfied
import com.nima.mymood.ui.theme.very_satisfied

@Composable
fun MenuScreen(
    navController: NavController,
) {

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
    ) {
        FilledIconButton(onClick = {
            navController.popBackStack()
        },
            shape = CircleShape,
            modifier = Modifier.padding(16.dp)
        ) {
            Icon(imageVector = Icons.Outlined.Home, contentDescription = null)
        }

        LazyVerticalGrid(columns = GridCells.Fixed(2),
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.Center,
            horizontalArrangement = Arrangement.Center,
            contentPadding = PaddingValues(16.dp)
        ){
            item {
                MenuItems(icon = null,
                    icon2 = R.drawable.ic_outline_sentiment_very_dissatisfied_24,
                    title = "Happy Effects",
                    tint = very_satisfied
                ) {
                    // go to happy effect
                    navController.navigate(Screens.HappyEffects.name)
                }
            }
            item {
                MenuItems(icon = null,
                    icon2 = R.drawable.ic_outline_sentiment_neutral_24,
                    title = "Neutral Effects",
                    tint = neutral
                ) {
                    // go to neutral effect
                    navController.navigate(Screens.NeutralEffects.name)

                }
            }
            item {
                MenuItems(icon = null,
                    icon2 = R.drawable.ic_outline_sentiment_very_dissatisfied_24,
                    title = "Sad Effects",
                    tint = very_dissatisfied
                ) {
                    // go to sad effect
                    navController.navigate(Screens.SadEffects.name)

                }
            }
            item {
                MenuItems(icon = Icons.Outlined.Search,
                    icon2 = null,
                    title = "Saved Days",
                    tint = Color.Black
                ) {
                    // go search
                    navController.navigate(Screens.SavedDays.name)
                }
            }
        }
    }
}