package fr.wonderfulappstudio.notifymehere.ui

import androidx.lifecycle.ViewModel
import com.google.android.gms.wearable.DataClient
import com.google.android.gms.wearable.DataEventBuffer
import dagger.hilt.android.lifecycle.HiltViewModel
import fr.wonderfulappstudio.notifymehere.model.InterestPoint
import fr.wonderfulappstudio.notifymehere.repository.InterestPointRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(interestPointRepository: InterestPointRepository): ViewModel(),
    DataClient.OnDataChangedListener {

    val interestPoints: Flow<List<InterestPoint>> = interestPointRepository.interestPoints
    override fun onDataChanged(p0: DataEventBuffer) {

    }
}