package com.example.digitaldashboard.compose

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.FilterChip
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.digitaldashboard.MyUiState
import com.example.digitaldashboard.VehicleGear

@Composable
fun GearIndicator(
    currentGear: VehicleGear?
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "GEAR",
            fontSize = 14.sp
        )
        Text(
            text = currentGear?.name ?: "--",
            fontSize = 56.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            VehicleGear.entries.forEach { gear ->
                Text(
                    text = gear.name,
                    fontSize = 18.sp,
                    fontWeight = if (gear == currentGear) {
                        FontWeight.Bold
                    } else {
                        FontWeight.Normal
                    },
                    color = if (gear == currentGear) {
                        MaterialTheme
                            .colorScheme.primary
                    } else {
                        MaterialTheme
                            .colorScheme.onSurface
                            .copy(alpha = 0.4f)
                    }
                )
            }
        }
    }
}

@Composable
fun BatteryIndicator(
    batteryPercent: Float?
) {

    val batteryColor = if (batteryPercent == null) {
        MaterialTheme.colorScheme.onSurface
    } else if (batteryPercent < 15f) {
        Color(0xFFE53935)
    } else {
        Color(0xFF36B87C)
    }

    Column(
        modifier = Modifier.width(140.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = batteryPercent?.let {
                "${it.toInt()}%"
            } ?: "--",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = batteryColor
        )
        LinearProgressIndicator(
            progress = {
                (batteryPercent ?: 0f)
                    .div(100f)
                    .coerceIn(0f, 1f)
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp),
            color = batteryColor
        )
        Text(
            text = "BATTERY",
            fontSize = 12.sp
        )
    }
}

@Composable
fun WarningIndicators(
    isDataAvailable: Boolean,
    isSeatBeltWarning: Boolean,
    isDoorOpen: Boolean,
    batteryPercent: Float?
) {

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        if (!isDataAvailable) {
            Text(
                text = "Warning data unavailable",
                color = MaterialTheme.colorScheme.error
            )
        } else {
            if (isSeatBeltWarning) {
                Text(
                    text = "SEAT BELT",
                    color = Color.Red,
                    fontWeight = FontWeight.Bold
                )
            }
            if (isDoorOpen) {
                Text(
                    text = "DOOR OPEN",
                    color = Color.Red,
                    fontWeight = FontWeight.Bold
                )
            }
            if (batteryPercent?.let {
                    it < 15f
                } == true
            ) {
                Text(
                    text = "LOW BATTERY",
                    color = Color(0xFFFFB300),
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun ClusterSimulator(
    speedKmh: Float?,
    rpm: Float?,
    batteryPercent: Float?,
    gear: VehicleGear?,
    isLeftIndicatorOn: Boolean,
    isRightIndicatorOn: Boolean,
    isSeatBeltWarning: Boolean,
    isDoorOpen: Boolean,
    isDataAvailable: Boolean,
    updateSpeed: (Float) -> Unit,
    updateRpm: (Float) -> Unit,
    updateBattery: (Float) -> Unit,
    updateGear: (VehicleGear) -> Unit,
    toggleLeftIndicator: () -> Unit,
    toggleRightIndicator: () -> Unit,
    toggleSeatBeltWarning: () -> Unit,
    toggleDoorWarning: () -> Unit,
    reset: () -> Unit,
    setDataAvailability: (Boolean) -> Unit,
) {

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "Vehicle Simulator",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            "Speed: ${speedKmh?.toInt()} km/h"
        )
        Slider(
            value = speedKmh ?: 0f,
            onValueChange = updateSpeed,
            valueRange = 0f..240f
        )
        Text(
            "RPM: ${rpm?.toInt()}"
        )
        Slider(
            value = rpm ?: 0f,
            onValueChange = updateRpm,
            valueRange = 0f..8000f
        )
        Text(
            "Battery: ${batteryPercent?.toInt()}%"
        )
        Slider(
            value = batteryPercent ?: 0f,
            onValueChange = updateBattery,
            valueRange = 0f..100f
        )
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            VehicleGear.entries.forEach { it ->
                FilterChip(
                    selected = gear == it,
                    onClick = {
                        updateGear(it)
                    },
                    label = {
                        Text(it.name)
                    }
                )
            }
        }
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = isLeftIndicatorOn,
                onCheckedChange = {
                    toggleLeftIndicator()
                }
            )
            Text("Left indicator")
        }
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = isRightIndicatorOn,
                onCheckedChange = {
                    toggleRightIndicator()
                }
            )
            Text("Right indicator")
        }
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = isSeatBeltWarning,
                onCheckedChange = {
                    toggleSeatBeltWarning()
                }
            )
            Text("Seat belt warning")
        }
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = isDoorOpen,
                onCheckedChange = {
                    toggleDoorWarning()
                }
            )
            Text("Door open")
        }
        Row(
            verticalAlignment =
                Alignment.CenterVertically
        ) {
            Checkbox(
                checked = isDataAvailable,
                onCheckedChange = setDataAvailability
            )
            Text("Vehicle data available")
        }

        Button(
            onClick = reset,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Reset Dashboard")
        }
    }
}