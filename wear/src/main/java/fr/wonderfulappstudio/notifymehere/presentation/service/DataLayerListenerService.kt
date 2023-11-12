package fr.wonderfulappstudio.notifymehere.presentation.service

import com.google.android.gms.wearable.DataEvent
import com.google.android.gms.wearable.DataEventBuffer
import com.google.android.gms.wearable.DataMapItem
import com.google.android.gms.wearable.WearableListenerService
import fr.wonderfulappstudio.notifymehere.presentation.MainActivity
import fr.wonderfulappstudio.notifymehere.presentation.repository.InterestPointRepository
import fr.wonderfulappstudio.notifymehere.presentation.utils.toInterestPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject

class DataLayerListenerService @Inject constructor(private val interestPointRepository: InterestPointRepository,  private val ioCoroutineScope: CoroutineScope) : WearableListenerService() {

    override fun onDataChanged(dataEvents: DataEventBuffer) {
        dataEvents.forEach { event ->
            if (event.type == DataEvent.TYPE_CHANGED && event.dataItem.uri.path == MainActivity.INTEREST_POINTS_PATH) {
                val dataMap = DataMapItem.fromDataItem(event.dataItem).dataMap
                val interestPointsDataMaps = dataMap.getDataMapArrayList("interestPoints")
                interestPointsDataMaps?.map { it.toInterestPoint() }?.let {
                    ioCoroutineScope.launch {
                        interestPointRepository.insertList(it)
                    }
                }
            }
        }
    }
}
