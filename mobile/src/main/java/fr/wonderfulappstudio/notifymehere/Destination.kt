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

    fun routeWithDetailsId(detailsId: Int) = "$route?$detailsIdKey=$detailsId"
}

object Map: Destination {
    override val route: String = "Map"
}