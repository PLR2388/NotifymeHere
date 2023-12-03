package fr.wonderfulappstudio.notifymehere.ui.details

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import fr.wonderfulappstudio.notifymehere.model.InterestPoint
import fr.wonderfulappstudio.notifymehere.repository.InterestPointRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class InterestPointDetailsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val interestPointRepository: InterestPointRepository
) :
    ViewModel() {

    private var detailsId: Int? = savedStateHandle.get<Int>("detailsId")

    var state: InterestPointDetailsState by mutableStateOf(InterestPointDetailsState.Add)

    var uiState: InterestPointUiState by mutableStateOf(InterestPointUiState())

    var isLoading: Boolean by mutableStateOf(false)
        private set

    var alertType: AlertType? by mutableStateOf(null)
        private set

    var showAlert: Boolean by mutableStateOf(false)
        private set

    var showSuccess: Boolean by mutableStateOf(false)
        private set

    init {
        val position = savedStateHandle.get<Pair<Double, Double>>("position")
        setDetailsId(detailsId)
        if (position != null) {
            uiState = uiState.copy(gpsPosition = position)
        }
    }

    fun setDetailsId(detailsId: Int?) {
        this.detailsId = detailsId
        if (detailsId == null || detailsId == -1) {
            state = InterestPointDetailsState.Add
            uiState = InterestPointUiState()
        } else {
            viewModelScope.launch(Dispatchers.IO) {
                val interestPoint = interestPointRepository.getInterestPointById(detailsId)
                withContext(Dispatchers.Main) {
                    uiState = if (interestPoint == null) {
                        state = InterestPointDetailsState.Add
                        InterestPointUiState()
                    } else {
                        state = InterestPointDetailsState.Read
                        uiState.copy(
                            name = interestPoint.name,
                            description = interestPoint.description,
                            gpsPosition = interestPoint.gpsPosition,
                            startDate = interestPoint.startDate,
                            endDate = interestPoint.endDate
                        )
                    }
                }

            }
        }
    }

    fun delete(completion: () -> Unit) {
        isLoading = true
        val interestPoint = buildInterestPoint()
        viewModelScope.launch(Dispatchers.IO) {
            interestPointRepository.delete(interestPoint)
            withContext(Dispatchers.Main) {
                isLoading = false
                completion()
            }
        }
    }

    fun toggleEditMode() {
        state = if (state == InterestPointDetailsState.Read) {
            InterestPointDetailsState.Modify
        } else {
            InterestPointDetailsState.Read
        }
    }

    fun hideAlert() {
        showAlert = false
    }

    fun saveInterestPoint(onDismiss: () -> Unit) {
        isLoading = true
        if (uiState.name.isEmpty()) {
            alertType = AlertType.NameIsEmpty
            showAlert = true
            isLoading = false
        } else if (uiState.gpsPosition == Pair(0.0, 0.0)) {
            alertType = AlertType.PositionIsEmpty
            showAlert = true
            isLoading = false
        } else if (uiState.startDate != null && uiState.endDate != null && uiState.endDate!! < uiState.startDate!!) {
            alertType = AlertType.StartDateGreaterEndDate
            showAlert = true
            isLoading = false
        } else {
            val interestPoint = buildInterestPoint()
            viewModelScope.launch {
                if (interestPoint.id == null) {
                    interestPointRepository.insert(interestPoint)
                } else {
                    interestPointRepository.update(interestPoint)
                }
                withContext(Dispatchers.Main) {
                    showSuccess = true
                    isLoading = false
                    onDismiss()
                }
            }
        }
    }

    private fun buildInterestPoint(): InterestPoint {
        val id = if (detailsId == null || detailsId == -1) null else detailsId
        return InterestPoint(
            id,
            uiState.name,
            uiState.description,
            uiState.gpsPosition,
            uiState.startDate,
            uiState.endDate
        )
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