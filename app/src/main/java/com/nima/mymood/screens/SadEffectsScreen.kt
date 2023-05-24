package com.nima.mymood.screens

import android.util.Log
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
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
import com.nima.mymood.viewmodels.SadEffectsViewModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

@Composable
fun SadEffectsScreen(
    navController: NavController,
    viewModel: SadEffectsViewModel
) {

    val sadEffects = viewModel.getSadMood().collectAsState(initial = emptyList())

    var deleteEffect by remember {
        mutableStateOf(false)
    }

    var effectToDelete: Effect? by remember {
        mutableStateOf(null)
    }

    var updateEffect by remember {
        mutableStateOf(false)
    }

    var newDescription by remember { mutableStateOf("") }

    var newRate by remember { mutableStateOf(2) }

    var effectToUpdate: Effect? by remember {
        mutableStateOf(null)
    }

    val scope = rememberCoroutineScope()

    if (sadEffects.value.isEmpty()){
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 64.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(painter = painterResource(id = R.drawable.ic_outline_sentiment_very_satisfied_24),
                contentDescription = null,
                tint = Color.LightGray,
                modifier = Modifier
                    .size(96.dp)
                    .padding(bottom = 8.dp)
            )
            Text(text = "No Sad Effects Yet!",
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.Light,
                color = Color.LightGray,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }
    }else{
        Column(
            modifier = Modifier.fillMaxSize(),
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
                        newDescription = ""
                        newRate = 2
                    },
                    text = {
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ){
                            TextField(
                                value = newDescription,
                                onValueChange = {
                                    newDescription = it
                                }
                            )

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 8.dp, horizontal = 12.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceAround
                            ) {
                                FilledIconToggleButton(checked = newRate == 0,
                                    onCheckedChange = {
                                        newRate = 0
                                    },
                                    shape = CircleShape,
                                    colors = IconButtonDefaults.iconToggleButtonColors(
                                        checkedContainerColor = animateColorAsState(
                                            targetValue = if (newRate == 0) Color.LightGray else Color.Transparent
                                        ).value
                                    )
                                ) {
                                    Icon(painter = painterResource(id = R.drawable.ic_outline_sentiment_very_satisfied_24),
                                        contentDescription = null,
                                        tint = very_satisfied
                                    )
                                }
                                FilledIconToggleButton(checked = newRate == 1,
                                    onCheckedChange = {
                                        newRate = 1
                                    },
                                    shape = CircleShape,
                                    colors = IconButtonDefaults.iconToggleButtonColors(
                                        checkedContainerColor = animateColorAsState(
                                            targetValue = if (newRate == 1) Color.LightGray else Color.Transparent
                                        ).value
                                    )
                                ) {
                                    Icon(painter = painterResource(id = R.drawable.ic_outline_sentiment_satisfied_alt_24),
                                        contentDescription = null,
                                        tint = satisfied
                                    )
                                }
                                FilledIconToggleButton(checked = newRate == 2,
                                    onCheckedChange = {
                                        newRate = 2
                                    },
                                    shape = CircleShape,
                                    colors = IconButtonDefaults.iconToggleButtonColors(
                                        checkedContainerColor = animateColorAsState(
                                            targetValue = if (newRate == 2) Color.LightGray else Color.Transparent
                                        ).value
                                    )
                                ) {
                                    Icon(painter = painterResource(id = R.drawable.ic_outline_sentiment_neutral_24),
                                        contentDescription = null,
                                        tint = neutral
                                    )
                                }
                                FilledIconToggleButton(checked = newRate == 3,
                                    onCheckedChange = {
                                        newRate = 3
                                    },
                                    shape = CircleShape,
                                    colors = IconButtonDefaults.iconToggleButtonColors(
                                        checkedContainerColor = animateColorAsState(
                                            targetValue = if (newRate == 3) Color.LightGray else Color.Transparent
                                        ).value
                                    )
                                ) {
                                    Icon(painter = painterResource(id = R.drawable.ic_outline_sentiment_dissatisfied_24),
                                        contentDescription = null,
                                        tint = dissatisfied
                                    )
                                }
                                FilledIconToggleButton(checked = newRate == 4,
                                    onCheckedChange = {
                                        newRate = 4
                                    },
                                    shape = CircleShape,
                                    colors = IconButtonDefaults.iconToggleButtonColors(
                                        checkedContainerColor = animateColorAsState(
                                            targetValue = if (newRate == 4) Color.LightGray else Color.Transparent
                                        ).value
                                    )
                                ) {
                                    Icon(painter = painterResource(id = R.drawable.ic_outline_sentiment_very_dissatisfied_24),
                                        contentDescription = null,
                                        tint = very_dissatisfied
                                    )
                                }
                            }
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = {
                            updateEffect = false
                            effectToUpdate = null
                            newDescription = ""
                            newRate = 2
                        }) {
                            Text(text = "Cancel")
                        }
                    },
                    confirmButton = {
                        TextButton(onClick = {
                            viewModel.updateEffect(effectToUpdate!!.copy(description = newDescription.takeIf { it.isNotBlank() } ?: effectToUpdate!!.description, rate = newRate)).invokeOnCompletion {
                                updateEffect = false
                                effectToUpdate = null
                                newDescription = ""
                                newRate = 2
                            }
                        }) {
                            Text(text = "Confirm")
                        }
                    },
                    icon = {
                        Icon(imageVector = Icons.Default.Edit, contentDescription = null)
                    },
                    title = {
                        Text(text = "Update Effect")
                    },
                )
            }
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(start = 32.dp, end = 32.dp, top = 16.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                items(items = sadEffects.value, key = {
                    it.id
                }) {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ){

                        var date: String? by remember {
                            mutableStateOf(null)
                        }

                        LaunchedEffect(key1 = Unit){
                            viewModel.getDayById(it.foreignKey).collectLatest {
                                date = "${it.day}/${it.month}/${it.year}"
                            }
                        }

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
    }
}