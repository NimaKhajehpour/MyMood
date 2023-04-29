package com.nima.mymood.screens

import android.annotation.SuppressLint
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.nima.mymood.R
import com.nima.mymood.components.EffectsListItem
import com.nima.mymood.model.Day
import com.nima.mymood.model.Effect
import com.nima.mymood.navigation.Screens
import com.nima.mymood.ui.theme.*
import com.nima.mymood.utils.Calculate
import com.nima.mymood.viewmodels.HomeViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.*
import androidx.compose.material3.TextField as TextField1

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
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
        mutableStateOf(calendar.get(Calendar.MONTH)+1)
    }
    val day by remember {
        mutableStateOf(calendar.get(Calendar.DAY_OF_MONTH))
    }

    val dayOfWeek by remember{
        mutableStateOf(calendar.get(Calendar.DAY_OF_WEEK))
    }

    var deleteEffect by remember {
        mutableStateOf(false)
    }

    var effectToDelete: Effect? by remember {
        mutableStateOf(null)
    }

    var updateEffect by remember {
        mutableStateOf(false)
    }

    var effectToUpdate: Effect? by remember {
        mutableStateOf(null)
    }

    val today = produceState<Day?>(initialValue = null){
        value = viewModel.getDayByDate(year, month, day)
    }.value

    var showDatePicker by remember {
        mutableStateOf(false)
    }

    var newDescription by remember { mutableStateOf("") }

    val datePickerState = rememberDatePickerState()
    datePickerState.displayMode = DisplayMode.Input

    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        if (deleteEffect){
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
                    Text(text = "You are about to delete an effect from your day! Remember that effects will not be deleted from your life." +
                            "\nDo you want to permanently delete this effect?")
                },
                title = {
                    Text(text = "Delete Effect?")
                }
            )
        }


        if (updateEffect) {
            AlertDialog(
                onDismissRequest = {
                    updateEffect = false
                    effectToUpdate = null
                },
                text = {
                    TextField1(
                        value = newDescription,
                        onValueChange = {
                            newDescription = it
                        }
                    )
                },
                confirmButton = {
                    TextButton(onClick = {
                        viewModel.updateEffect(effectToUpdate!!.copy(description = newDescription)).invokeOnCompletion {
                            updateEffect = false
                            effectToUpdate = null
                        }
                    }) {
                        Text(text = "Confirm")
                    }
                },
                title = {
                    Text(text = "Update Effect")
                }
            )
        }


        if (showDatePicker){
            DatePickerDialog(
                onDismissRequest = {
                    showDatePicker = false
                    datePickerState.setSelection(null)
                                   },
                confirmButton = {
                    TextButton(onClick = {
                        // get date and go to mood editing
                        val date = Date(datePickerState.selectedDateMillis!!)
                        val selectedDay = date.date+1
                        val selectedMonth = date.month+1
                        val selectedYear = date.year+1900
                        var selectedDate: Day? = null
                        scope.launch {
                            selectedDate = viewModel.getDayByDate(selectedYear, selectedMonth, selectedDay)
                        }.invokeOnCompletion {
                            if (selectedDate == null){
                                val tempDay = Day(day = selectedDay, month = selectedMonth, year = selectedYear)
                                scope.launch {
                                    viewModel.addDay(tempDay)
                                }.invokeOnCompletion {
                                    datePickerState.setSelection(null)
                                    showDatePicker = false
                                    navController.navigate(
                                        Screens.TodayMoodScreen.name+"/${tempDay.id}")
                                }
                            }else{
                                val dayId = selectedDate!!.id
                                datePickerState.setSelection(null)
                                showDatePicker = false
                                navController.navigate(
                                    Screens.TodayMoodScreen.name+"/${dayId}")
                            }
                        }
                    },
                        enabled = datePickerState.selectedDateMillis != null && datePickerState.selectedDateMillis!! <= calendar.timeInMillis,
                        colors = ButtonDefaults.textButtonColors(
                            contentColor = MaterialTheme.colorScheme.tertiary
                        )
                    ) {
                        Text(text = "Edit Day")
                    }
                },
                dismissButton = {
                    TextButton(onClick = {
                        showDatePicker = false
                        datePickerState.setSelection(null)
                    }) {
                        Text(text = "Cancel")
                    }
                }
            ) {
                DatePicker(
                    state = datePickerState,
                    title = {
                        Text(text = "Select a Date")
                    },
                    dateValidator = {
                        it <= calendar.timeInMillis
                    },
                    showModeToggle = false
                )
            }
        }

        CenterAlignedTopAppBar(
            title = {
                TextButton(
                    onClick = {
                        showDatePicker = true
                    },
                    border = BorderStroke(2.dp, MaterialTheme.colorScheme.tertiaryContainer),
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = MaterialTheme.colorScheme.secondary
                    )
                ){
                    Text(
                        text = "${Calculate.calculateDayName(dayOfWeek)} " +
                                "${Calculate.calculateMonthName(month)} " +
                                "$day $year",
                        style = MaterialTheme.typography.bodyLarge,
                    )
                }
            },
            actions = {
                FilledIconButton(onClick = {
                    // go to moods menu
                    navController.navigate(Screens.MenuScreen.name)
                },
                    modifier = Modifier.padding(end = 12.dp)
                ) {
                    Icon(imageVector = Icons.Default.ArrowForward, contentDescription = null)
                }
            }
        )
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
                            .size(96.dp)
                            .padding(top = 16.dp, bottom = 10.dp),
                        tint = Color.LightGray
                    )
                    Text(text = "No Entry For Today!",
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.Light,
                        color = Color.LightGray,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    ElevatedButton(onClick = {
                        // today mood screen
                        val tempDay = Day(
                            day = day,
                            month = month,
                            year = year,
                        )
                        runBlocking {
                            launch {
                                viewModel.addDay(tempDay)
                            }
                        }
                        navController.navigate(
                            Screens.TodayMoodScreen.name+"/${tempDay.id.toString()}")
                    },
                        elevation = ButtonDefaults.elevatedButtonElevation(15.dp)
                    ) {
                        Text(text = "How Is Your Mood?")
                    }
                }
            }
            else -> {

                val effects = viewModel.getDayEffect(today.id).collectAsState(initial = emptyList())

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp, start = 16.dp, end = 16.dp, bottom = 32.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.End
                ) {
                    ElevatedButton(onClick = {
                        // go to edit
                        navController.navigate(Screens.TodayMoodScreen.name+"/${today.id.toString()}")
                    },
                        shape = RoundedCornerShape(5.dp),
                        elevation = ButtonDefaults.elevatedButtonElevation(15.dp)
                    ) {
                        Icon(imageVector = Icons.Outlined.Edit, contentDescription = null)
                    }
                }

                if (effects.value.isNotEmpty()){

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        verticalArrangement = Arrangement.Top,
                        horizontalAlignment = Alignment.Start
                    ){
                        LazyColumn(
                            modifier = Modifier.fillMaxWidth(),
                            verticalArrangement = Arrangement.Top,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ){
                            items(items = effects.value, key = {
                                it.id
                            }) {
                                EffectsListItem(
                                    it.rate,
                                    it.description,
                                    onLongPress = {
                                        effectToDelete = it
                                        deleteEffect = true
                                },
                                    onDoubleTap = {
                                        effectToUpdate = it
                                        updateEffect = true
                                    }
                            )
                            }
                        }
                    }

                }

                else{
                    Text(text = "Nothing Here Yet!",
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Light,
                        color = Color.LightGray
                    )
                }
            }
        }
    }

}