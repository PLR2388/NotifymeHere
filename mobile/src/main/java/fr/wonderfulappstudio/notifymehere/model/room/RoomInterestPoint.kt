package fr.wonderfulappstudio.notifymehere.model.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class RoomInterestPoint(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val description: String,
    val latitude: Double,
    val longitude: Double,
    val startDate: Long,
    val endDate: Long
)