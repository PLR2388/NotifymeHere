package fr.wonderfulappstudio.notifymehere.presentation.ui.details

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import fr.wonderfulappstudio.notifymehere.presentation.model.InterestPoint
import fr.wonderfulappstudio.notifymehere.presentation.repository.InterestPointRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val interestPointRepository: InterestPointRepository
) : ViewModel() {


    private val detailsId: Int? = savedStateHandle.get<Int>("detailsId")
    private val stopNotification: Boolean = savedStateHandle.get<Boolean>("stopNotification") == true

    var interestPoint: InterestPoint? by mutableStateOf(null)
        private set

    init {
        if (detailsId == null) {
            interestPoint = null
        } else {
            viewModelScope.launch {
                interestPointRepository.interestPoints.collect { points ->
                    interestPoint = try {
                        points.first { it.id == detailsId }
                    } catch (e: Exception) {
                        null
                    }
                    if (stopNotification) {
                        interestPoint = interestPoint?.copy(alreadyNotify = true)
                        interestPoint?.let {
                            viewModelScope.launch(Dispatchers.IO) {
                                interestPointRepository.update(it)
                            }
                        }
                    }
                }
            }
        }
    }

    fun deleteInterestPoint() {
        viewModelScope.launch(Dispatchers.IO) {
            interestPoint?.let { interestPointRepository.delete(it) }
        }
    }

    fun reactivateNotification() {
        interestPoint = interestPoint?.copy(alreadyNotify = false)
        interestPoint?.let {
            viewModelScope.launch(Dispatchers.IO) {
                interestPointRepository.update(it)
            }
        }
    }
}