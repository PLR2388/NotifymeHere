package fr.wonderfulappstudio.notifymehere.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import fr.wonderfulappstudio.notifymehere.model.room.RoomInterestPoint
import kotlinx.coroutines.flow.Flow

@Dao
interface InterestPointDao {
    @Insert
    suspend fun insert(roomInterestPoint: RoomInterestPoint)

    @Update
    suspend fun update(roomInterestPoint: RoomInterestPoint)

    @Delete
    suspend fun delete(roomInterestPoint: RoomInterestPoint)

    @Query("SELECT * FROM RoomInterestPoint")
    fun getAll(): Flow<List<RoomInterestPoint>>

    @Query("SELECT * FROM RoomInterestPoint WHERE id = :id")
    fun getInterestPointById(id: Int): RoomInterestPoint?


}