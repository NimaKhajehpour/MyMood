package com.nima.mymood.screens

import android.annotation.SuppressLint
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.LocalPinnableContainer
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavController
import com.nima.mymood.R
import com.nima.mymood.components.EffectsListItem
import com.nima.mymood.model.Day
import com.nima.mymood.model.Effect
import com.nima.mymood.navigation.Screens
import com.nima.mymood.ui.theme.*
import com.nima.mymood.utils.Calculate
import com.nima.mymood.viewmodels.HomeViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.produce
import kotlinx.coroutines.channels.toList
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*
import androidx.compose.material3.TextField as TextField1

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "DefaultLocale")
@OptIn(ExperimentalMaterial3Api::class, ExperimentalCoroutinesApi::class)
@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: HomeViewModel
) {

    val calendar = Calendar.getInstance()

    val scope = rememberCoroutineScope()

    val year by remember {
        mutableStateOf(calendar.get(Calendar.YEAR))
    }
    val month by remember {
        mutableStateOf(calendar.get(Calendar.MONTH) + 1)
    }
    val day by remember {
        mutableStateOf(calendar.get(Calendar.DAY_OF_MONTH))
    }

    var deleteEffect by remember {
        mutableStateOf(false)
    }

    var effectToDelete: Effect? by remember {
        mutableStateOf(null)
    }

    val today = produceState<Day?>(initialValue = null) {
        value = viewModel.getDayByDate(year, month, day)
    }.value

    val todayMillis =
        remember { LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli() }

    val datePickerState = rememberDatePickerState(initialSelectedDateMillis = todayMillis,
        initialDisplayMode = DisplayMode.Picker,
        selectableDates = object : SelectableDates {
            override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                val selectedDate = Instant.ofEpochMilli(utcTimeMillis)
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate()
                return !selectedDate.isAfter(LocalDate.now()) // Disable future dates
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
                        scope.launch{
                            datePickerState.selectedDateMillis?.let { millis ->
                                val formattedDate = Instant.ofEpochMilli(millis)
                                    .atZone(ZoneId.systemDefault())
                                    .toLocalDate()
                                    .format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) // Format here
                                val date = formattedDate.split("/")
                                val exists = viewModel.getDayByDate(
                                    date[2].toInt(),
                                    date[1].toInt(),
                                    date[0].toInt()
                                )
                                if (exists != null) {
                                    createDay = false
                                    navController.navigate(Screens.TodayMoodScreen.name + "/" + exists.id.toString())
                                    return@launch
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
                                scope.launch {
                                    viewModel.addDay(tempDay)
                                }.invokeOnCompletion {
                                    navController.navigate(Screens.TodayMoodScreen.name + "/" + tempDay.id.toString())
                                }
                                createDay = false
                            }
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

    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
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
        Scaffold(
            containerColor = MaterialTheme.colorScheme.surface,
            floatingActionButton = {
                FloatingActionButton(
                    onClick = {
                        createDay = true
                    },
                    containerColor = MaterialTheme.colorScheme.secondaryContainer,
                    contentColor = MaterialTheme.colorScheme.onSecondaryContainer,

                    ) {
                    Icon(Icons.Default.Add, contentDescription = null)
                }
            }
        ) {
            when (today) {
                null -> {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 64.dp),
                        verticalArrangement = Arrangement.Top,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_outline_sentiment_very_dissatisfied_24),
                            contentDescription = null,
                            modifier = Modifier
                                .size(116.dp)
                                .padding(top = 16.dp, bottom = 10.dp),
                            tint = Color.Gray
                        )
                        Text(
                            text = "Nothing to see for today!",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Light,
                            color = Color.Gray,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                    }
                }

                else -> {
                    val effects =
                        viewModel.getDayEffect(today.id).collectAsState(initial = emptyList())

                    if (effects.value.isNotEmpty()) {
                        LazyColumn(
                            modifier = Modifier.fillMaxWidth(),
                            verticalArrangement = Arrangement.Top,
                            horizontalAlignment = Alignment.CenterHorizontally,
                            contentPadding = PaddingValues(vertical = 16.dp, horizontal = 32.dp)
                        ) {
                            items(items = effects.value, key = {
                                it.id
                            }) {
                                EffectsListItem(
                                    it.rate,
                                    it.description,
                                    effectHour = it.hour,
                                    effectMinute = it.minute,
                                    effectDate = String.format("%02d/%02d/%4d", day, month, year),
                                    onEditClicked = {
                                        navController.navigate(Screens.EditScreen.name+"/${it.id}")
                                    },
                                    onDeleteClicked = {
                                        deleteEffect = true
                                        effectToDelete = it
                                    }
                                )
                            }
                        }

                    } else {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 64.dp),
                            verticalArrangement = Arrangement.Top,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_outline_sentiment_very_dissatisfied_24),
                                contentDescription = null,
                                modifier = Modifier
                                    .size(116.dp)
                                    .padding(top = 16.dp, bottom = 10.dp),
                                tint = Color.Gray
                            )
                            Text(
                                text = "Nothing to see for today!",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Light,
                                color = Color.Gray,
                                modifier = Modifier.padding(bottom = 8.dp)
                            )
                        }
                    }
                }
            }
        }
    }

}