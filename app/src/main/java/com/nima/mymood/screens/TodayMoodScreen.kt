package com.nima.mymood.screens

import android.widget.Toast
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
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
import androidx.navigation.NavController
import com.nima.mymood.R
import com.nima.mymood.ThemeDataStore
import com.nima.mymood.components.EffectsListItem
import com.nima.mymood.model.Effect
import com.nima.mymood.navigation.Screens
import com.nima.mymood.ui.theme.*
import com.nima.mymood.utils.Calculate
import com.nima.mymood.viewmodels.TodayMoodViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TodayMoodScreen(
    navController: NavController,
    viewModel: TodayMoodViewModel,
    id: String?,
) {

    val day = viewModel.getDayById(UUID.fromString(id)).collectAsState(initial = null)

    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    val appDataStore = ThemeDataStore(context)
    val autoRate = appDataStore.getAutoRate.collectAsState(initial = false).value

    val clipboard = LocalClipboardManager.current

    var effectDescription by remember {
        mutableStateOf("")
    }

    var effectRate by remember {
        mutableStateOf(2)
    }

    val timePickerState = rememberTimePickerState()

    var deleteEffect by remember {
        mutableStateOf(false)
    }

    var effectToDelete: Effect? by remember {
        mutableStateOf(null)
    }

    var updateDay by remember {
        mutableStateOf(false)
    }

    var dayRate by remember{
        mutableStateOf(2f)
    }
    var dayRed by remember{
        mutableStateOf(150f)
    }
    var dayGreen by remember{
        mutableStateOf(150f)
    }
    var dayBlue by remember{
        mutableStateOf(150f)
    }

    val effectsList = viewModel.getDayEffects(UUID.fromString(id!!)).collectAsState(initial = emptyList())

    if (day.value != null){

        Column(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

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
                                deleteEffect = false
                                effectToDelete = null
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

            if (updateDay){
                AlertDialog(
                    onDismissRequest = {
                        updateDay = false
                },
                    text = {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .verticalScroll(rememberScrollState()),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(painter = painterResource(id =
                                when(dayRate.toInt()){
                                    0 -> R.drawable.ic_outline_sentiment_very_satisfied_24
                                    1 -> R.drawable.ic_outline_sentiment_satisfied_alt_24
                                    2 -> R.drawable.ic_outline_sentiment_neutral_24
                                    3 -> R.drawable.ic_outline_sentiment_dissatisfied_24
                                    else -> R.drawable.ic_outline_sentiment_very_dissatisfied_24
                                }
                            ), contentDescription = null,
                                modifier = Modifier
                                    .padding(top = 5.dp, bottom = 10.dp)
                                    .size(64.dp),
                                tint = Color(red = dayRed.toInt(), green = dayGreen.toInt(), blue = dayBlue.toInt())
                            )

                            Text(text = "Rate")

                            Slider(
                                value = dayRate,
                                onValueChange = {
                                    dayRate = it
                            },
                                modifier = Modifier.padding(vertical = 5.dp),
                                valueRange = 0f..4f,
                                steps = 3
                            )

                            Text(text = "Red")

                            Slider(
                                value = dayRed,
                                onValueChange = {
                                    dayRed = it
                                },
                                modifier = Modifier.padding(vertical = 5.dp),
                                valueRange = 0f..255f,
                            )

                            Text(text = "Green")

                            Slider(
                                value = dayGreen,
                                onValueChange = {
                                    dayGreen = it
                                },
                                modifier = Modifier.padding(vertical = 5.dp),
                                valueRange = 0f..255f,
                            )

                            Text(text = "Blue")

                            Slider(
                                value = dayBlue,
                                onValueChange = {
                                    dayBlue = it
                                },
                                modifier = Modifier.padding(vertical = 5.dp),
                                valueRange = 0f..255f,
                            )
                        }
                    },
                    title = {
                        Text(text = "Update Day Rate")
                    },
                    dismissButton = {
                        TextButton(onClick = {
                            updateDay = false
                        }) {
                            Text(text = "Cancel")
                        }
                    },
                    confirmButton = {
                        TextButton(onClick = {
                            viewModel.updateDay(day.value!!.copy(
                                rate = dayRate.toInt().toString(),
                                red = dayRed.toInt().toString(),
                                green = dayGreen.toInt().toString(),
                                blue = dayBlue.toInt().toString(),
                            ),
                            )
                            updateDay = false
                        }) {
                            Text(text = "Confirm")
                        }
                    }
                )
            }

            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                item {
                    if (autoRate == false){
                        if (day.value!!.rate.isNotBlank()) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 8.dp),
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Icon(
                                    painter = painterResource(
                                        id =
                                        when (day.value!!.rate.toInt()) {
                                            0 -> R.drawable.ic_outline_sentiment_very_satisfied_24
                                            1 -> R.drawable.ic_outline_sentiment_satisfied_alt_24
                                            2 -> R.drawable.ic_outline_sentiment_neutral_24
                                            3 -> R.drawable.ic_outline_sentiment_dissatisfied_24
                                            else -> R.drawable.ic_outline_sentiment_very_dissatisfied_24
                                        }
                                    ),
                                    contentDescription = null,
                                    tint = Color(
                                        red = day.value!!.red.toInt(),
                                        green = day.value!!.green.toInt(),
                                        blue = day.value!!.blue.toInt(),
                                    ),
                                    modifier = Modifier
                                        .size(120.dp)
                                        .padding(bottom = 8.dp)
                                )

                                Button(onClick = {
                                    updateDay = true
                                }) {
                                    Text("Edit Day Overall Rate")
                                }
                            }

                        } else {
                            Button(
                                onClick = {
                                    updateDay = true
                                },
                                modifier = Modifier.padding(top = 8.dp)
                            ) {
                                Text(text = "Select an Overall Mood")
                            }
                        }
                    }else{
                        val avg = if (effectsList.value.isNotEmpty()){
                            effectsList.value.sumOf {
                                it.rate
                            }/effectsList.value.size
                        } else 0

                        Calculate.calculateIconWithRate(rate = avg, size = 120.dp)
                    }
                }

                item {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Checkbox(checked = autoRate == true, onCheckedChange = {
                            scope.launch {
                                appDataStore.saveAutoRate(!autoRate!!)
                            }
                        })
                        Text(text = "Auto Rate Days?")
                    }
                }

                item {
                    Text(text = "${Calculate.calculateMonthName(day.value!!.month)} " +
                            "${day.value!!.day} ${day.value!!.year}",
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(16.dp)
                    )
                }

                item{
                    ElevatedCard(
                        shape = RoundedCornerShape(5.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 12.dp),
                        elevation = CardDefaults.elevatedCardElevation(10.dp)
                    ){
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(0.dp),
                            verticalArrangement = Arrangement.Top,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ){
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 8.dp, horizontal = 12.dp)
                                ,
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Start
                            ) {
                                Text(
                                    text = "What affected your mood today?",
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.Light
                                )
                            }

                            OutlinedTextField(value = effectDescription,
                                onValueChange = {
                                    effectDescription = it
                                },
                                label = {
                                    Text(text = "Effect")
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 5.dp, horizontal = 12.dp),
                                singleLine = false
                            )

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 8.dp, horizontal = 12.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceAround
                            ) {
                                FilledIconToggleButton(checked = effectRate == 0,
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
                                    Icon(painter = painterResource(id = R.drawable.ic_outline_sentiment_very_satisfied_24),
                                        contentDescription = null,
                                        tint = very_satisfied
                                    )
                                }
                                FilledIconToggleButton(checked = effectRate == 1,
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
                                    Icon(painter = painterResource(id = R.drawable.ic_outline_sentiment_satisfied_alt_24),
                                        contentDescription = null,
                                        tint = satisfied
                                    )
                                }
                                FilledIconToggleButton(checked = effectRate == 2,
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
                                    Icon(painter = painterResource(id = R.drawable.ic_outline_sentiment_neutral_24),
                                        contentDescription = null,
                                        tint = neutral
                                    )
                                }
                                FilledIconToggleButton(checked = effectRate == 3,
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
                                    Icon(painter = painterResource(id = R.drawable.ic_outline_sentiment_dissatisfied_24),
                                        contentDescription = null,
                                        tint = dissatisfied
                                    )
                                }
                                FilledIconToggleButton(checked = effectRate == 4,
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
                                    Icon(painter = painterResource(id = R.drawable.ic_outline_sentiment_very_dissatisfied_24),
                                        contentDescription = null,
                                        tint = very_dissatisfied
                                    )
                                }
                            }

                            TimeInput(state = timePickerState, modifier = Modifier.padding(top = 8.dp) )

                            Button(onClick = {
                                val effect = Effect(foreignKey = UUID.fromString(id),
                                    description = effectDescription.trim(),
                                    rate = effectRate,
                                    hour = timePickerState.hour.toString(),
                                    minute = timePickerState.minute.toString()
                                )
                                effectDescription = ""
                                effectRate = 2
                                runBlocking {
                                    launch {
                                        viewModel.addEffect(effect)
                                    }
                                }
                            },
                                enabled = effectDescription.isNotBlank(),
                                shape = RoundedCornerShape(
                                    5.dp
                                ),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 8.dp, horizontal = 12.dp)
                            ) {
                                Text(text = "Add Effect")
                            }
                        }
                    }
                }

                item{
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 12.dp, horizontal = 16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Start
                    ) {
                        Text(text = "Today's Effects",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Light
                        )
                    }
                }

                items(items = effectsList.value, key = {
                    it.id
                }) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(end = 16.dp, start = 32.dp, top = 4.dp, bottom = 4.dp),
                        verticalAlignment = Alignment.Top,
                        horizontalArrangement = Arrangement.Start
                    ) {
                        EffectsListItem(
                            it.rate,
                            it.description,
                            effectHour = it.hour,
                            effectMinute = it.minute,
                            onLongPress = {
                                effectToDelete = it
                                deleteEffect = true
                            },
                            onDoubleTap = {
                                navController.navigate(Screens.EditScreen.name+"/${it.id}")
                            },
                            onCopyClicked = {
                                clipboard.setText(AnnotatedString(it.description))
                                Toast.makeText(context, "Description Copied!", Toast.LENGTH_LONG).show()
                            }
                        )
                    }
                }

                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 10.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.End
                    ) {

                        TextButton(onClick = {
                            navController.popBackStack()
                        }) {
                            Text(text = "Save")
                        }
                    }
                }
            }
        }
    }
}