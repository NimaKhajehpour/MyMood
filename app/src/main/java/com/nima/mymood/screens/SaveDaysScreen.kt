package com.nima.mymood.screens

import android.annotation.SuppressLint
import android.util.Log
import android.view.translation.TranslationSpec
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DisplayMode
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
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
import com.nima.mymood.components.SavedDayItem
import com.nima.mymood.components.SavedDayItemWAVG
import com.nima.mymood.model.Day
import com.nima.mymood.navigation.Screens
import com.nima.mymood.utils.Calculate
import com.nima.mymood.viewmodels.SavedDaysViewModel
import kotlinx.coroutines.channels.getOrElse
import kotlinx.coroutines.channels.produce
import kotlinx.coroutines.channels.toList
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.produceIn
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun SaveDaysScreen(
    navController: NavController,
    viewModel: SavedDaysViewModel
) {
    val days = viewModel.gtDays().collectAsState(initial = emptyList())

    val context = LocalContext.current
    val appDataStore = ThemeDataStore(context)
    val autoRate = appDataStore.getAutoRate.collectAsState(initial = false).value

    val today = remember { LocalDate.now() }
    val todayMillis =
        remember { today.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli() }

    val datePickerState = rememberDatePickerState(initialSelectedDateMillis = todayMillis,
        initialDisplayMode = DisplayMode.Picker,
        selectableDates = object : SelectableDates {
            override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                val selectedDate = Instant.ofEpochMilli(utcTimeMillis)
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate()
                return !selectedDate.isAfter(today) // Disable future dates
            }
        }
    )

    var createDay by remember {
        mutableStateOf(false)
    }

    if (createDay) {
        DatePickerDialog(
            onDismissRequest = {
                createDay = false
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        datePickerState.selectedDateMillis?.let { millis ->
                            val formattedDate = Instant.ofEpochMilli(millis)
                                .atZone(ZoneId.systemDefault())
                                .toLocalDate()
                                .format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) // Format here
                            val date = formattedDate.split("/")
                            val exists = days.value.firstOrNull() {
                                it.day == date[0].toInt() && it.month == date[1].toInt() && it.year == date[2].toInt()
                            }
                            if (exists != null){
                                createDay = false
                                return@TextButton
                            }
                            val tempDay = Day(
                                year = date[2].toInt(),
                                month = date[1].toInt(),
                                day = date[0].toInt(),
                                rate = "",
                                red = "",
                                green = "",
                                blue = ""
                            )
                            viewModel.saveDay(tempDay)
                            createDay = false
                        }
                    }
                ) {
                    Text("Confirm")
                }
            },
        ) {
            DatePicker(state = datePickerState, showModeToggle = false)
        }
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    createDay = true
                },
                containerColor = MaterialTheme.colorScheme.secondaryContainer,
                contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = null)
            }
        },
        modifier = Modifier.fillMaxWidth(),
        containerColor = Color.Transparent,
    ) {
        if (days.value.isNotEmpty()) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(vertical = 8.dp, horizontal = 16.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                items(items = days.value, key = {
                    it.id
                }) {
                    if (autoRate == false) {
                        SavedDayItem(
                            day = it.day,
                            month = it.month,
                            year = it.year,
                            rate = it.rate.takeIf { rate -> rate.isNotBlank() }?.toInt() ?: -1,
                            red = it.red.takeIf { red -> red.isNotBlank() }?.toInt() ?: -1,
                            green = it.green.takeIf { green -> green.isNotBlank() }?.toInt() ?: -1,
                            blue = it.blue.takeIf { blue -> blue.isNotBlank() }?.toInt() ?: -1,
                            onSettingsClicked = {
                                navController.navigate(Screens.DaySettingsScreen.name+"/${it.id}")
                            }
                        )
                        {
                            // go to day
                            navController.navigate(Screens.DayScreen.name + "/${it.id}")
                        }
                    } else {
                        val rate = viewModel.getDayRating(it.id).collectAsState(initial = 0.0).value
                        Log.d("LOL", "SaveDaysScreen: $rate, ${it.id}")
                        SavedDayItemWAVG(
                            day = it.day,
                            month = it.month,
                            year = it.year,
                            icon = {
                                Calculate.calculateIconWithRate(rate = rate.toInt(), size = 40.dp)
                            },
                            onSettingsClicked = {
                                navController.navigate(Screens.DaySettingsScreen.name+"/${it.id}")
                            }
                        ) {
                            navController.navigate(Screens.DayScreen.name + "/${it.id}")
                        }
                    }
                }
            }
        }
    }
}