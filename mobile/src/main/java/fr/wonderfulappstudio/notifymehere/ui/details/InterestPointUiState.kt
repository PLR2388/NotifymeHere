package fr.wonderfulappstudio.notifymehere.ui.details

import androidx.compose.runtime.Stable

@Stable
data class InterestPointUiState(
    val name: String = "",
    val description: String? = null,
    val gpsPosition: Pair<Double, Double> = Pair(0.0,0.0),
    val startDate: Long? = null,
    val endDate: Long? = null
)
