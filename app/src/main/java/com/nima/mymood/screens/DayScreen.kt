package com.nima.mymood.screens

import android.util.Log
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
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.nima.mymood.R
import com.nima.mymood.components.EffectsListItem
import com.nima.mymood.model.Effect
import com.nima.mymood.navigation.Screens
import com.nima.mymood.ui.theme.*
import com.nima.mymood.utils.Calculate
import com.nima.mymood.viewmodels.DayViewModel
import java.util.*

@Composable
fun DayScreen (
    navController: NavController,
    viewModel: DayViewModel,
    id: String?
) {

    val day = viewModel.getDayById(UUID.fromString(id)).collectAsState(initial = null)
    val effects = viewModel.getDayEffects(UUID.fromString(id)).collectAsState(initial = emptyList())

    var deleteEffect by remember {
        mutableStateOf(false)
    }

    val context = LocalContext.current

    val clipboard = LocalClipboardManager.current

    var effectToDelete: Effect? by remember {
        mutableStateOf(null)
    }

    var showDeleteDay by remember {
        mutableStateOf(false)
    }

    var updateEffect by remember {
        mutableStateOf(false)
    }

    var effectToUpdate: Effect? by remember {
        mutableStateOf(null)
    }

    var newDescription by remember { mutableStateOf("") }

     var newRate by remember { mutableStateOf(2) }


    if (day.value != null){
        Column(
            modifier = Modifier.fillMaxSize(),
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
                            modifier = Modifier
                                .fillMaxWidth()
                                .verticalScroll(rememberScrollState()),
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


            if (showDeleteDay){

                AlertDialog(onDismissRequest = {
                    showDeleteDay = false
                },
                    icon = {
                        Icon(imageVector = Icons.Default.Delete, contentDescription = null)
                    },
                    title = {
                        Text(text = "Delete This Day?")
                    },
                    text = {
                        Text(text = "Seems like this day does not have any entries for effects. You can delete this day if you want," +
                                " remember you can add this day with effects anytime you want." +
                                "\nDo you want to delete this day?")
                    },
                    confirmButton = {
                        TextButton(onClick = {
                            viewModel.deleteDay(day.value!!).invokeOnCompletion {
                                navController.popBackStack()
                            }
                        },
                            colors = ButtonDefaults.textButtonColors(
                                contentColor = MaterialTheme.colorScheme.error
                            )
                        ) {
                            Text(text = "Delete Day")
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = {
                            showDeleteDay = false
                        }) {
                            Text(text = "Cancel")
                        }
                    }
                )
            }

            Text(text = "${Calculate.calculateMonthName(day.value!!.month)} " +
                    "${day.value!!.day} ${day.value!!.year}",
                modifier = Modifier.padding(vertical = 16.dp)
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp, horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.End
            ) {

                if (effects.value.isEmpty()){
                    ElevatedButton(
                        onClick = {
                            showDeleteDay = true
                        },
                        shape = RoundedCornerShape(5.dp),
                        elevation = ButtonDefaults.elevatedButtonElevation(15.dp)
                    ) {
                        Icon(imageVector = Icons.Outlined.Delete, contentDescription = null)
                    }
                }

                Spacer(modifier = Modifier.weight(1f))

                ElevatedButton(onClick = {
                    // go to edit
                    navController.navigate(Screens.TodayMoodScreen.name+"/${id!!}")
                },
                    shape = RoundedCornerShape(5.dp),
                    elevation = ButtonDefaults.elevatedButtonElevation(15.dp)
                ) {
                    Icon(imageVector = Icons.Outlined.Edit, contentDescription = null)
                }
            }

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(vertical = 8.dp, horizontal = 16.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                items(items = effects.value, key ={
                    it.id
                }){
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
                            effectToUpdate = it
                            updateEffect = true
                        },
                        onCopyClicked = {
                            clipboard.setText(AnnotatedString(it.description))
                            Toast.makeText(context, "Description Copied!", Toast.LENGTH_LONG).show()
                        }
                    )
                }
            }
        }
    }
}