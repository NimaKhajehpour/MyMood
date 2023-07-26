package com.nima.mymood.screens

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.text.isDigitsOnly
import androidx.navigation.NavController
import com.nima.mymood.R
import com.nima.mymood.graph.DataPoint
import com.nima.mymood.graph.GraphLine
import com.nima.mymood.graph.LineGraph
import com.nima.mymood.utils.Calculate
import com.nima.mymood.viewmodels.DayGraphViewModel
import java.util.*

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun DayGraphScreen(
    navController: NavController,
    id: String?,
    viewModel: DayGraphViewModel
){
    val day = viewModel.getDay(UUID.fromString(id!!)).collectAsState(initial = null)
    val effects = viewModel.getDayEffects(UUID.fromString(id!!)).collectAsState(initial = emptyList())

    val pointsList = remember {
        mutableStateOf(emptyList<DataPoint>())
    }

    val clipboard = LocalClipboardManager.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (day.value != null){
            Text(text = "${Calculate.calculateMonthName(day.value!!.month)} " +
                    "${day.value!!.day} ${day.value!!.year}",
                modifier = Modifier.padding(vertical = 16.dp)
            )
        }

        if (effects.value.isNotEmpty()){
            val effectsDataPoints = mutableListOf<DataPoint>()

            effects.value.forEachIndexed{index, effect ->
                effectsDataPoints.add(index, DataPoint(index+1f, effect.rate.toFloat()))
            }
            LineGraph(graphLine =
            GraphLine(
                listOf(
                    GraphLine.Line(
                        effectsDataPoints,
                        GraphLine.GraphConnection(MaterialTheme.colorScheme.secondary, 2.dp),
                        GraphLine.GraphIntersection(MaterialTheme.colorScheme.secondary, 5.dp),
                        GraphLine.GraphHighlight(MaterialTheme.colorScheme.tertiary, 7.dp),
                    ),
                ),
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
                xAxis = GraphLine.XAxis(
                    steps = effects.value.size,
                    content = {min, offset, _ ->
                        for (step in 0 until effects.value.size){
                            val value = step * offset + min
                            Text(text = if (effects.value[value.toInt()-1].hour.isNotBlank()) String
                                .format("%02d:\n%02d", effects.value[value.toInt()-1].hour.toInt(),
                                    effects.value[value.toInt()-1].minute.toInt()) else "No\nTime",
                                fontWeight = FontWeight.Light,
                                fontSize = MaterialTheme.typography.labelSmall.fontSize,
                                lineHeight = 12.sp,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                )
            ),

                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                onSelection = { offset, points ->

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

            if (pointsList.value.isNotEmpty()){

                val effect = effects.value[pointsList.value[0].x.toInt()-1]

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp, horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.End
                ) {
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
                
                Calculate.calculateIconWithRate(rate = effect.rate, size = 64.dp)

                SelectionContainer {
                    Text(text = effect.description,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.tertiary,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(vertical = 8.dp, horizontal = 16.dp)
                    )
                }

                Text(text = "Time: ${String.format("%02d:%02d", effect.hour.toInt(), effect.minute.toInt())}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.tertiary,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(vertical = 8.dp, horizontal = 16.dp)
                )
            }
        }
    }
}