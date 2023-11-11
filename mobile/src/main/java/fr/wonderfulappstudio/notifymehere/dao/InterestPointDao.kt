package fr.wonderfulappstudio.notifymehere.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import fr.wonderfulappstudio.notifymehere.model.room.RoomInterestPoint
import kotlinx.coroutines.flow.Flow

@Dao
interface InterestPointDao {
    @Insert
    suspend fun insert(roomInterestPoint: RoomInterestPoint)

    @Query("SELECT * FROM RoomInterestPoint")
    fun getAll(): Flow<List<RoomInterestPoint>>
}