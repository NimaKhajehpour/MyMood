package com.nima.mymood.screens

import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.nima.mymood.R
import com.nima.mymood.ui.theme.*
import com.nima.mymood.viewmodels.EditViewModel
import java.util.*

@OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class)
@Composable
fun EditScreen(
    navController: NavController,
    id: String?,
    viewModel: EditViewModel
){

    val effect = viewModel.getEffect(UUID.fromString(id!!)).collectAsState(initial = null)

    var editDescription by remember {
        mutableStateOf(false)
    }

    var editRate by remember {
        mutableStateOf(false)
    }

    var editTime by remember {
        mutableStateOf(false)
    }

    if (effect.value != null){
        var effectDescription by remember {
            mutableStateOf(effect.value!!.description)
        }

        var effectRate by remember {
            mutableStateOf(effect.value!!.rate)
        }
        val timePicker = rememberTimePickerState(initialHour = effect.value!!.hour.toInt(), initialMinute = effect.value!!.minute.toInt())
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
                    .selectable(
                        selected = editDescription,
                        enabled = true,
                        role = Role.Checkbox,
                        onClick = {
                            editDescription = !editDescription
                        }),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                Checkbox(
                    checked = editDescription,
                    onCheckedChange = {
                        editDescription = it
                    },
                    modifier = Modifier.padding(end = 5.dp, start = 32.dp, bottom = 8.dp),
                )

                Text(text = "Edit Description")
            }
            OutlinedTextField(
                value = effectDescription, onValueChange = {
                    effectDescription = it
                },
                enabled = editDescription,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp, vertical = 8.dp),
                shape = RoundedCornerShape(15.dp)
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
                    .selectable(
                        selected = editTime,
                        enabled = true,
                        role = Role.Checkbox,
                        onClick = {
                            editTime = !editTime
                        }),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                Checkbox(
                    checked = editTime,
                    onCheckedChange = {
                        editTime = it
                    },
                    modifier = Modifier.padding(end = 5.dp, start = 32.dp, bottom = 8.dp),
                )

                Text(text = "Edit Time")
            }
            AnimatedVisibility(visible = editTime, enter = fadeIn()+ slideInHorizontally(), exit = fadeOut()+ slideOutHorizontally()) {
                TimeInput(state = timePicker, modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp, horizontal = 32.dp)
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
                    .selectable(
                        selected = editRate,
                        enabled = true,
                        role = Role.Checkbox,
                        onClick = {
                            editRate = !editRate
                        }),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                Checkbox(
                    checked = editRate,
                    onCheckedChange = {
                        editRate = it
                    },
                    modifier = Modifier.padding(end = 5.dp, start = 32.dp, bottom = 8.dp),
                )

                Text(text = "Edit Rate")
            }
            AnimatedVisibility(visible = editRate, enter = fadeIn()+ slideInHorizontally(), exit = fadeOut()+ slideOutHorizontally()) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp, horizontal = 32.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Column(
                        modifier = Modifier.padding(end = 32.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        IconButton(onClick = {
                            effectRate --
                        },
                            enabled = effectRate != 0,
                        ) {
                            Icon(
                                imageVector = Icons.Default.KeyboardArrowUp,
                                contentDescription = null
                            )
                        }
                        IconButton(onClick = {
                            effectRate ++
                        },
                            enabled = effectRate != 4,
                        ) {
                            Icon(
                                imageVector = Icons.Default.KeyboardArrowDown,
                                contentDescription = null
                            )
                        }
                    }
                    AnimatedContent(targetState = effectRate) {
                        when (it){
                            0 -> Icon(painter = painterResource(id = R.drawable.ic_outline_sentiment_very_satisfied_24), contentDescription = null,
                                modifier = Modifier.size(128.dp),
                                tint = very_satisfied
                            )
                            1 -> Icon(painter = painterResource(id = R.drawable.ic_outline_sentiment_satisfied_alt_24), contentDescription = null,
                                modifier = Modifier.size(128.dp),
                                tint = satisfied
                            )
                            2 -> Icon(painter = painterResource(id = R.drawable.ic_outline_sentiment_neutral_24), contentDescription = null,
                                modifier = Modifier.size(128.dp),
                                tint = neutral)
                            3 -> Icon(painter = painterResource(id = R.drawable.ic_outline_sentiment_dissatisfied_24), contentDescription = null,
                                modifier = Modifier.size(128.dp),
                                tint = dissatisfied)
                            4 -> Icon(painter = painterResource(id = R.drawable.ic_outline_sentiment_very_dissatisfied_24), contentDescription = null,
                                modifier = Modifier.size(128.dp),
                                tint = very_dissatisfied)
                        }
                    }
                }
            }

            Button(onClick = {
                val effectToUpdate = effect.value!!.copy(
                    description = if (editDescription) effectDescription.trim() else effect.value!!.description,
                    rate = if (editRate) effectRate else effect.value!!.rate,
                    hour = if (editTime) timePicker.hour.toString() else effect.value!!.hour,
                    minute = if (editTime) timePicker.minute.toString() else effect.value!!.minute
                )
                viewModel.updateEffect(effectToUpdate){
                    navController.popBackStack()
                }
            },
                shape = RoundedCornerShape(15.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp, horizontal = 32.dp),
            ) {
                Text(text = "Update Effect")
            }
        }
    }
}