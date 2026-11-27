package com.example.digitaldashboard.utils

import androidx.compose.ui.geometry.Offset
import kotlin.math.cos
import kotlin.math.sin

fun calculatePoint(
    center: Offset,
    radius: Float,
    angle: Float
): Offset {

    val radians =
        Math.toRadians(angle.toDouble())

    return Offset(
        x = center.x +
                radius * cos(radians).toFloat(),

        y = center.y +
                radius * sin(radians).toFloat()
    )
}

fun calculateGaugeAngle(
    value: Float,
    maxValue: Float,
    startAngle: Float = 135f,
    sweepAngle: Float = 270f
): Float {

    require(maxValue > 0f)

    val progress =
        (value / maxValue).coerceIn(0f, 1f)

    return startAngle +
            progress * sweepAngle
}