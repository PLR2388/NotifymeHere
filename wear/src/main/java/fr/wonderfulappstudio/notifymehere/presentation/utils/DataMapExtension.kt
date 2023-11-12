package fr.wonderfulappstudio.notifymehere.presentation.utils

import com.google.android.gms.wearable.DataMap
import fr.wonderfulappstudio.notifymehere.presentation.model.InterestPoint

fun DataMap.toInterestPoint(): InterestPoint {
    return InterestPoint(
        id = if (getInt("id") != -1) getInt("id") else null,
        name = getString("name") ?: "",
        description = getString("description"),
        position = Pair(getDouble("latitude"), getDouble("longitude")),
        startDate = getLong("startDate", -1).takeIf { it != -1L },
        endDate = getLong("endDate", -1).takeIf { it != -1L }
    )
}
