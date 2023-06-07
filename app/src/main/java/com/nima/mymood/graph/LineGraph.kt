package com.nima.mymood.graph

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.rememberScrollableState
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import kotlin.math.ceil

@Composable
fun LineGraph(
    graphLine: GraphLine,
    modifier: Modifier,
    backgroundColor: Color = MaterialTheme.colorScheme.surface,
    onSelectionStart: () -> Unit = {},
    onSelection: ((Float, List<DataPoint>) -> Unit)? = null
){
    val paddingTop = graphLine.paddingTop
    val paddingRight= graphLine.paddingRight
    val horizontalGap = graphLine.horizontalExtraSpace

    val globalXScale = 1f
    val globalYScale = 1f

    var offset by remember{
        mutableStateOf(0f)
    }

    var maxScrollOffset by remember { mutableStateOf(0f) }
    var dragOffset by remember { mutableStateOf(0f) }
    var isDragging by remember { mutableStateOf(false) }
    var xZoom by remember { mutableStateOf(globalXScale) }
    var rowHeight by remember { mutableStateOf(0f) }
    var columnWidth by remember { mutableStateOf(0f) }

    val lines = graphLine.lines
    val xUnit = graphLine.xAxis.unit

    CompositionLocalProvider(
        LocalLayoutDirection provides LayoutDirection.Ltr
    ) {
        Box(
            modifier = modifier.clipToBounds()
        ){
            val points = lines.flatMap { it.dataPoints }
            val (xMin, xMax, xAxisScale) = getXAxisScale(points, graphLine)
            val (yMin, yMax, yAxisScale) = getYAxisScale(points, graphLine)

            Canvas(modifier = Modifier
                .align(Alignment.Center)
                .fillMaxHeight()
                .fillMaxWidth()
                .background(backgroundColor)
                .scrollable(
                    state = rememberScrollableState { delta ->
                        offset -= delta
                        if (offset < 0f) offset = 0f
                        if (offset > maxScrollOffset) {
                            offset = maxScrollOffset
                        }
                        delta
                    }, Orientation.Horizontal, enabled = true
                )
                .pointerInput(Unit, Unit) {
                    detectDragZoomGesture(
                        isZoomAllowed = true,
                        isDragAllowed = graphLine.selection.enabled,
                        detectDragTimeOut = graphLine.selection.detectionTime,
                        onDragStart = {
                            dragOffset = it.x
                            onSelectionStart()
                            isDragging = true
                        }, onDragEnd = {
                            isDragging = false
                        }, onZoom = { zoom ->
                            xZoom *= zoom
                        }
                    ) { change, _ ->
                        dragOffset = change.position.x
                    }
                },
                onDraw = {
                    val xLeft = columnWidth + horizontalGap.toPx()
                    val yBottom = size.height - rowHeight
                    val xOffset = 20.dp.toPx() * xZoom
                    val maxElementInYAxis = getMaxElementInYAxis(yAxisScale, graphLine.yAxis.steps)
                    val yOffset = ((yBottom - paddingTop.toPx()) / maxElementInYAxis) * globalYScale
                    val xLastPoint = (xMax - xMin) * xOffset * (1/xUnit) + xLeft + paddingRight.toPx()+ horizontalGap.toPx()
                    maxScrollOffset = if (xLastPoint > size.width) xLastPoint-size.width else 0f
                    val dragLocks = mutableMapOf<GraphLine.Line, Pair<DataPoint, Offset>>()

                    // draw graph and points
                    lines.forEach { line ->
                        val intersection = line.intersection
                        val connection = line.connection

                        var curOffset: Offset? = null
                        var nextOffset: Offset? = null
                        line.dataPoints.forEachIndexed { i, _ ->
                            if (i == 0){
                                val (x, y) = line.dataPoints[i]
                                val x1 = ((x - xMin) * xOffset * (1/xUnit)) + xLeft - offset
                                val y1 = yBottom - ((y - yMin) * yOffset)
                                curOffset = Offset(x1, y1)
                            }
                            if (line.dataPoints.indices.contains(i+1)){
                                val (x,y) = line.dataPoints[i+1]
                                val x2 = ((x - xMin) * xOffset * (1/xUnit)) + xLeft - offset
                                val y2 = yBottom - ((y - yMin) * yOffset)
                                nextOffset = Offset(x2, y2)
                            }
                            if (nextOffset != null && curOffset != null){
                                connection?.draw?.invoke(
                                    this,
                                    curOffset!!,
                                    nextOffset!!
                                )
                            }
                            curOffset?.let {
                                if (isDragging && isDragLocked(
                                        dragOffset,
                                        it,
                                        xOffset
                                    )){
                                    dragLocks[line] = line.dataPoints[i] to it
                                }else{
                                    intersection?.draw?.invoke(this, it, line.dataPoints[i])
                                }
                            }
                            curOffset = nextOffset
                        }
                    }

                    drawRect(
                        backgroundColor,
                        Offset(0f, 0f),
                        Size(columnWidth, size.height)
                    )

                    drawRect(
                        backgroundColor,
                        Offset(size.width - paddingRight.toPx(), 0f),
                        Size(paddingRight.toPx(), size.height)
                    )

                    if (isDragging){
                        dragLocks.values.firstOrNull()?.let { (_, location) ->
                            val (x, _) = location
                            if (x >= columnWidth && x<= size.width - paddingRight.toPx()){
                                graphLine.selection.highlight?.draw?.invoke(
                                    this,
                                    Offset(x, yBottom),
                                    Offset(x, 0f)
                                )
                            }
                        }

                        dragLocks.entries.forEach{ (line, lock) ->
                            val highlight = line.highlight
                            val location = lock.second
                            val x = location.x
                            if (x >= columnWidth && x <= size.width - paddingRight.toPx()){
                                highlight?.draw?.invoke(this, location)
                            }
                        }
                    }

                    if (isDragging){
                        val x = dragLocks.values.firstOrNull()?.second?.x
                        if (x != null){
                            onSelection?.invoke(x, dragLocks.values.map { it.first })
                        }
                    }
                })

            GraphXAxis(modifier = Modifier
                .align(Alignment.BottomStart)
                .fillMaxWidth()
                .wrapContentHeight()
                .clip(
                    RowClip(
                        columnWidth,
                        paddingRight
                    )
                )
                .onGloballyPositioned {
                    rowHeight = it.size.height.toFloat()
                }
                .padding(bottom = graphLine.xAxis.paddingBottom, top = graphLine.xAxis.paddingTop),
                xStart = columnWidth + horizontalGap.value * LocalDensity.current.density,
                scrollOffset = offset,
                scale = xZoom * xAxisScale *(1/xUnit),
                stepSize = graphLine.xAxis.stepSize) {
                graphLine.xAxis.content(xMin, xAxisScale, xMax)
            }

            GraphYAxis(
                Modifier
                    .align(Alignment.TopStart)
                    .fillMaxHeight()
                    .wrapContentWidth()
                    .onGloballyPositioned {
                        columnWidth = it.size.width.toFloat()
                    }
                    .padding(start = graphLine.yAxis.paddingStart, end = graphLine.yAxis.paddingEnd),
                paddingTop = paddingTop.value * LocalDensity.current.density,
                paddingBottom = rowHeight,
                scale = globalYScale,
            ) {
                graphLine.yAxis.content(yMin, yAxisScale, yMax)
            }
        }
    }
}

private fun isDragLocked(dragOffset: Float, it: Offset, xOffset: Float) =
    ((dragOffset) > it.x - xOffset / 2) && ((dragOffset) < it.x + xOffset / 2)

private fun getXAxisScale(
    points: List<DataPoint>,
    graphLine: GraphLine
): Triple<Float, Float, Float> {
    val xMin = points.minOf { it.x }
    val xMax = points.maxOf { it.x }
    val totalSteps =
        (xMax - xMin) + 1
    val temp = totalSteps / graphLine.xAxis.steps
    val scale = if (graphLine.xAxis.roundToInt) ceil(temp) else temp
    return Triple(xMin, xMax, scale)
}

private fun getYAxisScale(
    points: List<DataPoint>,
    graphLine: GraphLine
): Triple<Float, Float, Float> {
    val steps = graphLine.yAxis.steps
    val yMin = points.minOf { it.y }
    val yMax = points.maxOf { it.y }

    val totalSteps = (yMax - yMin)
    val temp = totalSteps / if (steps > 1) (steps - 1) else 1

    val scale = if (graphLine.yAxis.roundToInt) ceil(temp) else temp
    return Triple(yMin, yMax, scale)
}

private fun getMaxElementInYAxis(
    offset: Float, steps: Int
): Float {
    return (if (steps > 1) steps -1 else 1) * offset
}

private class RowClip(private val leftPadding: Float, private val rightPadding: Dp) : Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        return Outline.Rectangle(
            Rect(
                leftPadding,
                0f,
                size.width - rightPadding.value * density.density,
                size.height
            )
        )
    }
}