package com.nima.mymood.screens

import android.util.Log
import androidx.compose.foundation.gestures.FlingBehavior
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.materialIcon
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.nima.mymood.R
import com.nima.mymood.components.DaySelectItem
import com.nima.mymood.graph.DataPoint
import com.nima.mymood.graph.GraphLine
import com.nima.mymood.graph.LineGraph
import com.nima.mymood.model.Effect
import com.nima.mymood.navigation.Screens
import com.nima.mymood.utils.Calculate
import com.nima.mymood.viewmodels.DayCompareViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.UUID
import kotlin.random.Random

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DayCompareScreen(
    navController: NavController,
    viewModel: DayCompareViewModel
){

    val allDays = viewModel.getAllDays().collectAsState(initial = null).value

    val clipboard = LocalClipboardManager.current

    val scope = rememberCoroutineScope()
    val bottomSheetState = rememberStandardBottomSheetState(
        initialValue = SheetValue.Hidden,
        confirmValueChange = {
            it != SheetValue.PartiallyExpanded
        },
        skipHiddenState = false
    )
    val scaffoldState = rememberBottomSheetScaffoldState(bottomSheetState = bottomSheetState)

    val selectedDays = remember {
        mutableStateListOf<UUID>()
    }

    val days = remember {
        mutableStateMapOf<UUID, List<Effect>>()
    }

    val pointsList = remember {
        mutableStateOf(emptyList<DataPoint>())
    }

    if (!allDays.isNullOrEmpty()){
        BottomSheetScaffold(
            sheetContent = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState()),
//                    contentPadding = PaddingValues(vertical = 5.dp, horizontal = 16.dp),
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.CenterHorizontally,
                ){
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.End
                    ) {
                        IconButton(onClick = {
                            scope.launch {
                                scaffoldState.bottomSheetState.hide()
                            }
                        }) {
                            Icon(imageVector = Icons.Default.Close, contentDescription = null)
                        }
                    }
                    allDays.forEach{day ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
//                                .padding(vertical = 5.dp, horizontal = 16.dp)
                                .selectable(
                                    selected = selectedDays.contains(day.id),
                                    role = Role.Checkbox
                                ) {
                                    if (selectedDays.contains(day.id)) {
                                        selectedDays.remove(day.id)
                                        days.remove(day.id)
                                    } else {

                                        selectedDays.add(day.id)
                                        scope.launch {
                                            viewModel
                                                .getEffectsBuFK(day.id)
                                                .collectLatest {
                                                    days[day.id] = it
                                                }
                                        }
                                    }
                                },
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceAround
                        ) {
                            Checkbox(checked = selectedDays.contains(day.id), onCheckedChange = {
                                if (selectedDays.contains(day.id)) {
                                    selectedDays.remove(day.id)
                                    days.remove(day.id)
                                } else {
                                    selectedDays.add(day.id)
                                    scope.launch {
                                        viewModel
                                            .getEffectsBuFK(day.id)
                                            .collectLatest {
                                                days[day.id] = it
                                            }
                                    }
                                }
                            })
                            DaySelectItem(date = "${Calculate.calculateMonthName(day.month)} ${day.day } ${day.year}")
                        }
                    }
                }
            },
            modifier = Modifier.fillMaxSize(),
            scaffoldState = scaffoldState,
            sheetShape = RoundedCornerShape(15.dp),
            sheetSwipeEnabled = true
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Button(onClick = {
                    scope.launch {
                        scaffoldState.bottomSheetState.expand()
                    }
                },
                    modifier = Modifier.padding(vertical = 8.dp, horizontal = 16.dp)
                    ) {
                    Text(text = "Select Days")
                }

                if (!days.filter { it.value.isNotEmpty() }.isNullOrEmpty()){
                    LineGraph(graphLine =
                    GraphLine(
                        lines = days.map {
                            GraphLine.Line(
                                it.value.mapIndexed { index, item ->
                                    DataPoint(index + 1f, item.rate.toFloat())
                                },
                                GraphLine.GraphConnection(
                                    MaterialTheme.colorScheme.primary,
                                    2.dp
                                ),
                                GraphLine.GraphIntersection(
                                    MaterialTheme.colorScheme.primary,
                                    5.dp
                                ),
                                highlight = GraphLine.GraphHighlight(
                                    MaterialTheme.colorScheme.tertiary,
                                    7.dp
                                ),
                            )
                        },
                        selection = GraphLine.GraphSelection(
                            highlight = GraphLine.GraphConnection(
                                MaterialTheme.colorScheme.primary,
                                strokeWidth = 2.dp,
                            )
                        ),
                        paddingRight = 16.dp,
                        yAxis = GraphLine.YAxis(
                            steps = 5,
                            content = {min, offset, _ ->
                                for (step in 0 until 5){
                                    val value = step * offset + min
                                    Calculate.calculateIconWithRate(rate = value.toInt())
                                }
                            }),
                    ),

                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp),
                        onSelection = { _, points ->
                            pointsList.value = points
                        }
                    )

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp, bottom = 16.dp, start = 8.dp, end = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Text(text = "Zoom",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.ExtraLight
                        )
                        Text(text = "Pan",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.ExtraLight
                        )
                        Text(text = "* Double Tap and drag for selection",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.ExtraLight
                        )
                    }
                }

                if (pointsList.value.isNotEmpty()){

//                    val effect = effects.value[pointsList.value[0].x.toInt()-1]
                    val effects = remember(pointsList.value) {
                        mutableStateOf(days.values.filter {
                            it.size >= pointsList.value[0].x.toInt()
                        }.map { effects ->
                            effects[pointsList.value[0].x.toInt()-1]
                        })
                    }

                    effects.value.forEachIndexed { index, effect ->
                        val day = allDays.filter {
                            it.id == days.filter {
                                it.value.contains(effect)
                            }.keys.toList()[0]
                        }.first()
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
                            Text(text = "${Calculate.calculateMonthName(day.month)} ${day.day} ${day.year}",
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
    }
}