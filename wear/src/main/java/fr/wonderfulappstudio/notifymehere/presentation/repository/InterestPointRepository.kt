package fr.wonderfulappstudio.notifymehere.presentation.repository

import fr.wonderfulappstudio.notifymehere.presentation.datasource.InterestPointDatasource
import fr.wonderfulappstudio.notifymehere.presentation.model.InterestPoint
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class InterestPointRepository @Inject constructor(private val datasource: InterestPointDatasource) {
    val interestPoints: Flow<List<InterestPoint>> = datasource.interestPoints

    suspend fun insert(interestPoint: InterestPoint) = datasource.insert(interestPoint)
}