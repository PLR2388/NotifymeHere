package fr.wonderfulappstudio.notifymehere.ui.details

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.location.LocationServices
import dagger.hilt.android.lifecycle.HiltViewModel
import fr.wonderfulappstudio.notifymehere.model.InterestPoint
import fr.wonderfulappstudio.notifymehere.repository.InterestPointRepository
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class InterestPointDetailsViewModel @Inject constructor(private val interestPointRepository: InterestPointRepository) :
    ViewModel() {

    var uiState: InterestPointUiState by mutableStateOf(InterestPointUiState())

    fun saveInterestPoint() {
        val interestPoint = InterestPoint(
            null,
            uiState.name,
            uiState.description,
            uiState.gpsPosition,
            uiState.startDate,
            uiState.endDate
        )
        viewModelScope.launch { interestPointRepository.insert(interestPoint) }
    }

    fun setName(value: String) {
        uiState = uiState.copy(name = value)
    }

    fun setDescription(value: String?) {
        uiState = uiState.copy(description = value)
    }

    fun setGpsPosition(value: Pair<Double, Double>) {
        uiState = uiState.copy(gpsPosition = value)
    }

    fun setStartDate(value: Long?) {
        if (value == null) return
        uiState = uiState.copy(startDate = value)
    }

    fun setEndDate(value: Long?) {
        if (value == null) return
        uiState = uiState.copy(endDate = value)
    }
}