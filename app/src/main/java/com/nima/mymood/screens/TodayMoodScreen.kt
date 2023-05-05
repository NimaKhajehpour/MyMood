package com.nima.mymood.screens

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
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

    var deleteEffect by remember {
        mutableStateOf(false)
    }

    var effectToDelete: Effect? by remember {
        mutableStateOf(null)
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

            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ){
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
                            effectRate = it.rate,
                            effectDescription = it.description
                        ){
                            effectToDelete = it
                            deleteEffect = true
                        }
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