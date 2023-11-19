package fr.wonderfulappstudio.notifymehere.presentation.ui.main

import android.annotation.SuppressLint
import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import com.google.android.gms.wearable.CapabilityClient
import com.google.android.gms.wearable.CapabilityInfo
import com.google.android.gms.wearable.DataClient
import com.google.android.gms.wearable.DataEvent
import com.google.android.gms.wearable.DataEventBuffer
import com.google.android.gms.wearable.DataMapItem
import com.google.android.gms.wearable.MessageClient
import com.google.android.gms.wearable.MessageEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import fr.wonderfulappstudio.notifymehere.presentation.model.InterestPoint
import fr.wonderfulappstudio.notifymehere.presentation.repository.InterestPointRepository
import fr.wonderfulappstudio.notifymehere.presentation.service.DataLayerListenerService.Companion.INTEREST_POINTS_KEY
import fr.wonderfulappstudio.notifymehere.presentation.service.DataLayerListenerService.Companion.INTEREST_POINTS_PATH
import fr.wonderfulappstudio.notifymehere.presentation.utils.toInterestPoint
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    application: Application,
    interestPointRepository: InterestPointRepository
) :
    AndroidViewModel(application),
    DataClient.OnDataChangedListener,
    MessageClient.OnMessageReceivedListener,
    CapabilityClient.OnCapabilityChangedListener {
    val interestPoints: Flow<List<InterestPoint>> = interestPointRepository.interestPoints

    @SuppressLint("VisibleForTests")
    override fun onDataChanged(dataEvents: DataEventBuffer) {

        // Do additional work for specific events
        dataEvents.forEach { event ->
            if (event.type == DataEvent.TYPE_CHANGED && event.dataItem.uri.path == INTEREST_POINTS_PATH) {
                val dataMap = DataMapItem.fromDataItem(event.dataItem).dataMap
                val interestPointsDataMaps = dataMap.getDataMapArrayList(INTEREST_POINTS_KEY)
                interestPointsDataMaps?.map { it.toInterestPoint() }?.let {
                    Log.d("DATA", it.toString())
                }
            }
        }

    }

    override fun onMessageReceived(messageEvent: MessageEvent) {

    }

    override fun onCapabilityChanged(capabilityInfo: CapabilityInfo) {

    }
}