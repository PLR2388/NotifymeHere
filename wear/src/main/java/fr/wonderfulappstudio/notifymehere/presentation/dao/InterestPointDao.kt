package fr.wonderfulappstudio.notifymehere.presentation.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import fr.wonderfulappstudio.notifymehere.model.room.RoomInterestPoint
import kotlinx.coroutines.flow.Flow

@Dao
interface InterestPointDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(roomInterestPoint: RoomInterestPoint)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertList(roomInterestPoint: List<RoomInterestPoint>)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(roomInterestPoint: RoomInterestPoint)

    @Query("SELECT * FROM RoomInterestPoint")
    fun getAll(): Flow<List<RoomInterestPoint>>
}