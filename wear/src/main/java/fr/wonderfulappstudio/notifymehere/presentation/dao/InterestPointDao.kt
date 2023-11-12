package fr.wonderfulappstudio.notifymehere.presentation.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import fr.wonderfulappstudio.notifymehere.model.room.RoomInterestPoint
import kotlinx.coroutines.flow.Flow

@Dao
interface InterestPointDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(roomInterestPoint: RoomInterestPoint)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertList(roomInterestPoint: List<RoomInterestPoint>)

    @Query("SELECT * FROM RoomInterestPoint")
    fun getAll(): Flow<List<RoomInterestPoint>>
}