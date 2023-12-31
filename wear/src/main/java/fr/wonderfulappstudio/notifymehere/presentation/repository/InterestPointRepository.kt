package fr.wonderfulappstudio.notifymehere.presentation.repository

import fr.wonderfulappstudio.notifymehere.presentation.datasource.InterestPointDatasource
import fr.wonderfulappstudio.notifymehere.presentation.model.InterestPoint
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class InterestPointRepository @Inject constructor(private val datasource: InterestPointDatasource) {
    val interestPoints: Flow<List<InterestPoint>> = datasource.interestPoints

    suspend fun insertList(interestPoints: List<InterestPoint>) =
        datasource.insertList(interestPoints)

    suspend fun update(interestPoint: InterestPoint) = datasource.update(interestPoint)

    suspend fun delete(interestPoint: InterestPoint) = datasource.delete(interestPoint)
}