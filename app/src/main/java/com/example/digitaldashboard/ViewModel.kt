package com.example.digitaldashboard

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

class DashBoardViewModel(
    application: Application
): AndroidViewModel(application) {

    private val viewModelState = MutableStateFlow(
        MyViewModelState(
            isLoading = false
        )
    )

    val uiState = viewModelState
        .map { it.uiState() }
        .stateIn(viewModelScope, SharingStarted.Eagerly, viewModelState.value.uiState())

    fun updateSpeed(speed: Float) {
        viewModelState.update {
            it.copy(
                speedKmh = speed.coerceIn(0f, 240f)
            )
        }
    }

    fun updateRpm(rpm: Float) {
        viewModelState.update {
            it.copy(
                rpm = rpm.coerceIn(0f, 8000f)
            )
        }
    }

    fun updateGear(gear: VehicleGear) {
        viewModelState.update {
            it.copy(gear = gear)
        }
    }

    fun updateBattery(percent: Float) {
        viewModelState.update {
            it.copy(
                batteryPercent =
                    percent.coerceIn(0f, 100f)
            )
        }
    }

    fun toggleLeftIndicator() {
        viewModelState.update {
            it.copy(
                isLeftIndicatorOn =
                    !it.isLeftIndicatorOn
            )
        }
    }

    fun toggleRightIndicator() {
        viewModelState.update {
            it.copy(
                isRightIndicatorOn =
                    !it.isRightIndicatorOn
            )
        }
    }

    fun toggleSeatBeltWarning() {
        viewModelState.update {
            it.copy(
                isSeatBeltWarning =
                    !it.isSeatBeltWarning
            )
        }
    }

    fun toggleDoorWarning() {
        viewModelState.update {
            it.copy(
                isDoorOpen = !it.isDoorOpen
            )
        }
    }

    fun setDataAvailability(available: Boolean) {
        viewModelState.update {
            it.copy(isDataAvailable = available)
        }
    }

    fun reset() {
        viewModelState.value = MyViewModelState()
    }

}


data class MyViewModelState(
    val isLoading: Boolean? = false,
    val speedKmh: Float? = 0f,
    val rpm: Float? = 0f,
    val gear: VehicleGear? = VehicleGear.P,
    val batteryPercent: Float? = 84f,
    val odometerKm: Float? = 12480f,
    val isLeftIndicatorOn: Boolean = false,
    val isRightIndicatorOn: Boolean = false,
    val isSeatBeltWarning: Boolean = false,
    val isDoorOpen: Boolean = false,
    val isDataAvailable: Boolean = true
){
    fun uiState() = MyUiState(
        isLoading = isLoading,
        speedKmh = speedKmh,
        rpm = rpm,
        gear = gear,
        batteryPercent = batteryPercent,
        odometerKm = odometerKm,
        isLeftIndicatorOn = isLeftIndicatorOn,
        isRightIndicatorOn = isRightIndicatorOn,
        isSeatBeltWarning = isSeatBeltWarning,
        isDoorOpen = isDoorOpen,
        isDataAvailable = isDataAvailable
    )
}

data class MyUiState(
    val isLoading: Boolean?,
    val speedKmh: Float?,
    val rpm: Float?,
    val gear: VehicleGear?,
    val batteryPercent: Float?,
    val odometerKm: Float?,
    val isLeftIndicatorOn: Boolean,
    val isRightIndicatorOn: Boolean,
    val isSeatBeltWarning: Boolean,
    val isDoorOpen: Boolean,
    val isDataAvailable: Boolean
)

enum class VehicleGear {
    P, R, N, D
}