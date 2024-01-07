package fr.wonderfulappstudio.notifymehere.presentation.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import fr.wonderfulappstudio.common.manager.DataStoreManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(private val dataStoreManager: DataStoreManager): ViewModel() {
    val notificationDistance: Flow<Float?> = dataStoreManager.readNotificationDistance

    fun changeNotificationDistance(value: Int) {
        viewModelScope.launch {
            dataStoreManager.writeNotificationDistance(value.toFloat())
        }
    }
}