package fr.wonderfulappstudio.notifymehere.model

import android.location.Location
import com.google.android.gms.wearable.DataMap
import fr.wonderfulappstudio.notifymehere.model.room.RoomInterestPoint

data class InterestPoint(
    val id: Int? = null,
    val name: String = "",
    val description: String? = null,
    val position: Location = Location("app"),
    val startDate: Long? = null,
    val endDate: Long? = null,
    val alreadyNotify: Boolean = false
) {

    fun toDataMap(): DataMap {
        return DataMap().apply {
            putInt("id", id ?: -1)  // Using -1 or another value to represent null
            putString("name", name)
            description?.let { putString("description", it) }
            putDouble("latitude", position.latitude)
            putDouble("longitude", position.longitude)
            startDate?.let { putLong("startDate", it) }
            endDate?.let { putLong("endDate", it) }
        }
    }


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
                roomInterestPoint.endDate.takeIf { it > 0 },
                roomInterestPoint.alreadyNotify
            )
        }

    }

}