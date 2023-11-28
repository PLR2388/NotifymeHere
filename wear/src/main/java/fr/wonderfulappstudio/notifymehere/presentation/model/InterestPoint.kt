package fr.wonderfulappstudio.notifymehere.presentation.model

import android.location.Location
import fr.wonderfulappstudio.notifymehere.model.room.RoomInterestPoint

data class InterestPoint(
    val id: Int?,
    val name: String,
    val description: String? = null,
    val position: Location,
    val startDate: Long? = null,
    val endDate: Long? = null,
    val alreadyNotify: Boolean = false
) {
    fun toRoomInterestPoint(): RoomInterestPoint {
        return if (id != null) {
            RoomInterestPoint(
                id = id,
                name = name,
                description = description ?: "",
                latitude = position.latitude,
                longitude = position.longitude,
                startDate = startDate ?: 0L,
                endDate = endDate ?: 0L,
                alreadyNotify = alreadyNotify
            )
        } else {
            RoomInterestPoint(
                name = name,
                description = description ?: "",
                latitude = position.latitude,
                longitude = position.longitude,
                startDate = startDate ?: 0L,
                endDate = endDate ?: 0L,
                alreadyNotify = alreadyNotify
            )
        }

    }

    companion object {
        fun fromRoomInterestPoint(roomInterestPoint: RoomInterestPoint): InterestPoint {
            val location = Location("app").apply {
                latitude = roomInterestPoint.latitude
                longitude = roomInterestPoint.longitude
            }
            return InterestPoint(
                roomInterestPoint.id,
                roomInterestPoint.name,
                roomInterestPoint.description.ifEmpty { null },
                location,
                roomInterestPoint.startDate.takeIf { it > 0 },
                roomInterestPoint.endDate.takeIf { it > 0 }
            )
        }

    }
}