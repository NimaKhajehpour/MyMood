package com.nima.mymood.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.outlined.Edit
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
import com.nima.mymood.navigation.Screens
import com.nima.mymood.ui.theme.*
import com.nima.mymood.utils.Calculate
import com.nima.mymood.viewmodels.HomeViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.*

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
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
            .fillMaxSize(),
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
                    navController.navigate(Screens.MenuScreen.name)
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
                        val tempDay = Day(
                            day = day,
                            month = month,
                            year = year,
                        )
                        runBlocking {
                            launch {
                                viewModel.addDay(tempDay)
                            }
                        }
                        navController.navigate(
                            Screens.TodayMoodScreen.name+"/${tempDay.id.toString()}")
                    }) {
                        Text(text = "How Is Your Mood?")
                    }
                }
            }
            else -> {

                val effects = viewModel.getDayEffect(today.id).collectAsState(initial = emptyList())

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp, start = 16.dp, end = 16.dp, bottom = 32.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.End
                ) {
                    ElevatedButton(onClick = {
                        // go to edit
                        navController.navigate(Screens.TodayMoodScreen.name+"/${today.id.toString()}")
                    },
                        shape = RoundedCornerShape(5.dp),
                    ) {
                        Icon(imageVector = Icons.Outlined.Edit, contentDescription = null)
                    }
                }

                if (effects.value.isNotEmpty()){
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 16.dp, end = 16.dp, bottom = 16.dp),
                        verticalArrangement = Arrangement.Top,
                        horizontalAlignment = Alignment.Start,
                    ) {
                        items(items = effects.value) {
                            EffectsListItem(
                                it.rate,
                                it.description
                            )
                        }
                    }
                }

                else{
                    Text(text = "Nothing Here Yet!",
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Light,
                        color = Color.LightGray
                    )
                }
            }
        }
    }

}