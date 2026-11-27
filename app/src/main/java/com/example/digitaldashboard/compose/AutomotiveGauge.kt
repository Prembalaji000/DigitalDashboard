package com.example.digitaldashboard.compose

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.digitaldashboard.utils.calculateGaugeAngle
import com.example.digitaldashboard.utils.calculatePoint

@Composable
fun AutomotiveGauge(
    value: Float?,
    maxValue: Float,
    majorTickCount: Int,
    unit: String,
    progressColor: Color,
    modifier: Modifier = Modifier,
    labelDivisor: Float = 1f,
    valueFormatter: (Float) -> String = {
        "%.0f".format(it)
    }
) {
    require(maxValue > 0f)
    require(majorTickCount > 0)
    require(labelDivisor > 0f)

    val validValue = value?.takeIf {
        it.isFinite()
    }
    val animatedValue = animateFloatAsState(
        targetValue = validValue
            ?.coerceIn(0f, maxValue) ?: 0f,

        animationSpec = tween(
            durationMillis = 250,
            easing = LinearEasing
        ),

        label = "GaugeAnimation"
    )
    val textMeasurer = rememberTextMeasurer()
    val textColor = MaterialTheme.colorScheme.onSurface
    val trackColor = MaterialTheme.colorScheme.surfaceVariant

    Box(
        modifier = modifier.aspectRatio(1f),
        contentAlignment = Alignment.Center
    ) {
        Canvas(
            modifier = Modifier.fillMaxSize()
        ) {
            val gaugeCenter = center
            val radius = size.minDimension * 0.42f
            val startAngle = 135f
            val sweepAngle = 270f
            val strokeWidth = 10.dp.toPx()
            val arcTopLeft = Offset(
                x = gaugeCenter.x - radius,
                y = gaugeCenter.y - radius
            )
            val arcSize = Size(
                width = radius * 2f,
                height = radius * 2f
            )
            // 1. Background arc
            drawArc(
                color = trackColor,
                startAngle = startAngle,
                sweepAngle = sweepAngle,
                useCenter = false,
                topLeft = arcTopLeft,
                size = arcSize,
                style = Stroke(
                    width = strokeWidth,
                    cap = StrokeCap.Round
                )
            )
            // 2. Progress arc
            val progress =
                animatedValue.value / maxValue

            if (validValue != null &&
                progress > 0f
            ) {

                drawArc(
                    color = progressColor,
                    startAngle = startAngle,
                    sweepAngle =
                        sweepAngle * progress,
                    useCenter = false,
                    topLeft = arcTopLeft,
                    size = arcSize,
                    style = Stroke(
                        width = strokeWidth,
                        cap = StrokeCap.Round
                    )
                )
            }
            // 3. Major and minor tick marks
            val totalTicks = majorTickCount * 5
            for (i in 0..totalTicks) {
                val isMajor = i % 5 == 0
                val angle = startAngle + i.toFloat() / totalTicks * sweepAngle
                val outerRadius = radius * 0.90f
                val innerRadius = radius * if (isMajor) {
                    0.78f
                } else {
                    0.85f
                }
                val tickStart = calculatePoint(
                    center = gaugeCenter,
                    radius = innerRadius,
                    angle = angle
                )
                val tickEnd = calculatePoint(
                    center = gaugeCenter,
                    radius = outerRadius,
                    angle = angle
                )
                drawLine(
                    color = textColor.copy(
                        alpha = if (isMajor) {
                            0.9f
                        } else {
                            0.4f
                        }
                    ),
                    start = tickStart,
                    end = tickEnd,
                    strokeWidth = if (isMajor) {
                        2.dp.toPx()
                    } else {
                        1.dp.toPx()
                    }
                )
                // 4. Numeric labels
                if (isMajor) {
                    val labelValue = maxValue *
                                i.toFloat() /
                                totalTicks /
                                labelDivisor
                    val label = "%.0f".format(labelValue)
                    val labelPosition = calculatePoint(
                        center = gaugeCenter,
                        radius = radius * 0.65f,
                        angle = angle
                    )
                    val measuredText = textMeasurer.measure(
                        text = label,
                        style = TextStyle(
                            color = textColor,
                            fontSize = 12.sp
                        )
                    )
                    drawText(
                        textLayoutResult = measuredText,
                        topLeft = Offset(
                            x = labelPosition.x - measuredText.size.width / 2f,
                            y = labelPosition.y - measuredText.size.height / 2f
                        )
                    )
                }
            }
            // 5. Gauge needle
            if (validValue != null) {
                val needleAngle = calculateGaugeAngle(
                    value = animatedValue.value,
                    maxValue = maxValue
                )
                val needleEnd = calculatePoint(
                    center = gaugeCenter,
                    radius = radius * 0.58f,
                    angle = needleAngle
                )

                drawLine(
                    color = Color(0xFFFF6655),
                    start = gaugeCenter,
                    end = needleEnd,
                    strokeWidth = 4.dp.toPx(),
                    cap = StrokeCap.Round
                )
                // 6. Needle hub

                drawCircle(
                    color = textColor,
                    radius = 6.dp.toPx(),
                    center = gaugeCenter
                )
            }
        }
        // 7. Digital value and unit
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = validValue?.let(
                    valueFormatter
                ) ?: "--",
                fontSize = 40.sp,
                fontWeight = FontWeight.Bold,
                color = textColor
            )
            Text(
                text = unit,
                fontSize = 14.sp,
                color = textColor.copy(
                    alpha = 0.7f
                )
            )
        }
    }
}