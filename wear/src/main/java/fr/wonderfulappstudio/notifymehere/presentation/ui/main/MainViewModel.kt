package fr.wonderfulappstudio.notifymehere.presentation.ui.main


import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.wearable.DataClient
import com.google.android.gms.wearable.DataEvent
import com.google.android.gms.wearable.DataEventBuffer
import com.google.android.gms.wearable.DataMapItem
import dagger.hilt.android.lifecycle.HiltViewModel
import fr.wonderfulappstudio.notifymehere.presentation.model.InterestPoint
import fr.wonderfulappstudio.notifymehere.presentation.repository.InterestPointRepository
import fr.wonderfulappstudio.notifymehere.presentation.service.DataLayerListenerService.Companion.INTEREST_POINTS_KEY
import fr.wonderfulappstudio.notifymehere.presentation.service.DataLayerListenerService.Companion.INTEREST_POINTS_PATH
import fr.wonderfulappstudio.notifymehere.presentation.extension.toInterestPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    application: Application,
    private val interestPointRepository: InterestPointRepository
) :
    AndroidViewModel(application),
    DataClient.OnDataChangedListener {

    val interestPoints: Flow<List<InterestPoint>> = interestPointRepository.interestPoints

    var alertType: AlertType? by mutableStateOf(null)
        private set

    var showAlert: Boolean by mutableStateOf(false)
        private set

    override fun onDataChanged(dataEvents: DataEventBuffer) {
        dataEvents.forEach { event ->
            if (event.type == DataEvent.TYPE_CHANGED && event.dataItem.uri.path == INTEREST_POINTS_PATH) {
                val dataMap = DataMapItem.fromDataItem(event.dataItem).dataMap
                val interestPointsDataMaps = dataMap.getDataMapArrayList(INTEREST_POINTS_KEY)
                interestPointsDataMaps?.map { it.toInterestPoint() }?.let {
                    viewModelScope.launch(Dispatchers.IO) {
                        interestPointRepository.insertList(it)
                    }
                }
            }
        }
    }

    fun displayNotificationPermissionNotGranted() {
        alertType = AlertType.NotificationPermissionNotGranted
        showAlert = true
    }

    fun displayLocationPermissionNotGranted() {
        alertType = AlertType.LocationPermissionsNotGranted
        showAlert = true
    }

    fun displayBackgroundLocationPermissionNotGranted() {
        alertType = AlertType.BackgroundLocationPermissionNotGranted
        showAlert = true
    }


    fun hideAlert() {
        showAlert = false
        alertType = null
    }
}