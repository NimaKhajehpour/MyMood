package com.nima.mymood.screens

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DisplayMode
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.nima.mymood.R
import com.nima.mymood.model.Day
import com.nima.mymood.model.Effect
import com.nima.mymood.navigation.Screens
import com.nima.mymood.utils.Calculate
import com.nima.mymood.viewmodels.CalendarOverViewViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.produceIn
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.Date
import java.util.stream.Collector
import java.util.stream.Collectors

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DaysCalendarOverView(
    navController: NavController,
    viewModel: CalendarOverViewViewModel
) {

    BackHandler {
        navController.popBackStack()
    }

    val calendar = Calendar.getInstance()
    val clipboard = LocalClipboardManager.current

    var date: Date? by remember {
        mutableStateOf(null)
    }
    var selectedDay: Int? by remember(date) {
        mutableStateOf(date?.date)
    }
    var selectedMonth: Int? by remember(date) {
        mutableStateOf(date?.month?.plus(1))
    }
    var selectedYear: Int? by remember(date) {
        mutableStateOf(date?.year?.plus(1900))
    }

    var day: Day? by remember {
        mutableStateOf(null)
    }

    var effects by remember {
        mutableStateOf<List<Effect>?>(null)
    }

    val datePicker = rememberDatePickerState(initialDisplayMode = DisplayMode.Picker)

    LaunchedEffect(key1 = datePicker.selectedDateMillis){
        if (datePicker.selectedDateMillis != null){
            date = Date(datePicker.selectedDateMillis!!)
        }else{
            date = null
            selectedDay = null
            selectedMonth = null
            selectedYear = null
        }
    }

    LaunchedEffect(key1 = selectedDay){
        launch {
            if (selectedDay != null){
                Log.d("LOL", "DaysCalendarOverView: $selectedDay, $selectedYear, $selectedMonth")
                day = viewModel.getDay(year = selectedYear!!, month = selectedMonth!!, day = selectedDay!!)
            }else{
                day = null
            }
        }
    }

    LaunchedEffect(key1 = day){
        launch {
            if (day != null){
                val fk = day!!.id
                effects = viewModel.getEffects(fk).produceIn(this).receive()
            }else{
                effects = null
            }
        }
    }

    val scope = rememberCoroutineScope()
    val bottomSheetState = rememberStandardBottomSheetState(
        initialValue = SheetValue.Hidden,
        confirmValueChange = {
            it != SheetValue.PartiallyExpanded
        },
        skipHiddenState = false
    )
    val scaffoldState = rememberBottomSheetScaffoldState(bottomSheetState = bottomSheetState)

    LaunchedEffect(key1 = effects){
        launch {
            if (effects != null){
                scaffoldState.bottomSheetState.expand()
            }else{
                scaffoldState.bottomSheetState.hide()
            }
        }
    }

    BottomSheetScaffold(
        sheetContent = {
            if (!effects.isNullOrEmpty()){
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.End
                    ) {
                        IconButton(
                            onClick = {
                                scope.launch {
                                    scaffoldState.bottomSheetState.hide()
                                    datePicker.setSelection(null)
                                }
                            },
                            modifier = Modifier.padding(end = 8.dp)
                        ) {
                            Icon(imageVector = Icons.Default.Close, contentDescription = null)
                        }
                    }
                    effects!!.forEach {effect ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp, horizontal = 16.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.End
                        ) {
                            IconButton(onClick = {
                                navController.navigate(Screens.EditScreen.name+"/${effect.id}")
                            },
                                modifier = Modifier.padding(end = 8.dp)
                            ) {
                                Icon(imageVector = Icons.Default.Edit, contentDescription = null,
                                    tint = MaterialTheme.colorScheme.tertiary
                                )
                            }
                            OutlinedButton(onClick = {
                                clipboard.setText(AnnotatedString(effect.description))
                            },
                                shape = RoundedCornerShape(5.dp)
                            ) {
                                Icon(painter = painterResource(id = R.drawable.ic_baseline_content_copy_24),
                                    contentDescription = null,
                                    modifier = Modifier.padding(end = 5.dp)
                                )
                                Text("Copy Description")
                            }
                        }

                        ElevatedCard(
                            shape = RoundedCornerShape(15.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp, horizontal = 32.dp)
                        ){
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(8.dp),
                                verticalArrangement = Arrangement.Top,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Calculate.calculateIconWithRate(rate = effect.rate, size = 64.dp)

                                SelectionContainer {
                                    Text(
                                        text = effect.description,
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.tertiary,
                                        textAlign = TextAlign.Center,
                                        modifier = Modifier.padding(vertical = 8.dp, horizontal = 16.dp)
                                    )
                                }

                                if (effect.hour.isNotBlank()){
                                    Text(
                                        text = "Time: ${
                                            String.format(
                                                "%02d:%02d",
                                                effect.hour.toInt(),
                                                effect.minute.toInt()
                                            )
                                        }",
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.tertiary,
                                        textAlign = TextAlign.Center,
                                        modifier = Modifier.padding(vertical = 8.dp, horizontal = 16.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        },
        modifier = Modifier.fillMaxSize(),
        scaffoldState = scaffoldState,
        sheetShape = RoundedCornerShape(15.dp),
        sheetSwipeEnabled = false,
        sheetDragHandle = null
    ) {
        DatePicker(state = datePicker,
            dateValidator = {
                it <= calendar.timeInMillis
            },
            showModeToggle = false,
            title = null,
            modifier = Modifier.padding(vertical = 32.dp)
        )
    }

}