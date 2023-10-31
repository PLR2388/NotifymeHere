package fr.wonderfulappstudio.notifymehere

interface Destination {
    val route: String
}

object Main: Destination {
    override val route: String = "Main"
}

object Details: Destination {
    override val route: String = "Details"
}

object Map: Destination {
    override val route: String = "Map"
}