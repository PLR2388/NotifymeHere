package fr.wonderfulappstudio.notifymehere.presentation.ui

interface Destination {
    val route: String
}

object Main: Destination {
    override val route: String = "Main"
}

object Details: Destination {
    override val route: String = "Details"
}