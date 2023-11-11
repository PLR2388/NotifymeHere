package fr.wonderfulappstudio.notifymehere.presentation.model

import fr.wonderfulappstudio.notifymehere.model.room.RoomInterestPoint

data class InterestPoint(
    val id: Int?,
    val name: String,
    val description: String? = null,
    val position: Pair<Double, Double>,
    val startDate: Long? = null,
    val endDate: Long? = null
) {
    fun toRoomInterestPoint() : RoomInterestPoint {
        return if (id != null) {
            RoomInterestPoint(
                id = id,
                name = name,
                description = description ?: "",
                latitude = position.first,
                longitude = position.second,
                startDate = startDate ?: 0L,
                endDate = endDate ?: 0L
            )
        } else {
            RoomInterestPoint(
                name = name,
                description = description ?: "",
                latitude = position.first,
                longitude = position.second,
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