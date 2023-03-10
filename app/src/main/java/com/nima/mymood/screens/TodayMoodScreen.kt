package com.nima.mymood.screens

import android.util.Log
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.AbsoluteRoundedCornerShape
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.nima.mymood.components.EffectsListItem
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
    id: String?,
) {

    val day = viewModel.getDayById(UUID.fromString(id)).collectAsState(initial = null)

    var effectDescription by remember {
        mutableStateOf("")
    }

    var effectRate by remember {
        mutableStateOf(2)
    }

    val effectsList = viewModel.getDayEffects(UUID.fromString(id!!)).collectAsState(initial = emptyList())

    if (day.value != null){

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(text = "${Calculate.calculateMonthName(day.value!!.month)} " +
                    "${day.value!!.day} ${day.value!!.year}",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(16.dp)
            )

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
                            text = "What effected your mood today?",
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

                    Button(onClick = {
                        val effect = Effect(foreignKey = UUID.fromString(id),
                            description = effectDescription.trim(),
                            rate = effectRate
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

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 16.dp, start = 32.dp, top = 8.dp, bottom = 10.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.Start
            ) {
                effectsList.value.forEach {
                    EffectsListItem(
                        effectRate = it.rate,
                        effectDescription = it.description
                    )
                }
            }

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