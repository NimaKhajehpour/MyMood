package com.nima.mymood.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.nima.mymood.ThemeDataStore
import com.nima.mymood.components.MoodAssistChip
import com.nima.mymood.components.MoodSlider
import com.nima.mymood.components.MoodSwitch
import com.nima.mymood.utils.Calculate
import com.nima.mymood.viewmodels.DaySettingsViewModel
import kotlinx.coroutines.launch
import java.util.UUID
import kotlin.random.Random

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun DaySettingsScreen(
    navController: NavController,
    viewModel: DaySettingsViewModel,
    id: String?
) {

    val day = viewModel.getDay(UUID.fromString(id)).collectAsState(null).value
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val appDataStore = ThemeDataStore(context)

    val autoRate = appDataStore.getAutoRate.collectAsState(false).value

    if (day != null){

        var dayRate by remember {
            mutableStateOf(day.rate.ifBlank { "2" })
        }
        var dayRed by remember {
            mutableStateOf(day.red.ifBlank { "${Random.nextInt(0, 255)}" })
        }
        var dayGreen by remember {
            mutableStateOf(day.green.ifBlank { "${Random.nextInt(0, 255)}" })
        }
        var dayBlue by remember {
            mutableStateOf(day.blue.ifBlank { "${Random.nextInt(0, 255)}" })
        }

        Scaffold (
            modifier = Modifier.fillMaxWidth(),
            floatingActionButton = {
                FloatingActionButton(
                    onClick = {
                        val dayToUpdate = day.copy(
                            red = dayRed,
                            green = dayGreen,
                            blue = dayBlue,
                            rate = dayRate
                        )
                        viewModel.updateDay(dayToUpdate)
                        navController.popBackStack()
                    },
                    containerColor = MaterialTheme.colorScheme.secondaryContainer,
                    contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
                ) {
                    Icon(imageVector = Icons.Default.Done, contentDescription = null)
                }
            },
            containerColor = Color.Transparent
        ){
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                MoodAssistChip(
                    day = day.day,
                    month = day.month,
                    year = day.year
                )

                Row(
                    modifier = Modifier.fillMaxWidth()
                        .padding(horizontal = 32.dp, vertical = 16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    MoodSwitch(
                        checked = autoRate!!,
                        modifier = Modifier.padding(end = 32.dp)
                    ) {
                        if (autoRate == true){
                            scope.launch {
                                appDataStore.saveAutoRate(false)
                            }
                        }else{
                            scope.launch {
                                appDataStore.saveAutoRate(true)
                            }
                        }
                    }
                    Text("Auto Rate Days",
                        style = MaterialTheme.typography.titleMedium
                        )
                }
                Calculate.calculateIconWithRate(
                    rate = dayRate.toInt(),
                    size = 110.dp,
                    red = dayRed.toInt(),
                    green = dayGreen.toInt(),
                    blue = dayBlue.toInt()
                )
                Text("Rate", style = MaterialTheme.typography.titleMedium, modifier = Modifier.padding(top = 16.dp))
                MoodSlider(
                    value = dayRate.toInt().toFloat(),
                    onValueChange = {
                        dayRate = it.toInt().toString()
                    },
                    valueRange = 0f..4f,
                    steps = 3,
                    modifier = Modifier.padding(vertical = 8.dp, horizontal = 32.dp)
                )
                Text("Red", style = MaterialTheme.typography.titleMedium)
                MoodSlider(
                    value = dayRed.toInt().toFloat(),
                    onValueChange = {
                        dayRed = it.toInt().toString()
                    },
                    valueRange = 0f..255f,
                    steps = 0,
                    modifier = Modifier.padding(vertical = 8.dp, horizontal = 32.dp)
                )
                Text("Green", style = MaterialTheme.typography.titleMedium)
                MoodSlider(
                    value = dayGreen.toInt().toFloat(),
                    onValueChange = {
                        dayGreen = it.toInt().toString()
                    },
                    valueRange = 0f..255f,
                    steps = 0,
                    modifier = Modifier.padding(vertical = 8.dp, horizontal = 32.dp)
                )
                Text("Blue", style = MaterialTheme.typography.titleMedium)
                MoodSlider(
                    value = dayBlue.toInt().toFloat(),
                    onValueChange = {
                        dayBlue = it.toInt().toString()
                    },
                    valueRange = 0f..255f,
                    steps = 0,
                    modifier = Modifier.padding(vertical = 8.dp, horizontal = 32.dp)
                )
                Button(
                    onClick = {
                        viewModel.deleteDay(day)
                        viewModel.deleteEffects(day.id)
                        navController.popBackStack()
                    },
                    modifier = Modifier.fillMaxWidth()
                        .padding(vertical = 16.dp, horizontal = 32.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Transparent,
                        contentColor = MaterialTheme.colorScheme.errorContainer
                    ),
                    border = BorderStroke(width = 1.dp, color = MaterialTheme.colorScheme.errorContainer)
                ) {
                    Text("Delete Day")
                }
            }
        }
    }

}