package com.example.digitaldashboard.compose

import androidx.compose.foundation.background
import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.digitaldashboard.VehicleGear

@Composable
fun ClusterScreen(
    speedKmh: Float?,
    rpm: Float?,
    batteryPercent: Float?,
    odometerKm: Float?,
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
    val speed = speedKmh.takeIf {
        isDataAvailable
    }

    val rpm = rpm.takeIf {
        isDataAvailable
    }

    val gear = gear.takeIf {
        isDataAvailable
    }

    val battery = batteryPercent.takeIf {
        isDataAvailable
    }

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .background(
                MaterialTheme.colorScheme.background
            )
            .safeDrawingPadding()
            .padding(16.dp)
    ) {
        val isWide = maxWidth >= 840.dp
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(
                    rememberScrollState()
                ),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "SMART DRIVE",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "←",
                    fontSize = 32.sp,
                    color = if (isDataAvailable && isLeftIndicatorOn) {
                        Color.Green
                    } else {
                        Color.Gray
                    }
                )
                Text(
                    text = "→",
                    fontSize = 32.sp,
                    color = if (isDataAvailable && isRightIndicatorOn
                    ) {
                        Color.Green
                    } else {
                        Color.Gray
                    }
                )
            }
            if (isWide) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    AutomotiveGauge(
                        value = speed,
                        maxValue = 240f,
                        majorTickCount = 12,
                        unit = "km/h",
                        progressColor = Color(0xFF60C8FF),
                        modifier = Modifier.weight(1f)
                    )
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        GearIndicator(gear)

                        BatteryIndicator(battery)
                    }
                    AutomotiveGauge(
                        value = rpm,
                        maxValue = 8000f,
                        majorTickCount = 8,
                        unit = "RPM × 1000",
                        labelDivisor = 1000f,
                        progressColor = Color(0xFFE89452),
                        modifier = Modifier.weight(1f),
                        valueFormatter = {
                            "%.1f".format(it / 1000f)
                        }
                    )
                }
            } else {
                AutomotiveGauge(
                    value = speed,
                    maxValue = 240f,
                    majorTickCount = 12,
                    unit = "km/h",
                    progressColor = Color(0xFF60C8FF),
                    modifier = Modifier.widthIn(
                        max = 320.dp
                    )
                )
                Row(
                    horizontalArrangement = Arrangement.spacedBy(24.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    GearIndicator(gear)

                    BatteryIndicator(battery)
                }

                AutomotiveGauge(
                    value = rpm,
                    maxValue = 8000f,
                    majorTickCount = 8,
                    unit = "RPM × 1000",
                    labelDivisor = 1000f,

                    progressColor =
                        Color(0xFFE89452),

                    modifier = Modifier.widthIn(
                        max = 320.dp
                    ),

                    valueFormatter = {
                        "%.1f".format(it / 1000f)
                    }
                )
            }

            WarningIndicators(
                isDataAvailable = isDataAvailable,
                isSeatBeltWarning = isSeatBeltWarning,
                isDoorOpen = isDoorOpen,
                batteryPercent = batteryPercent
            )

            Text(
                text = if (isDataAvailable) {
                    odometerKm?.let {
                        "ODO ${it.toInt()} km"
                    } ?: "ODO --"
                } else {
                    "ODO --"
                },
                fontSize = 16.sp
            )

            if (!isDataAvailable) {
                Text(
                    text = "Vehicle data unavailable",
                    color = MaterialTheme.colorScheme.error
                )
            }

            HorizontalDivider()

            ClusterSimulator(
                speedKmh = speedKmh,
                rpm = rpm,
                batteryPercent = batteryPercent,
                gear = gear,
                isLeftIndicatorOn = isLeftIndicatorOn,
                isRightIndicatorOn = isRightIndicatorOn,
                isSeatBeltWarning = isSeatBeltWarning,
                isDoorOpen = isDoorOpen,
                isDataAvailable = isDataAvailable,
                updateSpeed = updateSpeed,
                updateRpm = updateRpm,
                updateBattery = updateBattery,
                updateGear = updateGear,
                toggleLeftIndicator = toggleLeftIndicator,
                toggleRightIndicator = toggleRightIndicator,
                toggleSeatBeltWarning = toggleSeatBeltWarning,
                toggleDoorWarning = toggleDoorWarning,
                reset = reset,
                setDataAvailability = setDataAvailability
            )
        }
    }
}