package com.nima.mymood.screens

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
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
import com.nima.mymood.navigation.Screens
import com.nima.mymood.utils.Calculate
import com.nima.mymood.viewmodels.HomeViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.*

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: HomeViewModel
) {

    val calendar = Calendar.getInstance()

    val year by remember {
        mutableStateOf(calendar.get(Calendar.YEAR))
    }
    val month by remember {
        mutableStateOf(calendar.get(Calendar.MONTH)+1)
    }
    val day by remember {
        mutableStateOf(calendar.get(Calendar.DAY_OF_MONTH))
    }

    val dayOfWeek by remember{
        mutableStateOf(calendar.get(Calendar.DAY_OF_WEEK))
    }
    
    val today = produceState<Day?>(initialValue = null){
        value = viewModel.getDayByDate(year, month, day)
    }.value

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        CenterAlignedTopAppBar(
            title = {
                Text(text = "${Calculate.calculateDayName(dayOfWeek)} " +
                        "${Calculate.calculateMonthName(month)} " +
                        "$day $year",
                    style = MaterialTheme.typography.bodyLarge
                )
            },
            actions = {
                IconButton(onClick = {
                    // go to moods menu
                }) {
                    Icon(imageVector = Icons.Default.ArrowForward, contentDescription = null)
                }
            }
        )
        when (today) {
            null -> {
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
                            .size(96.dp)
                            .padding(top = 16.dp, bottom = 10.dp),
                        tint = Color.LightGray
                    )
                    Text(text = "No Entry For Today!",
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.Light,
                        color = Color.LightGray,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    ElevatedButton(onClick = {
                        // today mood screen
                        val day = Day(
                            day = day,
                            month = month,
                            year = year,
                            overallMood = 2
                        )
                        runBlocking {
                            launch {
                                viewModel.addDay(day)
                            }
                        }
                        navController.navigate(Screens.TodayMoodScreen.name+"/${day.id.toString()}")
                    }) {
                        Text(text = "How Is Your Mood?")
                    }
                }
            }
            else -> {
                Text(text = "yes")
            }
        }
    }

}