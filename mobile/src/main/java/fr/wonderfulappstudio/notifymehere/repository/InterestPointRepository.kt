package fr.wonderfulappstudio.notifymehere.repository

import fr.wonderfulappstudio.notifymehere.datasource.InterestPointDatasource
import fr.wonderfulappstudio.notifymehere.model.InterestPoint
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class InterestPointRepository @Inject constructor(private val datasource: InterestPointDatasource) {
    val interestPoints: Flow<List<InterestPoint>> = datasource.interestPoints

    suspend fun insert(interestPoint: InterestPoint) = datasource.insert(interestPoint)
}