package com.nima.mymood.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.sharp.Info
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.nima.mymood.R
import com.nima.mymood.model.Day
import com.nima.mymood.model.Effect
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
    id: String?
) {

    val day = produceState<Day?>(initialValue = null){
        value = viewModel.getDayById(UUID.fromString(id!!))
    }.value

    var effectDescription by remember {
        mutableStateOf("")
    }

    var effectRate by remember {
        mutableStateOf(true)
    }



    val effectsList = viewModel.getDayEffects(UUID.fromString(id!!)).collectAsState(initial = emptyList())

    if (day != null){
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start,
            ) {
                Icon(imageVector = Icons.Outlined.Info, contentDescription = null)
                Text(text = "You Can Always Edit Today's Mood!",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Light,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }

            Text(text = "${Calculate.calculateMonthName(day.month)} " +
                    "${day.day} ${day.year}",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(16.dp)
            )

            Text(text = "How is your general mood today?",
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.Light,
                modifier = Modifier.padding(bottom = 5.dp)
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                OutlinedIconToggleButton(
                    checked = day.overallMood == 0,
                    onCheckedChange = {
                        day.overallMood = 0
                    },
                    colors = IconButtonDefaults.outlinedIconToggleButtonColors(
                        containerColor = Color.Transparent,
                        checkedContainerColor = Color.LightGray
                    ),
                    border = null
                ) {
                    Icon(painter = painterResource(id = R.drawable.ic_outline_sentiment_very_satisfied_24),
                        contentDescription = null,
                        tint = very_satisfied
                    )
                }

                OutlinedIconToggleButton(
                    checked = day.overallMood == 1,
                    onCheckedChange = {
                        day.overallMood = 1
                    },
                    colors = IconButtonDefaults.outlinedIconToggleButtonColors(
                        containerColor = Color.Transparent,
                        checkedContainerColor = Color.LightGray
                    ),
                    border = null
                ) {
                    Icon(painter = painterResource(id = R.drawable.ic_outline_sentiment_satisfied_alt_24),
                        contentDescription = null,
                        tint = satisfied
                    )
                }

                OutlinedIconToggleButton(
                    checked = day.overallMood == 2,
                    onCheckedChange = {
                        day.overallMood = 2
                    },
                    colors = IconButtonDefaults.outlinedIconToggleButtonColors(
                        containerColor = Color.Transparent,
                        checkedContainerColor = Color.LightGray
                    ),
                    border = null
                ) {
                    Icon(painter = painterResource(id = R.drawable.ic_outline_sentiment_neutral_24),
                        contentDescription = null,
                        tint = neutral

                    )
                }

                OutlinedIconToggleButton(
                    checked = day.overallMood == 3,
                    onCheckedChange = {
                        day.overallMood = 3
                    },
                    colors = IconButtonDefaults.outlinedIconToggleButtonColors(
                        containerColor = Color.Transparent,
                        checkedContainerColor = Color.LightGray
                    ),
                    border = null
                ) {
                    Icon(painter = painterResource(id = R.drawable.ic_outline_sentiment_dissatisfied_24),
                        contentDescription = null,
                        tint = dissatisfied

                    )
                }

                OutlinedIconToggleButton(
                    checked = day.overallMood == 4,
                    onCheckedChange = {
                        day.overallMood = 4
                    },
                    colors = IconButtonDefaults.outlinedIconToggleButtonColors(
                        containerColor = Color.Transparent,
                        checkedContainerColor = Color.LightGray
                    ),
                    border = null
                ) {
                    Icon(painter = painterResource(id = R.drawable.ic_outline_sentiment_very_dissatisfied_24),
                        contentDescription = null,
                        tint = very_dissatisfied
                    )
                }
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
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                singleLine = false
            )

            Checkbox(checked = effectRate, onCheckedChange = {
                effectRate = !effectRate
            },
            )
            IconButton(onClick = {
                val effect = Effect(foreignKey = UUID.fromString(id),
                    description = effectDescription,
                    rate = if (effectRate) 0 else 1
                )
                effectDescription = ""
                effectRate = false
                runBlocking {
                    launch {
                        viewModel.addEffect(effect)
                    }
                }
            },
                enabled = effectDescription.isNotBlank()
            ) {
                Icon(imageVector = Icons.Default.Done, contentDescription = null)
            }

            effectsList.value.forEach {
                Text(text = it.description)
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.End
            ) {
                TextButton(onClick = {
                    runBlocking {
                        launch {
                            viewModel.deleteDay(day)
                            viewModel.deleteDayEffects(UUID.fromString(id))
                        }
                    }
                    navController.popBackStack()
                }) {
                    Text(text = "Cancel")
                }

                TextButton(onClick = {
                    runBlocking {
                        launch {
                            viewModel.updateDay(day)
                        }
                    }
                    navController.popBackStack()
                }) {
                    Text(text = "Save")
                }
            }
        }
    }
}