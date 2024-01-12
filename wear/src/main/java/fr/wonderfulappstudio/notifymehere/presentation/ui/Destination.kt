package fr.wonderfulappstudio.notifymehere.presentation.ui

interface Destination {
    val route: String
}

object BrandedLaunch: Destination {
    override val route: String = "BrandedLaunch"
}

object Main: Destination {
    override val route: String = "Main"
}

object Details: Destination {
    override val route: String = "Details"

    const val detailsIdKey: String = "detailsId"
    const val stopNotificationKey: String = "stopNotification"

    fun buildRouteWithArguments(detailsId: Int?, stopNotification: Boolean) =
        "$route/$detailsId/$stopNotification"
}

object Settings: Destination {
    override val route: String = "Settings"
}