package com.example.digitaldashboard.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.digitaldashboard.DashBoardViewModel

@Composable
fun ClusterScreenRouter(
    viewModel: DashBoardViewModel
){

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    ClusterScreen(
        speedKmh = uiState.speedKmh,
        rpm = uiState.rpm,
        batteryPercent = uiState.batteryPercent,
        odometerKm = uiState.odometerKm,
        gear = uiState.gear,
        isLeftIndicatorOn = uiState.isLeftIndicatorOn,
        isRightIndicatorOn = uiState.isRightIndicatorOn,
        isSeatBeltWarning = uiState.isSeatBeltWarning,
        isDoorOpen = uiState.isDoorOpen,
        isDataAvailable = uiState.isDataAvailable,
        updateSpeed = { viewModel.updateSpeed(it) },
        updateRpm = { viewModel.updateRpm(it) },
        updateBattery = { viewModel.updateBattery(it) },
        updateGear = { viewModel.updateGear(it) },
        toggleLeftIndicator = { viewModel.toggleLeftIndicator() },
        toggleRightIndicator = { viewModel.toggleRightIndicator() },
        toggleSeatBeltWarning = { viewModel.toggleSeatBeltWarning() },
        toggleDoorWarning = { viewModel.toggleDoorWarning() },
        reset = { viewModel.reset() },
        setDataAvailability = { viewModel.setDataAvailability(it) }
    )
}