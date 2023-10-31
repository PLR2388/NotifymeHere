package fr.wonderfulappstudio.notifymehere

data class InterestPoint(
    val id: Int,
    val name: String,
    val description: String? = null,
    val gpsPosition: Pair<Double, Double>,
    val startDate: Long? = null,
    val endDate: Long? = null
)
