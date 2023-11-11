package fr.wonderfulappstudio.notifymehere.presentation.ui.main

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import fr.wonderfulappstudio.notifymehere.presentation.model.InterestPoint
import fr.wonderfulappstudio.notifymehere.presentation.repository.InterestPointRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(interestPointRepository: InterestPointRepository) : ViewModel() {
    val interestPoints: Flow<List<InterestPoint>> = interestPointRepository.interestPoints
}