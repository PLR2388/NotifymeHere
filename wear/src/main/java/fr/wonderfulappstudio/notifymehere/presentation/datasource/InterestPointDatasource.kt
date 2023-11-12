package fr.wonderfulappstudio.notifymehere.presentation.datasource

import fr.wonderfulappstudio.notifymehere.presentation.dao.InterestPointDao
import fr.wonderfulappstudio.notifymehere.presentation.model.InterestPoint
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class InterestPointDatasource @Inject constructor(private val interestPointDao: InterestPointDao) {
    val interestPoints: Flow<List<InterestPoint>> = interestPointDao.getAll().map {
        it.map(InterestPoint::fromRoomInterestPoint)
    }

    suspend fun insert(interestPoint: InterestPoint) {
        interestPointDao.insert(interestPoint.toRoomInterestPoint())
    }

    suspend fun insertList(interestPoints: List<InterestPoint>) {
        interestPointDao.insertList(interestPoints.map { it.toRoomInterestPoint() })
    }
}