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
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val interestPointRepository: InterestPointRepository
) : ViewModel() {


    private val detailsId: Int? = savedStateHandle.get<Int>("detailsId")

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

                }
            }
        }
    }
}