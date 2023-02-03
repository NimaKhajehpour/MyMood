package com.nima.mymood.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.nima.mymood.components.EffectsListItem
import com.nima.mymood.utils.Calculate
import com.nima.mymood.viewmodels.DayViewModel
import java.util.*

@Composable
fun DayScreen (
    navController: NavController,
    viewModel: DayViewModel,
    id: String?
) {

    val day = viewModel.getDayById(UUID.fromString(id)).collectAsState(initial = null)
    val effects = viewModel.getDayEffects(UUID.fromString(id)).collectAsState(initial = emptyList())

    if (day.value != null){
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "${Calculate.calculateMonthName(day.value!!.month)} " +
                    "${day.value!!.day} ${day.value!!.year}",
                modifier = Modifier.padding(vertical = 16.dp)
            )

            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(vertical = 8.dp, horizontal = 32.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                items(items = effects.value){
                    EffectsListItem(
                        it.rate,
                        it.description
                    )
                }
            }
        }
    }
}