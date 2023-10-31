package fr.wonderfulappstudio.notifymehere.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class InterestPointDetailsViewModel @Inject constructor() : ViewModel() {

    var uiState: InterestPointUiState by mutableStateOf(InterestPointUiState())

    fun setName(value: String) {
        uiState = uiState.copy(name = value)
    }

    fun setDescription(value: String?) {
        uiState = uiState.copy(description = value)
    }

    fun setGpsPosition(value: Pair<Double, Double>) {
        uiState = uiState.copy(gpsPosition = value)
    }

    fun setStartDate(value: Long) {
        uiState = uiState.copy(startDate = value)
    }

    fun setEndDate(value: Long) {
        uiState = uiState.copy(endDate = value)
    }
}