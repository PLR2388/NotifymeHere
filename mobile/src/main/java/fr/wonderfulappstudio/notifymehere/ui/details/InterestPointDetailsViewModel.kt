package fr.wonderfulappstudio.notifymehere.ui.details

import android.location.Location
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
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
    private val interestPointRepository: InterestPointRepository
) :
    ViewModel() {

    private var detailsId: Int? = null

    var state: InterestPointDetailsState by mutableStateOf(InterestPointDetailsState.Add)

    var interestPoint: InterestPoint by mutableStateOf(InterestPoint())
        private set

    var isLoading: Boolean by mutableStateOf(false)
        private set

    var alertType: AlertType? by mutableStateOf(null)
        private set

    var showAlert: Boolean by mutableStateOf(false)
        private set

    var showSuccess: Boolean by mutableStateOf(false)
        private set

    fun initInterestPoint(detailsId: Int?, stopNotification: Boolean) {
        this.detailsId = detailsId
        if (detailsId == null || detailsId == -1) {
            state = InterestPointDetailsState.Add
        } else {
            viewModelScope.launch(Dispatchers.IO) {
                val fetchedInterestPoint = interestPointRepository.getInterestPointById(detailsId)
                withContext(Dispatchers.Main) {
                    interestPoint = if (fetchedInterestPoint == null) {
                        state = InterestPointDetailsState.Add
                        InterestPoint()
                    } else {
                        state = InterestPointDetailsState.Read
                        fetchedInterestPoint
                    }
                }
                if (stopNotification) {
                    interestPoint = interestPoint.copy(alreadyNotify = true)
                    viewModelScope.launch(Dispatchers.IO) {
                        interestPointRepository.update(interestPoint)
                    }

                }
            }

        }
    }

    fun delete(completion: () -> Unit) {
        isLoading = true
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

    fun reactivateNotification() {
        interestPoint = interestPoint.copy(alreadyNotify = false)
        viewModelScope.launch(Dispatchers.IO) {
            interestPointRepository.update(interestPoint)
        }
    }

    fun saveInterestPoint(onDismiss: () -> Unit) {
        isLoading = true
        if (interestPoint.name.isEmpty()) {
            alertType = AlertType.NameIsEmpty
            showAlert = true
            isLoading = false
        } else if (interestPoint.position.latitude == 0.0 && interestPoint.position.longitude == 0.0) {
            alertType = AlertType.PositionIsEmpty
            showAlert = true
            isLoading = false
        } else if (interestPoint.startDate != null && interestPoint.endDate != null && interestPoint.endDate!! < interestPoint.startDate!!) {
            alertType = AlertType.StartDateGreaterEndDate
            showAlert = true
            isLoading = false
        } else {
            viewModelScope.launch(Dispatchers.IO) {
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

    fun displayNoPermissionForLocationAlert() {
        alertType = AlertType.NoLocationPermission
        showAlert = true
    }

    fun displayLocationPermissionExplanation() {
        alertType = AlertType.ExplanationLocationPermission
        showAlert = true
    }

    fun setName(value: String) {
        interestPoint = interestPoint.copy(name = value)
    }

    fun setDescription(value: String?) {
        interestPoint = interestPoint.copy(description = value)
    }

    fun setPosition(latitude: Double, longitude: Double) {
        interestPoint = interestPoint.copy(position = Location("app").apply {
            this.latitude = latitude
            this.longitude = longitude
        })
    }

    fun setStartDate(value: Long?) {
        if (value == null) return
        interestPoint = interestPoint.copy(startDate = value)
    }

    fun setEndDate(value: Long?) {
        if (value == null) return
        interestPoint = interestPoint.copy(endDate = value)
    }

    fun displayBackgroundLocationPermissionNotGranted() {
        alertType = AlertType.BackgroundLocationPermissionNotGranted
        showAlert = true
    }

    fun displayLocationPermissionNotGranted() {
        alertType = AlertType.LocationPermissionsNotGranted
        showAlert = true
    }
}