package com.nima.mymood.screens

import android.annotation.SuppressLint
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
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
import androidx.navigation.NavController
import com.nima.mymood.R
import com.nima.mymood.components.DayLazyItem
import com.nima.mymood.components.EffectsListItem
import com.nima.mymood.graph.DataPoint
import com.nima.mymood.graph.GraphLine
import com.nima.mymood.graph.LineGraph
import com.nima.mymood.model.Effect
import com.nima.mymood.navigation.Screens
import com.nima.mymood.utils.Calculate
import com.nima.mymood.viewmodels.DaysOverviewViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.UUID

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DaysGraphOverviewScreen(
    navController: NavController,
    viewModel: DaysOverviewViewModel
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
        mutableStateListOf<Pair<UUID, Effect>>()
    }

    val pointsList = remember {
        mutableStateOf(emptyList<DataPoint>())
    }

    if (!allDays.isNullOrEmpty()) {
        BottomSheetScaffold(
            sheetContent = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    LazyColumn(
                        modifier = Modifier.fillMaxWidth(),
                        contentPadding = PaddingValues(vertical = 16.dp, horizontal = 32.dp),
                        verticalArrangement = Arrangement.Top,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        items(items = allDays, key = {
                            it.id
                        }){ day ->
                            DayLazyItem(
                                selected = selectedDays.contains(day.id),
                                month = day.month,
                                day = day.day,
                                year = day.year
                            ) {
                                if (selectedDays.contains(day.id)) {
                                    selectedDays.remove(day.id)
                                    days.removeAll {
                                        it.first == day.id
                                    }
                                } else {
                                    selectedDays.add(day.id)
                                    scope.launch {
                                        viewModel
                                            .getEffectsBuFK(day.id)
                                            .collectLatest {
                                                it.forEach {effect ->
                                                    days.add(day.id to effect)
                                                }
                                            }
                                    }
                                }
                            }
                        }
                    }
                }
            },
            modifier = Modifier.fillMaxSize(),
            scaffoldState = scaffoldState,
            sheetSwipeEnabled = true,
            containerColor = Color.Transparent
        ) {
            Scaffold(
                floatingActionButton = {
                    ExtendedFloatingActionButton (
                        onClick = {
                            scope.launch {
                                scaffoldState.bottomSheetState.expand()
                            }
                        },
                        containerColor = MaterialTheme.colorScheme.secondaryContainer,
                        contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
                    ) {
                        Icon(imageVector = Icons.Default.Add, contentDescription = null)
                        Text("Add Day", modifier = Modifier.padding(start = 10.dp))
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                containerColor = Color.Transparent
            ){
                Column(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    if (!days.isNullOrEmpty()) {
                        LineGraph(graphLine =
                        GraphLine(
                            lines =
                            listOf(GraphLine.Line(
                                days.mapIndexed { index, pair ->
                                    DataPoint(index + 1f, (pair.second.rate.toFloat()-4f)*-1f)
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
                            )),
                            selection = GraphLine.GraphSelection(
                                highlight = GraphLine.GraphConnection(
                                    MaterialTheme.colorScheme.primary,
                                    strokeWidth = 2.dp,
                                )
                            ),
                            paddingRight = 16.dp,
                            yAxis = GraphLine.YAxis(
                                steps = 5,
                                content = { min, offset, max ->
                                    for (step in (0 until 5)) {
                                        val value = step * offset + min
                                        Calculate.calculateIconWithRate(rate = (value.toInt()-4)*-1)
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
                            Text(
                                text = "Zoom",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.ExtraLight
                            )
                            Text(
                                text = "Pan",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.ExtraLight
                            )
                            Text(
                                text = "* Double Tap and drag for selection",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.ExtraLight
                            )
                        }
                    }

                    if (pointsList.value.isNotEmpty()) {

                        val effect = days[pointsList.value[0].x.toInt()-1].second
                        val day = allDays.filter {
                            it.id == days[pointsList.value[0].x.toInt()-1].first
                        }.first()
                        EffectsListItem(
                            effectRate = effect.rate,
                            effectMinute = effect.minute,
                            effectHour = effect.hour,
                            effectDate = String.format("%02d/%02d/%4d", day.day, day.month, day.year),
                            effectDescription = effect.description,
                            modifier = Modifier.padding(bottom = 32.dp, start = 32.dp, end = 32.dp, top = 8.dp)
                        ) {
                            navController.navigate(Screens.EditScreen.name + "/${effect.id}")
                        }
                    }
                }
            }
        }
    }
}