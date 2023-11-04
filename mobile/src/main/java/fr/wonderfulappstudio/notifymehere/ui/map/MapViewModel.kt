package fr.wonderfulappstudio.notifymehere.ui.map

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MapViewModel @Inject constructor() : ViewModel() {

    var position: LatLng by mutableStateOf(LatLng(0.0, 0.0))
        private set

    fun onMapLongClick(latLng: LatLng) {
        position = latLng
    }
}