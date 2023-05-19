package com.nima.mymood.screens

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
                    },
                    text = {
                        TextField(
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
                                newDescription = ""
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
        }
    }
}