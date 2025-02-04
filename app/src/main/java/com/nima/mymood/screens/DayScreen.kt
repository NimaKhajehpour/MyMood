package com.nima.mymood.screens

import android.annotation.SuppressLint
import android.util.Log
import android.widget.Toast
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.ExperimentalFoundationApi
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.nima.mymood.R
import com.nima.mymood.components.EffectsListItem
import com.nima.mymood.components.MoodAssistChip
import com.nima.mymood.model.Effect
import com.nima.mymood.navigation.Screens
import com.nima.mymood.ui.theme.*
import com.nima.mymood.utils.Calculate
import com.nima.mymood.viewmodels.DayViewModel
import kotlinx.coroutines.launch
import java.util.*

@OptIn(ExperimentalFoundationApi::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun DayScreen (
    navController: NavController,
    viewModel: DayViewModel,
    id: String?
) {

    val day = viewModel.getDayById(UUID.fromString(id)).collectAsState(initial = null).value
    val effects = viewModel.getDayEffects(UUID.fromString(id)).collectAsState(initial = emptyList()).value

    var deleteEffect by remember {
        mutableStateOf(false)
    }

    var effectToDelete: Effect? by remember {
        mutableStateOf(null)
    }

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
                        navController.navigate(Screens.TodayMoodScreen.name + "/${day!!.id}")
                    },
                    containerColor = MaterialTheme.colorScheme.secondaryContainer,
                    contentColor = MaterialTheme.colorScheme.onSecondaryContainer,

                    ) {
                    Icon(Icons.Default.Add, contentDescription = null)
                }
            }
        ) {
            if (effects.isNotEmpty()) {
                LazyColumn(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    contentPadding = PaddingValues(vertical = 16.dp, horizontal = 32.dp)
                ) {
                    stickyHeader {
                        MoodAssistChip(
                            day = day!!.day,
                            month = day.month,
                            year = day.year
                        )
                    }
                    items(items = effects, key = {
                        it.id
                    }) {
                        EffectsListItem(
                            it.rate,
                            it.description,
                            effectHour = it.hour,
                            effectMinute = it.minute,
                            effectDate = String.format("%02d/%02d/%4d", day!!.day, day!!.month, day!!.year),
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
                        text = "Nothing to see yet!",
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