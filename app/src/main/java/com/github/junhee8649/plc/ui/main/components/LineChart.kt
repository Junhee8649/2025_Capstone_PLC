package com.github.junhee8649.plc.ui.main.components

import android.graphics.CornerPathEffect
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import com.patrykandpatrick.vico.compose.cartesian.CartesianChartHost
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberStart
import com.patrykandpatrick.vico.compose.cartesian.layer.rememberLineCartesianLayer
import com.patrykandpatrick.vico.compose.cartesian.rememberCartesianChart
import com.patrykandpatrick.vico.core.cartesian.axis.VerticalAxis
import com.patrykandpatrick.vico.core.cartesian.data.CartesianChartModelProducer
import com.patrykandpatrick.vico.core.cartesian.data.lineSeries
import com.patrykandpatrick.vico.core.cartesian.layer.LineCartesianLayer
import com.patrykandpatrick.vico.core.common.Fill

/**
 * Custom Line implementation with smooth/rounded corners using CornerPathEffect
 */
class SmoothLine(
    fill: LineCartesianLayer.LineFill,
    private val cornerRadius: Float = 20f
) : LineCartesianLayer.Line(fill) {
    init {
        linePaint.apply {
            // Apply CornerPathEffect to smooth out sharp angles between line segments
            pathEffect = CornerPathEffect(cornerRadius)
        }
    }
}

@Composable
fun rememberSmoothLine(
    color: Color = MaterialTheme.colorScheme.primary,
    cornerRadius: Float = 50f  // 더 부드럽게 하기 위해 증가
): SmoothLine {
    val lineFill = LineCartesianLayer.LineFill.single(Fill(color.toArgb()))
    return remember(color, cornerRadius) {
        SmoothLine(lineFill, cornerRadius)
    }
}

@Composable
fun LineChart(
    data: List<Float>,
    modifier: Modifier = Modifier,
    lineColor: Color = MaterialTheme.colorScheme.primary
) {
    if (data.isEmpty()) return

    val modelProducer = remember { CartesianChartModelProducer() }
    val smoothLine = rememberSmoothLine(color = lineColor)

    LaunchedEffect(data) {
        modelProducer.runTransaction {
            lineSeries { series(data) }
        }
    }

    CartesianChartHost(
        chart = rememberCartesianChart(
            rememberLineCartesianLayer(
                lineProvider = LineCartesianLayer.LineProvider.series(smoothLine)
            ),
            startAxis = VerticalAxis.rememberStart(),
            bottomAxis = null // X축 레이블 제거
        ),
        modelProducer = modelProducer,
        modifier = modifier
    )
}
