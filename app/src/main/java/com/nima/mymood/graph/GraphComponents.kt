package com.nima.mymood.graph


import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.DrawStyle
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import java.text.DecimalFormat

data class DataPoint(val x: Float, val y: Float)

data class GraphLine(
    val lines: List<Line>,
    val selection: GraphSelection = GraphSelection(),
    val xAxis: XAxis = XAxis(),
    val yAxis: YAxis = YAxis(),
    val paddingTop: Dp = 16.dp,
    val paddingRight: Dp = 0.dp,
    val horizontalExtraSpace: Dp = 6.dp,
){
    data class Line(
        val dataPoints: List<DataPoint>,
        val connection: GraphConnection?,
        val intersection: GraphIntersection?,
        val highlight: GraphHighlight? = null
    )

    data class GraphConnection(
        val color: Color,
        val strokeWidth: Dp = 3.dp,
        val strokeCap: StrokeCap = StrokeCap.Round,
        val pathEffect: PathEffect? = null,
        val draw: DrawScope.(Offset, Offset) -> Unit = {start, end ->
            drawLine(
                color,
                start,
                end,
                strokeWidth.toPx(),
                strokeCap,
            )
        }
    )

    data class GraphIntersection(
        val color: Color,
        val radius: Dp = 6.dp,
        val style: DrawStyle = Fill,
        val draw: DrawScope.(Offset, DataPoint) -> Unit = {center, _ ->
            drawCircle(
                color = color,
                radius = radius.toPx(),
                center = center,
                style = style,
            )
        }
    )

    data class GraphHighlight(
        val color: Color,
        val radius: Dp = 6.dp,
        val style: DrawStyle = Fill,
        val draw: DrawScope.(Offset) -> Unit = {center ->
            drawCircle(
                color = color,
                radius = radius.toPx(),
                center = center,
                style = style
            )
        }
    )

    data class GraphSelection(
        val enabled: Boolean = true,
        val highlight: GraphConnection? =
            GraphConnection(
                Color.Red,
                strokeWidth = 2.dp,
                pathEffect = PathEffect.dashPathEffect(floatArrayOf(40f, 20f))
            ),
        val detectionTime: Long = 100L
    )

    data class XAxis(
        val stepSize: Dp = 20.dp,
        val steps: Int = 10,
        val unit: Float = 1f,
        val paddingTop: Dp = 8.dp,
        val paddingBottom: Dp = 8.dp,
        val roundToInt: Boolean = true,
        val content: @Composable (Float, Float, Float) -> Unit =
            {min, offset, max ->
                for (it in 0 until steps){
                    val value = it * offset + min
                    Text(text = value.string(),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                    )
                    if (value > max){
                        break
                    }
                }
            }
    )

    data class YAxis(
        val steps: Int = 5,
        val roundToInt: Boolean = true,
        val paddingStart: Dp = 16.dp,
        val paddingEnd: Dp = 8.dp,
        val content: @Composable (Float, Float, Float) -> Unit = { min, offset, _ ->
            for (it in 0 until steps) {
                val value = it * offset + min
                Text(
                    text = value.string(),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    )
}

private fun Float.string() = DecimalFormat("#.#").format(
    this
)