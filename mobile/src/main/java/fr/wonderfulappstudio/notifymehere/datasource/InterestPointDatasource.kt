package fr.wonderfulappstudio.notifymehere.datasource

import fr.wonderfulappstudio.notifymehere.dao.InterestPointDao
import fr.wonderfulappstudio.notifymehere.model.InterestPoint
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

    suspend fun update(interestPoint: InterestPoint) {
        interestPointDao.update(interestPoint.toRoomInterestPoint())
    }

    suspend fun delete(interestPoint: InterestPoint) {
        interestPointDao.delete(interestPoint.toRoomInterestPoint())
    }

    fun getInterestPointById(id: Int): InterestPoint? {
        val roomInterestPoint = interestPointDao.getInterestPointById(id)
        return if (roomInterestPoint == null) {
            null
        } else {
            InterestPoint.fromRoomInterestPoint(roomInterestPoint)
        }
    }
}