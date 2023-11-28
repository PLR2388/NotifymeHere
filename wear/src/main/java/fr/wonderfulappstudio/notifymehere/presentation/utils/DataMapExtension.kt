package fr.wonderfulappstudio.notifymehere.presentation.utils

import android.location.Location
import com.google.android.gms.wearable.DataMap
import fr.wonderfulappstudio.notifymehere.presentation.model.InterestPoint

fun DataMap.toInterestPoint(): InterestPoint {
    val position = Location("app").apply {
        latitude = getDouble("latitude")
        longitude =  getDouble("longitude")
    }
    return InterestPoint(
        id = if (getInt("id") != -1) getInt("id") else null,
        name = getString("name") ?: "",
        description = getString("description"),
        position = position,
        startDate = getLong("startDate", -1).takeIf { it != -1L },
        endDate = getLong("endDate", -1).takeIf { it != -1L }
    )
}
