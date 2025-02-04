package com.nima.mymood.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.nima.mymood.components.EffectsListItem
import com.nima.mymood.components.MoodFilterChip
import com.nima.mymood.model.Effect
import com.nima.mymood.model.EffectWithDate
import com.nima.mymood.navigation.Screens
import com.nima.mymood.viewmodels.AllEffectsViewModel
import kotlinx.coroutines.flow.collectLatest
import java.util.UUID

@SuppressLint("DefaultLocale")
@Composable
fun AllEffectsScreen(
    navController: NavController,
    viewModel: AllEffectsViewModel
) {

    var selectedFilter by remember {
        mutableIntStateOf(0)
    }

    var effects: List<EffectWithDate>? by remember {
        mutableStateOf(null)
    }

    var deleteEffect by remember {
        mutableStateOf(false)
    }

    var effectToDelete: UUID? by remember {
        mutableStateOf(null)
    }

    LaunchedEffect(selectedFilter) {
        when(selectedFilter){
            0 -> {
                viewModel.getAllEffects().collect{
                    effects = it
                }
            }
            1 -> {
                viewModel.getAllEffects(rate = listOf(0,1)).collect{
                    effects = it
                }
            }
            2 -> {
                viewModel.getAllEffects(rate = listOf(2)).collect{
                    effects = it
                }
            }
            3 -> {
                viewModel.getAllEffects(listOf(3,4)).collect{
                    effects = it
                }
            }
        }
    }

    Column (
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ){

        if (deleteEffect) {
            AlertDialog(
                onDismissRequest = {
                    deleteEffect = false
                    effectToDelete = null
                },
                dismissButton = {
                    TextButton(onClick = {
                        deleteEffect = false
                        effectToDelete = null
                    }) {
                        Text(text = "Cancel")
                    }
                },
                confirmButton = {
                    TextButton(onClick = {
                        viewModel.deleteEffect(effectToDelete!!).invokeOnCompletion {
                            effectToDelete = null
                            deleteEffect = false
                        }
                    }) {
                        Text(text = "Confirm")
                    }
                },
                icon = {
                    Icon(imageVector = Icons.Default.Delete, contentDescription = null)
                },
                text = {
                    Text(
                        text = "You are about to delete an effect from your day! Remember that effects will not be deleted from your life." +
                                "\nDo you want to permanently delete this effect?"
                    )
                },
                title = {
                    Text(text = "Delete Effect?")
                }
            )
        }

        Row (
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp, horizontal = 32.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ){
            MoodFilterChip(
                selected = selectedFilter == 0,
                label = "All Effects"
            ) { selectedFilter = 0 }
            MoodFilterChip(
                selected = selectedFilter == 1,
                label = "Happy"
            ) { selectedFilter = 1 }
            MoodFilterChip(
                selected = selectedFilter == 2,
                label = "Neutral"
            ) { selectedFilter = 2 }
            MoodFilterChip(
                selected = selectedFilter == 3,
                label = "Sad"
            ) { selectedFilter = 3 }
        }
        if (effects != null){
            LazyColumn(
                modifier = Modifier.padding(horizontal = 32.dp)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                items(items = effects!!, key = {
                    it.id
                }) {
                    EffectsListItem(
                        effectRate = it.rate,
                        effectDate = String.format("%02d/%02d/%4d", it.day, it.month, it.year),
                        onEditClicked = {
                            navController.navigate(Screens.EditScreen.name+"/${it.id}")
                        },
                        onDeleteClicked = {
                            deleteEffect = true
                            effectToDelete = it.id
                        },
                        effectDescription = it.description,
                        effectHour = it.hour,
                        effectMinute = it.minute,

                    )
                }
            }
        }
    }
}