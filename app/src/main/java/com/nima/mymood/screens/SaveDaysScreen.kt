package com.nima.mymood.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.nima.mymood.components.SavedDayItem
import com.nima.mymood.navigation.Screens
import com.nima.mymood.viewmodels.SavedDaysViewModel

@Composable
fun SaveDaysScreen (
    navController: NavController,
    viewModel: SavedDaysViewModel
){
    val days = viewModel.gtDays().collectAsState(initial = emptyList())

    if (days.value.isNotEmpty()){
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(vertical = 8.dp, horizontal = 16.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            items(items = days.value){
                SavedDayItem(day = it.day,
                    month = it.month,
                    year = it.year,
                    id = it.id.toString(),
                    rate = it.rate.takeIf {rate -> rate.isNotBlank() }?.toInt() ?: -1,
                    red = it.red.takeIf {red -> red.isNotBlank() }?.toInt() ?: -1,
                    green = it.green.takeIf {green -> green.isNotBlank() }?.toInt() ?: -1,
                    blue = it.blue.takeIf {blue -> blue.isNotBlank() }?.toInt() ?: -1,
                )
                { id ->
                    // go to day
                    navController.navigate(Screens.DayScreen.name+"/$id")
                }
            }
        }
    }
}