package com.nima.mymood.screens

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.max
import androidx.navigation.NavController
import com.nima.mymood.R
import com.nima.mymood.ThemeDataStore
import com.nima.mymood.components.EffectsListItem
import com.nima.mymood.components.MoodAssistChip
import com.nima.mymood.model.Effect
import com.nima.mymood.navigation.Screens
import com.nima.mymood.ui.theme.*
import com.nima.mymood.utils.Calculate
import com.nima.mymood.viewmodels.TodayMoodViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.*

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TodayMoodScreen(
    navController: NavController,
    viewModel: TodayMoodViewModel,
    id: String?,
) {

    val day = viewModel.getDayById(UUID.fromString(id)).collectAsState(initial = null)

    val calendar = Calendar.getInstance()
    val hour = calendar.get(Calendar.HOUR_OF_DAY) // 24-hour format
    val minute = calendar.get(Calendar.MINUTE)

    val scope = rememberCoroutineScope()

    var moodText by remember { mutableStateOf("") }

    var effectRate by remember {
        mutableStateOf(2)
    }
    val timePickerState = rememberTimePickerState(initialMinute = minute, initialHour = hour)

    if (day.value != null){
        Scaffold (
            floatingActionButton = {
                FloatingActionButton(
                    onClick = {
                        if (moodText.isNotBlank()) {
                            val effect = Effect(foreignKey = UUID.fromString(id),
                                description = moodText.trim(),
                                rate = effectRate,
                                hour = timePickerState.hour.toString(),
                                minute = timePickerState.minute.toString()
                            )
                            scope.launch {
                                viewModel.addEffect(effect)
                            }.invokeOnCompletion {
                                navController.popBackStack()
                            }
                        }
                    },
                    containerColor = MaterialTheme.colorScheme.secondaryContainer,
                    contentColor = MaterialTheme.colorScheme.onSecondaryContainer,

                    ) {
                    Icon(Icons.Default.Done, contentDescription = null)
                }
            },
            containerColor = MaterialTheme.colorScheme.surface
        ){
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                MoodAssistChip(
                    day = day.value!!.day,
                    month = day.value!!.month,
                    year = day.value!!.year
                )
                OutlinedTextField(
                    value = moodText,
                    onValueChange = {
                        moodText = it
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 32.dp, end = 32.dp, bottom = 32.dp),
                    label = {
                        Text("What affected your mood?")
                    },
                    singleLine = false,
                    maxLines = 6
                )
                TimeInput(state = timePickerState, modifier = Modifier.padding(top = 32.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 32.dp, horizontal = 32.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    FilledIconToggleButton(
                        checked = effectRate == 0,
                        onCheckedChange = {
                            effectRate = 0
                        },
                        shape = CircleShape,
                        colors = IconButtonDefaults.iconToggleButtonColors(
                            checkedContainerColor = animateColorAsState(
                                targetValue = if (effectRate == 0) Color.LightGray else Color.Transparent
                            ).value
                        )
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_outline_sentiment_very_satisfied_24),
                            contentDescription = null,
                            tint = very_satisfied,
                            modifier = Modifier.size(38.dp)
                        )
                    }
                    FilledIconToggleButton(
                        checked = effectRate == 1,
                        onCheckedChange = {
                            effectRate = 1
                        },
                        shape = CircleShape,
                        colors = IconButtonDefaults.iconToggleButtonColors(
                            checkedContainerColor = animateColorAsState(
                                targetValue = if (effectRate == 1) Color.LightGray else Color.Transparent
                            ).value
                        )
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_outline_sentiment_satisfied_alt_24),
                            contentDescription = null,
                            tint = satisfied,
                            modifier = Modifier.size(38.dp)
                        )
                    }
                    FilledIconToggleButton(
                        checked = effectRate == 2,
                        onCheckedChange = {
                            effectRate = 2
                        },
                        shape = CircleShape,
                        colors = IconButtonDefaults.iconToggleButtonColors(
                            checkedContainerColor = animateColorAsState(
                                targetValue = if (effectRate == 2) Color.LightGray else Color.Transparent
                            ).value
                        )
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_outline_sentiment_neutral_24),
                            contentDescription = null,
                            tint = neutral,
                            modifier = Modifier.size(38.dp)
                        )
                    }
                    FilledIconToggleButton(
                        checked = effectRate == 3,
                        onCheckedChange = {
                            effectRate = 3
                        },
                        shape = CircleShape,
                        colors = IconButtonDefaults.iconToggleButtonColors(
                            checkedContainerColor = animateColorAsState(
                                targetValue = if (effectRate == 3) Color.LightGray else Color.Transparent
                            ).value
                        )
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_outline_sentiment_dissatisfied_24),
                            contentDescription = null,
                            tint = dissatisfied,
                            modifier = Modifier.size(38.dp)
                        )
                    }
                    FilledIconToggleButton(
                        checked = effectRate == 4,
                        onCheckedChange = {
                            effectRate = 4
                        },
                        shape = CircleShape,
                        colors = IconButtonDefaults.iconToggleButtonColors(
                            checkedContainerColor = animateColorAsState(
                                targetValue = if (effectRate == 4) Color.LightGray else Color.Transparent
                            ).value
                        )
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_outline_sentiment_very_dissatisfied_24),
                            contentDescription = null,
                            tint = very_dissatisfied,
                            modifier = Modifier.size(38.dp)
                        )
                    }
                }
            }
        }
    }
}