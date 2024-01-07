package fr.wonderfulappstudio.notifymehere

interface Destination {
    val route: String
}

object Main: Destination {
    override val route: String = "Main"
}

object Details: Destination {
    override val route: String = "Details"

    const val detailsIdKey: String = "detailsId"
    const val stopNotificationKey: String = "stopNotification"

    fun buildRouteWithArguments(detailsId: Int?, stopNotification: Boolean) =
        "$route?$detailsIdKey=$detailsId&$stopNotificationKey=$stopNotification"
}

object Map: Destination {
    override val route: String = "Map"
}