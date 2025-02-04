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
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.*
import androidx.compose.material3.TextField as TextField1

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "DefaultLocale")
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

    val datePickerState = rememberDatePickerState()
    datePickerState.displayMode = DisplayMode.Picker

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
                        if (today == null) {
                            val tempDay = Day(
                                day = day,
                                month = month,
                                year = year,
                                red = "",
                                green = "",
                                blue = "",
                                rate = ""
                            )
                            scope.launch {
                                viewModel.addDay(tempDay)
                            }.invokeOnCompletion {
                                navController.navigate(Screens.TodayMoodScreen.name +"/"+ tempDay.id.toString())
                            }
                        }else{
                            navController.navigate(Screens.TodayMoodScreen.name + "/${today.id.toString()}")
                        }
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