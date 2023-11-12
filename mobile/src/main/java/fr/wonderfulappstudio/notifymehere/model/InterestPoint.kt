package fr.wonderfulappstudio.notifymehere.model

import com.google.android.gms.wearable.DataMap
import fr.wonderfulappstudio.notifymehere.model.room.RoomInterestPoint

data class InterestPoint(
    val id: Int?,
    val name: String,
    val description: String? = null,
    val gpsPosition: Pair<Double, Double>,
    val startDate: Long? = null,
    val endDate: Long? = null
) {

    fun toDataMap(): DataMap {
        return DataMap().apply {
            putInt("id", id ?: -1)  // Using -1 or another value to represent null
            putString("name", name)
            description?.let { putString("description", it) }
            putDouble("latitude", gpsPosition.first)
            putDouble("longitude", gpsPosition.second)
            startDate?.let { putLong("startDate", it) }
            endDate?.let { putLong("endDate", it) }
        }
    }


    fun toRoomInterestPoint() : RoomInterestPoint {
        return if (id != null) {
            RoomInterestPoint(
                id = id,
                name = name,
                description = description ?: "",
                latitude = gpsPosition.first,
                longitude = gpsPosition.second,
                startDate = startDate ?: 0L,
                endDate = endDate ?: 0L
            )
        } else {
            RoomInterestPoint(
                name = name,
                description = description ?: "",
                latitude = gpsPosition.first,
                longitude = gpsPosition.second,
                startDate = startDate ?: 0L,
                endDate = endDate ?: 0L
            )
        }

    }

    companion object {
        fun fromRoomInterestPoint(roomInterestPoint: RoomInterestPoint): InterestPoint =
            InterestPoint(
                roomInterestPoint.id,
                roomInterestPoint.name,
                roomInterestPoint.description.ifEmpty { null },
                Pair(roomInterestPoint.latitude, roomInterestPoint.longitude),
                roomInterestPoint.startDate.takeIf { it > 0 },
                roomInterestPoint.endDate.takeIf { it > 0 }
            )
    }

}