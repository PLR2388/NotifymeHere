package fr.wonderfulappstudio.notifymehere.ui.main

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import fr.wonderfulappstudio.notifymehere.model.InterestPoint
import fr.wonderfulappstudio.notifymehere.repository.InterestPointRepository
import fr.wonderfulappstudio.notifymehere.ui.details.AlertType
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(interestPointRepository: InterestPointRepository): ViewModel() {

    val interestPoints: Flow<List<InterestPoint>> = interestPointRepository.interestPoints

    var alertType: AlertType? by mutableStateOf(null)
        private set

    var showAlert: Boolean by mutableStateOf(false)
        private set

    fun displayNotificationPermissionNotGranted() {
        alertType = AlertType.NotificationPermissionNotGranted
        showAlert = true
    }
}